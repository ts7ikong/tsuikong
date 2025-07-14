package cc.mrbird.febs.server.system.service;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.Version;
import cc.mrbird.febs.common.core.entity.system.Version;
import cc.mrbird.febs.server.system.dto.VersionDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 设置表(Version)表服务实现类
 *
 * @author zlkj_cg
 * @since 2021-03-03 16:44:15
 */
public interface VersionService extends IService<Version> {

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param version 设置表实体类
     * @return IPage<Version>
     */
    IPage<Version> findVersions(QueryRequest request, VersionDto version);

    /**
     * 新增
     *
     * @param version 设置表实体类
     */
    void createVersion(VersionDto.VersionDtoAdd version);

    /**
     * 修改
     *
     * @param version 设置表实体类
     */
    void updateVersion(VersionDto version);

    /**
     * 删除
     *
     * @param version 设置表实体类
     */
    void deleteVersion(VersionDto version);

    /**
     * 查询是否有新版本
     *
     * @param version 版本号
     * @return {@link cc.mrbird.febs.common.core.entity.system.Version}
     */
    VersionDto hasVersion(String version);
}
