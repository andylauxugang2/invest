package com.invest.ivmanager.controller;

import com.alibaba.fastjson.JSONObject;
import com.invest.ivmanager.annotations.SkipTokenValidation;
import com.invest.ivmanager.model.entity.Modle;
import com.invest.ivmanager.model.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController extends BaseController {

    /**
     * 登陆后进入portal
     *
     * @return
     */
    @RequestMapping(value = "/portal", method = RequestMethod.GET)
    public String portal() {
        User user = new User();
        List<Modle> modleList = new ArrayList<>();
        Modle m1 = new Modle();
        m1.setId(1l);
        m1.setModleName("导航树");
        m1.setSort(1);
        m1.setStatus(1);
        Modle m2 = new Modle();
        m2.setId(2l);
        m2.setIcon("grid");
        m2.setModleName("散标策略");
        m2.setStatus(1);
        m2.setSort(1);
        m2.setModleAction("loanpolicylist");
        m2.setParentId(1L);
        modleList.add(m1);
        modleList.add(m2);

        Modle userM = new Modle();
        userM.setId(3l);
        userM.setIcon("grid");
        userM.setModleName("用户管理");
        userM.setStatus(1);
        userM.setSort(2);
        userM.setModleAction("userlist");
        userM.setParentId(1L);
        modleList.add(userM);

        user.setMenus(getMenus(modleList));
        session.setAttribute("user", user);
        return "show.portal";
    }

    /**
     * 验证登陆
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    @SkipTokenValidation(uri = "/doLogin")
    public String doLogin(@RequestParam String username, @RequestParam String password) {
        //TODO
        return "show.portal";
    }

    /**
     * 登出
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(@RequestParam String username) {
        //TODO
        return "show.login";
    }

    /**
     * 欢迎界面
     *
     * @return
     */
    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    @SkipTokenValidation(uri = "/welcome")
    public String welcome() {
        //TODO
        return "welcome";
    }

    /**
     * 生成左侧树菜单
     *
     * @return JSON
     */
    private String getMenus(List<Modle> modleList) {
        for (int i = 0; i < modleList.size(); i++) {
            Modle modle = modleList.get(i);
            if (modle.getParentId() != null) {
                // 查找子节点
                for (int j = 0; j < modleList.size(); j++) {
                    Modle m = modleList.get(j);
                    if (m.getId().equals(modle.getParentId())) {
                        m.getLeaves().add(modle);
                    }
                }
                modleList.remove(i);
                i--;
            }
        }
        return JSONObject.toJSONString(modleList);
    }
}
