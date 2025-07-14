package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.tjdkxm.Sgrz;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 施工日志表(Sgrz)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:07
 */
public interface SgrzService extends IService<Sgrz> {

    /**
    * 查询（分页）
    *
    * @param request QueryRequest
    * @param sgrz 施工日志表实体类
    * @return IPage<Sgrz>
    */
    MyPage<Sgrz> findSgrzs(QueryRequest request, Sgrz sgrz);
    
    /**
    * 查询（所有）
    *
    * @param sgrz 施工日志表实体类
    * @return List<Sgrz>
    */
    List<Sgrz> findSgrzs(Sgrz sgrz);
    
    /**
    * 新增
    *
    * @param sgrz 施工日志表实体类
    */
    void createSgrz(Sgrz sgrz) throws FebsException;
    
    /**
    * 修改
    *
    * @param sgrz 施工日志表实体类
    */
    void updateSgrz(Sgrz sgrz) throws FebsException;
    
    /**
    * 删除
    *
    * @param sgrz 施工日志表实体类
    */
    void deleteSgrz(Sgrz sgrz) throws FebsException;
}
