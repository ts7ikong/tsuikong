package cc.mrbird.febs.server.system.mapper;


import cc.mrbird.febs.common.core.entity.system.Set;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设置表(Set)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2021-03-03 16:44:15
 */
@Mapper
public interface SetMapper extends BaseMapper<Set> {

}
