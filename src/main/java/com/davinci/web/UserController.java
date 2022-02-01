package com.davinci.web;

import com.davinci.domain.User;
import com.davinci.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 회원가입, 회원목록 조회
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @PostConstruct
    public void initData() {
        userRepository.save(new User("hojong1351", "12341234", "정호종", "hojong1351@gmail.com"));
        userRepository.save(new User("davincij1111", "11112222", "다빈치", "hojong1351@nvaer.com"));
    }

    // static or non-static
    private final List<User> users = new ArrayList<>();

    @Autowired
    private UserRepository userRepository;

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
     * 회원가입 폼(구 버전)
     */
    @GetMapping("/tempForm")
    public String formMember() {
        return "tempForm";
    }

    /**
     * 회원가입 폼(신 버전)
     */
    @GetMapping("/form")
    public String userFormMember() {
        return "/user/form";
    }

    /**
     * 회원가입
     * html 에서 form 테크로 값 넘길 때, name 지정해야줘야함!!!
     */
    @PostMapping("/create")
    public String createMember(User user) {
        // TODO
        System.out.println("user = " + user);

        userRepository.save(user);

        // 3. Redirect
        return "redirect:/user/list";
    }

    /**
     * 회원목록(신)
     */
    @GetMapping("/list")
    public String members(Model model) {
        model.addAttribute("users", userRepository.findAll());

        return "/user/list";
    }

    /**
     * 회원수정 폼
     */
    @GetMapping("/{id}/updateForm")
    public String updateForm(@PathVariable Long id, Model model) {
        System.out.println("id: " + id);

        Optional<User> user = userRepository.findById(id);

        System.out.println("user.get() = " + user.get());

        model.addAttribute("user", user.get());

        return "/user/updateForm";
    }

    /**
     * 회원수정
     */
    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, User updateUser) {
        System.out.println("id = " + id);
        System.out.println("updateUser = " + updateUser);

        User user = userRepository.findById(id).get();
        user.update(updateUser);

        System.out.println("after transform : user = " + user);

        userRepository.save(user);

        return "redirect:/user/list";
    }

}
