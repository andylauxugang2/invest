package com.invest.ivgateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivcommons.constant.ViewConstant;
import com.invest.ivcommons.util.date.DateUtil;
import com.invest.ivcommons.util.format.NumberUtil;
import com.invest.ivppad.common.*;
import com.invest.ivppad.common.SchoolTypeEnum;
import com.invest.ivuser.biz.service.UserPolicyService;
import com.invest.ivuser.biz.service.UserService;
import com.invest.ivuser.biz.service.UserThirdService;
import com.invest.ivuser.common.LoanRiskLevelEnum;
import com.invest.ivuser.common.SexEnum;
import com.invest.ivuser.model.entity.LoanPolicy;
import com.invest.ivuser.model.entity.User;
import com.invest.ivuser.model.result.UserPolicyResult;
import com.invest.ivuser.model.result.UserResult;
import com.invest.ivuser.model.vo.UserMainPolicyVO;
import com.invest.ivusergateway.common.constants.CodeEnum;
import com.invest.ivusergateway.model.response.APIResponse;
import com.invest.ivusergateway.model.vo.LoanPolicyVO;
import com.invest.ivusergateway.model.vo.UserPolicyDetailVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xugang on 2017/8/11.
 */
@RestController(value = "investgatewayPolicyController")
@RequestMapping(value = "/ivgatepolicy")
public class PolicyController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserPolicyService userPolicyService;
    @Resource
    private UserThirdService userThirdService;

    /**
     * 查看第三方授权信息页面
     *
     * @return
     */
    @RequestMapping(value = "/mainpolicyview", method = {RequestMethod.GET})
    public ModelAndView mainPolicyView(@RequestParam(value = "userId") Long userId) {
        ModelAndView mv = new ModelAndView();
        if (userId == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.setViewName(ViewConstant.VIEW_MAINPOLICY);
        //执行跳转
        return mv;
    }

    /**
     * 查看第三方授权信息页面
     *
     * @return
     */
    @RequestMapping(value = "/setusermainpolicyview", method = {RequestMethod.GET})
    public ModelAndView setUserMainPolicyView(@RequestParam(value = "userId") Long userId,
                                              @RequestParam(value = "thirdUserUUID") String thirdUserUUID,
                                              @RequestParam(value = "mainPolicyId") Long mainPolicyId) {
        ModelAndView mv = new ModelAndView();
        if (userId == null || StringUtils.isBlank(thirdUserUUID) || mainPolicyId == null) {
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

        //如果已设置过策略 回显
        UserMainPolicyVO userMainPolicyVO = userPolicyService.getUserMainPolicy(userId, thirdUserUUID, mainPolicyId);
        if (userMainPolicyVO == null) {
            userMainPolicyVO = new UserMainPolicyVO();
        }
        //设置view
        mv.addObject(ViewConstant.PARAM_USERMAINPOLICYVO, userMainPolicyVO);
        mv.addObject(ViewConstant.PARAM_MOBILE, user.getMobile());
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.addObject(ViewConstant.PARAM_NICK, user.getNick());
        mv.addObject(ViewConstant.PARAM_THIRDUSERUUID, thirdUserUUID);
        mv.addObject(ViewConstant.PARAM_MAINPOLICYID, mainPolicyId);
        mv.setViewName(ViewConstant.VIEW_MAINPOLICY_SET);
        //执行跳转
        return mv;
    }

    /**
     * 查看用户选择的系统散标策略页面
     */
    @RequestMapping(value = "/usersysloanpolicyview", method = {RequestMethod.GET})
    public ModelAndView userSysLoanPolicyView(@RequestParam(value = "userId") Long userId) {
        ModelAndView mv = new ModelAndView();
        if (userId == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.setViewName(ViewConstant.VIEW_USERSYSLOANPOLICY);
        //执行跳转
        return mv;
    }


    /**
     * 添加用户系统散标策略页面
     */
    @RequestMapping(value = "/addusersysloanpolicyview", method = {RequestMethod.GET})
    public ModelAndView addUserSysLoanPolicyView(@RequestParam(value = "userId") Long userId,
                                                 @RequestParam(value = "thirdUserUUID") String thirdUserUUID,
                                                 HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if (userId == null || StringUtils.isBlank(thirdUserUUID)) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

        //验证用户是否存在 TODO token 是否过期
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

        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.addObject(ViewConstant.PARAM_THIRDUSERUUID, thirdUserUUID);
        mv.setViewName(ViewConstant.VIEW_ADDUSERSYSLOANPOLICY);
        //执行跳转
        return mv;
    }

    /**
     * 散标详情页面
     */
    @RequestMapping(value = "/loanpolicydetailview", method = {RequestMethod.GET})
    public ModelAndView loanPolicyDetailView(@RequestParam(value = "userId") Long userId,
                                             @RequestParam(value = "policyId") Long policyId,
                                             HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if (userId == null || policyId == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

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

        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.setViewName(ViewConstant.VIEW_LOANPOLICYDETAIL);

        //查询所有散标 (系统/自定义)
        UserPolicyResult userPolicyResult = userPolicyService.getLoanPolicy(policyId);
        if (userPolicyResult.isFailed()) {
            logger.error("查询散标策略失败,userId={},policyId={}", new Object[]{userId, policyId});
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", userPolicyResult.getErrorMsg());
            return mv;
        }

        LoanPolicy loanPolicy = userPolicyResult.getLoanPolicy();

        LoanPolicyVO vo = new LoanPolicyVO();
        if(loanPolicy != null) {
            //有些标不能展示详情
            if(loanPolicy.getRiskLevel() != null && loanPolicy.getRiskLevel() == LoanRiskLevelEnum.sys.getCode()){
                mv.setViewName(ViewConstant.VIEW_ERROR);
                mv.addObject("error", "此策略不展示详情");
                return mv;
            }
            vo = buildLoanPolicyVOs(loanPolicy);
        }

        mv.addObject(ViewConstant.PARAM_LOANPOLICY, vo);
        //执行跳转
        return mv;
    }

    private LoanPolicyVO buildLoanPolicyVOs(LoanPolicy loanPolicy) {
        LoanPolicyVO vo = new LoanPolicyVO();
        vo.setId(loanPolicy.getId()); //策略id
//        vo.setUserPolicyId(loanPolicy.getUserPolicyId()); //用户策略主键id
//        vo.setUsername(loanPolicy.getThirdUserUUID());
        vo.setName(loanPolicy.getName());
        vo.setAmount(buildValueScope(loanPolicy.getAmountBegin(), loanPolicy.getAmountEnd()));
        vo.setMonth(buildValueScope(loanPolicy.getMonthBegin(), loanPolicy.getMonthEnd()));
        vo.setRate(buildValueScope(loanPolicy.getRateBegin(), loanPolicy.getRateEnd()));
        vo.setAge(buildValueScope(loanPolicy.getAgeBegin(), loanPolicy.getAgeEnd()));
        vo.setSex(loanPolicy.getSex() != null ? SexEnum.findByCode(loanPolicy.getSex()).getName() : LoanPolicyVO.VALUE_NO_LIMIT);
        StringBuilder temp;
        //第三方认证展示
        if (loanPolicy.getThirdAuthInfo() > 0) {
            temp = new StringBuilder();
            for (PPDThridAuthValidEnum _enum : PPDThridAuthValidEnum.values()) {
                if (_enum == PPDThridAuthValidEnum.unknown) {
                    continue;
                }
                if (PPDBinExpConstants.testFlagMatch(loanPolicy.getThirdAuthInfo(), _enum.getCode()))
                    temp.append(_enum.getDesc()).append(",");
            }
            vo.setThirdAuthInfo(temp.toString().substring(0, temp.length() - 1));
        }

        //学历认证展示
        if (loanPolicy.getCertificate() > 0) {
            temp = new StringBuilder();
            for (PPDCertificateEnum _enum : PPDCertificateEnum.values()) {
                if (_enum == PPDCertificateEnum.unknown) {
                    continue;
                }
                if (PPDBinExpConstants.testFlagMatch(loanPolicy.getCertificate(), _enum.getCode()))
                    temp.append(_enum.getDesc()).append(",");
            }
            vo.setCertificate(temp.toString().substring(0, temp.length() - 1));
        }

        //学习形式展示
        if (loanPolicy.getStudyStyle() > 0) {
            temp = new StringBuilder();
            for (PPDStudyStyleEnum _enum : PPDStudyStyleEnum.values()) {
                if (_enum == PPDStudyStyleEnum.unknown) {
                    continue;
                }
                if (PPDBinExpConstants.testFlagMatch(loanPolicy.getStudyStyle(), _enum.getCode()))
                    temp.append(_enum.getDesc()).append(",");
            }
            vo.setStudyStyle(temp.toString().substring(0, temp.length() - 1));
        }
        //毕业学校分类
        if (loanPolicy.getGraduateSchoolType() > 0) {
            temp = new StringBuilder();
            for (SchoolTypeEnum _enum : SchoolTypeEnum.values()) {
                if (PPDBinExpConstants.testFlagMatch(loanPolicy.getGraduateSchoolType(), _enum.getCode()))
                    temp.append(_enum.getName()).append(",");
            }
            vo.setGraduateSchoolType(temp.toString().substring(0, temp.length() - 1));
        }

        //魔镜等级
        if (loanPolicy.getCreditCode() > 0) {
            temp = new StringBuilder();
            for (PPDCreditCodeEnum _enum : PPDCreditCodeEnum.values()) {
                if (_enum == PPDCreditCodeEnum.UNKNOWN) {
                    continue;
                }
                if (PPDBinExpConstants.testFlagMatch(loanPolicy.getCreditCode(), _enum.getCode()))
                    temp.append(_enum.getDesc()).append(",");
            }
            vo.setCreditCode(temp.toString().substring(0, temp.length() - 1));
        }

        vo.setLoanerSuccessCount(buildValueScope(loanPolicy.getLoanerSuccessCountBegin(), loanPolicy.getLoanerSuccessCountEnd()));
        vo.setWasteCount(buildValueScope(loanPolicy.getWasteCountBegin(), loanPolicy.getWasteCountEnd()));
        vo.setNormalCount(buildValueScope(loanPolicy.getNormalCountBegin(), loanPolicy.getNormalCountEnd()));
        vo.setOverdueLessCount(buildValueScope(loanPolicy.getOverdueLessCountBegin(), loanPolicy.getOverdueLessCountEnd()));
        vo.setOverdueMoreCount(buildValueScope(loanPolicy.getOverdueMoreCountBegin(), loanPolicy.getOverdueMoreCountEnd()));
        vo.setTotalPrincipal(buildValueScope(loanPolicy.getTotalPrincipalBegin(), loanPolicy.getTotalPrincipalEnd()));
        vo.setOwingPrincipal(buildValueScope(loanPolicy.getOwingPrincipalBegin(), loanPolicy.getOwingPrincipalEnd()));
        vo.setAmountToReceive(buildValueScope(loanPolicy.getAmountToReceiveBegin(), loanPolicy.getAmountToReceiveEnd()));
        vo.setAmountOwingTotal(buildValueScope(loanPolicy.getAmountOwingTotalBegin(), loanPolicy.getAmountOwingTotalEnd()));
        vo.setLastSuccessBorrowDays(buildValueScope(loanPolicy.getLastSuccessBorrowDaysBegin(), loanPolicy.getLastSuccessBorrowDaysEnd()));
        vo.setRegisterBorrowMonths(buildValueScope(loanPolicy.getRegisterBorrowMonthsBegin(), loanPolicy.getRegisterBorrowMonthsEnd()));

        vo.setOwingHighestDebtRatio(buildValueScope(loanPolicy.getOwingHighestDebtRatioBegin(), loanPolicy.getOwingHighestDebtRatioEnd()));
        vo.setAmtDebtRat(buildValueScope(loanPolicy.getAmtDebtRatBg(), loanPolicy.getAmtDebtRatEd()));

        vo.setValidTime(DateUtil.dateToString(loanPolicy.getValidTime(), DateUtil.DATE_FORMAT_DATETIME_COMMON));
        vo.setCreateTime(DateUtil.dateToString(loanPolicy.getCreateTime(), DateUtil.DATE_FORMAT_DATETIME_COMMON));
//        vo.setBidAmount(loanPolicy.getBidAmount());

        //系统标
        if (loanPolicy.getRiskLevel() != null)
            vo.setRiskLevel(LoanRiskLevelEnum.findByCode(loanPolicy.getRiskLevel()).getDesc());
        return vo;
    }

    private String buildValueScope(Integer begin, Integer end) {
        String result = UserPolicyDetailVO.VALUE_NO_LIMIT;
        if (begin != null && end != null) {
            result = begin + "-" + end;
        } else if (begin != null) {
            result = ">=" + begin;
        } else if (end != null) {
            result = "<=" + end;
        }
        return result;
    }

    private String buildValueScope(Double begin, Double end) {
        String pattern = "##.##";
        String result = UserPolicyDetailVO.VALUE_NO_LIMIT;
        if (begin != null && end != null) {
            result = NumberUtil.formatPattern(begin, pattern) + "-" + NumberUtil.formatPattern(end, pattern);
        } else if (begin != null) {
            result = ">=" + NumberUtil.formatPattern(begin, pattern);
        } else if (end != null) {
            result = "<=" + NumberUtil.formatPattern(end, pattern);
        }
        return result;
    }

    /**
     * 查看用户自定义散标策略页面
     */
    @RequestMapping(value = "/userloanpolicyview", method = {RequestMethod.GET})
    public ModelAndView userLoanPolicyView(@RequestParam(value = "userId") Long userId) {
        ModelAndView mv = new ModelAndView();
        //TODO 放到拦截器统一验证
        /*if (userId == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }*/

        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.setViewName(ViewConstant.VIEW_USERLOANPOLICY);
        //执行跳转
        return mv;
    }

    /**
     * 添加用户自定义散标页面
     */
    @RequestMapping(value = "/adduserloanpolicyview", method = {RequestMethod.GET})
    public ModelAndView addUserLoanPolicyView(@RequestParam(value = "userId") Long userId,
                                              @RequestParam(value = "mainPolicyId") Long mainPolicyId,
                                              @RequestParam(value = "loanPolicyId", required = false) Long loanPolicyId) {
        ModelAndView mv = new ModelAndView();
        if (userId == null || mainPolicyId == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

        //验证用户是否存在 TODO token 是否过期
        /*UserResult userResult = userService.getUserBaseInfo(userId);
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
        }*/

        //获取改user下的所有授权的第三方账号
        /*UserThirdParam param = new UserThirdParam();
        param.setUserId(userId);
        UserThirdResult userThirdResult = userThirdService.getUserThirdBindInfo(param);
        if (userThirdResult.isFailed()) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", userThirdResult);
            return mv;
        }

        List<UserThirdBindInfo> userThirdBindInfos = userThirdResult.getUserThirdBindInfos();
        mv.addObject(ViewConstant.PARAM_USERTHIRDBINDINFOLIST, userThirdBindInfos);
        */


        LoanPolicy loanPolicy = new LoanPolicy();
        //判断是新增还是更新过来的操作
        if (loanPolicyId != null) {
            //调用rpc服务
            UserPolicyResult userPolicyResult = userPolicyService.getLoanPolicy(loanPolicyId);
            if (userPolicyResult.isFailed()) {
                logger.error("查询用户自定义散标策略失败,loanPolicyId={}", loanPolicyId);
                mv.setViewName(ViewConstant.VIEW_ERROR);
                mv.addObject("error", "查询用户自定义散标策略失败");
                return mv;
            }

            loanPolicy = userPolicyResult.getLoanPolicy();
            if (loanPolicy == null) {
                mv.setViewName(ViewConstant.VIEW_ERROR);
                mv.addObject("error", "未查询到任何记录");
                return mv;
            }
        }

        mv.addObject(ViewConstant.PARAM_LOANPOLICY, loanPolicy);

        //获取魔镜等级
        Map<String, String> map = new LinkedHashMap<>();
        for (PPDCreditCodeEnum _enum : PPDCreditCodeEnum.values()) {
            if (_enum == PPDCreditCodeEnum.UNKNOWN) {
                continue;
            }
            FlagCheckedModel flagCheckedModel = new FlagCheckedModel(_enum.getDesc(), false);
            if (PPDBinExpConstants.testFlagMatch(loanPolicy.getCreditCode(), _enum.getCode()))
                flagCheckedModel.setChecked(true);
            map.put(String.valueOf(_enum.getCode()), JSONObject.toJSONString(flagCheckedModel));
        }
        mv.addObject(ViewConstant.PARAM_CREDITCODEMAP, map);
        //学历认证
        map = new LinkedHashMap<>();
        for (PPDCertificateEnum _enum : PPDCertificateEnum.values()) {
            if (_enum == PPDCertificateEnum.unknown) {
                continue;
            }
            FlagCheckedModel flagCheckedModel = new FlagCheckedModel(_enum.getDesc(), false);
            if (PPDBinExpConstants.testFlagMatch(loanPolicy.getCertificate(), _enum.getCode()))
                flagCheckedModel.setChecked(true);
            map.put(String.valueOf(_enum.getCode()), JSONObject.toJSONString(flagCheckedModel));
        }
        mv.addObject(ViewConstant.PARAM_CERTIFICATEMAP, map);
        //学习形式
        map = new LinkedHashMap<>();
        for (PPDStudyStyleEnum _enum : PPDStudyStyleEnum.values()) {
            if (_enum == PPDStudyStyleEnum.unknown) {
                continue;
            }
            FlagCheckedModel flagCheckedModel = new FlagCheckedModel(_enum.getDesc(), false);
            if (PPDBinExpConstants.testFlagMatch(loanPolicy.getStudyStyle(), _enum.getCode()))
                flagCheckedModel.setChecked(true);
            map.put(String.valueOf(_enum.getCode()), JSONObject.toJSONString(flagCheckedModel));
        }
        mv.addObject(ViewConstant.PARAM_STUDYSTYLEMAP, map);
        //学校类型
        map = new LinkedHashMap<>();
        for (SchoolTypeEnum _enum : SchoolTypeEnum.values()) {
            FlagCheckedModel flagCheckedModel = new FlagCheckedModel(_enum.getName(), false);
            if (PPDBinExpConstants.testFlagMatch(loanPolicy.getGraduateSchoolType(), _enum.getCode()))
                flagCheckedModel.setChecked(true);
            map.put(String.valueOf(_enum.getCode()), JSONObject.toJSONString(flagCheckedModel));
        }
        mv.addObject(ViewConstant.PARAM_GRADUATESCHOOLTYPEMAP, map);
        //认证类型
        map = new LinkedHashMap<>();
        for (PPDThridAuthValidEnum _enum : PPDThridAuthValidEnum.values()) {
            if (_enum == PPDThridAuthValidEnum.unknown) {
                continue;
            }
            FlagCheckedModel flagCheckedModel = new FlagCheckedModel(_enum.getDesc(), false);
            if (PPDBinExpConstants.testFlagMatch(loanPolicy.getThirdAuthInfo(), _enum.getCode()))
                flagCheckedModel.setChecked(true);
            map.put(String.valueOf(_enum.getCode()), JSONObject.toJSONString(flagCheckedModel));
        }
        mv.addObject(ViewConstant.PARAM_THIRDAUTHINFOMAP, map);

        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.setViewName(ViewConstant.VIEW_ADDUSERLOANPOLICY);
        //执行跳转
        return mv;
    }

    @Data
    @AllArgsConstructor
    private static class FlagCheckedModel {
        private String desc;
        private boolean checked;
    }
}
