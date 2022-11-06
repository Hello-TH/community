package com.example.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.community.RoomPermissionFile;
import com.example.community.dto.RoomRequest;
import com.example.community.entity.Room;
import com.example.community.entity.User;
import com.example.community.repository.RoomMapper;
import com.example.community.repository.TalkMapper;

/**
 * ユーザー情報 Service
 */
@Service
public class RoomService {
    
    private final RoomMapper roomMapper;
    private final TalkMapper tarkMapper;
    
    @Autowired
    public RoomService (RoomMapper roomMapper, TalkMapper tarkMapper) {
        this.roomMapper = roomMapper;
        this.tarkMapper = tarkMapper;
    }
    
    /**
     * トップページの人気コミュニティー表示
     * 
     * @param  page
     * 
     * @param  pageable
     * 
     * @return
     */
    public List<Room> getMostPeopleLenRoom (int page) {
        return roomMapper.getMostPeopleLenRoom (page);
    }
    
    /**
     * Pagerの数字を表示する個数を求める →
     * 
     * @return
     */
    public int getPageSize () {
        
        int roomLength = roomMapper.getRoomLength (); // 部屋の総数
        int pageNum = roomLength / 10; // 部屋を10で割った数
        
        // ページが100を超えている場合は、Pagerの値の最高である10にセットする。
        // 51 など、あまりが出る場合には +1 して送る。
        if (roomLength % 10 >= 10) {
            return 10;
        } else if (roomLength % 10 == 0) {
            return pageNum;
        }
        return ++pageNum;
    }
    
    /**
     * 部屋の検索
     * 
     * @param  searchWord 検索ワード
     * @return
     */
    public List<Room> getSearchRoom (String searchWord) {
        return roomMapper.getSearchRoom (searchWord, RoomPermissionFile.ROOM_APPRIVAL_FREE);
    }
    
    /**
     * 部屋の作成者の情報を取得
     * 
     * @param  roomId 部屋ID
     * @return
     */
    public User getCreateRoomUser (int roomId) {
        return roomMapper.getCreateRoomUser (roomId);
    }
    
    /**
     * 現在の部屋の状況を取得する → 申請済み、未申請、参加
     * 
     * @param  roomId
     * @param  userId
     * @return
     */
    public int getJoinNumber (int roomId, String userId) {
        
        Integer joinNum = roomMapper.getJoinNumber (roomId, userId);
        
        if (joinNum == null) {
            return RoomPermissionFile.ROOM_CTRL_EMPTY;
        }
        
        return (int) joinNum;
    }
    
    /**
     * approvalNumを取得 部屋の承認状況を取得する。
     * 
     * @param  roomId
     * @return
     */
    public int getApprovalNum (int roomId) {
        
        Integer applovalNum = roomMapper.getApprovalNum (roomId);
        
        if (applovalNum == null) {
            return RoomPermissionFile.ROOM_APPROVAL_EMPTY;
        }
        
        return (int) applovalNum;
    }
    
    /**
     * 承認済みの部屋に参加申請を送る
     * 
     * @param roomId
     * @param userId
     */
    public void userJoinRequest (int roomId, String userId) {
        roomMapper.userJoinRequest (roomId, userId, RoomPermissionFile.ROOM_CTRL_WAIT);
    }
    
    /**
     * 部屋が承認の場合、承認リクエストを送る。自由だった場合、参加登録をする。
     * 
     * @param roomId リクエストをする部屋ID
     * @param userId リクエストをしたユーザーID
     */
    @Transactional
    public void requestJoin (int roomId, String userId) {
        // 部屋の承認、自由制を判定する番号
        int approvalNum = this.getApprovalNum (roomId);
        // 部屋IDの部屋の申請状況
        int joinNum = this.getJoinNumber (roomId, userId);
        
        // 部屋が自由制だったら、人数を更新して、参加
        if (approvalNum == RoomPermissionFile.ROOM_APPRIVAL_FREE && joinNum == RoomPermissionFile.ROOM_APPROVAL_EMPTY) {
            // 人数を更新する。
            this.roomPeopleAdd (roomId);
            // フラグを1にして参加する
            this.userJoin (roomId, userId);
        } else if (approvalNum == RoomPermissionFile.ROOM_APPROVAL_LIMIT) {
            // 参加申請ページを表示する フラグは0にする。 作成者に申請を送る。
            this.userJoinRequest (roomId, userId);
        }
    }
    
    /**
     * 部屋の人数を一人増やす
     * 
     * @param roomId
     */
    public void roomPeopleAdd (int roomId) {
        roomMapper.roomPeopleAdd (roomId);
    }
    
    public void roomPeopleSub (int roomId) {
        roomMapper.roomPeopleSub (roomId);
    }
    
    /**
     * 部屋の情報を取得する
     * 
     * @param  roomId
     * @return
     */
    public Room getRoom (int roomId) {
        return roomMapper.getRoom (roomId);
    }
    
    /**
     * 部屋の作成した後、部屋アクセス管理DBに作成者番号を登録する。
     * 
     * @param roomRequest 部屋作成フォームから受け取った部屋情報
     */
    @Transactional
    public void create (RoomRequest roomRequest) {
        // 部屋作成
        roomMapper.create (roomRequest);
        
        // 作成した部屋のIDを取得
        int roomId = roomMapper.createdRoomId ();
        // 部屋のアクセス管理DBに作成者番号を登録する。
        this.createMasterJoinUser (roomId, roomRequest.getCreateUserId ());
    }
    
