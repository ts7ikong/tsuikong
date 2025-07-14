package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.UserProject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author ZNKJ-R
 * @version V1.0
 * @date 2022/1/18 19:38
 */
@Mapper
@Repository
public interface UserProjectMapper extends MPJBaseMapper<UserProject> {

    /**
     * 查询数据
     *
     * @param page      分页
     * @param projectId 项目id
     * @param inProject 1项目用户 -1不是项目用户
     * @param wrapper   条件
     * @return {@link com.baomidou.mybatisplus.core.metadata.IPage<java.util.Map<java.lang.String,java.lang.Object>>}
     */
    <T> IPage<Map<String, Object>> selectPageInfo(Page<T> page, Long projectId, Long inProject,
                                                  @Param(Constants.WRAPPER) Wrapper<?> wrapper);

}
