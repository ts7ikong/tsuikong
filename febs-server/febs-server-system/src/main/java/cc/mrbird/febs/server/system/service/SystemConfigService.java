package cc.mrbird.febs.server.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.SystemConfig;
import cc.mrbird.febs.server.system.dto.SystemConfigDto;

import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/6/2 16:33
 */
public interface SystemConfigService extends IService<SystemConfig> {

    /**
     * 查询（分页）
     *
     * @param systemConfig 设置表实体类
     * @return IPage<SystemConfig>
     */
    List<SystemConfig> findSystemConfigs(SystemConfigDto systemConfig);

    /**
     * 新增
     *
     * @param systemConfig 设置表实体类
     */
    void createSystemConfig(SystemConfigDto systemConfig);

    /**
     * 修改
     *
     * @param systemConfig 设置表实体类
     */
    void updateSystemConfig(SystemConfigDto systemConfig);

    /**
     * 删除
     *
     * @param systemConfig 设置表实体类
     */
    void deleteSystemConfig(String systemConfig);

}