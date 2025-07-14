package cc.mrbird.febs.server.tjdkxm.service.feign;

import cc.mrbird.febs.common.core.entity.constant.ServerConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * websocket
 *
 * @author 14059
 * @version V1.0
 * @date 2022/5/6 11:31
 */
@FeignClient(value = ServerConstant.WEBSOCKET, fallback = FeignWebsocketCallback.class)
public interface WebsocketService {
    @PostMapping("/socket/server/push/userid")
    String pushMsgByUserId(@RequestParam("userId") String userId, @RequestParam("msg") String msg);

    @PostMapping("/socket/server/push/userids")
    String pushMsgByUserIds(@RequestParam("userIds") Set<Long> userIds, @RequestParam("msg") String msg);
}
