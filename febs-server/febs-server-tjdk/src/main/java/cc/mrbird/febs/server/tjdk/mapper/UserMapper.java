package cc.mrbird.febs.server.tjdk.mapper;

import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.handler.SimpleSelectInLangDriver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author MrBird
 */
public interface UserMapper extends BaseMapper<SystemUser> {

    /**
     * 查找用户详细信息
     *
     * @param page 分页对象
     * @param user 用户对象，用于传递查询条件
     * @param <T>  type
     * @return Ipage
     */
    <T> IPage<SystemUser> findUserDetailPage(Page<T> page, @Param("user") SystemUser user);

    /**
     * 查找用户详细信息
     *
     * @param user 用户对象，用于传递查询条件
     * @return List<User>
     */
    List<SystemUser> findUserDetail(@Param("user") SystemUser user);
    /**
     * 根据id 查询信息
     *
     * @param userIds HashSet
     * @return {@link java.util.List<java.util.Map<java.lang.String,java.lang.Object>>}
     */

    @Select("select USER_ID,USERNAME,REALNAME,MOBILE,t.DEPT_ID,p.DEPT_NAME from t_user t LEFT JOIN p_dept  p " +
            "on t.DEPT_ID=p.DEPT_ID where USER_ID IN (#{userIds}) and STATUS=1 and p.IS_DELETE = 0 ")
    @Lang(SimpleSelectInLangDriver.class)
    List<Map<String, Object>> selectByIds(Set<Long> userIds);

}
