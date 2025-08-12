package com.chokhaniyash.streambridge.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Authorization {
    public String getUserId() {
        JwtAuthenticationToken token = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Map<String,Object> user = token.getTokenAttributes();
        return (String) user.get("sub");
    }
}
