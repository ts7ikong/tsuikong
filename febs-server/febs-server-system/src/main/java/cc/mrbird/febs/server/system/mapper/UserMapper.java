package cc.mrbird.febs.server.system.mapper;

import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.handler.SimpleSelectInLangDriver;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author MrBird
 */
@Mapper
public interface UserMapper extends BaseMapper<SystemUser> {

    /**
     * 查找用户详细信息
     *
     * @param page 分页对象
     * @param user 用户对象，用于传递查询条件
     * @param <T> type
     * @return Ipage
     */
    <T> IPage<SystemUser> findUserDetailPage(Page<T> page, @Param("user") SystemUser user);

    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);

    /**
     * 查找用户详细信息
     *
     * @param user 用户对象，用于传递查询条件
     * @return List<User>
     */
    List<SystemUser> findUserDetail(@Param("user") SystemUser user);

    /**
     * 获取所有员工的id和姓名
     */
    @Select("select t.USER_ID,t.REALNAME,p.DEPT_NAME from  t_user t LEFT JOIN p_dept p on t.DEPT_ID=p.DEPT_ID where t"
        + ".STATUS=1 and t.TYPE=#{type} AND t.LEVEL=#{level}")
    List<Map<String, Object>> getUserStaff(Integer type, Integer level);

    /**
     * 查询项目中的用户列表-分页
     */

    <T> IPage<SystemUser> userByProjectList(Page<T> page, Long projectId, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * 查询没在此项目中的用户列表-分页
     */
    <T> IPage<SystemUser> userNotInProject(Page<T> page, Long projectId, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * 根据id 查询信息
     *
     * @param userIds HashSet
     * @return {@link java.util.List<java.util.Map<java.lang.String,java.lang.Object>>}
     */

    @Select("select USER_ID,USERNAME,REALNAME,MOBILE,t.DEPT_ID,p.DEPT_NAME from t_user t LEFT JOIN p_dept  p "
        + "on t.DEPT_ID=p.DEPT_ID where USER_ID IN (#{userIds}) and STATUS=1 and p.IS_DELETE = 0 ")
    @Lang(SimpleSelectInLangDriver.class)
    List<Map<String, Object>> selectByIds(Set<Long> userIds);

    <T> Page<SystemUser> getPageUser(Page<T> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    @Select("SELECT PROJECT_NAME from p_project where PROJECT_USERID in (#{userId}) ")
    @Lang(SimpleSelectInLangDriver.class)
    List<String> selectProjectNameByUserId(Set<String> userId);

    @Select("select USER_ID,USERNAME,REALNAME,AVATAR from projcet_user_view GROUP BY USER_ID")
    List<Map<String, Object>> getAllUsers();

    @Select("select AVATAR from t_user where USER_ID=#{userId}")
    String selectAvatar(Long userId);
}
