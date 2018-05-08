package com.invest.ivuser.biz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.exception.IVDAOException;
import com.invest.ivcommons.constant.SystemErrorEnum;
import com.invest.ivcommons.redis.client.cacheclient.CacheClientHA;
import com.invest.ivcommons.util.security.md5.MD5Util;
import com.invest.ivcommons.util.string.StringUtil;
import com.invest.ivpush.message.client.constants.CodeEnum;
import com.invest.ivpush.message.client.constants.GroupEnum;
import com.invest.ivpush.message.service.SmsSendService;
import com.invest.ivpush.message.service.exception.PushException;
import com.invest.ivuser.biz.manager.UserAccountManager;
import com.invest.ivuser.biz.manager.UserManager;
import com.invest.ivuser.biz.service.UserService;
import com.invest.ivuser.common.IVUserErrorEnum;
import com.invest.ivuser.common.SMSTemplateConstants;
import com.invest.ivuser.model.entity.User;
import com.invest.ivuser.model.entity.UserAccount;
import com.invest.ivuser.model.param.*;
import com.invest.ivuser.model.result.UserResult;
import com.invest.ivuser.util.KeyVersionUtils;
import com.invest.ivuser.util.RedisKeyUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by xugang on 2017/7/28.
 */
@Service
public class UserServiceImpl implements UserService {

    //头像缩略图 长宽
    public static final int HEAD_IMAGE_THUMBNAIL_WIDTH = 120;
    public static final int HEAD_IMAGE_THUMBNAIL_HEIGHT = 120;

    public static final String IMAGE_THUMBNAIL_FORMAT_NAME = "jpg"; //用户头像压缩图片格式

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 后期改为rpc服务
     */
    @Resource
    private SmsSendService smsSendService;

    @Resource(name = "cacheClientHA")
    protected CacheClientHA cacheClientHA;

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserAccountManager userAccountManager;

    @Override
    public UserResult verifyUniWasLoggedin(String token, Map<String, String> params) {
        UserResult result = new UserResult();
        if (StringUtils.isEmpty(token)) {
            logger.error("verifyUniWasLoggedin error param is error");
            SystemErrorEnum.PARAM_IS_INVALID.fillResult(result);
            return result;
        }

        try {
            String userTokenKey = RedisKeyUtils.keyUserToken(token);
            String userTokenValue = cacheClientHA.String().get(userTokenKey);
            if (StringUtils.isEmpty(userTokenValue)) {
                logger.error("用户登录过期,token={}", token);
                SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
                return result;
            }
            result.setUserId(Long.valueOf(userTokenValue));
        } catch (Exception e) {
            logger.error("验证token失败 Error/HTTP,token=" + token, e);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
        }
        return result;
    }

    @Override
    public UserResult sendSmsCheckCode(CheckCodeParam checkCodeParam) {
        //TODO 限流
        UserResult result = new UserResult();
        String mobile = checkCodeParam.getMobile();
        //获取六位数字验证码
        int code = StringUtil.getRandomNumberSix();
        try {
            //发送短信
            smsSendService.send(GroupEnum.common.getCode(), CodeEnum.common.getCode(), mobile, String.format(SMSTemplateConstants.TMP_SMS_CODE, code));
            //写redis
            String key = KeyVersionUtils.rediskeyUserSMSCheckCodeV(RedisKeyUtils.keyUserSMSCode(mobile));
            cacheClientHA.String().setex(key, KeyVersionUtils.getUserSMSCheckCodeExpiretime(), String.valueOf(code));
        } catch (PushException e) {
            logger.error("发送短信验证码失败,mobile=" + mobile, e);
            IVUserErrorEnum.SEND_SMS_CHECK_CODE_ERROR.fillResult(result);
            return result;
        }

        logger.info("发送短信验证码成功,mobile={},code={}", mobile, code);
        result.setSmsCheckCode(String.valueOf(code));
        return result;
    }

