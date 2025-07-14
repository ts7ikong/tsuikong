package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.*;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.config.LogRecordContext;
import cc.mrbird.febs.server.tjdkxm.mapper.ParcelMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.ProjectMapper;
import cc.mrbird.febs.server.tjdkxm.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ParcelService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:04
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ParcelServiceImpl extends ServiceImpl<ParcelMapper, Parcel> implements ParcelService {

    private final ParcelMapper parcelMapper;
    private final ProjectService projectService;
    private final CacheableService cacheableService;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public MyPage<Parcel> findParcels(QueryRequest request, Parcel parcel) throws FebsException {
        QueryWrapper<Parcel> queryWrapper = new QueryWrapper<Parcel>();
        queryWrapper.eq("IS_DELETE", 0);
        OrderUtils.setQuseryOrder(queryWrapper, request);
        Page<Parcel> page = new Page<>(request.getPageNum(), request.getPageSize());
        if (parcel.getParcelUnitId() != null) {
            queryWrapper.eq("PARCEL_UNITID", parcel.getParcelUnitId());
        }
        // 项目id
        if (parcel.getParcelProjectid() != null) {
            queryWrapper.eq("PARCEL_PROJECTID", parcel.getParcelProjectid());
        }
        if (parcel.getParcelUnitengineid() == null) {
            throw new FebsException("请先选择单位项目");
        }
        queryWrapper.eq("PARCEL_UNITENGINEID", parcel.getParcelUnitengineid());
        if (StringUtils.isNotBlank(parcel.getParcelContent())) {
            queryWrapper.and(wapper -> {
                wapper.like("PARCEL_CONTENT", parcel.getParcelContent());
                wapper.or().like("PARCEL_PERSON", parcel.getParcelContent());
            });
        }
        if (StringUtils.isNotBlank(parcel.getParcelName())) {
            queryWrapper.like("PARCEL_NAME", parcel.getParcelName());
        }
        this.parcelMapper.selectPage(page, queryWrapper);
        List<Map<String, Object>> parcelUnits = cacheableService.findParcelUnits();
        List<Parcel> records = page.getRecords();
        if (!records.isEmpty()) {
            Set<Long> projectIds = records.stream().map(Parcel::getParcelProjectid).collect(Collectors.toSet());
            if (!projectIds.isEmpty()) {
                List<Project> projects = projectService.getByIds(projectIds);
                if (!projects.isEmpty()) {
                    records.forEach(record -> projects.forEach(project -> {
                        if (project.getProjectId().equals(record.getParcelProjectid())) {
                            record.setProjectName(project.getProjectName());
                        }
                    }));
                }
            }
            records.forEach(parcel1 -> {
                Long unitId = parcel1.getParcelUnitId();
                if (unitId != null) {
                    for (Map<String, Object> parcelUnit : parcelUnits) {
                        Object parcelunitId = parcelUnit.get("PARCELUNIT_ID");
                        if (unitId.equals(parcelunitId)) {
                            parcel1.setParcelUnitName((String)parcelUnit.get("PARCELUNIT_NAME"));
                            break;
                        }
                    }
                }
            });
        }
        page.setRecords(records);
        MyPage<Parcel> myPage = new MyPage<>(page);
        MyPage.MyRecords<Parcel> myRecords = myPage.getMyRecords();
        myRecords.setProject(parcelUnits);
        return myPage;
    }

    @Override
    public List<Parcel> findParcels(Parcel parcel) throws FebsException {
        LambdaQueryWrapper<Parcel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Parcel::getIsDelete, 0);
        // 单位工程
        if (parcel.getParcelUnitengineid() == null) {
            throw new FebsException("请先选择单位工程");
        }
        queryWrapper.eq(Parcel::getParcelUnitengineid, parcel.getParcelUnitengineid());
        return this.parcelMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createParcel(Parcel parcel) throws FebsException {
        FebsUtil.isProjectNotNull(parcel.getParcelProjectid());
        parcel.setCreateUserid(FebsUtil.getCurrentUserId());
        parcel.setCreateTime(new Date());
        this.save(parcel);
        cacheableService.upgetAllProjectChooses(parcel.getParcelProjectid());
        logRecordContext.putVariable("id", parcel.getParcelId());
        logRecordContext.putVariable("projectId", parcel.getParcelProjectid());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateParcel(Parcel parcel) throws FebsException {
        // parcel.setParcelProjectid(null);
        Parcel parcel1 = parcelMapper.selectById(parcel.getParcelId());
        if (parcel1 == null) {
            throw new FebsException("权限不足");
        }
        cacheableService.hasPermission(parcel1.getCreateUserid());
        boolean b = this.updateById(parcel);

        if (b) {
            logRecordContext.putVariable("projectId", parcel1.getParcelProjectid());
            if (parcel.getParcelName() != null) {
                cacheableService.upgetAllProjectChooses(parcel.getParcelProjectid());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteParcel(Parcel parcel) throws Exception {
        Parcel parcel1 = parcelMapper.selectById(parcel.getParcelId());
        if (parcel1 == null) {
            throw new FebsException("权限不足");
        }
        cacheableService.hasPermission(parcel1.getCreateUserid());
        this.update(new LambdaUpdateWrapper<Parcel>().eq(Parcel::getParcelId, parcel.getParcelId())
            .set(Parcel::getIsDelete, 1));
        cacheableService.upgetAllProjectChooses(parcel1.getParcelProjectid());
        logRecordContext.putVariable("projectId", parcel1.getParcelProjectid());

    }
}
