package com.example.community;

/**
 * 部屋の承認の有無と、部屋に対してのユーザー
 * のステータスを表す数字を管理
 * 
 * @author user
 */
public class RoomPermissionFile {
    
    // 参加待ち番号
    public static final int ROOM_CTRL_WAIT = 1;
    // 参加中番号
    public static final int ROOM_CTRL_DONE = 2;
    // 作成者番号
    public static final int ROOM_CTRL_MASTER = 3;
    // 作成者番号
    public static final int ROOM_CTRL_EMPTY = 0; 
    
    // 部屋の参加 自由制
    public static final int ROOM_APPRIVAL_FREE = 1;
    // 部屋の参加 承認制
    public static final int ROOM_APPROVAL_LIMIT = 2;
    // 初めて部屋にアクセス
    public static final int ROOM_APPROVAL_EMPTY = 0; 
    
    // 画像を保存するためのパス
    public static final String IMG_FOLDER_PATH = "/home/user/communityUserImageFolders/";
    
}