    @Override
    public UserResult register(RegisterParam registerParam) {
        UserResult result = new UserResult();
        String mobile = registerParam.getMobile();

        //1.验证手机号是否存在
        User user = userManager.getUserByMobile(mobile);
        if (user != null) {
            logger.error("注册失败,该手机号已被占用,mobile={}", mobile);
            IVUserErrorEnum.REGISTER_MOBILE_EXISTS_ERROR.fillResult(result);
            return result;
        }

        //TODO 同盾认证手机号

        //2.验证短信验证码
        String key = KeyVersionUtils.rediskeyUserSMSCheckCodeV(RedisKeyUtils.keyUserSMSCode(mobile));
        String code = cacheClientHA.String().get(key);
        if (code == null) {
            logger.info("验证码过期,mobile={}", mobile);
            IVUserErrorEnum.CHECK_SMS_CHECK_CODE_EXPIRED_ERROR.fillResult(result);
            return result;
        }

        if (!registerParam.getCheckCode().equals(code)) {
            logger.error("短信验证码验证错误,mobile={},code={},new={}", mobile, code, registerParam.getCheckCode());
            IVUserErrorEnum.CHECK_SMS_CHECK_CODE_ERROR.fillResult(result);
            return result;
        }

        //3.生成usertoken
        User tokenUser = new User();
        tokenUser.setMobile(mobile);
        String userToken = userManager.genUserToken(tokenUser);
        logger.info("生成token成功,mobile={},userToken={}", mobile, userToken);

        //4.保存user
        user = new User();
        user.setMobile(mobile);
        user.setReferrerMobile(registerParam.getReferrerMobile());
        user.setRegisterSource(registerParam.getClientType());
        user.setPassword(registerParam.getPassword());
        user.setUserToken(userToken);
        String securityKey = MD5Util.md5(UUID.randomUUID().toString()); //用于密码加密的安全码
        user.setSecurityKey(securityKey);
        String encryptPassword = user.encryptPassword();
        user.setPassword(encryptPassword);
        userManager.saveUser(user);

        String userTokenKey = RedisKeyUtils.keyUserToken(userToken);
        cacheClientHA.String().setex(userTokenKey, 2 * 60 * 60, String.valueOf(user.getId()));

        //5.推荐人逻辑处理,积分或返现
        Long userId = user.getId();

        //6.初始化账户表
        try {
            UserAccount userAccount = new UserAccount();
            userAccount.setUserId(userId);
            //新用户送18币
            userAccount.setZhuobaoBalance(18);
            userAccount.setBidAmountBalance(18 * UserAccount.ZHUOBAOBI_BID_AMOUNT_BASE);
            userAccount.setStatus(NumberUtils.SHORT_ZERO);
            userAccountManager.addUserAccount(userAccount);
        } catch (Exception e) {
            logger.error("初始化账户表失败,userId=" + userId, e);
        }

        //7.发送注册成功短信
        /*try {
            smsSendService.send(GroupEnum.common.getCode(), CodeEnum.common.getCode(), mobile, "");
            logger.info("发送注册成功短信成功,手机号={}", mobile);
        } catch (PushException e) {
            logger.error("发送注册成功短信失败,手机号=" + mobile, e);
            //ignore
        }*/

        logger.info("注册成功,user={},手机号={}", JSONObject.toJSONString(user), mobile);
        result.setUser(user);
        result.setToken(userToken);
        return result;

    }

