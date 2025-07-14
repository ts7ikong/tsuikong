package cc.mrbird.febs.server.tjdk.mapper;

import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.handler.SimpleSelectInLangDriver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/30 19:06
 */
@Mapper
public interface OperateMapper extends BaseMapper<Operate> {
    @Select("select PROJECT_ID,PROJECT_NAME from p_project where PROJECT_ID IN (#{projectId}) and IS_DELETE = 0")
    @Lang(SimpleSelectInLangDriver.class)
    List<Project> getByOperateProjectid(Set<Long> projectId);
}
