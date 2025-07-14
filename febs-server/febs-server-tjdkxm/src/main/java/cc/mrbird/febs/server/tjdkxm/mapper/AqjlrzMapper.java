package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.Aqjlrz;
import cc.mrbird.febs.common.core.entity.tjdkxm.Bidd;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 安全监理日志表(Aqjlrz)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:07
 */
@Mapper
@Repository
public interface AqjlrzMapper extends BaseMapper<Aqjlrz>{
    <T> IPage<Aqjlrz> selectPageInfo(Page<T> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
