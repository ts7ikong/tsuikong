package cc.mrbird.febs.server.tjdk.mapper;

import cc.mrbird.febs.common.core.entity.tjdk.Sysnotify;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserSysnotify;
import cc.mrbird.febs.common.core.handler.SimpleSelectInLangDriver;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/23 17:56
 */
@Mapper
public interface UserSysnotifyMapper extends BaseMapper<UserSysnotify> {
    @Select("select DISTINCT USER_ID,PROJECT_ID from p_userproject where IS_DELETE = 0 and PROJECT_ID in " +
            "(#{projectId})")
    @Lang(SimpleSelectInLangDriver.class)
    List<Map<String, Object>> getProjectUserIds(Set<String> projectId);

    @Select("SELECT\n" +
            "   s.SYSNOTIFY_ID,\n" +
            "   s.SYSNOTIFY_TITLE,\n" +
            "   s.SYSNOTIFY_CONTENT,\n" +
            "   s.SYSNOTIFY_ANNX,\n" +
            "   s.SYSNOTIFY_TIME,\n" +
            "   s.SYSNOTITY_TYPE,\n" +
            "   s.SYSNOTITY_CONTRANT,\n" +
            "   s.SYSNOTIFY_PID,\n" +
            "   s.IS_DELETE,\n" +
            "   s.CREATE_USERID,\n" +
            "   s.CREATE_TIME,us.IS_READ\n" +
            "FROM\n" +
            "p_sysnotify s\n" +
            "RIGHT JOIN p_usersysnotify us ON s.SYSNOTIFY_ID = us.SYSNOTIFY_ID \n" +
            "${ew.customSqlSegment}")
    IPage<Sysnotify> getUserRead(IPage<?> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
