package com.example.community.auth;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * ログイン情報を保持するクラス
 * 
 * @author user
 *
 */
public class UserDetailsImpl implements UserDetails {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String user_name;
    private String email;
    private String password;
    private String img_name;
    private Blob img;
    private String user_comment;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Collection<GrantedAuthority> Authorities;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities () {
        return this.Authorities;
    }
    
    public static long getSerialversionuid () {
        return serialVersionUID;
    }
    
    @Override
    public boolean isAccountNonExpired () {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked () {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired () {
        return true;
    }
    
    @Override
    public boolean isEnabled () {
        return true;
    }
    
    @Override
    public String getPassword () {
        return this.password;
    }
    
    @Override
    public String getUsername () {
        return this.user_name;
    }
    
    public String getId () {
        return id;
    }
    
    public void setId (String id) {
        this.id = id;
    }
    

    public String getUser_name () {
        return user_name;
    }
    
    public void setUser_name (String user_name) {
        this.user_name = user_name;
    }
    
    public String getEmail () {
        return email;
    }
    
    public void setEmail (String email) {
        this.email = email;
    }
    
    public String getImg_name () {
        return img_name;
    }
    
    public void setImg_name (String img_name) {
        this.img_name = img_name;
    }
    
    public Blob getImg () {
        return img;
    }
    
    public void setImg (Blob img) {
        this.img = img;
    }
    
    public String getUser_comment () {
        return user_comment;
    }
    
    public void setUser_comment (String user_comment) {
        this.user_comment = user_comment;
    }
    
    public Timestamp getCreated_at () {
        return created_at;
    }
    
    public void setCreated_at (Timestamp created_at) {
        this.created_at = created_at;
    }
    
    public Timestamp getUpdated_at () {
        return updated_at;
    }
    
    public void setUpdated_at (Timestamp updated_at) {
        this.updated_at = updated_at;
    }
    
    public void setPassword (String password) {
        this.password = password;
    }
    
    public void setAuthorities (Collection<GrantedAuthority> authorities) {
        Authorities = authorities;
    }
    
}