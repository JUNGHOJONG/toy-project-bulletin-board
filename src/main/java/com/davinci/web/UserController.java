package com.davinci.web;

import com.davinci.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 회원가입, 회원목록 조회
 */
@Controller
public class UserController {

    // static or non-static
    private final List<User> users = new ArrayList<>();

    /**
     * test 용
     */
    @ResponseBody
    @GetMapping("/test")
    public String test(String userId, int age) {
        System.out.println("userId = " + userId);
        System.out.println("age = " + age);

        return "ok";
    }

    /**
     * 회원가입 폼
     */
    @GetMapping("/form")
    public String formMember() {
        return "form";
    }

    /**
     * 회원가입
     * html 에서 form 테크로 값 넘길 때, name 지정해야줘야함!!!
     */
    @PostMapping("/create")
    public String createMember(User user) {
        // TODO
        System.out.println("user = " + user);

        // 1. UserList 에 생성하기
        users.add(user);

        // 3. Redirect
        return "redirect:/list";
    }

    @GetMapping("/list")
    public String members(Model model) {
        model.addAttribute("users", users);

        return "list";
    }
}
