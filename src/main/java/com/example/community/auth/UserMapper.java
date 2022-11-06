package com.example.community.auth;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.community.entity.User;

/**
 * ユーザーに関するDB層
 * 
 * @author user
 *
 */
@Mapper
public interface UserMapper {
    
    @Select ("SELECT id, user_name, email, password, img_name, user_comment, role_user  FROM users WHERE email = #{email}")
    public UserDetailsImpl findByUserName (String email);
    
    public int isExistUser (String email);
    
    /**
     * ユーザーの情報を取得
     * 
     * @param  userId
     * @return
     */
    public User getUserInfo (String userId);
    
    /**
     * 部屋に参加しているユーザ情報を取得
     * 
     * @param  roomId 部屋ID
     * @return        部屋に参加しているユーザーの情報
     */
    List<User> getJoinRoomUser (int roomId);
    
    /**
     * ユーザーのイメージ画像の名前をセットする。
     * 
     * @param fileName
     */
    public void setUserImageName (String fileName, String userId);
    
    /**
     * EmailのIDを取得する。
     * 
     * @param  email
     * @return
     */
    public String getEmailId (String email);
    
    /**
     * 仮登録のIDを取得する。
     * 
     * @param  email
     * @return
     */
    public String getTemporaryUsersId (String email);
    
    /**
     * 仮登録をする
     * 
     * @param userInfoDTO
     * @param token
     */
    public void setTemporaryUsers (String userName, String email, String password, String token);
    
    /**
     * 仮登録を更新する。
     * 
     * @param userInfoDTO
     * @param token
     */
    public void updateTemporaryUser (String userName, String email, String password, String token);

    /**
     * tokenが存在しているか確認
     * 
     * @param token
     * @return
     */
    public UserDetailsImpl getTemporaryUser (String token);
    
    /**
     * ユーザー本登録
     * 
     * @param userName
     * @param email
     * @param password
     */
    public void registUser (String id, String userName, String email, String password);

    /**
     * 仮登録削除
     * 
     * @param email
     */
    public void deleteTemporaryUser (String email);
    
}
