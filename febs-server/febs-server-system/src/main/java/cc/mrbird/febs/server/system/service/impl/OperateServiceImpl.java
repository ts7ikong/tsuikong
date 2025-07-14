package cc.mrbird.febs.server.system.service.impl;

import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.common.redis.service.RedisService;
import cc.mrbird.febs.server.system.mapper.OperateMapper;
import cc.mrbird.febs.server.system.mapper.UserMapper;
import cc.mrbird.febs.server.system.service.OperateService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * OperateService实现
 *
 * @author 14059
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OperateServiceImpl extends ServiceImpl<OperateMapper, Operate> implements OperateService {

    private final OperateMapper operateMapper;
    private final UserMapper userMapper;

    @Override
    public IPage<Operate> findOperates(QueryRequest request, Operate operate) {
        QueryWrapper<Operate> queryWrapper = new QueryWrapper<Operate>();
        queryWrapper.eq("IS_DELETE", 0);
        OrderUtils.setQuseryOrder(queryWrapper, request);
        queryWrapper.orderByDesc("PARCELUNIT_ID");
        Page<Operate> page = new Page<>(request.getPageNum(), request.getPageSize());
        if (StringUtils.isNotBlank(operate.getOperateUserName())) {
            queryWrapper.inSql("OPERATE_USERID", "select USER_ID from t_user where REALNAME " +
                    "LIKE CONCAT('%','" + operate.getOperateUserName() + "','%')");
        }
        if (StringUtils.isNotBlank(operate.getOperateProjectName())) {
            queryWrapper.inSql("OPERATE_PROJECTID", "select PROJECT_ID from p_project where PROJECT_NAME " +
                    "LIKE CONCAT('%','" + operate.getOperateUserName() + "','%')");
        }
        if (StringUtils.isNotBlank(operate.getStartOperateTime())) {
            queryWrapper.ge("OPERATE_TIME", operate.getStartOperateTime());
        }
        if (StringUtils.isNotBlank(operate.getEndOperateTime())) {
            queryWrapper.le("OPERATE_TIME", operate.getEndOperateTime());
        }
        if (StringUtils.isNotBlank(operate.getOperateType())) {
            queryWrapper.le("OPERATE_TYPE", operate.getOperateType());
        }
        this.operateMapper.selectPage(page, queryWrapper);
        List<Operate> records = page.getRecords();
        HashSet<Long> userIds = new HashSet<>();
        HashSet<Long> projectIds = new HashSet<>();
        records.forEach(record -> {
            userIds.add(record.getOperateUserid());
            projectIds.add(record.getOperateProjectid());
        });
        List<Map<String, Object>> maps = new ArrayList<>();
        List<Project> projects = new ArrayList<>();
        if (userIds.size() > 0) {
            maps = userMapper.selectByIds(userIds);
        }
        if (projectIds.size() > 0) {
            projects = operateMapper.getByOperateProjectid(projectIds);
        }
        List<Map<String, Object>> finalMaps = maps;
        List<Project> finalProjects = projects;
        records.forEach(record -> {
            finalMaps.forEach(map -> {
                if (map.get("USER_ID").equals(record.getOperateUserid())) {
                    record.setOperateUserName((String) map.get("REALNAME"));
                }
            });
            finalProjects.forEach(project -> {
                if (project.getProjectId().equals(record.getOperateProjectid())) {
                    record.setOperateProjectName(project.getProjectName());
                }
            });
        });
        page.setRecords(records);
        return page;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Operate create(JSONObject json, String type, String id, Long userId) {
        Operate operate = new Operate();
        operate.setOperateType(type);
        long l = id == null ? -1L : Long.parseLong(id);
        operate.setOperateTableid(l);
        operate.setOperateUserid(userId);
        operate.setOperateTime(new Date());
        operate.setOperateBefore(json);
        this.save(operate);
        return operate;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOperate(Operate operate, String id, Object projectId) {
        long l = id == null ? -1L : Long.parseLong(id);
        Long l1 = projectId == null ? null : (Long) projectId;
        operate.setOperateTableid(l);
        operate.setOperateProjectid(l1);
        this.updateById(operate);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOperate(Operate parcel) {
        this.update(new LambdaUpdateWrapper<Operate>().eq(Operate::getOperateId, parcel.getOperateId())
                .set(Operate::getIsDelete, 1));
    }
}
