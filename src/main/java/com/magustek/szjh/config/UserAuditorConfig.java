package com.magustek.szjh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * 继承BaseEntity的实体类自动填充创建人、最后修改人字段
 * */

@Configuration
public class UserAuditorConfig implements AuditorAware<String> {
    @Override
    public String getCurrentAuditor() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx == null) {
            return "system";
        }
        if (ctx.getAuthentication() == null) {
            return "system";
        }
        if (ctx.getAuthentication().getPrincipal() == null) {
            return "system";
        }
        Object principal = ctx.getAuthentication().getPrincipal();
        if (principal.getClass().isAssignableFrom(User.class)) {
            return ((User) principal).getUsername();
        } else {
            return "system";
        }
    }
}
