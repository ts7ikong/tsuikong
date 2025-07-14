package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 项目信息(Project)表服务实现类
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:03
 */
public interface ProjectService extends IService<Project> {

    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param project 项目信息实体类
     * @return IPage<Project>
     */
    IPage<Project> findProjects(QueryRequest request, Project.Params project);

    /**
     * 查询（所有）
     *
     * @param project 项目信息实体类
     * @return List<Project>
     */
    List<Map<String, Object>> findProjects(Project project);

    /**
     * 新增
     *
     * @param project 项目信息实体类
     */
    void createProject(Project project) throws FebsException;

    /**
     * 修改
     *
     * @param project 项目信息实体类
     */
    void updateProject(Project project) throws FebsException;

    /**
     * 删除
     *
     * @param project 项目信息实体类
     */
    void deleteProject(Project.Params project) throws FebsException;

    /**
     * 根据id查询
     *
     * @param projectIds
     * @return {@link List< Project>}
     */
    List<Project> getByIds(Set<Long> projectIds);

    /**
     * 修改 项目负责人
     *
     * @param project 项目信息实体类
     */
    void updateProject(Project.UserModel project) throws FebsException;
}
