package com.invest.ivgateway.controller;

import com.invest.ivcommons.constant.ViewConstant;
import com.invest.ivuser.biz.service.UserService;
import com.invest.ivuser.model.entity.User;
import com.invest.ivuser.model.result.UserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xugang on 2017/8/11.
 */
@RestController
@RequestMapping(value = "/third")
public class ThirdController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 查看第三方授权信息页面
     *
     * @return
     */
    @RequestMapping(value = "/authview", method = {RequestMethod.GET})
    public ModelAndView authview(@RequestParam(value = "userId") Long userId,
                                 HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if (userId == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.setViewName(ViewConstant.VIEW_AUTHINFO);
        //执行跳转
        return mv;
    }

    /**
     * 查看第三方授权信息页面
     *
     * @return
     */
    @RequestMapping(value = "/userinfoview", method = {RequestMethod.GET})
    public ModelAndView userinfoview(@RequestParam(value = "userId") Long userId,
                                     HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if (userId == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.setViewName(ViewConstant.VIEW_USERINFO);
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
}
