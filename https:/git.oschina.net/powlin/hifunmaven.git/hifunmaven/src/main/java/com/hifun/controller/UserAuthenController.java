package com.hifun.controller;

import java.net.URLDecoder;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hifun.base.session.SessionUser;
import com.hifun.service.IUserAuthenService;
import com.hifun.util.MD5Util;
import com.hifun.util.ParamUtil;

@Controller
@RequestMapping(value = "/userAuthen")
public class UserAuthenController extends BaseController {

    @Autowired
    private IUserAuthenService userAuthenService;

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    @ResponseBody
    public boolean login(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password) {
        // 验证用户名和密码
        SessionUser u = userAuthenService.queryUser(username,
            MD5Util.getMD5(password));
        if (u == null) {
            // 用户为空，则跳转到首页
            return false;
        }
        sessionProvider.setUserDetail(u);
        return true;
    }

    @RequestMapping(value = "/register.do", method = RequestMethod.POST)
    @ResponseBody
    public int register(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "nickname", required = true) String nickname,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "passwordr", required = true) String passwordr) {
        if (username == null || nickname == null || password == null
                || passwordr == null || !password.equals(passwordr)) {
            return -2;
        }
        // 查询用户名是否已经注册
        SessionUser u = userAuthenService.queryUserByUsername(username);
        if (u == null) {
            // 查询昵称是否被使用过
            Integer count = userAuthenService
                .queryUserCountByNickname(nickname);
            if (count > 0) {
                // 昵称被使用过
                return -1;
            }
            // 用户为空，新增到用户表
            userAuthenService.insertUserInfo(username, nickname,
                MD5Util.getMD5(password));
            u = new SessionUser(username, nickname);
            sessionProvider.setUserDetail(u);
            return 1;
        }
        return 0;
    }

    @RequestMapping(value = "/main.do", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView doMain() {
        ModelAndView view = new ModelAndView("redirect:/headpage/index.do");
        return view;
    }

    @RequestMapping(value = "/logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView logout() {
        // session非空，则清除
        if (sessionProvider != null
                && sessionProvider.getUserDetail() != null) {
            sessionProvider.removeUserDetail();
        }
        return new ModelAndView("redirect:/headpage/index.do");
    }

    @RequestMapping(value = "/judgeSession.do", method = RequestMethod.GET)
    @ResponseBody
    public boolean judgeSession() {
        if (sessionProvider != null
                && sessionProvider.getUserDetail() != null) {
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/userinfo.do", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView userinfo(
            @RequestParam(value = "username", required = false) String username) {
        ModelAndView view = new ModelAndView("/userinfo");
        try {
            username = URLDecoder
                .decode(new String(Base64.decodeBase64(username)), "UTF-8");
        } catch (Exception e) {
            username = "";
        }
        username = ParamUtil.checkUsername(username);
        SessionUser user = userAuthenService.queryUserByUsername(username);
        view.addObject("user", user == null ? new SessionUser(username) : user);
        return view;
    }

}
