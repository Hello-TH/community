package com.example.community.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.community.dto.TalkRequest;
import com.example.community.entity.Room;
import com.example.community.entity.Talk;

/**
 * Talk関連のDB処理
 * 
 * @author user
 */
@Mapper
public interface TalkMapper {
    
    /**
     * 最新トーク情報を取得する
     * 
     * @return
     */
    List<Talk> getRecentTalk ();
    
    /**
     * 参加中の部屋情報を取得
     * 
     * @param  userId
     * @return
     */
    List<Room> getJoinRoomData (String userId, int ROOM_CTRL_DONE, int ROOM_CTRL_MASTER);
    
    /**
     * 作成した部屋情報を取得
     * 
     * @param  userId
     * @param  MASTER_JOIN_NUM
     * @return
     */
    List<Room> getMasterRoomData (String userId, int ROOM_CTRL_MASTER);
    
    /**
     * コメント投稿処理
     * 
     * @param tarkRequest
     */
    void createMessage (TalkRequest talkRequest);
    
    /**
     * talkの情報を取得と、ユーザー名をDB結合で取得する。
     * 
     * @param  roomId 部屋ID
     * @return        talk情報
     */
    List<Talk> getTalk (int roomId);
    
    /**
     * talkを削除します。
     * 
     * @param roomId
     */
    void talkDelete (int roomId);
    
    /**
     * ユーザーがコメントした回数を返却
     * 
     * @param  userId ユーザーID
     * @return        コメントした回数
     */
    int getUserTalkLength (String userId);
    
    /**
     * 部屋に対してのリクエストを検索して渡す
     * 
     * @param  userId 部屋ID
     * @return        部屋情報を渡す
     */
    List<Talk> getJoinRequest (String userId, int ROOM_CTRL_WAIT);
    
}