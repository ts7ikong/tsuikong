package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.Safeproblemlog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
/**
 * 安全隐患清单(Safeproblemlog)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:05
 */
@Mapper
@Repository 
public interface SafeproblemlogMapper extends BaseMapper<Safeproblemlog> {

    /**
     * safeproblenId 级联删除操作日志
     *
     * @param safeproblenId 质量问题id
     * @return {@link int} 删除数量
     */
    @Delete("DELETE FROM p_safeproblemlog p where p.SAFEPROBLEN_ID=#{safeproblenId} and p.IS_DELETE = 0")
    int deleteBySafeproblenId(Long safeproblenId);


}
