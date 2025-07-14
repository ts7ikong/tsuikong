package cc.mrbird.febs.server.tjdk.service;

import cc.mrbird.febs.common.core.entity.tjdk.Sysnotify;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 系统通知(Sysnotify)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:08
 */
public interface SysnotifyService extends IService<Sysnotify> {

    /**
    * 查询（分页）
    *
    * @param request QueryRequest
    * @param sysnotify 系统通知实体类
    * @return IPage<Sysnotify>
    */
    IPage<Sysnotify> findSysnotifys(QueryRequest request, Sysnotify.Params sysnotify);

    
    /**
    * 新增
    *
    * @param sysnotify 系统通知实体类
    */
    void createSysnotify(Sysnotify sysnotify) throws FebsException;
    
    /**
    * 修改
    *
    * @param sysnotify 系统通知实体类
    */
    void updateSysnotify(Sysnotify sysnotify) throws FebsException;
    
    /**
    * 删除
    *
    * @param sysnotify 系统通知实体类
    */
    void deleteSysnotify(Sysnotify sysnotify);
}
