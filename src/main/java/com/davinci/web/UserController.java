package com.davinci.web;

import com.davinci.domain.User;
import com.davinci.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 회원가입, 회원목록 조회
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @PostConstruct
    public void initData() {
        userRepository.save(new User("hojong1351", "12341234", "정호종", "hojong1351@gmail.com"));
        userRepository.save(new User("davincij1111", "11112222", "다빈치", "hojong1351@nvaer.com"));
    }

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
        log.debug("createMember : user = {}", user);

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
        log.debug("updateForm : id = {}", id);

        Optional<User> user = userRepository.findById(id);

        log.debug("updateForm : user.get() = {}", user.get());

        model.addAttribute("user", user.get());

        return "/user/updateForm";
    }

    /**
     * 회원수정
     */
    @PutMapping("/{id}/update")
    public String update(@PathVariable Long id, User updateUser) {
        log.debug("update : id = {}", id);
        log.debug("updateUser : {}", updateUser);
        User user = userRepository.findById(id).get();
        user.update(updateUser);

        log.debug("updatedUser : {}", user);

        userRepository.save(user);

        return "redirect:/user/list";
    }

    /**
     * 회원삭제
     */
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        userRepository.deleteById(id);

        return "redirect:/user/list";
    }

    /**
     * 로그인 폼
     */
    @GetMapping("/loginForm")
    public String loginForm() {
        return "/user/loginForm";
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public String login(String userId, String password, HttpSession httpSession) {
        log.debug("userId = {}, password = {}", userId, password);

        User user = userRepository.findByUserId(userId);

        if (user == null) {
            log.debug("login failure");
            return "redirect:/user/loginForm";
        }

        if (!password.equals(user.getPassword())) {
            log.debug("login failure");
            return "redirect:/user/loginForm";
        }

        log.debug("login success");
        httpSession.setAttribute("user", user);

        return "redirect:/";
    }

    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        log.debug("logout success");
        httpSession.removeAttribute("user");

        return "redirect:/";
    }

}