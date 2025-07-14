package cc.mrbird.febs.server.tjdk.service.impl;

import cc.mrbird.febs.common.core.entity.constant.RedisKey;
import cc.mrbird.febs.common.core.entity.tjdk.Banner;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.redis.service.RedisService;
import cc.mrbird.febs.server.tjdk.config.LogRecordContext;
import cc.mrbird.febs.server.tjdk.mapper.BannerMapper;
import cc.mrbird.febs.server.tjdk.service.BannerService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cc.mrbird.febs.common.core.entity.QueryRequest;

import java.util.List;

/**
 * BannerService实现
 *
 * @author zlkj_cg
 * @date 2022-01-12 15:51:03
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
    @Autowired
    private BannerMapper bannerMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private LogRecordContext logRecordContext;

    @Override
    public IPage<Banner> findBanners(QueryRequest request, Banner banner) {
        String key = RedisKey.BANNER;
        if (redisService.hasKey(key)) {
            return (IPage<Banner>)redisService.get(key);
        }
        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Banner::getIsDelete, 0);
        Page<Banner> page = new Page<>(request.getPageNum(), request.getPageSize());
        this.page(page, queryWrapper);
        redisService.set(key, page);
        return page;
    }

    @Override
    public List<Banner> findBanners(Banner banner) {
        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Banner::getIsDelete, 0);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBanner(List<Banner> banner) throws FebsException {
        if (banner == null || banner.isEmpty()) {
            throw new FebsException("失败");
        }
        int count = bannerMapper
            .selectCount(new LambdaQueryWrapper<Banner>().select(Banner::getBannerId).eq(Banner::getIsDelete, 0));
        int size = count + banner.size();
        if (size > 10) {
            throw new FebsException("最多上传10张图片，请删除其中一张后再添加");
        }
        this.saveBatch(banner, banner.size());
        redisService.del(RedisKey.BANNER);
        logRecordContext.putVariable("id", banner.get(0).getBannerId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBanner(Banner banner) {
        this.updateById(banner);
        redisService.del(RedisKey.BANNER);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBanner(Banner banner) throws FebsException {
        if (bannerMapper
            .selectCount(new LambdaQueryWrapper<Banner>().select(Banner::getBannerId).eq(Banner::getIsDelete, 0)) < 2) {
            throw new FebsException("最少传1张图片");
        }
        this.update(new LambdaUpdateWrapper<Banner>().eq(Banner::getBannerId, banner.getBannerId())
            .set(Banner::getIsDelete, 1));
        redisService.del(RedisKey.BANNER);
    }
}
