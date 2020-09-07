package com.example.oauth.component;


import com.example.oauth.util.UserPermissions;
import com.sun.deploy.nativesandbox.comm.Response;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Aspect
@Component
public class UserPermissionsImpl  {

    @Around(value = "@annotation(anno)", argNames = "anno") // aspect method who have the annotation @Delegate
    public Object handle(ProceedingJoinPoint joinPoint, UserPermissions permissions) throws Throwable {
        try {

            if (permissions.value().isEmpty()) {
                return joinPoint.proceed();
            }

            SimpleGrantedAuthority scope = new SimpleGrantedAuthority(permissions.value());
            Collection<SimpleGrantedAuthority> authorities =
                    (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

            if (authorities == null || authorities.size() == 0 || !authorities.contains(scope)) {
                return "Insufficient permission, access denied";
            }

            return joinPoint.proceed();
        }catch (Exception e) {
            return "Insufficient permission, access denied";
        }
    }
}
