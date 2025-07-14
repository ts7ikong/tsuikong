package cc.mrbird.febs.server.tjdk.mapper;

import cc.mrbird.febs.common.core.entity.tjdk.Sysnotify;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 系统通知(Sysnotify)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:08
 */
@Mapper
@Repository 
public interface SysnotifyMapper extends BaseMapper<Sysnotify> {

     <T> IPage<Sysnotify> selectPageInfo(Page<T> page,@Param(Constants.WRAPPER) Wrapper<?> wrapper);
     /**
      * id查询
      *
      * @param id tableid
      * @return {@link com.alibaba.fastjson.JSONObject}
      */
     JSONObject selectInfoById(@Param("id") Long id);

}
