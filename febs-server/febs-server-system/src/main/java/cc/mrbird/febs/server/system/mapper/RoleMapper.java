package cc.mrbird.febs.server.system.mapper;

import cc.mrbird.febs.common.core.entity.system.Role;
import cc.mrbird.febs.common.core.entity.system.RoleDto;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.handler.SimpleSelectInLangDriver;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author MrBird
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 通过用户名查找用户角色
     *
     * @param username 用户名
     * @return 用户角色集合
     */
    List<Role> findUserRole(String username);

    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);

    /**
     * 查找角色详情
     *
     * @param page 分页
     * @param role 角色
     * @param <T> type
     * @return IPage<User>
     */
    <T> IPage<Role> findRolePage(Page<T> page, @Param("role") Role role);

    /**
     * 查找角色详情
     *
     * @param page 分页
     * @param role 角色
     * @param <T> type
     * @return IPage<User>
     */
    List<RoleDto> findRoleNewPage(@Param("pageSize") Integer pageSize, @Param("pageNum") Integer pageNum,
        @Param("role") Role role);

    /**
     * 查找角色详情
     *
     * @param page 分页
     * @param role 角色
     * @param <T> type
     * @return IPage<User>
     */
    Integer findRoleNewPageTotal(@Param("role") Role role);

    /**
     * 查找角色详情 COUNT
     *
     * @param role 角色
     * @return IPage<User>
     */
    Integer findRolePageCount(@Param("role") Role role);

    /**
     * 根据项目ids 获取最小的role id 用于默认项目默认角色
     *
     * @param projectIds 项目ids
     * @return {@link List< Map< String, Object>>}
     */
    @Select("SELECT min(ROLE_ID) as ROLE_ID,PROJECT_ID from t_role  where IS_DELETE = 0 GROUP  BY PROJECT_ID HAVING PROJECT_ID IN "
        + " " + "(#{projectIds}) ")
    @Lang(SimpleSelectInLangDriver.class)
    List<Map<String, Object>> selectByIds(Set<Long> projectIds);

    /**
     * 根据项目ids 获取最小的role id 用于默认项目默认角色
     *
     * @param projectIds 项目ids
     * @return {@link List< Map< String, Object>>}
     */
    @Select("SELECT\n" + "\tu.USER_ID,r.ROLE_ID,r.ROLE_NAME,ur.PROJECT_ID\n" + "FROM\n" + "\tt_user u\n"
        + "\tLEFT JOIN t_user_role ur ON u.USER_ID = ur.USER_ID\n" + "\tLEFT JOIN t_role r ON ur.ROLE_ID = r.ROLE_ID \n"
        + "WHERE\n" + "\t r.ROLE_ID !=1 and r.IS_DELETE = 0 and u.USER_ID IN" + "(#{userIds}) ")
    @Lang(SimpleSelectInLangDriver.class)
    List<Map<String, Object>> selectByUserIds(Set<Long> userIds);

    @Select("select r.PROJECT_ID from  t_role r " + "LEFT JOIN t_user_role ur on r.ROLE_ID= ur.ROLE_ID "
        + "where ur.PROJECT_ID=#{projectId} and ur.USER_ID=#{userId} and r.IS_DELETE = 0")
    Long getProjectId(Long projectId, Long userId);

    List<Long> getSpecialPermissions(@Param("ids") Set<Long> ids);

    List<Long> getProjectIds();

    List<Project> selectProject(@Param("projectIds") List<Long> projectIds);


}