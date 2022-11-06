package com.example.community.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.community.RoomPermissionFile;
import com.example.community.auth.UserDetailsImpl;
import com.example.community.dto.TalkRequest;
import com.example.community.entity.Room;
import com.example.community.entity.Talk;
import com.example.community.service.RoomService;
import com.example.community.service.TalkService;

/**
 * トークに関する処理分けを行うクラス
 * 
 * @author user
 *
 */
@Controller
@RequestMapping ("/")
public class TalkController {
    
    private final TalkService talkService;
    private final RoomService roomService;
    
    @Autowired
    public TalkController (TalkService talkService, RoomService roomService) {
        this.talkService = talkService;
        this.roomService = roomService;
    }
    
    /**
     * 参加している部屋のリストと作成した部屋のリストを取得
     * 
     * @param  userDetails ログイン中のユーザーID
     * @param  model
     * @return             一覧画面
     */
    @GetMapping ("/showTalkList")
    public String showtalkList (@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        
        // 参加リストをもとに、Room情報を取得する。
        List<Room> joinRoomData = talkService.getJoinRoomData (userDetails.getId ());
        // 作ったリストをもとに、Room情報を取得する。
        List<Room> masterRoomData = talkService.getMasterRoomData (userDetails.getId ());
        // // 申請がある場合、情報を取得して返却する。
        List<Talk> joinRequestData = talkService.getJoinRequest (userDetails.getId ());
        
        model.addAttribute ("joinRoomData", joinRoomData);
        model.addAttribute ("masterRoomData", masterRoomData);
        model.addAttribute ("joinRequestData", joinRequestData);
        //
        return "room/talk/showTalkList";
    }
    
    /**
     * RoomControllerでのチェックをのけてきた後の処理 トークを表示する処理
     * 
     * @param  model
     * @param  roomId
     * @return
     */
    @GetMapping ("/talk")
    public String taklRoom (Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        int roomId = 0;
        try {
            roomId = (int) model.getAttribute ("roomId");
        } catch (Exception e) {
            return "redirect:/";
        }
        
        int joinNum = roomService.getJoinNumber (roomId, userDetails.getId ());
        // 部屋の作成者か、参加者だったら、トーク情報を取得してトーク画面に遷移
        if (joinNum == RoomPermissionFile.ROOM_CTRL_DONE || joinNum == RoomPermissionFile.ROOM_CTRL_MASTER) {
            List<Talk> talk = talkService.getTalk (roomId);
            
            model.addAttribute ("talkData", talk);
            model.addAttribute ("roomId", roomId);
            model.addAttribute ("talkForm", new Talk ());
            
            return "/room/talk/talkRoom";
        }
        
        return "redirect:/";
    }
    
    /**
     * コメントの投稿
     * 
     * @param  talkRequest
     * @param  userDetails
     * @param  modelr
     * @return
     */
    @Transactional
    @PostMapping ("/talk/createMessage")
    public String createtalk (@ModelAttribute TalkRequest talkRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails, Model model, @Validated Talk talk,
            BindingResult result, RedirectAttributes attributes, RedirectAttributes redirectAttributes) {
        
        if (!result.hasErrors ()) {
            // ユーザーIDをセットする
            talkRequest.setUserId (userDetails.getId ());
            // コメントの投稿処理
            talkService.createMessage (talkRequest);
        } else {
            attributes.addFlashAttribute ("errMsg", result.hasFieldErrors ("talk"));
        }
        
        redirectAttributes.addFlashAttribute ("roomId", talkRequest.getRoomId ());
        return "redirect:/talk/";
    }
    
}
