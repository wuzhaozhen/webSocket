package com.wuzz.webScoket;

import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * @author wuzz
 * @date 15:06 2019/10/8
 */
@Component
public class RequestListener implements ServletRequestListener {

    public void requestInitialized(ServletRequestEvent sre) {
        //将所有request请求都携带上httpSession
        ((HttpServletRequest) sre.getServletRequest()).getSession();
    }

    public RequestListener() {

    }

    public void requestDestroyed(ServletRequestEvent arg0) {
    }
}
