package cc.mrbird.febs.server.tjdkxm.mapper;

import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.entity.tjdkxm.PunchArea;
import cc.mrbird.febs.common.core.entity.tjdkxm.PunchAreaClocktime;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserPunch;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/9 17:49
 */
@Mapper
public interface UserPunchMapper extends BaseMapper<UserPunch> {
    /**
     * 获取是否可以打卡
     *
     * @param latitude 纬度
     * @param longitude 精度
     * @param userId 用户id
     * @return {@link List< PunchArea>}
     */
    PunchArea areInPunchArea(@Param("latitude") String latitude, @Param("longitude") String longitude,
        @Param("userId") Long userId);

    /**
     * 获取时间
     *
     * @param userId 用户id
     * @param time 时间 小时*60+分钟
     * @return {@link Long}
     */
    List<PunchAreaClocktime> arePunchAreaTime(@Param("userId") Long userId, @Param("time") Integer time);

    /**
     * 获取时间
     *
     * @param userId 用户id
     * @return {@link Long}
     */
    List<PunchArea> hasPuncharea(@Param("userId") Long userId);

    /**
     * 获取时间
     *
     * @param userId 用户id
     * @return {@link Long}
     */
    List<PunchAreaClocktime> userPunchareaInfo(@Param("punchAreas") List<PunchArea> punchAreas);

    <T> IPage<UserPunch> selectPageInfo(Page<T> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    <T> IPage<UserPunch> selectPageInfo2(Page<T> page, @Param(Constants.WRAPPER) Wrapper<?> wrapper);

    <T> List<Integer> selectPageInfo1(@Param(Constants.WRAPPER) Wrapper<?> wrapper);

    <T> List<UserPunch> selectInfo( @Param("userPunchIds") String userPunchIds);

    /**
     * id查询
     *
     * @param id tableid
     * @return {@link com.alibaba.fastjson.JSONObject}
     */
    JSONObject selectInfoById(@Param("id") Long id);

    /**
     * 分页查询--未打卡
     *
     * @param page 分页
     * @param username 用户名
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return {@link IPage< Map< String, Object>>}
     */
    IPage<Map<String, Object>> noUserPunch(Page<T> page, @Param("username") String username,
        @Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 可以打卡人数
     *
     * @param userId
     * @return {@link java.lang.Integer}
     */
    Set<SystemUser> selectUserCount(@Param("userId") Long userId);

    /**
     * 按时间段统计打卡
     *
     * @param userId
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return {@link List< UserPunch>}
     */
    List<UserPunch> selectUserPunchByTime(@Param("userId") Long userId, @Param("startTime") String startTime,
        @Param("endTime") String endTime);

    /**
     * 打卡时间统计 今日是否打卡
     * 
     * @param userId 用户id
     * @return {@link java.lang.Integer}
     */
    UserPunch selectCountByDay(@Param("userId") Long userId);
}
