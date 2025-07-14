package cc.mrbird.febs.server.websocket.controller;

import cc.mrbird.febs.server.websocket.service.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author tsuikong
 * @version V1.0
 * @date 2024年4月12日 10:34
 */
@RestController
@RequestMapping("/socket/server")
@Api(tags = "socket")
@Slf4j
public class WebSocketController {
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @PostMapping("/push/userid")
    @ApiOperation(value = "单个发送")
    public String pushMsgByUserId(String userId, String msg) {
        log.info("userId:{},msg:{}", userId, msg);
        WebSocketServer.sendMessage(userId, msg);
        return "ok";
    }

    @PostMapping("/push/userids")
    @ApiOperation(value = "多个发送")
    public String pushMsgByUserIds(@RequestParam("userIds") Set<Long> userIds, @RequestParam("msg") String msg) {
        log.info("userId:{},msg:{}", userIds, msg);
        userIds.forEach(userId -> {
            if (userId != null) {
                taskExecutor.execute(() -> {
                    WebSocketServer.sendMessage(userId.toString(), msg);
                });
            }
        });
        return "ok";
    }
}