    @Override
    public UserResult login(LoginParam loginParam) {
        UserResult result = new UserResult();
        String mobile = loginParam.getUsername();
        String pass = loginParam.getPassword();

        //验证用户是否存在
        UserResult userResult = this.getUserBaseInfo(mobile);
        if (userResult.isFailed()) {
            logger.error("登录失败,查询用户失败,mobile={},result={}", mobile, userResult);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            return result;
        }
        User user = userResult.getUser();

        if (user == null) {
            logger.error("登录失败,用户不存在,mobile={}", mobile);
            IVUserErrorEnum.USER_LOGIN_USERNAME_NOT_EXISTS_ERROR.fillResult(result);
            return result;
        }

        //TODO 统计登录失败次数 加验证码验证
        String checkCode = loginParam.getCheckCode();
        /*String key = KeyVersionUtils.rediskeyUserSMSCheckCodeV(RedisKeyUtils.keyUserSMSCode(mobile));
        String code = cacheClientHA.String().get(key);
        if (code == null) {
            logger.info("验证码过期,mobile={}", mobile);
            IVUserErrorEnum.CHECK_SMS_CHECK_CODE_EXPIRED_ERROR.fillResult(result);
            return result;
        }*/

        //验证密码
        String passwordDB = user.getPassword();
        user.setPassword(pass);
        String encryptPassword = user.encryptPassword();
        if (!passwordDB.equalsIgnoreCase(encryptPassword)) {
            logger.error("登录失败密码错误,mobile={}", mobile);
            IVUserErrorEnum.USER_LOGIN_USERNAME_NOT_EXISTS_ERROR.fillResult(result);
            return result;
        }

        //3.生成usertoken
        User tokenUser = new User();
        tokenUser.setMobile(mobile);
        String userToken = userManager.genUserToken(tokenUser);
        logger.info("生成token成功,mobile={},userToken={}", mobile, userToken);

        String userTokenKey = RedisKeyUtils.keyUserToken(userToken);
        cacheClientHA.String().setex(userTokenKey, 2 * 60 * 60, String.valueOf(user.getId()));

        //4.保存user 登录时间 和 状态
        Date now = new Date(System.currentTimeMillis());
        User userDB = new User();
        userDB.setLastLoginTime(now);
        userDB.setCreateTime(null);
        userDB.setId(user.getId());
        userManager.updateUserById(userDB);

        logger.info("登录成功,username={}", mobile);
        result.setUser(user);
        result.setToken(userToken);
        return result;
    }

    @Override
    public UserResult logout(LogoutParam logoutParam) {
        // 参数校验
        UserResult result = new UserResult();
        Long userId = logoutParam.getUserId();
        String token = logoutParam.getToken();
        if (StringUtils.isBlank(token)) {
            logger.info("登出失败,token不存在,userId={}", userId);
            IVUserErrorEnum.USER_TOKEN_NOT_EXISTS_ERROR.fillResult(result);
            return result;
        }
        // 查询db
        User user = userManager.getUserById(userId);
        if (user == null) {
            logger.info("登出失败,用户id不存在,userId={}", userId);
            IVUserErrorEnum.USER_NOT_EXISTS_ERROR.fillResult(result);
            return result;
        }
        // 更新在线状态 为下线 TODO
        /*User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setOnline(Boolean.FALSE); // 下线状态
        userDAO.updateByPrimaryKeySelective(updateUser);*/

        //删除token
        String userTokenKey = RedisKeyUtils.keyUserToken(token);
        cacheClientHA.Key().del(userTokenKey);
        return result;
    }

    @Override
    public UserResult resetPassword(ResetPasswordParam resetPasswordParam) {
        UserResult result = new UserResult();
        Long userId = resetPasswordParam.getUserId();

        //验证手机号是否存在
        User user = userManager.getUserById(userId);
        if (user == null) {
            logger.error("失败,用户不存在,userId={}", userId);
            IVUserErrorEnum.MODIFY_ERROR.fillResult(result);
            return result;
        }

        String oldEncryptPasswordDB = user.getPassword();
        user.setPassword(resetPasswordParam.getOldPassword());
        String oldEncryptPassword = user.encryptPassword();
        //判断原密码
        if (!oldEncryptPasswordDB.equalsIgnoreCase(oldEncryptPassword)) {
            logger.error("失败,原密码错误,userId={}", userId);
            IVUserErrorEnum.RESET_OLD_PWD_ERROR.fillResult(result);
            return result;
        }
        //保存user
        user.setPassword(resetPasswordParam.getNewPassword());
        String encryptPassword = user.encryptPassword();
        user.setPassword(encryptPassword);
        user.setUpdateTime(new Date(System.currentTimeMillis()));
        userManager.updateUserById(user);

        logger.info("重置密码成功,user={}", JSONObject.toJSONString(user));

        result.setUser(user);
        return result;
    }

