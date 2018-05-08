package com.invest.ivmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ViewController extends BaseController {

    /**
     * 标专家列表页面
     */
    @RequestMapping(value = "/userlist", method = RequestMethod.GET)
    public String userList() {
        return "show.userlist";
    }


}
