package cc.mrbird.febs.server.tjdkxm.service.impl;

import cc.mrbird.febs.common.core.entity.tjdkxm.ConferenceUser;
import cc.mrbird.febs.server.tjdkxm.mapper.ConferenceUserMapper;
import cc.mrbird.febs.server.tjdkxm.service.ConferenceUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/14 13:57
 */
@Service
public class ConferenceUserServiceImpl  extends ServiceImpl<ConferenceUserMapper, ConferenceUser> implements ConferenceUserService {
}
