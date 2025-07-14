package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.Conference;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 会议表ID(Conference)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */
@Mapper
@Repository 
public interface ConferenceMapper extends BaseMapper<Conference> {
    @Select("SELECT\n" +
            "\tcount( 1 ) \n" +
            "FROM\n" +
            "\tp_conference p \n" +
            "WHERE\n" +
            "\tp.IS_DELETE = 0 \n" +
            "\tAND ( " +
            "p.CONFERENCE_ID IN ( SELECT p1.TABLE_ID FROM p_conference_user p1 WHERE p1.USER_ID = #{userId} ) " +
            "OR p.CONFERENCE_CREATEUSERID = #{userId} ) \n" +
            "\tAND p.CONFERENCE_TIME BETWEEN ( NOW() - INTERVAL 1 HOUR ) \n" +
            "\tAND ( NOW() + INTERVAL 2 HOUR ) \n" +
            "\tAND p.CONFERENCE_TYPE <=1")
    Integer notStart(Long userId);
    @Select("SELECT\n" +
            "\t*\n" +
            "FROM\n" +
            "\tp_conference p \n" +
            "WHERE\n" +
            "\tp.CONFERENCE_PROJECTID = #{projectId} and p.IS_DELETE = 0\n" +
            "\tAND (find_in_set(#{userId}, p.CONFERENCE_USERIDS)>0 OR p.CONFERENCE_CREATEUSERID = #{userId})\n" +
            "\tAND p.CONFERENCE_TIME >=(NOW() - INTERVAL 24 HOUR) \n" +
            "\tAND p.CONFERENCE_TYPE IN ('0','1')")
    List<Conference> notStartInfo(Long userId, Long projectId);

    <T> IPage<Conference> selectPageInfo(Page<T> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);

}
