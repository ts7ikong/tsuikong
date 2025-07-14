package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.PunchAreaClocktime;
import cc.mrbird.febs.common.core.entity.tjdkxm.Reporting;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserProject;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserPunch;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdkxm.mapper.ReportingMapper;
import cc.mrbird.febs.server.tjdkxm.mapper.UserPunchMapper;
import cc.mrbird.febs.server.tjdkxm.service.UserPunchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/9 17:22
 */
@Slf4j
@Validated
@RestController
@Api(tags = "打卡")
@RequestMapping("/userpunch")
@RequiredArgsConstructor
public class UserPunchController {
    /**
     * 服务对象
     */
    @Autowired
    private UserPunchService userPunchService;

    @GetMapping
    @ApiOperation(value = "是否在打卡区域,打卡类型 打卡类型 0内勤打卡 1外勤打卡")
    @ApiImplicitParams({@ApiImplicitParam(name = "latitude", value = "纬度", required = true),
            @ApiImplicitParam(name = "longitude", value = "径度", required = true)})
    public FebsResponse getAllUserPunchs(String latitude, String longitude) {
        return new FebsResponse().data(userPunchService.areInPunchArea(latitude, longitude));
    }

    @GetMapping("record")
    @ApiOperation(value = "获取打卡统计图")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "类型 1月 2周 3日", required = true),
            @ApiImplicitParam(name = "startTime", value = "日期 标准格式 开始", required = true),
            @ApiImplicitParam(name = "endTime", value = "标准格式 开始 结束", required = false)})
    public FebsResponse getPunchRecord(@RequestParam(value = "type", defaultValue = "1") String type,
                                       @RequestParam(value = "startTime") String startTime, @RequestParam(value = "endTime") String endTime)
            throws FebsException {
        return new FebsResponse().data(userPunchService.getPunchRecord(type, startTime, endTime, true));
    }


    @GetMapping("my/record")
    @ApiOperation(value = "获取打卡统计图")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "类型 1月 2周 3日", required = true),
            @ApiImplicitParam(name = "startTime", value = "日期 标准格式 开始", required = true),
            @ApiImplicitParam(name = "endTime", value = "标准格式 开始 结束", required = false)})
    public FebsResponse getMyPunchRecord(@RequestParam(value = "type", defaultValue = "1") String type,
                                         @RequestParam(value = "startTime") String startTime, @RequestParam(value = "endTime") String endTime)
            throws FebsException {
        return new FebsResponse().data(userPunchService.getPunchRecord(type, startTime, endTime, false));
    }

    @GetMapping("v2/has")
    @ApiOperation(value = "是否在打卡 时间内")
    public FebsResponse getAllUserPunchs() {
        List<PunchAreaClocktime> punchAreaClocktimes = userPunchService.areInPunchAreaNew(FebsUtil.getCurrentUserId());
        return new FebsResponse().data(punchAreaClocktimes != null && !punchAreaClocktimes.isEmpty());
    }

    @GetMapping("/day")
    @ApiOperation(value = " 当日已打卡 返回 true,未打卡false")
    public FebsResponse arePunchAreaByDay() {
        return new FebsResponse().data(userPunchService.arePunchAreaByDay());
    }

    @PostMapping()
    @ApiOperation(value = "打卡")
    @ApiImplicitParams({@ApiImplicitParam(name = "latitude", value = "纬度", required = true),
            @ApiImplicitParam(name = "longitude", value = "径度", required = true),
            @ApiImplicitParam(name = "punchAddr", value = "打卡地址", required = true)})
    @OperateLog(mapper = UserPunchMapper.class, className = UserPunch.class, type = "用户打卡")
    public FebsResponse userPush(String latitude, String longitude, String punchAddr) throws FebsException {
        System.out.println(latitude+ " " + longitude + " " + punchAddr);
        return new FebsResponse().data(userPunchService.userPush(latitude, longitude, punchAddr));
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse userProjectList(QueryRequest request, UserPunch.Params userPunch) throws FebsException {
        Map<String, Object> dataTable =
                FebsUtil.getDataTable(userPunchService.findUserPunch(request, userPunch, false));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "分页查询2", notes = "暂无查询条件")
    @GetMapping("list1")
    public FebsResponse userProjectList1(QueryRequest request, UserPunch.Params userPunch) throws FebsException {
        return userPunchService.findUserPunch1(request, userPunch, false);
    }

    /**
     * 导出考勤记录
     *
     * @param userPunchIds 考勤记录ids
     */
    @ApiOperation(value = "导出考勤记录", notes = "导出考勤记录")
    @PostMapping("/download")
    public void download(HttpServletResponse response, String userPunchIds) throws FebsException {
        try {
            this.userPunchService.download(response, userPunchIds);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new FebsException(e.getMessage());
        }
    }

    @PostMapping("record/download")
    @ApiOperation(value = "获取打卡统计图导出")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "类型 1月 2周 3日", required = true),
            @ApiImplicitParam(name = "startTime", value = "日期 标准格式 开始", required = true),
            @ApiImplicitParam(name = "endTime", value = "标准格式 开始 结束", required = false)})
    public void getPunchRecord1(HttpServletResponse response, @RequestParam(value = "type", defaultValue = "1") String type,
                                @RequestParam(value = "startTime") String startTime, @RequestParam(value = "endTime") String endTime)
            throws FebsException, IOException, ParseException {
        userPunchService.getPunchRecord1(response, type, startTime, endTime, true);
    }

    @PostMapping("my/record/download")
    @ApiOperation(value = "获取打卡统计图导出")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "类型 1月 2周 3日", required = true),
            @ApiImplicitParam(name = "startTime", value = "日期 标准格式 开始", required = true),
            @ApiImplicitParam(name = "endTime", value = "标准格式 开始 结束", required = false)})
    public void getPunchRecord2(HttpServletResponse response, @RequestParam(value = "type", defaultValue = "1") String type,
                                @RequestParam(value = "startTime") String startTime, @RequestParam(value = "endTime") String endTime)
            throws FebsException, IOException, ParseException {
        userPunchService.getPunchRecord2(response, type, startTime, endTime, true);
    }


    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list/app")
    public FebsResponse userAppProjectList(QueryRequest request, UserPunch.Params userPunch) throws FebsException {
        Map<String, Object> dataTable = FebsUtil.getDataTable(userPunchService.findUserPunch(request, userPunch, true));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "分页查询--未打卡", notes = "暂无查询条件")
    @GetMapping("no/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "用户名、姓名", required = false),
            @ApiImplicitParam(name = "date", value = "时间 yyyy-MM-dd/yyyy-MM", required = false),
            @ApiImplicitParam(name = "type", value = "类型 1日 2周 3月 ", required = false, defaultValue = "1")})
    public FebsResponse userProjectList(QueryRequest request, @PathParam("username") String username,
                                        @PathParam("date") String date, @PathParam("type") String type) throws FebsException {
        Map<String, Object> dataTable =
                FebsUtil.getDataTable(userPunchService.noUserPunch(request, username, date, type));
        return new FebsResponse().data(dataTable);
    }

    // @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    // @PutMapping
    // public void updateUserPunch(UserPunch punchArea) throws FebsException {
    // try {
    // this.punchAreaService.updateUserPunch(punchArea);
    // } catch (Exception e) {
    // String message = "修改项目信息失败";
    // log.error(message, e);
    // throw new FebsException(message);
    // }
    // }
}
