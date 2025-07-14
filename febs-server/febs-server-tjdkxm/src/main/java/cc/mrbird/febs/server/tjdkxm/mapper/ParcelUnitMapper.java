package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.tjdkxm.ParcelUnit;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/8 17:18
 */
@Mapper
public interface ParcelUnitMapper extends BaseMapper<ParcelUnit> {
    <T> IPage<ParcelUnit> selectPageInfo(Page<T> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);
    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);

}
