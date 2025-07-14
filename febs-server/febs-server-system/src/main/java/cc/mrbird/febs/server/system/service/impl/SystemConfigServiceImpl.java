package cc.mrbird.febs.server.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.SystemConfig;
import cc.mrbird.febs.server.system.dto.SystemConfigDto;
import cc.mrbird.febs.server.system.mapper.SystemConfigMapper;
import cc.mrbird.febs.server.system.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/28 16:52
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@RequiredArgsConstructor
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig>
    implements SystemConfigService {

    /**
     * 查询（分页）
     *
     * @param systemConfig 设置表实体类
     * @return IPage<SystemConfig>
     */
    @Override
    public List<SystemConfig> findSystemConfigs(SystemConfigDto systemConfig) {
        LambdaQueryWrapper<SystemConfig> wrapper =
            Wrappers.lambdaQuery(SystemConfig.class).eq(SystemConfig::getIsDelete, 0);
        if (StringUtils.isNotBlank(systemConfig.getKey())) {
            wrapper.likeLeft(SystemConfig::getKey, systemConfig.getKey());
        }
        return baseMapper.selectList(wrapper);
    }

    /**
     * 新增
     *
     * @param systemConfig 设置表实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSystemConfig(SystemConfigDto systemConfig) {
        Integer count = baseMapper.selectCountByKey(systemConfig.getKey());
        if (count > 0) {
            throw new IllegalArgumentException("已存在相同的key");
        }
        SystemConfig systemConfig1 = new SystemConfig();
        BeanUtils.copyProperties(systemConfig, systemConfig1);
        baseMapper.insert(systemConfig1);
    }

    /**
     * 修改
     *
     * @param systemConfig 设置表实体类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSystemConfig(SystemConfigDto systemConfig) {
        Integer count = baseMapper.selectCountByKey(systemConfig.getKey());
        if (count > 0) {
            throw new IllegalArgumentException("已存在相同的key");
        }
        SystemConfig systemConfig1 = new SystemConfig();
        BeanUtils.copyProperties(systemConfig, systemConfig1);
        baseMapper.updateById(systemConfig1);
    }

    /**
     * 删除
     *
     * @param id 设置表实体类
     */
    @Override
    public void deleteSystemConfig(String id) {
        removeById(id);
    }

}