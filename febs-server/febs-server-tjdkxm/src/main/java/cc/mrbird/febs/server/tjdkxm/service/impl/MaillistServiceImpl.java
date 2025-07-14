package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.tjdk.Dept;
import cc.mrbird.febs.common.core.entity.tjdkxm.Sgrz;
import cc.mrbird.febs.common.core.utils.OrderUtils;
import cc.mrbird.febs.common.core.utils.SortUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.UserMapper;
import cc.mrbird.febs.server.tjdkxm.service.DeptService;
import cc.mrbird.febs.server.tjdkxm.service.MaillistService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author ZNKJ-R
 * @version V1.0
 * @date 2022/1/18 19:03
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MaillistServiceImpl implements MaillistService {
    private final UserMapper userMapper;

    @Override
    public IPage<Map<String, Object>> findMailLists(QueryRequest request, String name, Long deptId) {
        QueryWrapper<SystemUser> queryWrapper = Wrappers.query();
        QueryWrapper<SystemUser> queryWrapper1 = Wrappers.query();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like("REALNAME", name);
            queryWrapper1.like("REALNAME", name);
        }
        queryWrapper.orderByAsc("CONVERT(REALNAME USING gbk) ");
        queryWrapper1.orderByAsc("CONVERT(REALNAME USING gbk) ");
        if (deptId != null && deptId != -1) {
            queryWrapper.eq("DEPT_ID", deptId);
            queryWrapper1.eq("t.DEPT_ID", deptId);
        }
        if (deptId != null && deptId == -1) {
            queryWrapper.isNull("DEPT_ID");
            queryWrapper1.isNull("t.DEPT_ID");
        }
        Integer integer = userMapper.selectCount(queryWrapper.select("1"));
        if (integer != null || integer > 0) {
            IPage<Map<String, Object>> mapIPage = userMapper
                .selectMailList(new Page<>(request.getPageNum(), request.getPageSize(), false), queryWrapper1);
            mapIPage.setTotal(integer);
            return mapIPage;
        }
        return new Page<>();
    }

}
