package com.wuzz.webScoket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * webSocket的配置
 *
 * @author wuzz
 * @date 14:09 2019/10/8
 */
@ServerEndpoint(value = "/webSocket", configurator = GetHttpSessionConfigurator.class)
@Component
public class WebSocketServer {

    static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //接收sid
    private String sid = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        this.sid = httpSession.getId();
        log.info("有新窗口开始监听:" + sid + ",当前在线人数为" + getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        log.info("收到来自窗口" + sid + "的信息:" + message);
        this.session.getBasicRemote().sendText(message);
    }

    @OnError
    public void onError(Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, String sid) {
        log.info("推送消息到窗口" + sid + "，推送内容:" + message);
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (sid == null) {
                    item.sendMessage(message);
                } else if (item.sid.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
