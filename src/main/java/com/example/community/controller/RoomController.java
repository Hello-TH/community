package com.example.community.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.community.RoomPermissionFile;
import com.example.community.auth.UserDetailsImpl;
import com.example.community.auth.UserDetailsServiceImpl;
import com.example.community.dto.RoomRequest;
import com.example.community.entity.Room;
import com.example.community.entity.Talk;
import com.example.community.entity.User;
import com.example.community.service.RoomService;
import com.example.community.service.TalkService;

/**
 * 部屋に対しての処理のコントローラー
 * 
 * @author user
 *
 */
@Controller
@RequestMapping ("/")
public class RoomController {
    
    private final RoomService roomService;
    private final TalkService talkService;
    private final UserDetailsServiceImpl userService;
    private final MessageSource messageSource;
    
    @Autowired
    public RoomController (RoomService roomService, TalkService talkService, UserDetailsServiceImpl userService,
            MessageSource messageSource) {
        this.roomService = roomService;
        this.talkService = talkService;
        this.userService = userService;
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
     * TOPページを表示する。
     * 
     * @param  model 参加人数の多いコミュニティーを20個表示する
     * @return       トップページ
     */
    @GetMapping ("/")
    public String index (Model model) {
        
        int pageSize = roomService.getPageSize (); // 最大ページ数を取得する。
        int defaultPage = 0; // 並べ替えて、値 〜 (値 + 10)取ってくる
        int currentPage = 1; // 現在表示しているページ
        
        List<Room> roomData = roomService.getMostPeopleLenRoom (defaultPage); // 最も参加人数の多い部屋を10件取る
        List<Talk> talk = talkService.getRecentTalk (); // 自由制の部屋の中で、最新のトークを10個表示する。
        
        model.addAttribute ("currentPage", currentPage);
        model.addAttribute ("pageSize", pageSize);
        model.addAttribute ("roomData", roomData);
        model.addAttribute ("tarkData", talk);
        
        return "index";
    }
    
    /**
     * ページャー機能、トップページのみ
     * 
     * @param  model
     * @param  page
     * @return
     */
    @PostMapping ("/")
    public String pager (Model model, @RequestParam int page) {
        
        int pageSize = roomService.getPageSize (); // 最大ページ数を取得する。
        int pageNum = page; // 並べ替えて、値 〜 (値 + 10)取ってくる
        
        if (page > pageSize) {
            pageNum = pageSize;
            page = pageSize;
        } else if (page < 1) {
            pageNum = 1; // 送られてくるPageが1より小さかったら、1に設定する。
            page = 1;
        }
        
        List<Room> roomData = roomService.getMostPeopleLenRoom ((pageNum - 1) * 10); // 渡された値 ~ (渡された値 + 10)取ってくる。
        List<Talk> talk = talkService.getRecentTalk (); // 自由制の部屋の中で、最新のトークを10個表示する。
        
        model.addAttribute ("currentPage", page);
        model.addAttribute ("pageSize", pageSize);
        model.addAttribute ("roomData", roomData);
        model.addAttribute ("tarkData", talk);
        
        return "index";
    }
    
    /**
     * 部屋を検索する
     * 
     * @param  searchWord 検索ワード
     * @param  model
     * @return            検索ワードをもとにした部屋情報
     */
    @GetMapping ("/roomSerch")
    public String roomSerch (@RequestParam ("searchWord") String searchWord, Model model) {
        
        List<Room> room = roomService.getSearchRoom (searchWord);
        List<Talk> talk = talkService.getRecentTalk (); // 自由制の部屋の中で、最新のトークを10個表示する。
        
        // 検索のページャーを後ほど作成
        int pageSize = 1; // 最大ページ数を取得する。
        int currentPage = 0; // 現在表示しているページ
        model.addAttribute ("currentPage", currentPage);
        model.addAttribute ("pageSize", pageSize);
        
        model.addAttribute ("searchWord", searchWord);
        model.addAttribute ("tarkData", talk);
        model.addAttribute ("roomData", room);
        return "index";
    }
    
    /**
     * 部屋の詳細と、参加しているユーザーを渡す
     * 
     * @param  model
     * @param  roomId 部屋ID
     * @return
     */
    @GetMapping ("/roomDetails/{roomId}")
    public String roomDitals (Model model, @PathVariable int roomId,
            @AuthenticationPrincipal UserDetailsImpl userDetails, RedirectAttributes attributes) {
        
        Room room = roomService.getRoom (roomId);
        int joinNum = roomService.getJoinNumber (roomId, userDetails.getId ()); // 現在の部屋に対しての状況を示す番号
        
        // ルームが存在しない、または入室していないユーザー
        if (room == null || joinNum < RoomPermissionFile.ROOM_CTRL_DONE) {
            attributes.addFlashAttribute ("msg", getMessage ("NotFound.Room"));
            return "redirect:/errorPage";
        }
        
        List<User> userData = userService.getJoinRoomUser (roomId); // 入室しているユーザーの情報を取得
        int evaluation = roomService.getEvaluationStatus (userDetails.getId (), roomId); // 部屋に対する評価
        String createUserName = roomService.getCreateRoomUser (roomId).getUserName (); // 作成者の名前
        String createUserId = roomService.getCreateRoomUser (roomId).getId (); // 作成者のID
        
        model.addAttribute ("roomData", room);
        model.addAttribute ("userData", userData);
        model.addAttribute ("evaluation", evaluation);
        model.addAttribute ("createUserName", createUserName);
        model.addAttribute ("createUserId", createUserId);
        
        return "/room/roomDetail";
    }
    
    /**
     * 部屋の承認、自由を確認する。 自由だったら参加処理をして、トークルームに移動。 承認だったら、承認だったら、承認待ちページを表示する。
     * 
     * @param  userDetails
     * @param  roomId
     * @return
     */
    @PostMapping ("/room/joinRequest")
    public String joinRequest (RedirectAttributes redirectAttributes, @RequestParam int roomId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        redirectAttributes.addFlashAttribute ("roomId", roomId);
        roomService.requestJoin (roomId, userDetails.getId ());
        return "redirect:/talkRoomRequest/" + roomId;
    }
    
    /**
     * 参加済みの部屋であればトークルームを表示する。 未参加であれば、参加ボタン、申請ボタンを表示する。 すでに参加済みの場合はトーク画面を表示する。
     * 
     * @return
     */
    @GetMapping ("/talkRoomRequest/{roomId}")
    public String tarkRoomRequest (@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable int roomId,
            Model model, RedirectAttributes redirectAttributes) {
        
        // 現在のユーザーに対してのルーム権限
        int joinNum = roomService.getJoinNumber (roomId, userDetails.getId ());
        
        // FLEE_APPRIVAL_NUM = 自由制 APPRIVAL_NUM = 承認制 EMPTY_APPROVAL_NUM = 処理なし
        int approvalNum = roomService.getApprovalNum (roomId);
        
        // 部屋情報 → もし削除されていたりして、部屋情報を取得できなかったらホームに戻す
        try {
            model.addAttribute ("roomData", roomService.getRoom (roomId));
        } catch (Exception e) {
            return "redirect:/";
        }
        
        // 部屋に参加しているか未参加か
        if (joinNum == RoomPermissionFile.ROOM_CTRL_EMPTY) {
            // 部屋が自由制か承認制かで分ける
            if (approvalNum == RoomPermissionFile.ROOM_APPRIVAL_FREE) {
                // 自由参加申請ページ表示
                return "/room/freeJoinPage";
            } else {
                // 承認制参加申請ページを表示する。
                return "/room/limitJoinPage";
            }
        } else if (joinNum == RoomPermissionFile.ROOM_CTRL_DONE || joinNum == RoomPermissionFile.ROOM_CTRL_MASTER) {
            // 作成者か承認済みの参加者であればトーク画面に遷移する。
            redirectAttributes.addFlashAttribute ("roomId", roomId);
            return "redirect:/talk";
        } else if (joinNum == RoomPermissionFile.ROOM_CTRL_WAIT) {
            // 参加待ちの場合、参加待ちページに遷移する。
            return "/room/watiJoinPage";
        }
        
        // すべてに当てはまらない場合はエラーページを表示する。
        return "redirect:/";
    }
    
    /**
     * 申請ユーザーの入室の許可
     * 
     * @param  model
     * @param  roomId     リクエストされている部屋ID
     * @param  userId     リクエストをしたユーザーID
     * @param  attributes
     * @return
     */
    @PostMapping ("/doPermission")
    public String doPermission (Model model, @RequestParam int roomId, @RequestParam String userId,
            RedirectAttributes attributes) {
        
        roomService.doPermission (roomId, userId);
        
        attributes.addFlashAttribute ("msg", getMessage ("roomJoin.ok"));
        return "redirect:/showTalkList";
    }
    
    /**
     * 申請ユーザーの入室の非許可
     * 
     * @param  model
     * @param  roomId     リクエストされている部屋ID
     * @param  userId     リクエストをしたユーザーID
     * @param  attributes
     * @return
     */
    @PostMapping ("/notPermission")
    public String notPermission (Model model, @RequestParam int roomId, @RequestParam String userId,
            RedirectAttributes attributes) {
        
        roomService.notPermission (roomId, userId);
        
        attributes.addFlashAttribute ("msg", getMessage ("roomJoin.not"));
        return "redirect:/showTalkList";
    }
    
    /**
     * 部屋作成フォーム表示
     * 
     * @param  model 部屋作成の項目
     * @return
     */
    @GetMapping ("/room/createForm")
    public String roomCreateForm (Model model) {
        
        model.addAttribute ("roomData", new Room ());
        return "room/createRoom";
    }
    
    /**
     * 部屋作成実行
     * 
     * @param  roomRequest
     * @param  userDetails
     * @return
     */
    @Transactional
    @PostMapping ("/room/create")
    public String roomCreate (@ModelAttribute RoomRequest roomRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails, RedirectAttributes attributes, @Validated Room room,
            BindingResult result, Model model) {
        
        // バリデーションに引っかかったら、受け取った情報を戻す。
        if (result.hasErrors ()) {
            
            List<String> errorList = new ArrayList<String> ();
            for (ObjectError error : result.getAllErrors ()) {
                errorList.add (error.getDefaultMessage ());
            }
            
            model.addAttribute ("validationError", errorList);
            model.addAttribute ("roomData", roomRequest);
            
            return "room/createRoom";
        }
        
        // 部屋の作成者IDにログイン中のユーザーIDを入れる
        roomRequest.setCreateUserId (userDetails.getId ());
        
        // 部屋登録、部屋管理DB登録
        roomService.create (roomRequest);
        
        attributes.addFlashAttribute ("msg", getMessage ("create.Room"));
        
        return "redirect:/showTalkList";
    }
    
    /**
     * 部屋編集フォームを表示
     * 
     * @param  model 部屋の情報
     * @return
     */
    @GetMapping ("/room/editForm/{roomId}")
    public String roomEditForm (Model model, @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable int roomId) {
        
        Room room = roomService.getRoom (roomId);
        
        // ログイン中のIDと作成者IDが一致したら、、、
        if (userDetails.getId ().equals (room.getCreateUserId ())) {
            model.addAttribute ("roomData", room);
            return "room/editForm";
        }
        
        return "redirect:/";
    }
    
    /**
     * 部屋編集実行
     * 
     * @param  model
     * @param  roomRequest
     * @return
     */
    @PostMapping ("/room/edit")
    public String roomEdit (Model model, @ModelAttribute RoomRequest roomRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails, RedirectAttributes attributes, @Validated Room room,
            BindingResult result) {
        
        // バリデーションに引っかかったら、受け取った情報を戻す。
        if (result.hasErrors ()) {
            List<String> errorList = new ArrayList<String> ();
            for (ObjectError error : result.getAllErrors ()) {
                errorList.add (error.getDefaultMessage ());
            }
            
            model.addAttribute ("validationError", errorList);
            model.addAttribute ("roomData", roomRequest);
            
            return "room/editForm";
        }
        
        // 作成者のユーザーID
        String createUserId = roomService.getCreateRoomUser (roomRequest.getId ()).getId ();
        
        // ログイン中のユーザーが、作成者ユーザーと一致していない場合、編集させない
        if (!createUserId.equals (userDetails.getId ())) {
            attributes.addFlashAttribute ("msg", getMessage ("editNotPermission.Room"));
            return "redirect:/showTalkList";
        }
        
        roomService.edit (roomRequest);
        attributes.addFlashAttribute ("msg", getMessage ("editExe.Room"));
        return "redirect:/showTalkList";
    }
    
    /**
     * 部屋を削除する。
     * 
     * @return
     */
    @PostMapping ("/room/delete")
    public String roomDeleteForm (@RequestParam int roomId, Model model) {
        model.addAttribute ("roomData", roomService.getRoom (roomId));
        return "/room/deleteCheck";
    }
    
    /**
     * 部屋を削除する join_roomとtalkと部屋を削除する
     * 
     * @param  roomId     部屋ID
     * @param  attributes 削除メッセージをリダイレクト先に送る
     * @return
     */
    @PostMapping ("/room/deleteExe")
    public String roomDelete (@RequestParam int roomId, RedirectAttributes attributes,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String createUserId = roomService.getCreateRoomUser (roomId).getId ();
        
        // 削除するルームが、部屋の作成者とログインしている人で一致していない場合削除させない
        if (!createUserId.equals (userDetails.getId ())) {
            attributes.addFlashAttribute ("msg", getMessage ("deleteNotPermission.Room"));
            return "redirect:/showTalkList";
        }
        
        roomService.deleteRoom (roomId); // 部屋削除
        attributes.addFlashAttribute ("msg", getMessage ("deleteExe.Room"));
        return "redirect:/showTalkList";
    }
    
    /**
     * 部屋退室処理
     * 
     * @param  roomId      部屋ID
     * @param  userId      ユーザーID
     * @param  attributes  部屋に関するメッセージ
     * @param  userDetails ログインデータ取得
     * @return
     */
    @Transactional
    @PostMapping ("/doExit")
    public String doExit (@RequestParam int roomId, @RequestParam String userId, RedirectAttributes attributes,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        int joinNum = roomService.getJoinNumber (roomId, userId);
        
        // 部屋に参加中かつ、ログイン中のIDがある
        if (joinNum > 1) {
            roomService.roomPeopleSub (roomId);
            roomService.notPermission (roomId, userId);
            attributes.addFlashAttribute ("msg", getMessage ("doExit.Room"));
        } else {
            attributes.addFlashAttribute ("msg", getMessage ("exitError.Room"));
        }
        
        return "redirect:/showTalkList";
    }
    
    /**
     * 部屋を退出させる処理。作成者限定
     * 
     * @param  roomId       部屋ID
     * @param  userId       削除させる部屋ID
     * @param  userName     削除させるユーザー名
     * @param  createUserId 作成した人のユーザーID
     * @param  attributes   退出処理後のメッセージ処理
     * @param  userDetails  ログイン中のユーザーIDを取得
     * @return
     */
    @PostMapping ("/forcedExit")
    public String forcedExit (@RequestParam int roomId, @RequestParam String userId, @RequestParam String userName,
            @RequestParam String createUserId, RedirectAttributes attributes,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        
        int joinNum = roomService.getJoinNumber (roomId, userDetails.getId ());
        
        // 部屋の作成者であれば退出させる。
        if (joinNum == 3) {
            roomService.roomPeopleSub (roomId);
            roomService.notPermission (roomId, userId);
            attributes.addFlashAttribute ("msg", userName + getMessage ("forcedExit.Room"));
        } else {
            attributes.addFlashAttribute ("msg", userName + getMessage ("forcedExitError.Room"));
        }
        
        return "redirect:/roomDetails/" + roomId;
    }
    
    @PostMapping ("/room/evaluation/")
    public String evaluation (@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam String evaluation,
            @RequestParam int roomId, Model model, RedirectAttributes attributes) {
        
        roomService.changeEvaluation (userDetails.getId (), roomId, evaluation);
        return "redirect:/roomDetails/" + roomId;
    }
    
    @GetMapping ("/errorPage")
    public String errorPage () {
        return "/errorPage";
    }
    
}
