package com.example.community.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * ファイルアップロードのフォームクラス
 */
public class UploadForm {
    
    private MultipartFile multipartFile;
    
    public MultipartFile getMultipartFile () {
        return multipartFile;
    }
    
    public void setMultipartFile (MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
    
}