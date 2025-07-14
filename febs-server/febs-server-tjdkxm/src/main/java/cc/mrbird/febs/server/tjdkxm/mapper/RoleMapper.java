package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.system.Role;
import cc.mrbird.febs.common.core.handler.SimpleSelectInLangDriver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Mapper;
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
     * 根据项目ids 获取最小的role id  用于默认项目默认角色
     *
     * @param projectIds 项目ids
     * @return {@link List< Map< String, Object>>}
     */
    @Select("SELECT min(ROLE_ID) as ROLE_ID,PROJECT_ID from t_role where IS_DELETE = 0  GROUP  BY PROJECT_ID HAVING PROJECT_ID IN " +
            " (#{projectIds}) ")
    @Lang(SimpleSelectInLangDriver.class)
    List<Map<String, Object>> selectByIds(Set<Long> projectIds);

}