package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdkxm.PunchAreaClocktime;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserPunch;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/9 17:38
 */
public interface UserPunchService extends IService<UserPunch> {
    /**
     * 是否在打卡区域,是否可以打卡
     *
     * @return {@link java.lang.Boolean}
     */
    Boolean areInPunchArea(String latitude, String longitude);

    List<PunchAreaClocktime> areInPunchAreaNew(Long userId);

    /**
     * 打卡
     *
     * @return {@link cc.mrbird.febs.common.core.entity.tjdkxm.UserPunch}
     */
    UserPunch userPush(String latitude, String longitude, String punchAddr) throws FebsException;

    IPage<UserPunch> findUserPunch(QueryRequest request, UserPunch.Params userPunch, boolean app) throws FebsException;

    FebsResponse findUserPunch1(QueryRequest request, UserPunch.Params userPunch, boolean app) throws FebsException;

    UserPunch.AreaByDay arePunchAreaByDay();

    /**
     * 分页查询--未打卡
     *
     * @param request  分页
     * @param username 用户名、姓名
     * @param date     时间
     * @param type     类型 日 周 月
     * @return {@link com.baomidou.mybatisplus.core.metadata.IPage<?>}
     */
    IPage<Map<String, Object>> noUserPunch(QueryRequest request, String username, String date, String type);

    Map<String, Object> getPunchRecord(String type, String time, String weekTime, boolean isAdmin) throws FebsException;

    void download(HttpServletResponse response, String userPunchIds) throws FebsException, IOException;

    void getPunchRecord1(HttpServletResponse response, String type, String time, String weekTime, boolean isAdmin) throws FebsException, IOException, ParseException;

    void getPunchRecord2(HttpServletResponse response, String type, String startTime, String endTime, boolean b) throws IOException, ParseException;

    /**
     * 修改
     *
     * @param punchArea
     */
    // void updateUserPunch(UserPunch punchArea);
}
