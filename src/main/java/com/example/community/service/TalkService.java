package com.example.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.community.RoomPermissionFile;
import com.example.community.dto.TalkRequest;
import com.example.community.entity.Room;
import com.example.community.entity.Talk;
import com.example.community.repository.TalkMapper;

/**
 * ユーザー情報 Service
 */
@Service
public class TalkService {
    
    private TalkMapper talkMapper;
    
    @Autowired
    public TalkService (TalkMapper talkMapper) {
        this.talkMapper = talkMapper;
    }
    
    /**
     * 自由参加制の部屋の最新トークを10個表示する。
     * 
     * @return
     */
    public List<Talk> getRecentTalk () {
        return talkMapper.getRecentTalk ();
    }
    
    /**
     * 参加中の部屋情報を取得
     * 
     * @param  userId ユーザーID
     * @return        参加中情報
     */
    public List<Room> getJoinRoomData (String userId) {
        return talkMapper.getJoinRoomData (userId, RoomPermissionFile.ROOM_CTRL_DONE,
                RoomPermissionFile.ROOM_CTRL_MASTER);
    }
    
    /**
     * 作成したの部屋情報を取得
     * 
     * @param  userId ユーザーID
     * @return        作成部屋情報
     */
    public List<Room> getMasterRoomData (String userId) {
        return talkMapper.getMasterRoomData (userId, RoomPermissionFile.ROOM_CTRL_MASTER);
    }
    
    /**
     * コメント投稿処理
     * 
     * @param talkRequest
     */
    public void createMessage (TalkRequest talkRequest) {
        talkMapper.createMessage (talkRequest);
    }
    
    /**
     * トーク情報を取得した後、ユーザー名を取得する
     * 
     * @param  roomId ルームID
     * @return        トーク情報
     */
    @Transactional
    public List<Talk> getTalk (int roomId) {
        return talkMapper.getTalk (roomId);
    }
    
    /**
     * トークした数を取得する
     * 
     * @param  userId
     * @return
     */
    public int getUserTalkLength (String userId) {
        return talkMapper.getUserTalkLength (userId);
    }
    
    /**
     * 部屋に対してのリクエストを検索して渡す
     * 
     * @param  userId
     * @return
     */
    public List<Talk> getJoinRequest (String userId) {
        return talkMapper.getJoinRequest (userId, RoomPermissionFile.ROOM_CTRL_WAIT);
    }
    
}