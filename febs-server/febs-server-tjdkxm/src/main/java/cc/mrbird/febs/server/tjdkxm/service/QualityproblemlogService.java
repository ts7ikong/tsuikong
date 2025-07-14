package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityproblem;
import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityproblemlog;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 质量问题清单(Qualityproblemlog)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:02
 */
@Service("QualityproblemlogService")
public interface QualityproblemlogService extends IService<Qualityproblemlog> {

    /**
    * 查询（分页）
    *
    * @param request QueryRequest
    * @param qualityproblemlog 质量问题清单实体类
    * @return IPage<Qualityproblemlog>
    */
    IPage<Qualityproblemlog> findQualityproblemlogs(QueryRequest request, Qualityproblemlog qualityproblemlog);
    
    /**
    * 查询（所有）
    *
    * @param qualityproblemlog 质量问题清单实体类
    * @return List<Qualityproblemlog>
    */
    List<Qualityproblemlog> findQualityproblemlogs(Qualityproblemlog qualityproblemlog);
    
    /**
    * 新增
    *
    * @param qualityproblemlog 质量问题清单实体类
    */
    void createQualityproblemlog(Qualityproblemlog qualityproblemlog, Qualityproblem qualityproblem);
    
    /**
    * 修改
    *
    * @param qualityproblemlog 质量问题清单实体类
    */
    void updateQualityproblemlog(Qualityproblemlog qualityproblemlog);
    
    /**
    * 删除
    *
    * @param qualityproblemlog 质量问题清单实体类
    */
    void deleteQualityproblemlog(Qualityproblemlog qualityproblemlog);
}
