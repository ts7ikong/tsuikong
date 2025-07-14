package cc.mrbird.febs.server.tjdkxm.service.feign;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdk.Banner;
import cc.mrbird.febs.common.core.entity.tjdk.Sysnotify;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.constant.ServerConstant;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/5/30 18:40
 */
@FeignClient(ServerConstant.TJDK)
public interface TJDKService {
    /**
     * 获取所有banner图
     *
     * @param request
     * @param banner
     * @return {@link FebsResponse}
     */
    @GetMapping("/banner/list")
    FebsResponse bannerList(@SpringQueryMap QueryRequest request, @SpringQueryMap Banner banner);

    @GetMapping(value = "/usersysnotify/list", consumes = MediaType.APPLICATION_JSON_VALUE)
    FebsResponse usersysnotifyThree(@SpringQueryMap QueryRequest request, @SpringQueryMap Sysnotify.Params sysnotify);
}
