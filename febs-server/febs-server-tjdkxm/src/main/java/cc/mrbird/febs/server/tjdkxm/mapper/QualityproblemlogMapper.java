package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityproblemlog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 质量问题清单(Qualityproblemlog)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:02
 */
@Mapper
@Repository
public interface QualityproblemlogMapper extends BaseMapper<Qualityproblemlog> {
    /**
     * 根据qualityproblenId 级联删除操作日志
     *
     * @param qualityproblenId 质量问题id
     * @return {@link int} 删除数量
     */
    @Delete("UPDATE  p_qualityproblemlog p SET p.IS_DELETE = 1 WHERE p.QUALITYPROBLEN_ID=#{qualityproblenId}")
    int deleteByQualityproblenId(Long qualityproblenId);

}
