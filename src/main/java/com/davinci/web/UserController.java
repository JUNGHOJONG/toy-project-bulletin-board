package com.davinci.web;

import com.davinci.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 회원가입, 회원목록 조회
 */
@Controller
public class UserController {

    // static or non-static
    private final List<User> users = new ArrayList<>();

    /**
     * mustache 연습용
     */
    @GetMapping("/mustacheTest")
    public String mustacheTest(Model model) {
        model.addAttribute("name", "Chris");
        model.addAttribute("value", 10000);
        model.addAttribute("taxed_value", 10000 - (10000 * 0.4));
        model.addAttribute("in_ca", true);

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        model.addAttribute("nothin", list);

        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("name", "one");
        HashMap<String, String> hashMap2 = new HashMap<>();
        hashMap2.put("name", "two");
        HashMap<String, String> hashMap3 = new HashMap<>();
        hashMap3.put("name", "three");

        List<Map<String, String>> mapList = Arrays.asList(hashMap1, hashMap2, hashMap3);
        model.addAttribute("repo", mapList);

        HashMap<String, String> hashMap4 = new HashMap<>();
        hashMap4.put("name", "Willy");
        HashMap<String, String> hashMap5 = new HashMap<>();
        hashMap5.put("wrapped", "((text: String) => <b>{text}</b>)");

        List<Map<String, String>> mapList1 = Arrays.asList(hashMap4, hashMap5);

        model.addAttribute("wrapped", mapList1);

        return "test";
    }

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
