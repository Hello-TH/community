package com.example.community.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.community.auth.UserDetailsImpl;
import com.example.community.auth.UserDetailsServiceImpl;
import com.example.community.auth.UserInfoDTO;

/**
 * ログイン、新規登録に関してのコントローラー
 * 
 * @author user
 *
 */
@Controller
@RequestMapping ("/")
public class AuthController {
    
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final MessageSource messageSource;
    
    @Autowired
    public AuthController (UserDetailsServiceImpl userDetaisDetailsServiceImpl, MessageSource messageSource) {
        this.userDetailsServiceImpl = userDetaisDetailsServiceImpl;
        this.messageSource = messageSource;
    }
    
    
    /**
     * msg取得
     * 
     * @param  msgName メッセージの名前
     * @return
     */
    public String getMessage (String msgName) {
        return messageSource.getMessage (msgName, new String[] {}, Locale.getDefault ());
    }
    
    /**
     * ログインページ表示
     * 
     * @return
     */
    @GetMapping ("/login")
    public String login (Model model) {
        
        model.addAttribute ("createUserData", new UserInfoDTO ());
        return "login";
    }
    
    /**
     * 仮登録状態でDBに登録して、メールを送信する。
     * 
     * @return
     */
    @Transactional
    @PostMapping ("/temporarySignup")
    public String temporarySignup (Model model, @ModelAttribute UserInfoDTO userInfoDTO, @Validated UserInfoDTO e,
            BindingResult result, RedirectAttributes attributes) {
        
        // バリデーションチェック
        if (result.hasErrors ()) {
            
            List<String> errorList = new ArrayList<String> ();
            for (ObjectError error : result.getAllErrors ()) {
                errorList.add (error.getDefaultMessage ());
            }
            
            model.addAttribute ("validationError", errorList);
            model.addAttribute ("createUserData", userInfoDTO);
            
            return "login";
        }
        
        // メールが登録済みかどうか確認
        boolean registrationConf = userDetailsServiceImpl.registrationConf (userInfoDTO.getEmail ());
        
        if (registrationConf) {
            attributes.addFlashAttribute ("msg", getMessage ("email.notEmpty"));
            return "redirect:/login";
        }
        
        // DBに仮登録 & メール送信
        try {
            userDetailsServiceImpl.temporarySignup (userInfoDTO);
        } catch (Exception e1) {
            System.out.println (e1);
            
        }
        
        return "redirect:/sendMailMsg";
    }
    
    /**
     * メール送信済みメッセージ
     * 
     * @return
     */
    @GetMapping ("/sendMailMsg")
    public String sendMailMsg () {
        return "sendMailMsg";
    }
    
    /**
     * 本登録処理
     * 
     * @return
     */
    @GetMapping ("/signUp/{token}")
    public String signUp (@PathVariable String token, Model model, RedirectAttributes attributes) {
        
        // 仮登録のユーザー情報
        UserDetailsImpl tempUser = userDetailsServiceImpl.getTemporaryUser (token);
        
        // tokenの登録がない場合
        if (tempUser == null) {
            attributes.addFlashAttribute ("msg", getMessage ("token.empty"));
            return "redirect:/login";
        }
        
        long now = new Timestamp (System.currentTimeMillis ()).getTime (); // 現在時刻
        long registData = tempUser.getUpdated_at ().getTime (); // 登録時刻
        long daySeconds = 60 * 1000 * 60 * 24; // 1日ミリ秒　

        // メール送信から24時間経過している場合
        if ((now - registData) / daySeconds >= 1) {
            attributes.addFlashAttribute ("msg", getMessage ("token.timeout"));
            return "redirect:/login";
        }
        
        // 本登録
        String id = UUID.randomUUID ().toString ();
        userDetailsServiceImpl.registUser (id, tempUser.getUser_name (), tempUser.getEmail (), tempUser.getPassword ());
        
        attributes.addFlashAttribute ("msg", getMessage ("registUserMsg"));
        
        return "redirect:/login";
    }
    
}
