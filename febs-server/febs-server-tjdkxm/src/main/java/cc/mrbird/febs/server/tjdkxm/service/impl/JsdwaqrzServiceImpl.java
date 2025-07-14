package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.MyPage;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.Askfleave;
import cc.mrbird.febs.common.core.entity.tjdkxm.ConferenceThere;
import cc.mrbird.febs.common.core.entity.tjdkxm.Jsdwaqrz;
import cc.mrbird.febs.common.core.entity.tjdkxm.Project;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.server.tjdkxm.mapper.JsdwaqrzMapper;
import cc.mrbird.febs.server.tjdkxm.service.CacheableService;
import cc.mrbird.febs.server.tjdkxm.service.JsdwaqrzService;
import cc.mrbird.febs.server.tjdkxm.service.ProjectService;
import cc.mrbird.febs.server.tjdkxm.service.UserProjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import java.util.stream.Collectors;

/**
 * JsdwaqrzService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:02
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class JsdwaqrzServiceImpl extends ServiceImpl<JsdwaqrzMapper, Jsdwaqrz> implements JsdwaqrzService {
//
//    private final JsdwaqrzMapper jsdwaqrzMapper;
//    private final UserProjectService userProjectService;
//    private final CacheableService cacheableService;
//    private final ProjectService projectService;
//
//
//    @Override
//    public MyPage<Jsdwaqrz> findJsdwaqrzs(QueryRequest request, Jsdwaqrz jsdwaqrz) {
//        QueryWrapper<Jsdwaqrz> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("IS_DELETE",0);
//        Long projectId = userProjectService.getUserProjectId();
//        queryWrapper.eq("JSDWAQRZ_PROJECTID", projectId);
//        queryWrapper.orderByDesc("JSDWAQRZ_CREATETIME");
//        OrderUtils.setQuseryOrder(queryWrapper, request);
//        if (StringUtils.isNotBlank(jsdwaqrz.getJsdwaqrzCreateman())) {
//            queryWrapper.like("JSDWAQRZ_CREATEMAN",jsdwaqrz.getJsdwaqrzCreateman());
//        }
//        if (StringUtils.isNotBlank(jsdwaqrz.getJsdwaqrzJdqk())) {
//            queryWrapper.and(wapper -> {
//                wapper.like("JSDWAQRZ_CREATEMAN", jsdwaqrz.getJsdwaqrzJdqk());
//                wapper.or().like("JSDWAQRZ_JDQK", jsdwaqrz.getJsdwaqrzJdqk());
//                wapper.or().like("JSDWAQRZ_XTWTFLAG", jsdwaqrz.getJsdwaqrzJdqk());
//                wapper.or().like("JSDWAQRZ_GDHYJK", jsdwaqrz.getJsdwaqrzJdqk());
//                wapper.or().like("JSDWAQRZ_GJJCQK", jsdwaqrz.getJsdwaqrzJdqk());
//            });
//        }
//        if (StringUtils.isNotBlank(jsdwaqrz.getStartCreateTime())) {
//            queryWrapper.ge("JSDWAQRZ_CREATETIME", jsdwaqrz.getStartCreateTime());
//        }
//        if (StringUtils.isNotBlank(jsdwaqrz.getEndCreateTime())) {
//            queryWrapper.le("JSDWAQRZ_CREATETIME", jsdwaqrz.getEndCreateTime());
//        }
//        Page<Jsdwaqrz> page = this.page(new Page<>(request.getPageNum(), request.getPageSize()), queryWrapper);
//        List<Jsdwaqrz> records = page.getRecords();
//        List<Map<String, Object>> allUserByProject =
//                cacheableService.getAllUserByProject(new HashSet<Long>(){{add(1L);}});
//        if (records.size() > 0) {
//            Set<Long> projectIds = records.stream().map(Jsdwaqrz::getJsdwaqrzProjectid).collect(Collectors.toSet());
//            if (projectIds.size() > 0) {
//                List<Project> projects = projectService.getByIds(projectIds);
//                if (projects.size() > 0) {
//                    records.forEach(record -> {
//                        projects.forEach(project -> {
//                            if (project.getProjectId().equals(record.getJsdwaqrzProjectid())) {
//                                record.setProjectName(project.getProjectName());
//                            }
//                        });
//                    });
//                }
//            }
//            page.setRecords(records);
//        }
//
//        page.getRecords().forEach(record -> {
//            record.setJsdwaqrzXmjlname(getName(allUserByProject, record.getJsdwaqrzXmjl()));
//            record.setJsdwaqrzJsfzrname(getName(allUserByProject, record.getJsdwaqrzJsfzr()));
//            record.setJsdwaqrzZzaqyname(getName(allUserByProject, record.getJsdwaqrzZzaqy()));
//            record.setJsdwaqrzZjname(getName(allUserByProject, record.getJsdwaqrzZj()));
//            record.setJsdwaqrzZyjlname(getName(allUserByProject, record.getJsdwaqrzZyjl()));
//        });
//        List<Map<String, Object>> allUserByProjects = new ArrayList<>();
//        allUserByProject.forEach(map -> {
//            if (!new Long("4").equals(map.get("ROLE_ID"))) {
//                allUserByProjects.add(map);
//            }
//        });
//        MyPage<Jsdwaqrz> myPage = new MyPage<>(page);
//        MyPage.MyRecords<Jsdwaqrz> myRecords = myPage.getMyRecords();
//        myRecords.setUserList(allUserByProjects);
//        return myPage;
//    }
//
//    private String getName(List<Map<String, Object>> allUserByProjects, String id) {
//        if (id != null) {
//            String[] ids = id.split(",");
//            if (ids.length > 0) {
//                Set<String> collect = Arrays.stream(ids).collect(Collectors.toSet());
//                List<String> name = new ArrayList<>(2);
//                collect.forEach(s -> {
//                    allUserByProjects.forEach(map -> {
//                        try {
//                            if (map.get("USER_ID").equals(Long.valueOf(s))) {
//                                name.add((String) map.get("REALNAME"));
//                            }
//                        } catch (Exception e) {
//                        }
//                    });
//                });
//                if (name.isEmpty()) {
//                    return "";
//                }
//                return String.join(",", name.toArray(new String[0]));
//            }
//        }
//        return "";
//    }
//
//    @Override
//    public List<Jsdwaqrz> findJsdwaqrzs(Jsdwaqrz jsdwaqrz) {
//        LambdaQueryWrapper<Jsdwaqrz> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Jsdwaqrz::getIsDelete,0);
//        queryWrapper.eq(Jsdwaqrz::getJsdwaqrzProjectid, userProjectService.getUserProjectId());
//        return this.list(queryWrapper);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void createJsdwaqrz(Jsdwaqrz jsdwaqrz) {
//        jsdwaqrz.setJsdwaqrzProjectid(userProjectService.getUserProjectId());
//        jsdwaqrz.setJsdwaqrzCreatetime(new Date());
//        jsdwaqrz.setJsdwaqrzCreateman(FebsUtil.getCurrentRealname());
//        jsdwaqrz.setJsdwaqrzCreatemanid(FebsUtil.getCurrentUserId());
//        this.save(jsdwaqrz);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateJsdwaqrz(Jsdwaqrz jsdwaqrz) {
//        this.updateById(jsdwaqrz);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void deleteJsdwaqrz(Jsdwaqrz jsdwaqrz) {
//        LambdaQueryWrapper<Jsdwaqrz> wrapper = new LambdaQueryWrapper<>();
//        this.update(null,new LambdaUpdateWrapper<Jsdwaqrz>().eq(Jsdwaqrz::getJsdwaqrzId,jsdwaqrz.getJsdwaqrzId()).
//                set(Jsdwaqrz::getIsDelete,1));
//    }


}
