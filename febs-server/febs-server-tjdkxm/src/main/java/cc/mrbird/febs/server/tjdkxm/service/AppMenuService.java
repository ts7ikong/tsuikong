package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.QueryRequest;

import java.util.List;
import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/8 11:36
 */
public interface AppMenuService {
    List<Map<String, Object>> getAppMenuAgencyNum();

    Map<String, Object> appHomepageInfo(QueryRequest request);
}
