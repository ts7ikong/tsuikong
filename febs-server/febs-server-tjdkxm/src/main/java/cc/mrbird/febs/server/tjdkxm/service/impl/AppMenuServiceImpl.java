package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.model.AuthUserModel;
import cc.mrbird.febs.common.core.entity.tjdk.Banner;
import cc.mrbird.febs.common.core.entity.tjdk.Sysnotify;
import cc.mrbird.febs.common.core.entity.tjdkxm.MajorProjectLog;
import cc.mrbird.febs.common.core.entity.tjdkxm.Partylearn;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.service.AppMenuService;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.MajorProjectLogService;
import cc.mrbird.febs.server.tjdkxm.service.PartylearnService;
import cc.mrbird.febs.server.tjdkxm.service.feign.TJDKService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/8 11:36
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AppMenuServiceImpl implements AppMenuService {
    @Resource
    private CacheableService cacheableService;
    @Resource
    private TJDKService tjdkService;
    @Resource
    private MajorProjectLogService majorProjectLogService;
    @Resource
    private PartylearnService partylearnService;


    @Override
    public List<Map<String, Object>> getAppMenuAgencyNum() {
        Long userId = FebsUtil.getCurrentUserId();
        return cacheableService.getAppMenuAgencyNum(userId);
    }

    @Override
    public Map<String, Object> appHomepageInfo(QueryRequest request) {
        Map<String, Object> map = new HashMap<>(4);

        try {
            FebsResponse febsResponse = tjdkService.bannerList(new QueryRequest(), new Banner());
            map.put("/tjdk/banner/list", febsResponse);
        } catch (Exception e) {
            map.put("/tjdk/banner/list", new JSONObject());
        }
        try {
            FebsResponse febsResponse1 = tjdkService.usersysnotifyThree(request, new Sysnotify.Params());
            map.put("/tjdk/usersysnotify/list", febsResponse1);
        } catch (Exception e) {
            map.put("/tjdk/usersysnotify/list", new JSONObject());
        }
        try {
            MyPage<MajorProjectLog> majorProjectLogs =
                majorProjectLogService.findMajorProjectLogs(request, new MajorProjectLog.Params());
            Map<String, Object> map1 = new HashMap<>(2);
            map1.put("total", majorProjectLogs.getTotal());
            map1.put("records", majorProjectLogs.getRecords());
            map.put("/tjdkxm/majorProjectLog/list", map1);
        } catch (Exception e) {
            map.put("/tjdkxm/majorProjectLog/list", new JSONObject());
        }
        try {
            Partylearn partylearn = new Partylearn();
            partylearn.setPartylearnType(2);
            IPage<Partylearn> partylearns = partylearnService.findPartylearns(request, partylearn);
            Map<String, Object> map1 = new HashMap<>(2);
            map1.put("total", partylearns.getTotal());
            map1.put("records", partylearns.getRecords());
            map.put("/tjdkxm/partylearn/list/xuexi", map1);
        } catch (Exception e) {
            map.put("/tjdkxm/partylearn/list/xuexi", new JSONObject());
        }
        return map;
    }

}