    @Override
    public UserResult refindPassword(RefindPasswordParam refindPasswordParam) {
        UserResult result = new UserResult();
        String mobile = refindPasswordParam.getMobile();

        //1.验证手机号是否存在
        User user = userManager.getUserByMobile(mobile);
        if (user == null) {
            logger.error("失败,手机号不存在,mobile={}", mobile);
            IVUserErrorEnum.REFUND_PWD_PHONE_ERROR.fillResult(result);
            return result;
        }

        //2.验证短信验证码
        String key = KeyVersionUtils.rediskeyUserSMSCheckCodeV(RedisKeyUtils.keyUserSMSCode(mobile));
        String code = cacheClientHA.String().get(key);
        if (code == null) {
            logger.info("验证码过期,mobile={}", mobile);
            IVUserErrorEnum.CHECK_SMS_CHECK_CODE_EXPIRED_ERROR.fillResult(result);
            return result;
        }

        if (!refindPasswordParam.getCheckCode().equals(code)) {
            logger.error("短信验证码验证错误,mobile={},code={},new={}", mobile, code, refindPasswordParam.getCheckCode());
            IVUserErrorEnum.CHECK_SMS_CHECK_CODE_ERROR.fillResult(result);
            return result;
        }

        //4.保存user
        user.setPassword(refindPasswordParam.getPassword());
        String encryptPassword = user.encryptPassword();
        user.setPassword(encryptPassword);
        user.setUpdateTime(new Date(System.currentTimeMillis()));
        userManager.updateUserById(user);

        logger.info("找回密码成功,user={},手机号={}", JSONObject.toJSONString(user), mobile);

        result.setUser(user);
        return result;
    }

    @Override
    public UserResult getUserBaseInfo(Long userId) {
        UserResult result = new UserResult();
        User user = userManager.getUserById(userId);
        if (user == null) {
            logger.error("用户未找到,userId={}", userId);
            IVUserErrorEnum.USER_NOT_EXISTS_ERROR.fillResult(result);
            return result;
        }
        result.setUser(user);
        return result;
    }

    @Override
    public UserResult getUserBaseInfo(String mobile) {
        UserResult result = new UserResult();
        User user = userManager.getUserByMobile(mobile);
        result.setUser(user);
        return result;
    }

    @Override
    public UserResult modifyUser(User user) {
        UserResult result = new UserResult();
        try {
            userManager.updateUserById(user);
        } catch (Exception e) {
            throw new IVDAOException(e);
        }
        return result;
    }

