package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.tjdkxm.Partylearn;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import javax.annotation.processing.FilerException;
import java.util.List;
/**
 * 党建中心(Partylearn)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */
@Service("PartylearnService")
public interface PartylearnService extends IService<Partylearn> {

    /**
    * 查询（分页）
    *
    * @param request QueryRequest
    * @param partylearn 党建学习表实体类
    * @return IPage<Partylearn>
    */
    IPage<Partylearn> findPartylearns(QueryRequest request, Partylearn partylearn);
    
    /**
    * 查询（所有）
    *
    * @param partylearn 党建学习表实体类
    * @return List<Partylearn>
    */
    List<Partylearn> findPartylearns(Partylearn partylearn);
    
    /**
    * 新增
    *
    * @param partylearn 党建学习表实体类
    */
    void createPartylearn(Partylearn partylearn) throws FebsException;
    
    /**
    * 修改
    *
    * @param partylearn 党建学习表实体类
    */
    void updatePartylearn(Partylearn partylearn) throws FilerException, FebsException;
    
    /**
    * 删除
    *
    * @param partylearn 党建学习表实体类
    */
    void deletePartylearn(Partylearn partylearn) throws FilerException, FebsException;
}
