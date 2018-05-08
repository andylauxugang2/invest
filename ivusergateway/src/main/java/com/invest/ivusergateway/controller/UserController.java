package com.invest.ivusergateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.base.result.Result;
import com.invest.ivcommons.constant.ViewConstant;
import com.invest.ivcommons.validate.model.MobileValidParam;
import com.invest.ivuser.biz.service.UserService;
import com.invest.ivuser.model.entity.User;
import com.invest.ivuser.model.param.*;
import com.invest.ivuser.model.result.UserResult;
import com.invest.ivusergateway.base.BaseController;
import com.invest.ivusergateway.common.constants.CodeEnum;
import com.invest.ivusergateway.common.constants.HttpConstant;
import com.invest.ivusergateway.model.request.*;
import com.invest.ivusergateway.model.response.APIResponse;
import com.invest.ivusergateway.model.vo.HeadImgVO;
import com.invest.ivusergateway.model.vo.UserVO;
import com.invest.ivusergateway.util.CheckCodeUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by xugang on 2016/9/6.
 */
@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 生成验证码图形
     */
    @RequestMapping(value = "/getCheckCode", method = RequestMethod.GET)
    public APIResponse<String> getCheckCode(HttpServletRequest request) throws Exception {
        APIResponse<String> result = new APIResponse<>();
        try {
            String sRand = "";
            Random random = new Random();
            for (int i = 0; i < 4; i++) {
                String rand = String.valueOf(random.nextInt(10));
                sRand += rand;
            }
            HttpSession session = request.getSession();
            session.setAttribute("checkCode", sRand);
            BufferedImage image = CheckCodeUtil.create(sRand);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);
            ImageIO.write(image, CheckCodeUtil.IMAGE_THUMBNAIL_FORMAT_NAME, imageOutputStream);
            result.setData(Base64.encodeBase64String(byteArrayOutputStream.toByteArray()));
        } catch (Exception e) {
            logger.error("生成验证码图形异常", e);
        }
        return result;
    }

    @RequestMapping(value = "/getSMSCode", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<String> getSMSCode(@RequestBody GetSMSCodeReq getSMSCodeReq, HttpServletRequest request) {
        APIResponse<String> result = new APIResponse<>();

        //1.验证手机号格式
        String mobile = getSMSCodeReq.getPhone();
        MobileValidParam mobileValidParam = new MobileValidParam();
        mobileValidParam.setMobile(mobile);
        Result validResult = mobileValidator.valid(mobileValidParam);
        if (validResult.isFailed()) {
            logger.error("手机号参数验证失败,mobile={},error={}", mobile, validResult.getErrorMsg());
            return APIResponse.createResult(CodeEnum.PARAM_ERROR.getCode(), validResult.getErrorMsg());
        }

        //图形验证码
        String imageCheckCode = getSMSCodeReq.getImageCheckCode();
        if (StringUtils.isBlank(imageCheckCode)) {
            logger.error("参数错误,图形验证码为空");
            return APIResponse.createResult(CodeEnum.IMAGE_CODE_ERROR);
        }
        HttpSession session = request.getSession();
        String imageCode = (String) session.getAttribute("checkCode");
        if (!imageCheckCode.equalsIgnoreCase(imageCode)) {
            logger.error("图形验证码输入错误,sessioncode={},code={}", imageCode, imageCheckCode);
            return APIResponse.createResult(CodeEnum.IMAGE_CODE_ERROR);
        }
        //清空验证码
        session.removeAttribute("checkCode");

        //手机号是否已注册
        UserResult userResult = userService.getUserBaseInfo(mobile);
        if (userResult.isFailed()) {
            logger.error("获取用户手机验证码失败,查询用户失败,mobile={},result={}", mobile, userResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), userResult.getErrorMsg());
        }
        if (userResult.getUser() != null) {
            logger.error("获取用户手机验证码失败,手机号已被注册,mobile={}", mobile);
            return APIResponse.createResult(CodeEnum.REPEAT_REGIST);
        }

        //2.验证客户端安全调用
        //ClientTypeEnum clientTypeEnum = ClientTypeEnum.findByCode(HttpRequestUtil.getClientPlat(request));
        /*if (clientTypeEnum == ClientTypeEnum.UNKNOWN) {
            logger.error("客户端访问类型查询失败,mobile={}", mobile);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), "客户端访问类型获取失败");
        }*/

        //3.调用rpc服务
        CheckCodeParam checkCodeParam = new CheckCodeParam();
        checkCodeParam.setMobile(mobile);
        UserResult sendSmsCheckCodeResult = userService.sendSmsCheckCode(checkCodeParam);
        if (sendSmsCheckCodeResult.isFailed()) {
            logger.error("获取用户手机验证码失败,mobile={},result={}", mobile, sendSmsCheckCodeResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), sendSmsCheckCodeResult.getErrorMsg());
        }

        String code = sendSmsCheckCodeResult.getSmsCheckCode();
        result.setData(code);

        return result;

    }

    @RequestMapping(value = "/getForgetPwdSMSCode", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<String> getForgetPwdSMSCode(@RequestBody GetSMSCodeReq getSMSCodeReq, HttpServletRequest request) {
        APIResponse<String> result = new APIResponse<>();

        //1.验证手机号格式
        String mobile = getSMSCodeReq.getPhone();
        MobileValidParam mobileValidParam = new MobileValidParam();
        mobileValidParam.setMobile(mobile);
        Result validResult = mobileValidator.valid(mobileValidParam);
        if (validResult.isFailed()) {
            logger.error("手机号参数验证失败,mobile={},error={}", mobile, validResult.getErrorMsg());
            return APIResponse.createResult(CodeEnum.PARAM_ERROR.getCode(), validResult.getErrorMsg());
        }

        //图形验证码
        String imageCheckCode = getSMSCodeReq.getImageCheckCode();
        if (StringUtils.isBlank(imageCheckCode)) {
            logger.error("参数错误,图形验证码为空");
            return APIResponse.createResult(CodeEnum.IMAGE_CODE_ERROR);
        }
        HttpSession session = request.getSession();
        String imageCode = (String) session.getAttribute("checkCode");
        if (!imageCheckCode.equalsIgnoreCase(imageCode)) {
            logger.error("图形验证码输入错误,sessioncode={},code={}", imageCode, imageCheckCode);
            return APIResponse.createResult(CodeEnum.IMAGE_CODE_ERROR);
        }
        //清空验证码
        session.removeAttribute("checkCode");

        //手机号是否已注册
        UserResult userResult = userService.getUserBaseInfo(mobile);
        if (userResult.isFailed()) {
            logger.error("获取用户手机验证码失败,查询用户失败,mobile={},result={}", mobile, userResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), userResult.getErrorMsg());
        }
        if (userResult.getUser() == null) {
            logger.error("获取用户手机验证码失败,手机号不存在,mobile={}", mobile);
            return APIResponse.createResult(CodeEnum.MOBILE_NOT_EXISTS);
        }

        //3.调用rpc服务
        CheckCodeParam checkCodeParam = new CheckCodeParam();
        checkCodeParam.setMobile(mobile);
        UserResult sendSmsCheckCodeResult = userService.sendSmsCheckCode(checkCodeParam);
        if (sendSmsCheckCodeResult.isFailed()) {
            logger.error("获取用户手机验证码失败,mobile={},result={}", mobile, sendSmsCheckCodeResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), sendSmsCheckCodeResult.getErrorMsg());
        }

        String code = sendSmsCheckCodeResult.getSmsCheckCode();
        result.setData(code);

        return result;

    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<UserVO> login(@RequestBody UserLoginReq userLoginReq, HttpServletRequest request, HttpServletResponse response) {
        APIResponse<UserVO> result = new APIResponse<>();

        //1.验证手机号格式
        String mobile = userLoginReq.getUsername();
        MobileValidParam mobileValidParam = new MobileValidParam();
        mobileValidParam.setMobile(mobile);
        Result validResult = mobileValidator.valid(mobileValidParam);
        if (validResult.isFailed()) {
            logger.error("登录失败,手机号参数验证失败,mobile={},error={}", mobile, validResult.getErrorMsg());
            return APIResponse.createResult(CodeEnum.PARAM_ERROR.getCode(), validResult.getErrorMsg());
        }
        String pass = userLoginReq.getPass();
        if (StringUtils.isBlank(pass)) {
            logger.error("登录失败,密码为空");
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //验证客户端安全调用
        /*ClientTypeEnum clientTypeEnum = ClientTypeEnum.findByCode(HttpRequestUtil.getClientPlat(request));
        if (clientTypeEnum == ClientTypeEnum.UNKNOWN) {
            logger.error("客户端访问类型查询失败,mobile={}", mobile);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), "客户端访问类型获取失败");
        }*/

        //调用rpc服务
        LoginParam loginParam = new LoginParam();
        loginParam.setUsername(mobile);
        loginParam.setPassword(pass);
        loginParam.setCheckCode(userLoginReq.getCheckCode());
        UserResult loginResult = userService.login(loginParam);
        if (loginResult.isFailed()) {
            logger.error("用户登录返回失败,mobile={},result={}", mobile, loginResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), loginResult.getErrorMsg());
        }

        User user = loginResult.getUser();

        //6.设置VO
        UserVO userVO = new UserVO();
        userVO.setUserId(user.getId());
        userVO.setMobile(user.getMobile());
        userVO.setNick(user.getNick());
        userVO.setUserToken(user.getUserToken());
        result.setData(userVO);

        addCookie(response, loginResult.getToken());
        return result;

    }

    @RequestMapping(value = "/autologin", method = {RequestMethod.GET}, produces = {"application/json"})
    public ModelAndView autoLogin(@RequestParam(name = "token") String token,
                                  HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();

        if (StringUtils.isBlank(token)) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

        UserResult userResult = userService.autoLogin(token);
        if (userResult.isFailed()) {
            logger.error("用户自动登录返回失败,token={},result={}", token, userResult);
            mv.addObject("error", userResult.getErrorMsg());
            mv.setViewName(ViewConstant.VIEW_LOGIN);
            return mv;
        }

        addCookie(response, token);
        //设置view
//        mv.addObject(ViewConstant.PARAM_USER, user);

        StringBuilder dispatcherPath = new StringBuilder("redirect:");
        dispatcherPath.append("../").append(ViewConstant.VIEW_INDEX);
        mv.setViewName(dispatcherPath.toString());
        //执行跳转
        return mv;

    }

    //创建cookie，并将新cookie添加到“响应对象”response中。
    public void addCookie(HttpServletResponse response, String usertoken) {
        Cookie cookie = new Cookie(HttpConstant.COOKIE_TOKEN, usertoken);//创建新cookie
        cookie.setMaxAge(60 * 60 * 2); // 设置存在时间为5分钟
        cookie.setPath("/"); //设置作用域
        response.addCookie(cookie); //将cookie添加到response的cookie数组中返回给客户端
    }

    @RequestMapping(value = "/register", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<UserVO> register(@RequestBody UserRegisterReq userRegisterReq, HttpServletRequest request, HttpServletResponse response) {
        APIResponse<UserVO> result = new APIResponse<>();

        //1.验证手机号格式
        String mobile = userRegisterReq.getPhone();
        MobileValidParam mobileValidParam = new MobileValidParam();
        mobileValidParam.setMobile(mobile);
        Result validResult = mobileValidator.valid(mobileValidParam);
        if (validResult.isFailed()) {
            logger.error("手机号参数验证失败,mobile={},error={}", mobile, validResult.getErrorMsg());
            return APIResponse.createResult(CodeEnum.PARAM_ERROR.getCode(), validResult.getErrorMsg());
        }

        //2.验证密码格式
        mobileValidParam.setMatchedString(userRegisterReq.getPass());
        validResult = loginPasswordValidator.valid(mobileValidParam);
        if (validResult.isFailed()) {
            logger.error("密码参数验证失败,mobile={},error={}", mobile, validResult.getErrorMsg());
            return APIResponse.createResult(CodeEnum.PARAM_ERROR.getCode(), validResult.getErrorMsg());
        }

        //3.验证客户端安全调用
        /*ClientTypeEnum clientTypeEnum = ClientTypeEnum.findByCode(HttpRequestUtil.getClientPlat(request));
        if (clientTypeEnum == ClientTypeEnum.UNKNOWN) {
            logger.error("客户端访问类型查询失败,mobile={}", mobile);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), "客户端访问类型获取失败");
        }*/

        //4.调用注册rpc服务
        RegisterParam registerParam = new RegisterParam();
        registerParam.setMobile(mobile);
        registerParam.setCheckCode(userRegisterReq.getCheckCode());
        registerParam.setClientType("3");
        registerParam.setPassword(userRegisterReq.getPass());
        registerParam.setConfirmPassword(userRegisterReq.getRepass());
        registerParam.setReferrerMobile(userRegisterReq.getReferrerMobile());
        UserResult registerResult = userService.register(registerParam);
        if (registerResult.isFailed()) {
            logger.error("用户注册返回失败,mobile={},result={}", mobile, registerResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), registerResult.getErrorMsg());
        }

        User user = registerResult.getUser();

        //6.设置VO
        UserVO userVO = new UserVO();
        userVO.setUserId(user.getId());
        userVO.setMobile(user.getMobile());
        userVO.setNick(user.getNick());
        userVO.setUserToken(user.getUserToken());
        result.setData(userVO);

        addCookie(response, registerResult.getToken());

        return result;

    }

    @RequestMapping(value = "/forgetPwd", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<UserVO> forgetPwd(@RequestBody UserForgetPwdReq req, HttpServletRequest request) {
        APIResponse<UserVO> result = new APIResponse<>();

        //1.验证手机号格式
        String mobile = req.getPhone();
        MobileValidParam mobileValidParam = new MobileValidParam();
        mobileValidParam.setMobile(mobile);
        Result validResult = mobileValidator.valid(mobileValidParam);
        if (validResult.isFailed()) {
            logger.error("手机号参数验证失败,mobile={},error={}", mobile, validResult.getErrorMsg());
            return APIResponse.createResult(CodeEnum.PARAM_ERROR.getCode(), validResult.getErrorMsg());
        }

        //2.验证密码格式
        mobileValidParam.setMatchedString(req.getPass());
        validResult = loginPasswordValidator.valid(mobileValidParam);
        if (validResult.isFailed()) {
            logger.error("密码参数验证失败,mobile={},error={}", mobile, validResult.getErrorMsg());
            return APIResponse.createResult(CodeEnum.PARAM_ERROR.getCode(), validResult.getErrorMsg());
        }

        //3.调用rpc服务
        RefindPasswordParam refindPasswordParam = new RefindPasswordParam();
        refindPasswordParam.setMobile(mobile);
        refindPasswordParam.setCheckCode(req.getCheckCode());
        refindPasswordParam.setPassword(req.getPass());
        refindPasswordParam.setConfirmPassword(req.getRepass());
        UserResult userResult = userService.refindPassword(refindPasswordParam);
        if (userResult.isFailed()) {
            logger.error("忘记密码重置返回失败,mobile={},result={}", mobile, userResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), userResult.getErrorMsg());
        }

        return result;
    }

    @RequestMapping(value = "/resetPwd", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<UserVO> resetPwd(@RequestBody UserResetPwdReq req, HttpServletRequest request) {
        APIResponse<UserVO> result = new APIResponse<>();

        Long userId = req.getUserId();
        //验证密码格式
        MobileValidParam mobileValidParam = new MobileValidParam();
        mobileValidParam.setMatchedString(req.getNewPassword());
        Result validResult = loginPasswordValidator.valid(mobileValidParam);
        if (validResult.isFailed()) {
            logger.error("密码参数验证失败,userId={},error={}", userId, validResult.getErrorMsg());
            return APIResponse.createResult(CodeEnum.PARAM_ERROR.getCode(), validResult.getErrorMsg());
        }

        //调用rpc服务
        ResetPasswordParam param = new ResetPasswordParam();
        param.setOldPassword(req.getPassword());
        param.setNewPassword(req.getNewPassword());
        param.setUserId(userId);
        UserResult userResult = userService.resetPassword(param);
        if (userResult.isFailed()) {
            logger.error("密码重置返回失败,userId={},result={}", userId, userResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), userResult.getErrorMsg());
        }

        return result;
    }

    /**
     * 保存用户基本信息
     */
    @RequestMapping(value = "/saveUserInfo", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<Void> saveUserInfo(@RequestBody SaveUserInfoReq saveUserInfoReq,
                                          HttpServletRequest request, HttpServletResponse response) {
        APIResponse<Void> result = new APIResponse<>();

        Long userId = saveUserInfoReq.getUserId();
        if (userId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        String nick = saveUserInfoReq.getNick();
        if (StringUtils.isBlank(nick)) nick = User.NICK_DEFAULT;
        User user = new User();
        user.setId(userId);
        user.setNick(nick);

        //调用rpc服务

        UserResult modifyResult = userService.modifyUser(user);
        if (modifyResult.isFailed()) {
            logger.error("保存用户个人信息返回失败,param={}", JSONObject.toJSONString(saveUserInfoReq));
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), modifyResult.getErrorMsg());
        }

        return result;

    }

    /**
     * 修改个人头像
     * 仅支持一个头像上传
     */
    @RequestMapping(value = "/changeHeadImg/{userId}", method = {RequestMethod.POST})
    public APIResponse<HeadImgVO> changeHeadImg(@PathVariable Long userId,
                                                @RequestParam("userFace") CommonsMultipartFile[] userFace,
                                                HttpServletRequest request) {
        APIResponse<HeadImgVO> response = new APIResponse<>();

        if (userFace == null || userFace.length == 0 || userId == null) {
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        //上传头像
        User user = new User();
        user.setId(userId);
        CommonsMultipartFile userFaceItem = userFace[0];
        try {
            InputStream is = userFaceItem.getInputStream();
            byte[] fileData = new byte[(int) userFaceItem.getSize()];
            is.read(fileData);

            ChangeHeadImgParam changeHeadImgParam = new ChangeHeadImgParam();
            changeHeadImgParam.setUserId(userId);
            changeHeadImgParam.setFileName(userFaceItem.getOriginalFilename());
            changeHeadImgParam.setContent(fileData);

            UserResult userResult = userService.changeHeadImg(changeHeadImgParam);
            if (userResult.isFailed()) {
                logger.error("上传头像失败,userId={}", userId);
                return APIResponse.createResult(CodeEnum.FAILED.getCode(), userResult.getErrorMsg());
            }
            HeadImgVO headImgVO = new HeadImgVO();
            headImgVO.setImgOrgBase64(userResult.getHeadImgOrgBase64());
            headImgVO.setImgBase64(userResult.getHeadImgBase64());
            response.setData(headImgVO);
        } catch (Exception e) {
            logger.error("upload file failed,userId=" + userId, e);
            return APIResponse.createResult(CodeEnum.INTERNET_FAIL);
        }

        return response;
    }

    /**
     * 退出
     */
    @RequestMapping(value = "/logout", method = {RequestMethod.GET})
    public APIResponse<Void> logout(@RequestParam("userId") Long userId, HttpServletRequest request) {

        APIResponse<Void> response = new APIResponse<>();
//        StringBuilder dispatcherPath = new StringBuilder("redirect:");

        //退出业务
        LogoutParam param = new LogoutParam();
        param.setUserId(userId);
        String token = getCookie(request, HttpConstant.COOKIE_TOKEN);
        param.setToken(token);
        UserResult userResult = userService.logout(param);

        if (userResult.isFailed()) {
            logger.error("退出失败失败,userId={},result={}", userId, userResult);
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), userResult.getErrorMsg());
        }

//        dispatcherPath.append(ViewConstants.VIEW_LOGIN);
//        mv.setViewName(dispatcherPath.toString());
        return response;

    }

    private String getCookie(HttpServletRequest request, String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie c : cookies) {
            if (name.equalsIgnoreCase(c.getName())) {
                logger.info("getCookie success name={}, value={}", name, c.getValue());
                return c.getValue();
            }
        }
        logger.info("getCookie result is null, name={}", name);
        return null;
    }
}
