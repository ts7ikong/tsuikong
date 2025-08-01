package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 项目信息(Project)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */
@Mapper
@Repository
public interface ProjectMapper extends MPJBaseMapper<Project> {

    /**
     * 连表查询 获取分部信息
     *
     * @param page {@link IPage<Project>}
     * @param wrapper Wrapper
     * @return {@link IPage<Project>}
     */
    public <T> IPage<Project> selectPageInfo(IPage<Project> page, @Param(Constants.WRAPPER) Wrapper<Project> wrapper);

    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);

    /***
     * 获取项目的分部分项
     *
     * @return {@link List< Map< String, Object>>}
     */
    List<Map<String, Object>> selectAllChoosesAll();

    /***
     * 获取项目的分部分项
     *
     * @return {@link List< Map< String, Object>>}
     */
    Set<Long> selectProjectId(Set<Long> projectIds, String projectName);
}
