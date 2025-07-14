package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.tjdkxm.Zzaqglryrz;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 专职安全管理人员日志表(Zzaqglryrz)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:07
 */
@Service("ZzaqglryrzService")
public interface ZzaqglryrzService extends IService<Zzaqglryrz> {

    /**
    * 查询（分页）
    *
    * @param request QueryRequest
    * @param zzaqglryrz 专职安全管理人员日志表实体类
    * @return IPage<Zzaqglryrz>
    */
    MyPage<Zzaqglryrz> findZzaqglryrzs(QueryRequest request, Zzaqglryrz zzaqglryrz);
    
    /**
    * 查询（所有）
    *
    * @param zzaqglryrz 专职安全管理人员日志表实体类
    * @return List<Zzaqglryrz>
    */
    List<Zzaqglryrz> findZzaqglryrzs(Zzaqglryrz zzaqglryrz);
    
    /**
    * 新增
    *
    * @param zzaqglryrz 专职安全管理人员日志表实体类
    */
    void createZzaqglryrz(Zzaqglryrz zzaqglryrz) throws FebsException;
    
    /**
    * 修改
    *
    * @param zzaqglryrz 专职安全管理人员日志表实体类
    */
    void updateZzaqglryrz(Zzaqglryrz zzaqglryrz) throws FebsException;
    
    /**
    * 删除
    *
    * @param zzaqglryrz 专职安全管理人员日志表实体类
    */
    void deleteZzaqglryrz(Zzaqglryrz zzaqglryrz) throws FebsException;
}
