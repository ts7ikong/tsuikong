package cc.mrbird.febs.server.tjdk.service;

import cc.mrbird.febs.common.core.entity.tjdk.Banner;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * banner管理(Banner)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */
public interface BannerService extends IService<Banner> {

    /**
    * 查询（分页）
    *
    * @param request QueryRequest
    * @param banner banner管理实体类
    * @return IPage<Banner>
    */
    IPage<Banner> findBanners(QueryRequest request, Banner banner);
    
    /**
    * 查询（所有）
    *
    * @param banner banner管理实体类
    * @return List<Banner>
    */
    List<Banner> findBanners(Banner banner);
    
    /**
    * 新增
    *
    * @param banner banner管理实体类
    */
    void createBanner(List<Banner> banner) throws FebsException;
    
    /**
    * 修改
    *
    * @param banner banner管理实体类
    */
    void updateBanner(Banner banner);
    
    /**
    * 删除
    *
    * @param banner banner管理实体类
    */
    void deleteBanner(Banner banner) throws FebsException;
}
