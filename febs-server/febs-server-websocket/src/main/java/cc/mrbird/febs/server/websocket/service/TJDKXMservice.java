package cc.mrbird.febs.server.websocket.service;

import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.constant.ServerConstant;
import cc.mrbird.febs.common.core.entity.tjdkxm.Bidd;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/8 10:43
 */
@FeignClient(ServerConstant.TJDKXM)
public interface TJDKXMservice {
    @GetMapping("/bidd/list/{biddId}")
    Rest<Bidd> getAllBidds(@PathVariable("biddId")Long biddId);
    @GetMapping("/bidd")
    Rest<List<Bidd>> getbiddS(@RequestParam Bidd bidd);
}
