package com.invest.ivgateway.controller;

import com.invest.ivcommons.constant.ViewConstant;
import com.invest.ivcommons.util.beans.BeanUtils;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivcommons.util.format.NumberUtil;
import com.invest.ivppad.biz.service.ppdopenapi.LoanService;
import com.invest.ivuser.biz.service.*;
import com.invest.ivuser.common.UserPolicyStatusEnum;
import com.invest.ivuser.common.UserPolicyTypeEnum;
import com.invest.ivuser.model.entity.*;
import com.invest.ivuser.model.param.UserLoanRecordParam;
import com.invest.ivuser.model.param.UserNotifyMessageParam;
import com.invest.ivuser.model.param.UserPolicyParam;
import com.invest.ivuser.model.param.UserThirdParam;
import com.invest.ivuser.model.result.*;
import com.invest.ivuser.model.vo.UserVO;
import com.invest.ivusergateway.common.constants.CodeEnum;
import com.invest.ivusergateway.model.response.APIResponse;
import com.invest.ivusergateway.model.vo.NotifyMessageVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * Created by xugang on 2017/8/11.
 */
@RestController
@RequestMapping(value = "/")
public class CommonController extends BaseController {

    @Autowired
    private UserService userService;
    @Resource
    private LoanService loanService;
    @Resource
    private UserPolicyService userPolicyService;
    @Resource
    private UserLoanService userLoanService;
    @Resource
    private UserThirdService userThirdService;
    @Resource
    private MessageService messageService;

    @Resource
    private UserAccountService userAccountService;

    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();

