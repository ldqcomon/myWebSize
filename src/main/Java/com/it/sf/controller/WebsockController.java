package com.it.sf.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Auther: ldq
 * @Date: 2020/8/9
 * @Description:
 * @Version: 1.0
 */
@ServerEndpoint(value = "/page_room/{username}")
@Slf4j
public class WebsockController {
    //这里只是简单测试用，真正的场景请考虑线程安全的容器或其它并发解决案
    private static Map<String, Session> sessions = new HashMap<>();

    /**
     * @param session  与客户端的会话对象【可选】
     * @param username 路径参数值 【可选】
     * @throws IOException
     * @OnOpen 标注此方法在 ws 连接建立时调用，可用来处理一些准备性工作 可选参数
     * EndpointConfig（端点配置信息对象） Session 连接会话对象
     */
    @OnOpen
    public void OnOpen(Session session, @PathParam("username") String username) throws IOException {
        // 设置session的超时时间:单位:毫秒
        session.setMaxIdleTimeout(3600000);
        String decode = URLDecoder.decode(username, "UTF-8");
        if (sessions.size()>5) {
            session.getBasicRemote().sendText("more than 3");
            return;
        }
        sessions.put(session.getId() + ":" + username, session);
        StringBuilder builder = new StringBuilder();
        sessions.forEach((k, se) ->{
            String user = k.substring(k.indexOf(":") + 1);
            try {
                builder.append(URLDecoder.decode(user, "UTF-8")+" ");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } );
        sendTextMsg("好友 [" + decode + "] 加入群聊,有"+sessions.size()+"人在线:"+builder.toString(),"1",null);
    }

    /**
     * @param msg      接受客户端消息
     * @param username RESTful 路径方式获取用户名
     * @throws IOException
     * @OnMessage 在收到客户端消息调用 消息形式不限于文本消息，还可以是二进制消息(byte[]/ByteBuffer等)，ping/pong 消息
     */
    @OnMessage
    public void OnMsg(String msg, @PathParam("username") String username) throws IOException {
        String decode = URLDecoder.decode(username, "UTF-8");
        String msgType = "";
        AtomicReference<Session> session = new AtomicReference<>();
        if (msg.contains("@")) {
            // 私发给个人
            msgType = "2";
            int i = msg.indexOf(":");
            String user = msg.substring(0+1, i);
            msg = msg.substring(i + 1);
            sessions.forEach((k, ses) -> {
                String userKey = StringUtils.substring(k, k.indexOf(":")+1);
                try {
                     userKey = URLDecoder.decode(userKey, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (StringUtils.equals(user, userKey)) {
                    session.set(ses);
                }
            });
        }else if (msg.equals("ping")) {
            log.info("heart test:{}",msg);
            return;
        }else{
            // 群发
            msgType = "1";
        }
        sendTextMsg(decode + "：" + msg,msgType,  Optional.ofNullable(session).orElseGet(()->  new AtomicReference<Session>()).get());
    }


    /**
     * @OnClose 连接关闭调用
     */
    @OnClose
    public void OnClose(Session session, @PathParam("username") String username) throws IOException {
        sessions.remove(session.getId()+":"+username);
        String decode = URLDecoder.decode(username, "UTF-8");
        int size = sessions.size();
        StringBuilder builder = new StringBuilder();
        sessions.forEach((k, se) ->{
            String user = k.substring(k.indexOf(":") + 1);
            try {
                builder.append(URLDecoder.decode(user, "UTF-8")+" ");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } );
        sendTextMsg("好友 [" + decode + "] 退出群聊,还有:"+size+"人在线:"+builder.toString(),"1",null);
    }

    /**
     * @param e 异常参数
     * @OnError 连接出现错误调用
     */
    @OnError
    public void OnError(Throwable e) {
        e.printStackTrace();
    }

    private void sendTextMsg(String msg, String msgType, Session session) throws IOException {
        switch (msgType) {
            case "1":
                //群聊
                for (Map.Entry<String, Session> entry : sessions.entrySet()) {
                    //session.getAsyncRemote().sendText(msg);
                    entry.getValue().getBasicRemote().sendText(msg);
                }
                break;
            case "2":
                // 私聊
                session.getBasicRemote().sendText(msg);
                break;
            default:
                throw new IllegalArgumentException();
        }

    }

}
