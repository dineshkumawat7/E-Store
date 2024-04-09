package com.ecommerce.shop.utils;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
public class SessionHelper {
    public void removeMessageFromSession() {
        try {
            System.out.println("removing message form session ");
            HttpSession session = ((ServletRequestAttributes) Objects
                    .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getSession();
            session.removeAttribute("message");
        } catch (Exception exception) {
            System.out.println("ERROR : " + exception);
        }
    }
}