        //获取userId
        Long userId = getSessionUserId(request);
        if (userId == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "获取userId失败");
            return mv;
        }

        UserResult userResult = userService.getUserBaseInfo(userId);
        if (userResult.isFailed()) {
            logger.error("进入首页失败,userId={},result={}", userId, userResult);
            mv.addObject("error", userResult.getErrorMsg());
            mv.setViewName(ViewConstant.VIEW_LOGIN);
            return mv;
        }

        User user = userResult.getUser();

        boolean showWelcomeTipData = user.getLastLoginTime() == null;
        //更新登录时间 防止刷新页面重复弹出提示
        if (showWelcomeTipData) {
            User userDB = new User();
            userDB.setLastLoginTime(DateUtil.getCurrentDatetime());
            userDB.setCreateTime(null);
            userDB.setId(userId);
            userService.modifyUser(userDB);
        }

        //获取专家提醒
        UserNotifyMessageParam param = new UserNotifyMessageParam();
        param.setStatus(UserNotifyMessage.STATUS_UNREAD);
        param.setUserId(userId);
        UserNotifyMessageResult notifyMessageResult = messageService.getUserNotifyMessageCount(param);
        int messageCount = notifyMessageResult.getCount();

        user.clearPwd();
        //设置view
        mv.addObject(ViewConstant.PARAM_USER, user);
        mv.addObject(ViewConstant.PARAM_SHOWWELCOMETIPDATA, String.valueOf(showWelcomeTipData));
        mv.addObject(ViewConstant.PARAM_MESSAGECOUNT, String.valueOf(messageCount));
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.setViewName(ViewConstant.VIEW_INDEX);
        //执行跳转
        return mv;
    }

    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView();
        //设置view
        mv.setViewName(ViewConstant.VIEW_LOGIN);
        //执行跳转
        return mv;
    }

    @RequestMapping(value = "/register", method = {RequestMethod.GET})
    public ModelAndView register() {
        ModelAndView mv = new ModelAndView();
        //设置view
        mv.setViewName(ViewConstant.VIEW_REGISTER);
        //执行跳转
        return mv;
    }

    @RequestMapping(value = "/forgetPassword", method = {RequestMethod.GET})
    public ModelAndView forgetPassword() {
        ModelAndView mv = new ModelAndView();
        //设置view
        mv.setViewName(ViewConstant.VIEW_FORGETPASSWORD);
        //执行跳转
        return mv;
    }

    /**
     * 个人信息设置页面
     *
     * @return
     */
    @RequestMapping(value = "/setuserinfoview", method = {RequestMethod.GET})
    public ModelAndView setserinfovView(@RequestParam(value = "userId") Long userId,
                                        HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if (userId == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

        UserResult userResult = userService.getUserBaseInfo(userId);
        if (userResult.isFailed()) {
            logger.error("个人信息失败,userId={},result={}", userId, userResult);
            mv.addObject("error", userResult.getErrorMsg());
            mv.setViewName(ViewConstant.VIEW_LOGIN);
            return mv;
        }

        User user = userResult.getUser();
        user.clearPwd();
        //设置view
        mv.addObject(ViewConstant.PARAM_USER, user);
        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.setViewName(ViewConstant.VIEW_USER_SETINFO);
        //执行跳转
        return mv;
    }

    /**
     * resetpwdview
     */
    @RequestMapping(value = "/resetpwdview", method = {RequestMethod.GET})
    public ModelAndView resetpwdView(@RequestParam(value = "userId") Long userId,
                                     HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if (userId == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

        UserResult userResult = userService.getUserBaseInfo(userId);
        if (userResult.isFailed()) {
            logger.error("个人信息失败,userId={},result={}", userId, userResult);
            mv.addObject("error", userResult.getErrorMsg());
            mv.setViewName(ViewConstant.VIEW_LOGIN);
            return mv;
        }

        User user = userResult.getUser();
        user.clearPwd();
        //设置view
        mv.addObject(ViewConstant.PARAM_USER, user);
        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.setViewName(ViewConstant.VIEW_RESET_PASWWORD);
        //执行跳转
        return mv;
    }

    /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = "/mainview", method = {RequestMethod.GET})
    public ModelAndView mainView(@RequestParam(value = "userId") Long userId,
                                 HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if (userId == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

        UserResult userResult = userService.getUserBaseInfo(userId);
        if (userResult.isFailed()) {
            logger.error("首页失败,userId={},result={}", userId, userResult);
            mv.addObject("error", userResult.getErrorMsg());
            mv.setViewName(ViewConstant.VIEW_LOGIN);
            return mv;
        }

        User user = userResult.getUser();
        user.clearPwd();
        //设置view
        UserVO userVO = buildUserVO(user);
        mv.addObject(ViewConstant.PARAM_USER, userVO);
        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.setViewName(ViewConstant.VIEW_MAIN);
        //执行跳转
        return mv;
    }

    private UserVO buildUserVO(User user) {
        Long userId = user.getId();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setLastLoginTimeFormat(DateUtil.dateToString(user.getLastLoginTime(), DateUtil.DATE_FORMAT_DATETIME_COMMON));
        //今日投标个数
        UserLoanRecordParam param = new UserLoanRecordParam();
        param.setUserId(userId);
        param.setBidLoanBeginTime(DateUtil.truncate2Day(new Date(System.currentTimeMillis())));
        UserLoanResult userLoanResult = userLoanService.getUserLoanRecordCount(param);
        userVO.setBidCountToday(userLoanResult.getCount());
        //系统散标策略个数
        UserPolicyParam userPolicyParam = new UserPolicyParam();
        userPolicyParam.setUserId(userId);
        userPolicyParam.setPolicyType(UserPolicyTypeEnum.SYS_LOAN_POLICY.getCode());
        userPolicyParam.setStatus(UserPolicyStatusEnum.start.getCode());
        UserPolicyResult userPolicyResult = userPolicyService.getUserPolicyCount(userPolicyParam);
        userVO.setSysLoanPolicyCount(userPolicyResult.getUserPolicyCount());
        //自定义散标策略个数
        userPolicyParam.setPolicyType(UserPolicyTypeEnum.USER_LOAN_POLICY.getCode());
        userPolicyResult = userPolicyService.getUserPolicyCount(userPolicyParam);
        userVO.setLoanPolicyCount(userPolicyResult.getUserPolicyCount());
        //捉宝币余额
        UserAccountResult userAccountResult = userAccountService.getUserAccount(userId);
        if (userAccountResult.isFailed()) {
            logger.error("查询用户账户信息失败,userId={},error={}", userId, userAccountResult.getErrorMsg());
        } else {
            String pattern = "###,###,###";
            userVO.setZhuobaoBalance(NumberUtil.formatPattern(Double.valueOf(userAccountResult.getUserAccount().getZhuobaoBalance()), pattern));
        }

        userVO.setLoanPolicyCount(userPolicyResult.getUserPolicyCount());
        //绑定用户数
        UserThirdParam userThirdParam = new UserThirdParam();
        userThirdParam.setUserId(userId);
        UserThirdResult userThirdResult = userThirdService.getUserThirdBindInfo(userThirdParam);
        List<UserThirdBindInfo> userThirdBindInfos = userThirdResult.getUserThirdBindInfos();
        if (CollectionUtils.isNotEmpty(userThirdBindInfos)) userVO.setBindUserCount(userThirdBindInfos.size());
        return userVO;
    }

    /**
     * 用户通知消息查询
     */
    @RequestMapping(value = "/usernotifymessageview", method = {RequestMethod.GET})
    public ModelAndView userNotifyMessageView(@RequestParam(value = "userId") Long userId) {
        ModelAndView mv = new ModelAndView();
        if (userId == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.setViewName(ViewConstant.VIEW_USERNOTIFYMESSAGEVIEW);
        //执行跳转
        return mv;
    }

    /**
     * 用户通知消息查询
     */
    @RequestMapping(value = "/usernotifymessagedetailview", method = {RequestMethod.GET})
    public ModelAndView userNotifyMessageDetailView(@RequestParam(value = "userId") Long userId,
                                                    @RequestParam(name = "messageId") Long messageId,
                                                    @RequestParam(name = "userMessageId") Long userMessageId) {
        ModelAndView mv = new ModelAndView();
        if (userId == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

        //调用rpc服务
        UserNotifyMessageResult notifyMessageResult = messageService.getNotifyMessageById(messageId);
        if (notifyMessageResult.isFailed()) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

        //验证用户是否存在
        UserResult userResult = userService.getUserBaseInfo(userId);
        if (userResult.isFailed()) {
            logger.error("查询用户失败,userId={}", userId);
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "查询用户失败");
            return mv;
        }

        User user = userResult.getUser();
        if (user == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "用户不存在");
            logger.error("用户不存在,userId={}", userId);
            return mv;
        }

        NotifyMessage notifyMessage = notifyMessageResult.getNotifyMessage();
        UserNotifyMessageResult userNotifyMessageResult = messageService.getUserNotifyMessageById(userMessageId);
        if (userNotifyMessageResult.isFailed()) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "查询失败");
        }
        UserNotifyMessage userNotifyMessage = userNotifyMessageResult.getUserNotifyMessage();

        notifyMessage.setCreateTimeFormat(DateUtil.dateToString(userNotifyMessage.getCreateTime(), DateUtil.DATE_FORMAT_DATETIME_COMMON));
        notifyMessage.setContent(String.format(notifyMessage.getContent(), userNotifyMessage.getMessageValue()));

        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.addObject(ViewConstant.PARAM_MOBILE, user.getMobile());
        mv.addObject(ViewConstant.PARAM_NOTIFYMESSAGE, notifyMessage);
        mv.setViewName(ViewConstant.VIEW_USERNOTIFYMESSAGEDETAILVIEW);
        //执行跳转
        return mv;
    }

    /**
     * 用户通知消息查询
     */
    @RequestMapping(value = "/accountinfoview", method = {RequestMethod.GET})
    public ModelAndView accountInfoView(@RequestParam(value = "userId") Long userId) {
        ModelAndView mv = new ModelAndView();
        if (userId == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.setViewName(ViewConstant.VIEW_ACCOUNTINFOVIEW);
        //执行跳转
        return mv;
    }

    /**
     * 逾期数据分析
     */
    @RequestMapping(value = "/overduedataview", method = {RequestMethod.GET})
    public ModelAndView overdueDataView(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, getSessionUserId(request));
        mv.setViewName(ViewConstant.VIEW_OVERDUEANALYSISVIEW);
        //执行跳转
        return mv;
    }

    /**
     * 逾期数据分析-详情
     */
    @RequestMapping(value = "/overduedatadetailview", method = {RequestMethod.GET})
    public ModelAndView overdueDataDetailView(@RequestParam(value = "analysisId") Long analysisId,
                                              @RequestParam(value = "overdueType") Short overdueType,
                                              HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, getSessionUserId(request));
        mv.addObject(ViewConstant.PARAM_ANALYSISID, analysisId);
        mv.addObject(ViewConstant.PARAM_OVERDUETYPE, overdueType);
        mv.setViewName(ViewConstant.VIEW_OVERDUEANALYSISDETAILVIEW);
        //执行跳转
        return mv;
    }
}
