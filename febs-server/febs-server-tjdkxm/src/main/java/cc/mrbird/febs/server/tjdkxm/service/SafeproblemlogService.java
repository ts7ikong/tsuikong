package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.tjdkxm.Safeproblem;
import cc.mrbird.febs.common.core.entity.tjdkxm.Safeproblemlog;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 安全隐患清单(Safeproblemlog)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:06
 */
@Service("SafeproblemlogService")
public interface SafeproblemlogService extends IService<Safeproblemlog> {

    /**
    * 查询（分页）
    *
    * @param request QueryRequest
    * @param safeproblemlog 安全隐患清单实体类
    * @return IPage<Safeproblemlog>
    */
    IPage<Safeproblemlog> findSafeproblemlogs(QueryRequest request, Safeproblemlog safeproblemlog);
    
    /**
    * 查询（所有）
    *
    * @param safeproblemlog 安全隐患清单实体类
    * @return List<Safeproblemlog>
    */
    List<Safeproblemlog> findSafeproblemlogs(Safeproblemlog safeproblemlog);
    
    /**
    * 新增
    *
    * @param safeproblemlog 安全隐患清单实体类
    */
    public void createSafeproblemlog(Safeproblemlog safeproblemlog, Safeproblem safeproblem);
    
    /**
    * 修改
    *
    * @param safeproblemlog 安全隐患清单实体类
    */
    void updateSafeproblemlog(Safeproblemlog safeproblemlog);
    
    /**
    * 删除
    *
    * @param safeproblemlog 安全隐患清单实体类
    */
    void deleteSafeproblemlog(Safeproblemlog safeproblemlog);
}
