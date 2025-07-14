package cc.mrbird.febs.server.system.mapper;

import cc.mrbird.febs.common.core.entity.tjdk.Dept;
import cc.mrbird.febs.common.core.handler.SimpleSelectInLangDriver;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 部门表(Dept)表数据库访问层
 *
 * @author
 * @since 2022-01-12 15:51:04
 */
@Mapper
@Repository
public interface DeptMapper extends BaseMapper<Dept> {
    @Select("select DEPT_ID,DEPT_NAME from p_dept where IS_DELETE = 0 and DEPT_ID in " +
            "(#{deptIds})")
    @Lang(SimpleSelectInLangDriver.class)
    List<Map<String, Object>> getDeptIds(@Param("deptIds") Set<Long> deptIds);
}
