package com.example.community.auth;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.community.RoomPermissionFile;
import com.example.community.entity.User;

/***
 * ユーザー処理に関するビジネスロジック
 * 
 * @author user
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserMapper userMapper;
    private MailSender mailSender;
    
    @Autowired
    public UserDetailsServiceImpl (UserMapper userMapper, MailSender mailSender) {
        this.userMapper = userMapper;
        this.mailSender = mailSender;
    }
    
    /**
     * ログイン処理、SpringSecurityの既存機能
     * 
     * @param  email メールアドレス
     * @return
     */
    @Override
    public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {
        
        UserDetailsImpl myUser = userMapper.findByUserName (email);
        return myUser;
    }
    
    /**
     * ユーザー名がすでに登録されているか調べる 登録されていたらTrueを返す
     * 
     * @param  username ユーザー名
     * @return
     */
    public boolean isExistUser (String username) {
        int count = userMapper.isExistUser (username);
        
        if (count == 0) {
            return false;
        }
        return true;
    }
    
    /**
     * 部屋に参加しているユーザーを取得
     * 
     * @param  roomId
     * @return
     */
    public List<User> getJoinRoomUser (int roomId) {
        return userMapper.getJoinRoomUser (roomId);
    }
    
    /**
     * ユーザー情報を取得する。
     * 
     * @param  userId
     * @return
     */
    public User getUserInfo (String userId) {
        return userMapper.getUserInfo (userId);
    }
    
    /**
     * 画像の名前をデータベースに登録する
     * 
     * @param fileName 画像の名前
     * @param userId   画像を変更するユーザーID
     */
    public void setUserImageName (String fileName, String userId) {
        userMapper.setUserImageName (fileName, userId);
    }
   
    
    /**
     * アップロード実行処理
     * 
     * @param multipartFile
     */
    public void uploadAction (MultipartFile multipartFile, String userId) {
        // ファイル名取得
        String fileName = multipartFile.getOriginalFilename ();
        
        // 格納先のフルパス ※事前に格納先フォルダ「UploadTest」をCドライブ直下に作成しておく
        Path filePath = Paths.get (RoomPermissionFile.IMG_FOLDER_PATH + fileName);
        
        try {
            // アップロードファイルをバイト値に変換
            byte[] bytes = multipartFile.getBytes ();
            
            // バイト値を書き込む為のファイルを作成して指定したパスに格納
            OutputStream stream = Files.newOutputStream (filePath);
            this.setUserImageName (fileName, userId);
            // ファイルに書き込み
            stream.write (bytes);
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
    
    /**
     * 本登録のDBにメールアドレスが登録済みか確認する。
     * 
     * @param  email メールアドレス
     * @return
     */
    public boolean registrationConf (String email) {
        
        String id = userMapper.getEmailId (email);
        if (id == null) {
            return false;
        }
        
        return true;
    }
    
    /**
     * DBにTokenを登録、更新する。
     * 
     * @param  userInfoDTO
     * @return             Token
     */
    public void temporarySignup (UserInfoDTO userInfoDTO) {
        
        // パスワードをハッシュ化 
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        String encodeedPassword = bcpe.encode (userInfoDTO.getPassword ());
        userInfoDTO.setPassword (encodeedPassword);
        
        // tokenを生成する。
        String token = UUID.randomUUID ().toString ();
        
        // 仮登録しているUserId
        String id = userMapper.getTemporaryUsersId (userInfoDTO.getEmail ());
        
        // 登録されていなければ新規登録、登録済みであれば更新する。
        if (id == null) {
            userMapper.setTemporaryUsers (userInfoDTO.getUserName (), userInfoDTO.getEmail (),
                    userInfoDTO.getPassword (), token);
        } else {
            userMapper.updateTemporaryUser (userInfoDTO.getUserName (), userInfoDTO.getEmail (),
                    userInfoDTO.getPassword (), token);
        }
        
        sendMail (userInfoDTO.getUserName (), userInfoDTO.getEmail (), token);
    }
    
    public void sendMail (String userName, String email, String token) {
        
        SimpleMailMessage msg = new SimpleMailMessage ();
        
        msg.setFrom (""); // 送信元メールアドレス
        msg.setTo (email); // 送信先メールアドレス
        msg.setSubject ("メールアドレスの確認"); // タイトル
        msg.setText ("こんにちは、" + userName + "さん。\r\n\r\n" + "Communityにご登録いただいたメールアドレスを確認します。\r\n"
                + "24時間以内に下記へアクセスし、登録を完了してください。\r\n\r\n" + " http://localhost:8765/signUp/" + token + "\r\n");
        
        try {
            mailSender.send (msg);
        } catch (MailException e) {
            e.printStackTrace ();
        }
        
    }
    
    /**
     * tokenから仮登録の情報を取得
     * 
     * @param token
     * @return User 
     */
    public UserDetailsImpl getTemporaryUser(String token) {
        return userMapper.getTemporaryUser(token);
    }

    /**
     * ユーザー登録した後、仮登録のユーザーを削除する
     * 
     * @param id
     * @param userName
     * @param email
     * @param password
     */
    public void registUser (String id, String userName, String email, String password) {
        userMapper.registUser(id, userName, email, password);
        userMapper.deleteTemporaryUser(email);
    }
}
