package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.QualityproblemUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用于安全检查计划多选中间表
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:08
 */
@Mapper
@Repository
public interface QualityproblemUserMapper extends BaseMapper<QualityproblemUser> {

}
