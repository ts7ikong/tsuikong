package cc.mrbird.febs.server.system.mapper;

import cc.mrbird.febs.common.core.entity.system.OrgStructure;
import cc.mrbird.febs.common.core.entity.tjdkxm.Askfleave;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/5/6 15:22
 */
@Mapper
public interface OrgStructureMapper extends BaseMapper<OrgStructure> {

    <T> IPage<OrgStructure> selectPageInfo(Page<T> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);

    List<Map<String, Object>> selectOrgProjectMaps();

}
