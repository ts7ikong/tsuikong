package cc.mrbird.febs.server.websocket.service;

import cc.mrbird.febs.common.core.entity.constant.ParamsConstant;
import cc.mrbird.febs.common.redis.service.RedisService;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/7 18:20
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 */
// @Component
@Slf4j
@Service
@ServerEndpoint("/websocket/{type}/{id}/{token}")
public class WebSocketServer {
    /**
     * 保留最近10条
     */
    private static final Long MSG_NUM = 10L;
    /**
     * RedisService
     */
    public static RedisService redisService;

    /**
     * keu websocket_msg:userid value list
     */
    private static final String MSG_KEY = "websocket_msg:";
    public static String userInfoUri;
    public static String clientId;

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    // concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<String, WebSocketClient> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收id
     */
    private String id = "";

    @Data
    public static class WebSocketClient {
        // 与某个客户端的连接会话，需要通过它来给客户端发送数据
        private Session session;
        // 连接的uri
        private String uri;
        // 连接的uri
        private Date time;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("type") String type, @PathParam("id") String id,
                       @PathParam("token") String token) {
        log.info("WebSocket连接尝试 - type: {}, id: {}, token: {}", type, id, token);
        checkToekn(token, type, id);
        id = type + "_" + id;
        if (!webSocketMap.containsKey(id)) {
            addOnlineCount(); // 在线数 +1
        }
        this.session = session;
        this.id = id;
        WebSocketClient client = new WebSocketClient();
        client.setSession(session);
        client.setUri(session.getRequestURI().toString());
        webSocketMap.put(id, client);

        log.info("----------------------------------------------------------------------------");
        log.info("用户连接:" + id + ",当前在线人数为:" + getOnlineCount());
        try {
            sendMessage("来自后台的反馈：连接成功");
            sendToId(id);
        } catch (IOException e) {
            log.error("用户:" + id + ",网络异常!!!!!!");
        }
    }

    public void checkToekn(String token, String type, String id) {
        String key = "webscoket" + token;
        Integer count = 0;
        if (redisService.hasKey(key)) {
            count = (Integer) redisService.get(key);
            count += 1;
        }
        if (count > 5) {
            return;
            // throw new IllegalArgumentException("未知错误");
        } else {
            redisService.set(key, count, 60L);
        }
        OAuth2RestOperations restTemplate = null;
        if (restTemplate == null) {
            BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
            resource.setClientId(clientId);
            restTemplate = new OAuth2RestTemplate(resource);
        }

        OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext().getAccessToken();
        if (existingToken == null || !token.equals(existingToken.getValue())) {
            DefaultOAuth2AccessToken token1 = new DefaultOAuth2AccessToken(token);
            token1.setTokenType("Bearer");
            restTemplate.getOAuth2ClientContext().setAccessToken(token1);
        }
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.add(ParamsConstant.LOGIN_TYPE, type);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        Map<String, Object> body = restTemplate.exchange(userInfoUri, HttpMethod.GET, request, Map.class).getBody();
        System.out.println(userInfoUri);
        JSONObject json = new JSONObject(body);
        String string = json.getJSONObject("userAuthentication").getJSONObject("principal").getString("userId");
        if (!Objects.equals(id, string)) {
            throw new IllegalArgumentException("未知错误");
        }
    }

    private void sendToId(String id) {
        String key = MSG_KEY + id;
        if (redisService.hasKey(key)) {
            List<Object> objects = redisService.lGet(key, 0L, MSG_NUM);
            try {
                for (Object object : objects) {
                    sendMessage(object.toString());
                    redisService.lRemove(key, 1L, object);
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(id)) {
            webSocketMap.remove(id);
            // 从set中删除
            subOnlineCount();
        }
        log.info("----------------------------------------------------------------------------");
        log.info(id + "用户退出,当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到用户消息:" + id + ",报文:" + message);

        // 可以群发消息
        // 消息保存到数据库、redis
        if (StringUtils.isNotBlank(message)) {
            // TODO: 2022/4/8 格式处理 message 固定话 sign time data type [暂定]
            try {
                try {
                    JSONObject jsonObject = JSONObject.parseObject(message);
                    sendMessage("收到了消息:" + jsonObject.toString());
                } catch (JSONException e) {
                    sendMessage("收到了消息" + message);
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.id + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 连接服务器成功后主动推送
     */
    public void sendMessage(String message) throws IOException {
        synchronized (session) {
            this.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 此为广播消息
     *
     * @param message
     */
    public void sendAllMessage(String message) {
        webSocketMap.forEach((id, webSocketClient) -> {
            if (webSocketClient != null) {
                try {
                    webSocketClient.getSession().getBasicRemote().sendText(message);
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        });
    }

    /**
     * 向指定客户端发送消息
     *
     * @param id
     * @param message
     */
    public static void sendMessage(String id, String message) {
        try {
            WebSocketClient webSocketClient = webSocketMap.get(id);
            if (webSocketClient != null) {
                webSocketClient.getSession().getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            String key = MSG_KEY + id;
            Long aLong = redisService.lGetListSize(key);
            if (aLong != null && aLong > MSG_NUM) {
                redisService.trim(key, 1, Math.toIntExact(aLong));
            }
            redisService.lSet(MSG_KEY + id, message);
            log.error(e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
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

    public static void setOnlineCount(int onlineCount) {
        WebSocketServer.onlineCount = onlineCount;
    }

    public static ConcurrentHashMap<String, WebSocketClient> getWebSocketMap() {
        return webSocketMap;
    }

    public static void setWebSocketMap(ConcurrentHashMap<String, WebSocketClient> webSocketMap) {
        WebSocketServer.webSocketMap = webSocketMap;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getUserName() {
        return id;
    }

    public void setUserName(String id) {
        this.id = id;
    }

}