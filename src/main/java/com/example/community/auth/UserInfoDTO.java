package com.example.community.auth;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserInfoDTO {
    
    @Size (min = 1, max = 20, message = "{size.userName}")
    private String userName;
    
    @Pattern (regexp = "^([\\w])+([\\w\\._-])*\\@([\\w])+([\\w\\._-])*\\.([a-zA-Z])+$", message = "{irregular.email}")
    private String email;
    
    @Pattern (regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[!-~]{8,30}$", message = "{irregular.password}")
    private String password;
    
    private String passwordConfirm;
    
    // パスワード一致確認
    @AssertTrue (message = "{isConf.password}")
    public boolean isPasswordCofirmed () {
        return password.equals (passwordConfirm);
    }
    
    public String getUserName () {
        return userName;
    }
    
    public void setUserName (String userName) {
        this.userName = userName;
    }
    
    public String getEmail () {
        return email;
    }
    
    public void setEmail (String email) {
        this.email = email;
    }
    
    public String getPassword () {
        return password;
    }
    
    public void setPassword (String password) {
        this.password = password;
    }
    
    public String getPasswordConfirm () {
        return passwordConfirm;
    }
    
    public void setPasswordConfirm (String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
    
}
