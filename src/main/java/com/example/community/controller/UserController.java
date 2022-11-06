package com.example.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.community.auth.UserDetailsImpl;
import com.example.community.auth.UserDetailsServiceImpl;
import com.example.community.dto.UploadForm;
import com.example.community.entity.User;
import com.example.community.service.RoomService;
import com.example.community.service.TalkService;

/**
 * ユーザ処理を行うコントローラー
 * 
 * @author user
 */
@Controller
@RequestMapping ("/")
public class UserController {
    
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final TalkService talkService;
    private final RoomService roomService;
    
    @Autowired
    public UserController (UserDetailsServiceImpl userDetailsServiceImpl, TalkService talkService,
            RoomService roomService) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.talkService = talkService;
        this.roomService = roomService;
    }
    
    /**
     * マイページを表示する
     * 
     * @param  userDetails ログイン中のユーザーID取得
     * @param  model       マイページに表示する情報を渡す。
     * @return             マイページ
     */
    @GetMapping ("/showMyPage")
    public String showMyPage (@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        this.setUserInfo (userDetails.getId (), model);
        return "/user/myPage";
    }
    
    /**
     * ユーザーページを表示
     * 
     * @param  model
     * @param  userId ユーザーページを表示するユーザーID
     * @return
     */
    @GetMapping ("/showUserPage/{userId}")
    public String showUserPage (Model model, @PathVariable String userId) {
        this.setUserInfo (userId, model);
        return "/user/myPage";
    }
    
    /**
     * ユーザーページ、マイページの表示情報をセットする
     * 
     * @param  userId
     * @param  model
     * @return
     */
    public void setUserInfo (String userId, Model model) {
        int createRoomLen = roomService.getCreateRoomLength (userId);
        int joinRoomLen = roomService.getJoinRoomLength (userId);
        int doCommentLen = talkService.getUserTalkLength (userId);
        User user = userDetailsServiceImpl.getUserInfo (userId);
        
        model.addAttribute ("createRoomLen", createRoomLen); // 部屋を作成した数
        model.addAttribute ("joinRoomLen", joinRoomLen); // 部屋に参加している数
        model.addAttribute ("doCommentLen", doCommentLen); // コメントした数
        model.addAttribute ("userInfo", user); // ユーザーページを表示するためのユーザー情報
        model.addAttribute ("createUserId", userId); // 作成したユーザーID
    }
    
    /**
     * ユーザー画像投稿フォームを表示
     * 
     * @param  model 画像投稿フォーム
     * @return       画像投稿フォーム
     */
    @GetMapping ("/user/changeImage")
    public String changeImage (Model model) {
        model.addAttribute ("uploadForm", new UploadForm ());
        return "/user/changeImageForm";
    }
    
    /**
     * ファイルアップロード処理
     * 
     * @param  uploadForm
     * @return
     */
    @PostMapping ("/upload")
    String upload (UploadForm uploadForm, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // フォームで渡されてきたアップロードファイルを取得
        MultipartFile multipartFile = uploadForm.getMultipartFile ();
        // アップロード実行処理メソッド呼び出し
        userDetailsServiceImpl.uploadAction (multipartFile, userDetails.getId ());
        // リダイレクト
        return "redirect:/";
    }
    
}
