package com.example.community.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * ユーザー作成フォーム
 * 
 * @author user
 *
 */
public class SignupForm {
    
    @NotBlank
    @Size (min = 1, max = 50)
    private String username;
    
    @NotBlank
    @Size (min = 6, max = 20)
    private String password;
    
    public String getUsername () {
        return username;
    }
    
    public void setUsername (String username) {
        this.username = username;
    }
    
    public String getPassword () {
        return password;
    }
    
    public void setPassword (String password) {
        this.password = password;
    }
}