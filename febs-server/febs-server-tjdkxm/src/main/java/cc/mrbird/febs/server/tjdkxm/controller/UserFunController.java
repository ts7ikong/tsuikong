package cc.mrbird.febs.server.tjdkxm.controller;

import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.Rest;
import cc.mrbird.febs.common.core.entity.tjdkxm.UserFun;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.server.tjdkxm.service.UserFunService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 分部表(Parcel)表控制层
 *
 * @author zlkj_cg
 * @since 2022-01-12 15:51:04
 */

@Slf4j
@Validated
@RestController
@Api(tags = "用户常用功能")
@RequestMapping("/userfun")
@RequiredArgsConstructor
public class UserFunController {
    /**
     * 服务对象
     */
    @Autowired
    private UserFunService userFunService;

//
//    @GetMapping
//    public Rest<UserFun> getAllUserProjects() {
//        return Rest.data(userFunService.findUserFun());
//    }
//    @ApiOperation(value = "修改接口", notes = "根据id修改一条数据，只执行修改操作")
//    @PutMapping
//    public void updateUserFun(UserFun userFun) throws FebsException {
//        try {
//            this.userFunService.updateUserProject(userFun);
//        } catch (Exception e) {
//            String message = "修改分项表失败";
//            log.error(message, e);
//            throw new FebsException(message);
//        }
//    }
    @PostMapping
    @ApiOperation(value = "insertOrupdate接口", notes = "featuresString必须存在")
    public void createUserFun(UserFun userFun) throws FebsException {
        try {
            this.userFunService.crateUserFun(userFun);
        } catch (Exception e) {
            String message = "修改分项表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    @GetMapping
    @ApiOperation(value = "获取用户常用功能")
    public FebsResponse findUserFun() throws FebsException {
        try {
            return new FebsResponse().data(this.userFunService.findUserFun());
        } catch (Exception e) {
            String message = "修改分项表失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }


}
