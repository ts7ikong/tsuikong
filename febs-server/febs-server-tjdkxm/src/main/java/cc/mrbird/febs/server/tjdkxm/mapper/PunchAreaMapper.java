package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.entity.tjdkxm.PunchArea;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/9 17:26
 */
@Mapper
public interface PunchAreaMapper extends BaseMapper<PunchArea> {
    <T> IPage<PunchArea> selectPageInfo(Page<T> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);

    /**
     * 获取没在打卡区域的用户 {userId:用户id,username:用户名,realname:用户姓名,deptName:用户部门,projectName:用户项目名称}
     *
     * @param page 分页
     * @param username 用户查询
     * @param deptId 部门id
     * @param projectId 项目id
     * @param punchAreaId 区域id
     * @param type 类型 1 未在区域中 2 区域中
     * @return {@link java.util.List<java.util.Map<java.lang.String,java.lang.Object>>}
     */
    IPage<Map<String, Object>> getPunchAreaUser(Page<T> page, @Param("username") String username,
        @Param("deptId") Long deptId, @Param("projectId") Long projectId, @Param("punchAreaId") Long punchAreaId,
        @Param("type") Integer type);
}
