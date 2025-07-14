package cc.mrbird.febs.server.system.service.impl;

import lombok.Data;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.Version;
import cc.mrbird.febs.server.system.dto.VersionDto;
import cc.mrbird.febs.server.system.mapper.VersionMapper;
import cc.mrbird.febs.server.system.service.VersionService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/28 16:52
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class VersionServiceImpl extends ServiceImpl<VersionMapper, Version> implements VersionService {
    @Autowired
    private VersionMapper versionMapper;

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param version 设置表实体类
     * @return IPage<Version>
     */
    @Override
    public IPage<Version> findVersions(QueryRequest request, VersionDto version) {
        return null;
    }

    /**
     * 新增
     *
     * @param version 设置表实体类
     */
    @Override
    public void createVersion(VersionDto.VersionDtoAdd version) {
        Version version1 = new Version();
        version1.setVersion(version.getVersion());
        version1.setAndroidPath(version.getAndroidPath());
        version1.setIosPath(version.getIosPath());
        version1.setState(version.getState());
        version1.setIsForce(version.getIsForce());
        save(version1);
    }

    /**
     * 修改
     *
     * @param version 设置表实体类
     */
    @Override
    public void updateVersion(VersionDto version) {

    }

    /**
     * 删除
     *
     * @param version 设置表实体类
     */
    @Override
    public void deleteVersion(VersionDto version) {

    }

    /**
     * 查询是否有新版本
     *
     * @param version 版本号
     * @return {@link Version}
     */
    @Override
    public VersionDto hasVersion(String version) {
        return this.versionMapper.selectByVersion(version);
    }
}