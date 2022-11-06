package com.example.community.auth;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

/**
 * ログイン処理を作る際に必須の項目を設定
 * @author user
 *
 */
public interface UserDetails extends Serializable {

	Collection<? extends GrantedAuthority> getAuthorities(); // 権限リストを返す
	String getPassword(); // パスワードを返す
	String getName(); // ユーザー名を返す
	boolean isAccountNonExpired(); // アカウントの有効期限の判定
	boolean isAccountNonLocked(); // アカウントのロック状態の判定
	boolean isCredentialsNonExpired(); // 資格情報の有効期限の判定
	boolean isEnabled(); // 有効なユーザーであるかの判定
	
}
