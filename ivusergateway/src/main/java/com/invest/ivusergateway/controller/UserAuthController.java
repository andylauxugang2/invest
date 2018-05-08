package com.invest.ivusergateway.controller;

import com.invest.ivcommons.constant.ViewConstant;
import com.invest.ivcommons.redis.client.cacheclient.CacheClientHA;
import com.invest.ivcommons.util.format.MoneyUtil;
import com.invest.ivppad.biz.service.ppdopenapi.AccountService;
import com.invest.ivppad.biz.service.ppdopenapi.AuthService;
import com.invest.ivppad.model.result.PPDOpenApiAccountResult;
import com.invest.ivppad.model.result.PPDOpenApiUserAuthResult;
import com.invest.ivuser.biz.service.UserPolicyService;
import com.invest.ivuser.biz.service.UserService;
import com.invest.ivuser.biz.service.UserThirdService;
import com.invest.ivuser.model.entity.User;
import com.invest.ivuser.model.entity.UserThirdBindInfo;
import com.invest.ivuser.model.param.UserThirdParam;
import com.invest.ivuser.model.result.UserPolicyResult;
import com.invest.ivuser.model.result.UserResult;
import com.invest.ivuser.model.result.UserThirdResult;
import com.invest.ivuser.model.vo.ThirdUserInfoVO;
import com.invest.ivusergateway.base.BaseController;
import com.invest.ivusergateway.common.constants.CodeEnum;
import com.invest.ivusergateway.model.request.UserBaseReq;
import com.invest.ivusergateway.model.response.APIResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xugang on 2016/9/6.
 */
@RestController
@RequestMapping(value = "/userauth")
public class UserAuthController extends BaseController {

    @Resource
    private AuthService authService;
    @Resource
    private UserPolicyService userPolicyService;
    @Resource
    private UserThirdService userThirdService;
    @Resource
    private UserService userService;
    @Resource(name = "cacheClientHA")
    protected CacheClientHA cacheClientHA;
    @Resource
    private AccountService accountService;

    @RequestMapping(value = "/login", method = {RequestMethod.POST}, produces = {"application/json"})
    public APIResponse<String> login(@RequestBody UserBaseReq userBaseReq,
                                     HttpServletRequest request, HttpServletResponse response) {
        Long userId = userBaseReq.getUserId();
        if (userId == null) {
            logger.error("userId参数为空");
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        UserResult userResult = userService.getUserBaseInfo(userId);
        if (userResult.isFailed()) {
            logger.error("授权跳转失败,查询用户失败,userId={},result={}", userId, userResult);
            return APIResponse.createResult(CodeEnum.SESSION_EXPIRED.getCode(), userResult.getErrorMsg());
        }

        APIResponse<String> result = new APIResponse<>();
        String loginUrl = authService.getUserAuthUrl("http://www.biaozhuanjia.com/userauth/callback?userId=" + userId);
        result.setData(loginUrl);
        logger.info("用户授权登录地址,loginUrl={}", loginUrl);
        return result;
    }

    @RequestMapping(value = "/gologin", method = {RequestMethod.GET}, produces = {"application/json"})
    public ModelAndView goLogin(@RequestParam(name = "userId") Long userId,
                                HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        if (userId == null) {
            logger.error("userId参数为空");
            mv.setViewName(ViewConstant.VIEW_ERROR);
            return mv;
        }

        UserResult userResult = userService.getUserBaseInfo(userId);
        if (userResult.isFailed()) {
            logger.error("授权跳转失败,查询用户失败,userId={},result={}", userId, userResult);
            mv.addObject("error", userResult.getErrorMsg());
            mv.setViewName(ViewConstant.VIEW_ERROR);
        }

        String loginUrl = authService.getUserAuthUrl("http://www.biaozhuanjia.com/userauth/callback?userId=" + userId);
        logger.info("用户授权登录地址,loginUrl={}", loginUrl);

        //设置view
        StringBuilder dispatcherPath = new StringBuilder("redirect:");
        dispatcherPath.append(loginUrl);
        mv.setViewName(dispatcherPath.toString());
        return mv;
    }

    @RequestMapping(value = "/callback", method = {RequestMethod.GET}, produces = {"application/json"})
    public ModelAndView callback(@RequestParam(name = "code") String code,
                                 @RequestParam(name = "userId") Long userId,
                                 HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();

        //1.验证授权码
        if (StringUtils.isBlank(code)) {
            logger.error("用户授权回调失败,参数:授权码为空");
            mv.setViewName(ViewConstant.VIEW_ERROR);
            return mv;
        }

        if (userId == null) {
            logger.error("用户授权回调失败,参数:userId为空");
            mv.setViewName(ViewConstant.VIEW_ERROR);
            return mv;
        }

        if (userId == null) {
            mv.setViewName(ViewConstant.VIEW_LOGIN);
            return mv;
        }
        UserResult userResult = userService.getUserBaseInfo(userId);
        if (userResult.isFailed()) {
            logger.error("授权回调失败,用户不存在,userId={},result={}", userId, userResult);
            mv.addObject("error", userResult.getErrorMsg());
            mv.setViewName(ViewConstant.VIEW_ERROR);
            return mv;
        }

        User user = userResult.getUser();

        //2.调用rpc服务 处理回调 获取token 获取username
        PPDOpenApiUserAuthResult userAuthResult = authService.authorize(code, userId);
        if (userAuthResult.isFailed()) {
            logger.error("用户授权回调处理失败,code={},error={}", code, userAuthResult);
            mv.setViewName(ViewConstant.VIEW_ERROR);
        }

        String accessToken = userAuthResult.getAccessToken();
        String ppdUsername = userAuthResult.getUserName();
        logger.info("用户授权回调处理成功,ppdUsername={},accessToken={}", ppdUsername, accessToken);

        //设置view
        /*mv.addObject(ViewConstant.PARAM_USER, user);
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.addObject(ViewConstant.VIEW_MODLE_USERNAMENAME, ppdUsername);
        mv.setViewName("forward:" + ViewConstant.VIEW_ROOT + ViewConstant.VIEW_INDEX);*/
        //设置view
        StringBuilder dispatcherPath = new StringBuilder("redirect:");
        dispatcherPath.append("../").append(ViewConstant.VIEW_INDEX);
        mv.setViewName(dispatcherPath.toString());

        //执行跳转
        return mv;
    }

    /**
     * 第三方用户授权信息查询
     */
    @RequestMapping(value = "/thirdAuthInfo", method = {RequestMethod.GET}, produces = {"application/json"})
    public APIResponse<List<UserThirdBindInfo>> thirdAuthInfo(@RequestParam(value = "userId") Long userId,
                                                              HttpServletRequest request, HttpServletResponse response) {
        if (userId == null) {
            logger.error("userId参数为空错误");
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }
        APIResponse<List<UserThirdBindInfo>> result = new APIResponse<>();
        UserThirdParam param = new UserThirdParam();
        param.setUserId(userId);
        UserThirdResult userThirdResult = userThirdService.getUserThirdBindInfo(param);
        if (userThirdResult.isFailed()) {
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), userThirdResult.getErrorMsg());
        }

