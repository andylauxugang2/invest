package com.invest.ivmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoanPolicyController extends BaseController {

    /**
     * 散标列表页面
     */
    @RequestMapping(value = "/loanpolicylist", method = RequestMethod.GET)
    public String list() {
        return "show.loanpolicylist";
    }


}
