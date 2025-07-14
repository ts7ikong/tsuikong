package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdk.Dept;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 部门表(Dept)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */
public interface DeptService extends IService<Dept> {

    /**
    * 查询（分页）
    *
    * @param request QueryRequest
    * @param dept 部门表实体类
    * @return IPage<Dept>
    */
    IPage<Dept> findDepts(QueryRequest request, Dept dept);
    
    /**
    * 查询（所有）
    *
    * @param dept 部门表实体类
    * @return List<Dept>
    */
    List<Dept> findDepts(Dept dept);
    


    
    /**
    * 修改
    *
    * @param dept 部门表实体类
    */
    void updateDept(Dept dept);
    
    /**
    * 删除
    *
    * @param dept 部门表实体类
    */
    void deleteDept(Dept dept);
}
