package cc.mrbird.febs.server.tjdk.controller;

import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdk.Sysnotify;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.tjdk.service.UserSysnotifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 系统通知(Sysnotify)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:08
 */

@Slf4j
@Validated
@RestController
@Api(tags = "用户系统通知(UserSysnotify)控制层")
@RequestMapping("/usersysnotify")
@RequiredArgsConstructor
public class UserSysnotifyController {
    /**
     * 服务对象
     */
    @Autowired
    private UserSysnotifyService userSysnotifyService;

    @ApiOperation(value = "新增接口", notes = "只执行新增，后端不进行任何处理")
    @PostMapping
    @ApiIgnore
    public void addSysnotify(Long sysnotifyId) throws FebsException {
        try {
            this.userSysnotifyService.createUserSysnotify(sysnotifyId);
        } catch (Exception e) {
            String message = "新增系统通知失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "分页查询", notes = "暂无查询条件")
    @GetMapping("list")
    public FebsResponse getAllUserSysnotifyNotReads(QueryRequest request, Sysnotify.Params sysnotify) {
        Map<String, Object> dataTable =
            FebsUtil.getDataTable(userSysnotifyService.findSysnotifyNotReads(request, sysnotify));
        return new FebsResponse().data(dataTable);
    }

    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
    @PutMapping
    public void updateSysnotify(Long sysnotifyId) throws FebsException {
        try {
            this.userSysnotifyService.updateUserSysnotify(sysnotifyId);
        } catch (Exception e) {
            String message = "修改系统通知失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "全部已读")
    @PostMapping("all/read")
    public void allSysnotify() throws FebsException {
        try {
            this.userSysnotifyService.readAllSysnotity();
        } catch (Exception e) {
            String message = "读系统通知失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除全部已读")
    @PostMapping("del/all/read")
    public void delAllSysnotify() throws FebsException {
        try {
            this.userSysnotifyService.delAllReadSysnotity();
        } catch (Exception e) {
            String message = "删除已读系统通知失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "删除接口", notes = "根据id")
    @DeleteMapping
    public void deleteSysnotify(Long sysnotifyId) throws FebsException {
        try {
            this.userSysnotifyService.deleteUserSysnotify(sysnotifyId);
        } catch (Exception e) {
            String message = "修改系统通知失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @ApiOperation(value = "系统通知")
    @GetMapping("notRead")
    public FebsResponse getNotRead() throws FebsException {
        try {
            return new FebsResponse().data(this.userSysnotifyService.getNotRead());
        } catch (Exception e) {
            throw new FebsException("获取消息通知失败！");
        }
    }

}
