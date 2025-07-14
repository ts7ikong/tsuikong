package cc.mrbird.febs.common.core.entity.constant;

/**
 * rediskey 前缀
 *
 * @author 14059
 * @version V1.0
 * @date 2022/4/8 18:42
 */
public interface RedisKey {
    /**
     * 用户数据权限key 前缀 后跟userId
     */
    String DATA_AUTH_PREFIX = "febs.USER_AUTH.DATA_AUTH:";
    /**
     * 用户前端数据权限key 前缀 后跟userId
     */
    String DATA_FRONT_AUTH_PREFIX = "febs.USER_DATA_FRONT_AUTH:";
    /**
     * 用户数据权限key 前缀 后跟userId
     */
    String DATA_PROJECT = "febs.DATA_PROJECT:";
    /**
     * 项目 所有的单位工程、分部、分项
     */
    String PROJECT_ALL = "febs.project-global-all";
    /**
     * 查询所有用户 除除超级管理员
     */
    String USER_ALL = "febs.user-global-all";
    /**
     * 项目对应的所有用户 hash key未projectId
     */
    String PROJECT_USER_ALL = "febs.project-global-user-all";
    /**
     * 供应商信息
     */
    String SUPPLIER_ALL = "febs.supplier_all";

    /**
     * 时间 一分钟s
     */
    Long ONE_TIME = 60L;
    /**
     * 代办 前缀 后跟userId
     */
    String AGENCY_NUM = "febs.user.agency_num:";
    /**
     * banner图
     */
    String BANNER = "febs.banner";
    /**
     * 用户当日打卡情况 后跟日期
     */
    String USER_PUNCH_DAY = "febs.USER_PUNCH_NEW:%s:%d";
    /**
     * 项目中拥有该菜单按钮的用户信息
     */
    String PROJECT_MENU_BTN_REF = "febs.PROJECT_MENU_BTN:";
    /**
     * 1 公共 2 项目
     */
    String MENU_BUTTON_TYPE = "febs.MENU_BUTTON_TYPE";
    /**
     * 系统配置
     */
    String SYSTEM_CONFIG = "febs:system:config";
}
