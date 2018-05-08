package com.invest.ivgateway.controller;

import com.invest.ivcommons.constant.ViewConstant;
import com.invest.ivuser.biz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by xugang on 2017/8/11.
 */
@RestController
@RequestMapping(value = "/record")
public class RecordController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 投资记录查询
     */
    @RequestMapping(value = "/investrecordview", method = {RequestMethod.GET})
    public ModelAndView investRecordView(@RequestParam(value = "userId") Long userId) {
        ModelAndView mv = new ModelAndView();
        if (userId == null) {
            mv.setViewName(ViewConstant.VIEW_ERROR);
            mv.addObject("error", "请求参数错误");
            return mv;
        }

        //设置view
        mv.addObject(ViewConstant.PARAM_USERID, userId);
        mv.setViewName(ViewConstant.VIEW_INVESTRECORDVIEW);
        //执行跳转
        return mv;
    }
}
