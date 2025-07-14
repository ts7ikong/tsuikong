package cc.mrbird.febs.server.tjdkxm.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.handler.SimpleSelectInLangDriver;

/**
 * @author MrBird
 */
@Mapper
public interface UserMapper extends BaseMapper<SystemUser> {

    /**
     * 根据id 查询信息
     *
     * @param userIds HashSet
     * @return {@link java.util.List<java.util.Map<java.lang.String,java.lang.Object>>}
     */

    @Select("select USER_ID,USERNAME,REALNAME,MOBILE,t.AVATAR,t.DEPT_ID,p.DEPT_NAME from t_user t LEFT JOIN p_dept  p"
        + " on t.DEPT_ID=p.DEPT_ID where USER_ID IN (#{userIds}) and STATUS=1 ")
    @Lang(SimpleSelectInLangDriver.class)
    List<Map<String, Object>> selectByIds(Set<Long> userIds);

    /**
     * 查询员工信息
     *
     * @return {@link List< Map< String, Object>>}
     */
    <T> IPage<Map<String, Object>> selectMailList(Page<T> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * @param projectIds
     * @param username
     * @return {@link Set< Long>}
     */
    Set<Long> getUserIdByQualityproblem(Set<Long> projectIds, String username);

    /**
     * 查询项目中的用户信息
     *
     * @param projectId 项目id
     * @return {@link List< Map< String, Object>>}
     */
    @Select("select USER_ID,USERNAME,REALNAME,AVATAR,MOBILE,DEPT_ID,DEPT_NAME,PROJECT_ID,PROJECT_NAME from "
        + "projcet_user_view where PROJECT_ID= #{projectId}")
    List<Map<String, Object>> getProjectUserByProjectId(Long projectId);

    /**
     * 查询项目中的用户信息
     *
     * @return {@link List< Map< String, Object>>}
     */
    @Select("select USER_ID,USERNAME,REALNAME,AVATAR,MOBILE,PROJECT_ID,PROJECT_NAME from projcet_user_view")
    List<Map<String, Object>> getProjectUser();

    /**
     * 查询项目中的用户信息
     *
     * @param menuId 菜单id
     * @param buttonId 用户id
     * @param projectIds 项目ids
     * @return {@link java.util.List<java.util.Map<java.lang.String,java.lang.Object>>}
     */
    List<Map<String, Object>> getProjectUserNew(Integer menuId, Integer buttonId, Set<Long> projectIds);

    /**
     * 查询项目中的用户信息
     *
     * @param menuId 菜单id
     * @param buttonId 用户id 可null
     * @return {@link java.util.List<java.util.Map<java.lang.String,java.lang.Object>>}
     */
    List<Map<String, Object>> getUserListNewByMenuIdAndButotnId(Integer menuId, Integer buttonId);

    /**
     * 查询所有用户
     *
     * @return {@link List< Map< String, Object>>}
     */
    @Select("select USER_ID,USERNAME,REALNAME,MOBILE,AVATAR from projcet_user_view GROUP BY USER_ID")
    List<Map<String, Object>> getAllUser();

    @Select("select USER_ID,USERNAME,REALNAME,MOBILE,AVATAR from t_user where `STATUS`!=2 and `LEVEL`=2")
    List<Map<String, Object>> getAllUserNew();

}