        List<UserThirdBindInfo> userThirdBindInfos = userThirdResult.getUserThirdBindInfos();
        if (CollectionUtils.isEmpty(userThirdBindInfos)) userThirdBindInfos = new ArrayList<>(0);
        result.setData(userThirdBindInfos);
        return result;

    }

    /**
     * 第三方用户授权信息 解绑
     */
    @RequestMapping(value = "/delThirdAuthInfo", method = {RequestMethod.GET}, produces = {"application/json"})
    public APIResponse<Void> delThirdAuthInfo(@RequestParam(value = "id") Long id,
                                              @RequestParam(value = "userId") Long userId,
                                              HttpServletRequest request) {
        if (id == null || userId == null) {
            logger.error("参数为空错误");
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }

        APIResponse<Void> result = new APIResponse<>();
        //删除绑定信息
        UserThirdResult userThirdResult = userThirdService.removeUserThirdBindInfo(userId, id);
        if (userThirdResult.isFailed()) {
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), userThirdResult.getErrorMsg());
        }

        //删除redis token
        UserThirdBindInfo userThirdBindInfo = userThirdResult.getUserThirdBindInfo();
        String thirdUserUUID = userThirdBindInfo.getThirdUserUUID();
        authService.delPPDAccessToken(thirdUserUUID);

        //删除所有主策略
        UserPolicyResult userPolicyResult = userPolicyService.removeUserMainPolicy(userId, thirdUserUUID, null);
        if (userPolicyResult.isFailed()) {
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), userThirdResult.getErrorMsg());
        }

        return result;
    }

    /**
     * 第三方用户信息查询
     */
    @RequestMapping(value = "/getThirdUserInfo", method = {RequestMethod.GET}, produces = {"application/json"})
    public APIResponse<List<ThirdUserInfoVO>> getThirdUserInfo(@RequestParam(value = "userId") Long userId,
                                                               HttpServletRequest request) {
        if (userId == null) {
            logger.error("userId参数为空错误");
            return APIResponse.createResult(CodeEnum.PARAM_ERROR);
        }
        APIResponse<List<ThirdUserInfoVO>> result = new APIResponse<>();
        List<ThirdUserInfoVO> data = new ArrayList<>();

        //查询该用户所有第三方账户
        UserThirdParam param = new UserThirdParam();
        param.setUserId(userId);
        UserThirdResult userThirdResult = userThirdService.getUserThirdBindInfo(param);
        if (userThirdResult.isFailed()) {
            return APIResponse.createResult(CodeEnum.FAILED.getCode(), userThirdResult.getErrorMsg());
        }

        List<UserThirdBindInfo> userThirdBindInfos = userThirdResult.getUserThirdBindInfos();
        if (CollectionUtils.isEmpty(userThirdBindInfos)) {
            result.setData(data);
            return result;
        }

        userThirdBindInfos.forEach(o -> {
            ThirdUserInfoVO vo = new ThirdUserInfoVO();
            String userName = o.getThirdUserUUID();
            vo.setThirdUserUUID(userName);
            //查询余额
            PPDOpenApiAccountResult accountResult = accountService.getAccountBalance(userName);
            if (accountResult.isSuccess()) {
                int balance = accountResult.getBalance(); //分
                String balanceRMB = MoneyUtil.formatMoneyRMB(balance);
                vo.setThirdUserBalance(balanceRMB);
            } else {
                vo.setThirdUserBalance(ThirdUserInfoVO.ERROR_THIRDUSERBALANCE);
            }
            data.add(vo);
        });

        result.setData(data);
        return result;

    }
}
