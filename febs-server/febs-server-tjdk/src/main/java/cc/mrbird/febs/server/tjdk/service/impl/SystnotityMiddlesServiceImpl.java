package cc.mrbird.febs.server.tjdk.service.impl;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdk.Sysnotify;
import cc.mrbird.febs.common.core.entity.tjdk.SystnotityMiddle;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserSysnotify;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.server.tjdk.mapper.SysnotifyMapper;
import cc.mrbird.febs.server.tjdk.mapper.SystnotityMiddleMapper;
import cc.mrbird.febs.server.tjdk.service.SysnotifyService;
import cc.mrbird.febs.server.tjdk.service.SystnotityMiddlesService;
import cc.mrbird.febs.server.tjdk.service.UserSysnotifyService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SysnotifyService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:08
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SystnotityMiddlesServiceImpl extends ServiceImpl<SystnotityMiddleMapper, SystnotityMiddle> implements SystnotityMiddlesService {
}
