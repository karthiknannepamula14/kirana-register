package com.jar.assignment.kiranaregister.dto;

import com.jar.assignment.kiranaregister.utils.AuthSource;
import lombok.Builder;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
@Builder
public class AuthDto {
    private HttpServletRequest request;
    private String deviceId;
    private AuthSource authSource;
}