    @Override
    public UserResult changeHeadImg(ChangeHeadImgParam changeHeadImgParam) {
        UserResult result = new UserResult();

        Long userId = changeHeadImgParam.getUserId();
        // 判断用户是否存在
        User user = userManager.getUserById(userId);
        if (user == null) {
            logger.info("修改头像失败,用户id不存在,userId={}", userId);
            IVUserErrorEnum.USER_NOT_EXISTS_ERROR.fillResult(result);
            return result;
        }

        // 获取输入流 注意 InputStream 不能复制，每次读完到结尾
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(changeHeadImgParam.getContent());
            while ((len = bais.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        } catch (IOException e) {
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            logger.error("修改头像失败,文件操作发生异常,userId=" + userId, e);
            return result;
        }

        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
//        InputStream inputStreamOriginal = new ByteArrayInputStream(baos.toByteArray());
        // 获取缩略图
        // 读入源图像
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(inputStream);
            if (bufferedImage == null) {
                throw new IllegalStateException("用户头像处理失败");
            }
        } catch (IOException e) {
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            logger.error("修改头像失败,处理缩略图发生异常,userId=" + userId, e);
            return result;
        }

        // 获取一个宽、长是原来scale的图像实例
        Image image = bufferedImage.getScaledInstance(HEAD_IMAGE_THUMBNAIL_WIDTH, HEAD_IMAGE_THUMBNAIL_HEIGHT, java.awt.Image.SCALE_SMOOTH);
        // 缩放图像
        BufferedImage tag = new BufferedImage(HEAD_IMAGE_THUMBNAIL_WIDTH, HEAD_IMAGE_THUMBNAIL_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) tag.getGraphics();
        graphics.drawImage(image, NumberUtils.INTEGER_ZERO, NumberUtils.INTEGER_ZERO, null); // 绘制缩小后的图
        image.flush();

//        InputStream inputStreamDispose; // 缩略图文件流
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);
            ImageIO.write(tag, IMAGE_THUMBNAIL_FORMAT_NAME, imageOutputStream);
//            inputStreamDispose = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            User saveUser = new User();
            saveUser.setId(userId);
            saveUser.setHeadImgOrg(Base64.encodeBase64String(baos.toByteArray()));
            saveUser.setHeadImg(Base64.encodeBase64String(byteArrayOutputStream.toByteArray()));
            saveUser.setCreateTime(null);
            saveUser.setNick(null);
            userManager.updateUserById(saveUser);
            result.setHeadImgOrgBase64(saveUser.getHeadImgOrg());
            result.setHeadImgBase64(saveUser.getHeadImg());
        } catch (Exception e) {
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            logger.error("修改头像失败,处理缩略图发生异常,userId=" + userId, e);
            return result;
        }

        // 生成唯一文件名
        //String filename = UUID.randomUUID().toString() + System.currentTimeMillis() + Constants.SYMBOL_PERIOD + Constants.IMAGE_THUMBNAIL_FORMAT_NAME;
        // 获取当前日期作为目录 如果s3目录超过10w 性能会有所下降
        //String now = DateHelper.dateToString(null, DateHelper.DATE_FORMAT_DATE_COMMON);
        //assert StringUtils.isNotBlank(now);
        //String path = Constants.SUB_PATH_HEADIMAGE + now + Constants.SYMBOL_VIRGULE;
        //String fileNameDispose = path + ChangeHeadPictureUserParam.DISPOSE_PREFIX + filename;
        //String fileNameOriginal = path + ChangeHeadPictureUserParam.ORIGINAL_PREFIX + filename;

        /*try {
            // 上传缩略图
            imgService.saveInputStream(fileNameDispose, inputStreamDispose);
            logger.info("上传缩略图成功,userId={},fileName={}", userId, fileNameDispose);
            // 上传原图
            imgService.saveInputStream(fileNameOriginal, inputStreamOriginal);
            logger.info("上传原图成功,userId={},fileName={}", userId, fileNameOriginal);
        } catch (Exception e) {
            SystemErrorEnum.SYSTEM_ERROR.fillResult(result);
            logger.error("修改头像失败,上传用户头像发生异常,userId=" + userId + ",fileName=" + filename, e);
            return result;
        }*/

        logger.info("修改头像成功,userId={}", userId);

        return result;
    }

    @Override
    public UserResult getLoginKey(String appid, String mobile) {
        UserResult result = new UserResult();
        //验证用户是否存在
        UserResult userResult = this.getUserBaseInfo(mobile);
        if (userResult.isFailed()) {
            logger.error("getLoginKey失败,查询用户失败,mobile={},appid={},result={}", mobile, appid, userResult);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            return result;
        }
        User user = userResult.getUser();

        if (user == null) {
            logger.error("getLoginKey失败,用户不存在,mobile={},appid={}", mobile, appid);
            IVUserErrorEnum.USER_LOGIN_USERNAME_NOT_EXISTS_ERROR.fillResult(result);
            return result;
        }

        String userToken = userManager.genUserToken(user);
        String userTokenKey = RedisKeyUtils.keyUserToken(userToken);
        cacheClientHA.String().setex(userTokenKey, 30 * 60, String.valueOf(user.getId()));
        result.setToken(userToken);
        return result;
    }

    @Override
    public UserResult autoLogin(String loginKey) {
        UserResult result = new UserResult();

        String userTokenKey = RedisKeyUtils.keyUserToken(loginKey);
        String userTokenValue = cacheClientHA.String().get(userTokenKey);
        if (StringUtils.isEmpty(userTokenValue)) {
            logger.error("用户登录过期,token={}", loginKey);
            IVUserErrorEnum.USER_TOKEN_NOT_EXISTS_ERROR.fillResult(result);
            return result;
        }

        Long userId = Long.valueOf(userTokenValue);

        //验证用户是否存在
        UserResult userResult = this.getUserBaseInfo(userId);
        if (userResult.isFailed()) {
            logger.error("一键登录失败,查询用户失败,userId={},result={}", userId, userResult);
            SystemErrorEnum.SYSTEM_INNER_ERROR.fillResult(result);
            return result;
        }
        User user = userResult.getUser();

        if (user == null) {
            logger.error("一键登录失败,用户不存在,userId={}", userId);
            IVUserErrorEnum.USER_LOGIN_USERNAME_NOT_EXISTS_ERROR.fillResult(result);
            return result;
        }

        logger.info("一键登录成功,username={}", user.getMobile());
        result.setUser(user);
        return result;
    }


}
