package com.davinci.web;

import com.davinci.domain.User;
import com.davinci.exception.Constants;
import com.davinci.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
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
    public String updateForm(@PathVariable Long id, Model model, HttpSession httpSession) {
        // 방어 코드 짜기(세션에 있는 정보와 변경하고자 하는 대상이 다를 경우 예외 처리
        if (!HttpSessionUtils.isLoginUser(httpSession)) {
            return "redirect:/user/loginForm";
        }

        if (!HttpSessionUtils.getUserFormSession(httpSession).matchId(id)) {
            throw new IllegalStateException(Constants.ONLY_OWN_INFORMATION_EDIT);
        }

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
    public String update(@PathVariable Long id, User updateUser, HttpSession httpSession) {

        if (!HttpSessionUtils.isLoginUser(httpSession)) {
            return "redirect:/user/loginForm";
        }

        if (!HttpSessionUtils.getUserFormSession(httpSession).matchId(id)) {
            throw new IllegalStateException(Constants.ONLY_OWN_INFORMATION_EDIT);
        }

        log.debug("update : id = {}", id);
        log.debug("updateUser : {}", updateUser);
        User user = userRepository.findById(id).get();
        user.update(updateUser);

        log.debug("updatedUser : {}", user);

        userRepository.save(user);

        if (!ObjectUtils.isEmpty(httpSession.getAttribute(HttpSessionUtils.SESSION_USER_ID))) { // 로그인 상태에서 자신의 정보를 수정했을 때 세션에도 바로 반영되게 수정
            httpSession.setAttribute(HttpSessionUtils.SESSION_USER_ID, user);
        }

        return "redirect:/user/list";
    }

    /**
     * 회원삭제
     */
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession httpSession) {

        if (!HttpSessionUtils.isLoginUser(httpSession)) {
            return "redirect:/user/loginForm";
        }

        if (!HttpSessionUtils.getUserFormSession(httpSession).matchId(id)) {
            throw new IllegalStateException(Constants.ONLY_OWN_INFORMATION_DELETE);
        }

        userRepository.deleteById(id);

        // 만약에 현재 로그인 중인 정보를 삭제할 경우 해당 세션 정보를 삭제하고 다시 로그인 화면으로 redirect 한다.
        httpSession.removeAttribute(HttpSessionUtils.SESSION_USER_ID);

        return "redirect:/user/loginForm";
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

        if (!user.matchPassword(password)) {
            log.debug("login failure");
            return "redirect:/user/loginForm";
        }

        log.debug("login success");
        httpSession.setAttribute(HttpSessionUtils.SESSION_USER_ID, user);

        return "redirect:/";
    }

    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        log.debug("logout success");
        httpSession.removeAttribute(HttpSessionUtils.SESSION_USER_ID);

        return "redirect:/";
    }

}