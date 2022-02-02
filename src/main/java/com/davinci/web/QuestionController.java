package com.davinci.web;

import com.davinci.domain.Question;
import com.davinci.domain.User;
import com.davinci.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@RequestMapping("/questions")
@Controller
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * 질문하기 폼
     */
    @GetMapping("/form")
    public String form(HttpSession httpSession) {

        // 로그인한 사용자만 사용할 수 있도록 구현
        if (!HttpSessionUtils.isLoginUser(httpSession)) {
            return "redirect:/user/loginForm";
        }

        return "qna/form";
    }

    /**
     * 질문하기
     */
    @PostMapping("/create")
    public String createQuestion(String title, String contents, HttpSession httpSession) {

        if (!HttpSessionUtils.isLoginUser(httpSession)) {
            return "redirect:/user/loginForm";
        }

        User user = HttpSessionUtils.getUserFormSession(httpSession);

        // Question 객체를 생성 및 값 설정
        Question question = new Question(user.getUserId(), title, contents);

        // Repository 에 저장한다
        questionRepository.save(question);

        // 메인화면으로 redirect
        return "redirect:/";
    }
}
