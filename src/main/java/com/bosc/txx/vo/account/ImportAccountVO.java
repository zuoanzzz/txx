package com.bosc.txx.vo.account;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImportAccountVO {
    private MultipartFile file;
}
