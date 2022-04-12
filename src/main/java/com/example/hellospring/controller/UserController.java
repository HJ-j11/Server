package com.example.hellospring.controller;


import com.example.hellospring.entity.User;
import com.example.hellospring.entity.UserInfo;
import com.example.hellospring.login.LoginForm;
import com.example.hellospring.login.SessionConstants;
import com.example.hellospring.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    @Autowired
    TestService testService;

    @GetMapping("/login")
    public String LoginForm(Model model) {
        LoginForm loginForm = new LoginForm();
        model.addAttribute("loginForm", loginForm);
        return "Login";
    }

    @PostMapping("/login")
    public String Login(@ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return "Login";
        }

        User loginUser = testService.Login(loginForm.getLoginId(), loginForm.getPassword());

        if(loginUser == null) {
            bindingResult.reject("loginFail", "Invalid Id or Password you input");
            return "Login";
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionConstants.LOGIN_USER, loginUser);
        return "sample";
    }

    @PostMapping("logout")
    public String Logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }

        return "Login";
    }

}