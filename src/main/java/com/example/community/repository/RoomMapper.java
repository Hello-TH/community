package com.example.community.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.community.dto.RoomRequest;
import com.example.community.entity.Room;
import com.example.community.entity.User;

/**
 * 部屋関連のDB処理
 * 
 * @author user
 */
@Mapper
public interface RoomMapper {
    
    /**
     * 部屋の一覧表示
     * @param page 
     * 
     * @return 部屋一覧
     */
    List<Room> getMostPeopleLenRoom (int page);
    
    /**
     * 部屋の総数取得
     * 
     * @return
     */
    int getRoomLength ();

    
    /**
     * 検索された単語で、部屋を検索して、返却する
     * 
     * @param  searchWord
     * @return
     */
    List<Room> getSearchRoom (String searchWord, int ROOM_APPRIVAL_FREE);
    
    /**
     * 部屋の作成者の情報を取得
     * 
     * @param  roomId 部屋ID
     * @return
     */
    User getCreateRoomUser (int roomId);
    
    /**
     * 部屋に対して、ユーザーの権限を返す
     * 
     * @param  roomId 部屋ID
     * @param  userId ユーザーID
     * @return        権限番号
     */
    Integer getJoinNumber (int roomId, String userId);
    
    /**
     * 部屋の権限を返却する
     * 
     * @param  roomId 部屋ID
     * @return        0 = 自由 1 = 承認
     */
    Integer getApprovalNum (int roomId);
    
    /**
     * 自由の部屋に参加をする処理
     * 
     * @param roomId 部屋ID
     * @param userId ユーザーID
     */
    void userJoin (int roomId, String userId, int ROOM_CTRL_DONE);
    
    /**
     * 参加人数を1加算する。
     * 
     * @param roomId 部屋ID
     * 
     */
    void roomPeopleAdd (int roomId);
    
    /**
     * 参加人数を1減らす
     * 
     * @param roomId
     */
    void roomPeopleSub (int roomId);
    
    /**
     * 受け取った部屋IDのデータを返却する
     * 
     * @return 部屋情報
     */
    Room getRoom (int roomId);
    
    /**
     * 登録した部屋のIDを取得する
     * 
     * @return 最新のユーザーID
     */
    int createdRoomId ();
    
    /**
     * 部屋登録をする
     * 
     * @param roomRequest 作成フォームから受け取った部屋情報
     */
    void create (RoomRequest roomRequest);
    
    /**
     * 作成者の部屋権限を設定する
     * 
     * @param roomId 部屋ID
     * @param userId ユーザーID
     */
    void createMasterJoinUser (int roomId, String userId, int ROOM_CTRL_MASTER);
    
    /**
     * 部屋参加申請をする
     * 
     * @param roomId 部屋ID
     * @param userId ユーザーID
     */
    void userJoinRequest (int roomId, String userId, int ROOM_CTRL_WAIT);
    
    /**
     * ルーム編集
     * 
     * @param roomRequest 部屋編集情報
     */
    void roomEdit (RoomRequest roomRequest);
    
    /**
     * ルーム削除
     * 
     * @param roomId 部屋ID
     */
    void roomDelete (int roomId);
    
    /**
     * ルームの権限リストから部屋IDを削除します。
     * 
     * @param roomId 部屋ID
     */
    void join_delete (int roomId);
    
    /**
     * ユーザーが作成した部屋の数を取得
     * 
     * @param  userId ユーザーID
     * @return        作成した部屋の数
     */
    int getCreateRoomLength (String userId);
    
    /**
     * ユーザーが参加している部屋の数を取得
     * 
     * @param  userId      ユーザーID
     * @param  doneJoinNum 参加を示す番号
     * @return             参加している部屋の数
     */
    int getJoinRoomLength (String userId, int ROOM_CTRL_DONE);
    
    /**
     * 申請ユーザーの入室の許可
     * 
     * @param  roomId
     * @param  userId
     * @return
     */
    void doPermission (int roomId, String userId, int ROOM_CTRL_DONE);
    
    /**
     * 申請ユーザーの入室の非許可
     * 
     * @param  roomId
     * @param  userId
     * @return
     */
    void notPermission (int roomId, String userId);
    
    /**
     * 部屋に対しての評価を求める。
     * 
     * @param  userId
     * @return
     */
    Integer getEvaluationStatus (String userId, int roomId);
    
    /**
     * 部屋のGoodとBadを更新する。
     * 
     * @param roomId
     * @param good
     * @param bad
     */
    void evaluationTotalCange (int roomId, int good, int bad);
    
    /**
     * 受け取ったユーザーIDと部屋IDで、評価テーブルを作成する。
     * 
     * @param roomId
     * @param userId
     * @param status
     */
    void evalutionCreate (int roomId, String userId, int status);
    
    /**
     * 受け取ったユーザーIDと部屋IDで、評価テーブルを更新する。
     * 
     * @param roomId
     * @param userId
     * @param status
     */
    void evaluationChange (int roomId, String userId, int status);

    /**
     * 削除する部屋IDの評価を削除する。
     * 
     * @param roomId
     */
    void evaluationDelete (int roomId);
    
}