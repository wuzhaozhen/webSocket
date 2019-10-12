package com.wuzz.controller;

import com.wuzz.config.AjaxJson;
import com.wuzz.webScoket.WebSocketServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 测试控制层
 *
 * @author wuzz
 * @date 14:14 2019/10/8
 */
@RestController
@RequestMapping(value = "/hello")
public class HelloController {

    //推送数据接口
    @RequestMapping("/socket/push")
    public AjaxJson pushToWeb(HttpServletRequest request, String message) {
        AjaxJson ajaxJson = new AjaxJson();
        WebSocketServer.sendInfo(message, request.getSession().getId());
        return ajaxJson;
    }
}
