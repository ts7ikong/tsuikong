package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.tjdkxm.PunchAreaUser;
import cc.mrbird.febs.server.tjdkxm.mapper.PunchAreaUserMapper;
import cc.mrbird.febs.server.tjdkxm.service.PunchAreaUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/5/5 15:46
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PunchAreaUserServiceImpl extends ServiceImpl<PunchAreaUserMapper, PunchAreaUser> implements PunchAreaUserService {
}
