package cc.mrbird.febs.server.tjdkxm.service.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/5/6 11:35
 */
@Slf4j
public class FeignWebsocketCallback implements WebsocketService {
    @Override
    public String pushMsgByUserId(String userId, String msg) {
        log.info("userId:{},msg:{}", userId, msg);
        return "error";
    }

    @Override
    public String pushMsgByUserIds(Set<Long> userIds, String msg) {
        log.info("userIds:{},msg:{}", userIds, msg);
        return "error";
    }
}