    /**
     * 部屋の作成者情報を、部屋とユーザーの管理テーブルにInsertする。
     * 
     * @param roomId 部屋ID
     * @param userId
     */
    public void createMasterJoinUser (int roomId, String userId) {
        roomMapper.createMasterJoinUser (roomId, userId, RoomPermissionFile.ROOM_CTRL_MASTER);
    }
    
    /**
     * 部屋参加
     * 
     * @param roomId
     * @param userId
     */
    public void userJoin (int roomId, String userId) {
        roomMapper.userJoin (roomId, userId, RoomPermissionFile.ROOM_CTRL_DONE);
    }
    
    /**
     * 部屋の編集
     * 
     * @param roomRequest 部屋編集データ
     */
    public void edit (RoomRequest roomRequest) {
        roomMapper.roomEdit (roomRequest);
    }
    
    /**
     * 部屋の削除を実行する。
     * 
     * @param roomId
     */
    @Transactional
    public void deleteRoom (int roomId) {
        roomMapper.evaluationDelete (roomId); // 評価を削除する。
        roomMapper.join_delete (roomId); // 部屋の参加状況のテーブルを削除する。
        tarkMapper.talkDelete (roomId); // トークを削除する。
        roomMapper.roomDelete (roomId); // 部屋を削除する。
    }
    
    /**
     * 作成した部屋の数を取得
     * 
     * @param  userId ユーザーID
     * @return
     */
    public int getCreateRoomLength (String userId) {
        return roomMapper.getCreateRoomLength (userId);
    }
    
    /**
     * 参加している部屋の数を取得
     * 
     * @param  userId
     * @return
     */
    public int getJoinRoomLength (String userId) {
        return roomMapper.getJoinRoomLength (userId, RoomPermissionFile.ROOM_CTRL_DONE);
    }
    
    /**
     * 申請ユーザーの入室の許可
     * 
     * @param roomId
     * @param userId
     */
    public void doPermission (int roomId, String userId) {
        roomMapper.doPermission (roomId, userId, RoomPermissionFile.ROOM_CTRL_DONE);
        roomMapper.roomPeopleAdd (roomId);
    }
    
    /**
     * 申請ユーザーの入室の非許可
     * 
     * @param roomId
     * @param userId
     */
    public void notPermission (int roomId, String userId) {
        roomMapper.notPermission (roomId, userId);
    }
    
    /**
     * good,badの評価と、総数の整合性を合わせる
     * 
     * @param userId
     * @param roomId
     * @param evalution
     */
    @Transactional
    public void changeEvaluation (String userId, int roomId, String evaluation) {
        
        // 評価管理のステータスナンバー
        int NeitherNum = 0;
        int goodNum = 1;
        int badNum = 2;
        
        // 総合評価数を変更する際のナンバー
        int goodSub = -1;
        int badSub = -1;
        int goodAdd = 1;
        int badAdd = 1;
        int goodStay = 0;
        int badStay = 0;
        
        // 現在の部屋に対しての評価 なし null good 1 bad 0
        Integer evalutionStatus = roomMapper.getEvaluationStatus (userId, roomId);
        
        // 高評価を押したかどうか
        boolean goodRequest = evaluation.equals ("good");
        
        // 過去に評価が無いため、新規登録
        if (evalutionStatus == null) {
            
            int good = 0; // 高評価
            int bad = 0; // 低評価
            int status = 0; // 現在の評価ステータス
            
            // リクエストに応じて、数字をセットする。
            if (goodRequest == true) {
                good = goodAdd;
                bad = badStay;
                status = goodNum;
            } else {
                good = goodStay;
                bad = badAdd;
                status = badNum;
            }
            
            roomMapper.evaluationTotalCange (roomId, good, bad); // 評価総数の更新
            roomMapper.evalutionCreate (roomId, userId, status); // 評価を管理しているテーブルに登録する。
        }
        
        // すでに評価を1回以上行っている場合 評価の更新を行う
        if (evalutionStatus != null) {
            
            int good = 0; // 高評価
            int bad = 0; // 低評価
            int status = 0; // 現在の評価ステータス
            
            // goodかbad、どちらのリクエストかによって処理を分ける;
            if (goodRequest == true) {
                
                // 高評価を選択した場合の更新を現在の値と比較して、整合性を保つように更新する。
                switch (evalutionStatus) {
                    // good希望で、現在評価なし
                    case 0:
                        good = goodAdd;
                        bad = badStay;
                        status = goodNum;
                        break;
                    // good希望で、現在高評価の場合、Goodを増やして、badを減らす。
                    case 1:
                        good = goodSub;
                        bad = badStay;
                        status = NeitherNum;
                        break;
                    case 2:
                        good = goodAdd;
                        bad = badSub;
                        status = goodNum;
                        break;
                }
            } else {
                switch (evalutionStatus) {
                    case 0:
                        good = goodStay;
                        bad = badAdd;
                        status = badNum;
                        break;
                    case 1:
                        good = goodSub;
                        bad = badAdd;
                        status = badNum;
                        break;
                    case 2:
                        good = goodStay;
                        bad = badSub;
                        status = NeitherNum;
                        break;
                }
            }
            
            roomMapper.evaluationTotalCange (roomId, good, bad); // 評価総数の更新
            roomMapper.evaluationChange (roomId, userId, status); // 評価の更新
            
        } // if
    } // changeEvaluation
    
    /**
     * 現在の部屋に対しての評価を取り出して返す。 null だったら
     * 
     * @param  userId
     * @param  roomId
     * @return
     */
    public int getEvaluationStatus (String userId, int roomId) {
        
        Integer evaluation = roomMapper.getEvaluationStatus (userId, roomId);
        
        if (evaluation == null) {
            return 0;
        }
        
        return (int) evaluation;
    }
    
}
