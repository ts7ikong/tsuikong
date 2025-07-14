package cc.mrbird.febs.server.tjdk.mapper;

import cc.mrbird.febs.common.core.entity.tjdk.Dept;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
/**
 * 部门表(Dept)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */
@Mapper
@Repository 
public interface DeptMapper extends BaseMapper<Dept> {
    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);

}
