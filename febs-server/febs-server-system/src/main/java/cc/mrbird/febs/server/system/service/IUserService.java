package cc.mrbird.febs.server.system.service;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.system.SystemUser;
import cc.mrbird.febs.common.core.exception.FebsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
public interface IUserService extends IService<SystemUser> {

    List<SystemUser> projectUserInfoListSafePlanYsr(Long projectId, Long buttonId);

    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
    SystemUser findByName(String username);

    /**
     * 查找用户详细信息
     *
     * @param request request
     * @param user 用户对象，用于传递查询条件
     * @return IPage
     */
    IPage<SystemUser> findUserDetailList(SystemUser user, QueryRequest request);

    /**
     * 更新用户登录时间
     *
     * @param username username
     */
    void updateLoginTime(String username);

    /**
     * 新增用户
     *
     * @param user user
     */
    void createUser(SystemUser user) throws FebsException;

    /**
     * 修改用户
     *
     * @param user user
     */
    void updateUser(SystemUser user) throws FebsException;

    /**
     * 删除用户
     *
     * @param userIds 用户 id数组
     */
    void deleteUsers(String[] userIds) throws FebsException;

    void deleteUser(String userId) throws FebsException;

    /**
     * 更新个人信息
     *
     * @param user 个人信息
     * @throws FebsException 异常
     */
    void updateProfile(SystemUser user) throws FebsException;

    /**
     * 更新用户头像
     *
     * @param avatar 用户头像
     */
    void updateAvatar(String avatar);

    /**
     * 更新用户密码
     *
     * @param password 新密码
     */
    void updatePassword(String password) throws FebsException;

    /**
     * 重置密码
     *
     * @param usernames 用户集合
     */
    void resetPassword(String[] usernames);

    /**
     * 查找需回避公证人
     *
     * @return 用户
     */
    void resetPassword(SystemUser user);

    /**
     * 查找需回避公证人
     *
     * @return 用户
     */
    void restMyPassword(SystemUser user) throws FebsException;

    /**
     * 获取websocket连接名
     *
     * @return cc.mrbird.febs.common.core.entity.system.SystemUser
     */
    String getCurrentUser();

    /**
     * 查找员工详细信息
     *
     * @param queryRequest request
     * @param user 用户对象，用于传递查询条件
     * @return IPage
     */
    IPage<SystemUser> findYgUserDetailList(SystemUser user, QueryRequest queryRequest);

    /**
     * 新增员工
     *
     * @param user
     */
    void addYgUser(SystemUser user) throws FebsException;

    /**
     * 修改员工
     *
     * @param user
     */
    void updateYgUser(SystemUser user) throws FebsException;

    void updateYgUserToProject(SystemUser user) throws FebsException;

    /**
     * 删除员工
     */
    void deleteYgUser(String userId) throws FebsException;

    /**
     * 查询项目中的用户列表-分页
     *
     * @param user
     * @param queryRequest
     * @return {@link IPage<?>}
     */

    IPage<?> userByProjectList(SystemUser user, Long projectId, QueryRequest queryRequest) throws FebsException;

    /**
     * 查询没在此项目中的用户列表-分页
     *
     * @param user
     * @param queryRequest
     * @return {@link IPage<?>}
     */
    IPage<?> userNotInProject(SystemUser user, Long projectId, QueryRequest queryRequest) throws FebsException;

    /**
     * 项目临时用户 查询
     *
     * @param user 用户实体条件
     * @param queryRequest 分页参数
     * @return {@link null}
     */
    IPage<?> findTempUserList(SystemUser user, QueryRequest queryRequest) throws FebsException;

    /**
     * 项目临时用户 新增
     *
     * @param user 用户实体
     * @return {@link null}
     */
    void addTempUser(SystemUser user) throws FebsException;

    /**
     * 项目临时用户 修改
     *
     * @param user 用户实体
     * @return {@link null}
     */
    void updateTempUser(SystemUser user) throws FebsException;

    /**
     * 获取所有用户
     *
     * @return {@link java.util.List<java.util.Map<java.lang.String,java.lang.Object>>}
     */
    List<Map<String, Object>> getAllUsers();

    String getAvatar(Long userId);

    void updateTempUserToUser(boolean isTemp, SystemUser user) throws FebsException;
}