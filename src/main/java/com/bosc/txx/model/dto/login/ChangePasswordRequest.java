package com.bosc.txx.model.dto.login;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ChangePasswordRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String newPassword;
}


