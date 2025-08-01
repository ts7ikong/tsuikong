package cc.mrbird.febs.common.core.entity.constant;

/**
 * FEBS系统常量类
 *
 * @author MrBird
 */
public interface FebsConstant {

    /**
     * 排序规则：降序
     */
    String ORDER_DESC = "descending";
    /**
     * 排序规则：升序
     */
    String ORDER_ASC = "ascending";

    /**
     * Gateway请求头TOKEN名称（不要有空格）
     */
    String GATEWAY_TOKEN_HEADER = "GatewayToken";
    /**
     * Gateway请求头TOKEN值
     */
    String GATEWAY_TOKEN_VALUE = "febs:gateway:123456";

    /**
     * 允许下载的文件类型，根据需求自己添加（小写）
     */
    String[] VALID_FILE_TYPE = {"template", "zip"};

    /**
     * 验证码 key前缀
     */
    String CODE_PREFIX = "febs.captcha.";
    /**
     * pc项目 key前缀
     */
    String USER_PROJECT_PREFIX = "febs.userproject.project.";
    /**
     * pc项目 key前缀
     */
    String PROJECT_PARCEL_SUBITEM_PREFIX = "febs.userproject.project.parcelAndSubitem";
    /**
     * pc 公司 key前缀
     */
    String USER_PROJECT_PREFIX_COMPANY = "febs.userproject.company.";

    /**
     * PC端
     */
    String PC = "pc_project";
    /**
     * APP端
     */
    String APP = "app_project";
    /**
     * app项目 key前缀
     */
    String USER_PROJECT_PREFIX_APP = "febs.userproject.app.";

    /**
     * 异步线程池名称
     */
    String ASYNC_POOL = "febsAsyncThreadPool";

    /**
     * OAUTH2 令牌类型 https://oauth.net/2/bearer-tokens/
     */
    String OAUTH2_TOKEN_TYPE = "bearer";
    /**
     * Java默认临时目录
     */
    String JAVA_TEMP_DIR = "java.io.tmpdir";
    /**
     * utf-8
     */
    String UTF8 = "utf-8";
    /**
     * 注册用户角色ID
     */
    Long REGISTER_ROLE_ID = 2L;

    String LOCALHOST = "localhost";
    String LOCALHOST_IP = "127.0.0.1";
    /**
     * admin role id
     */
    String ADMIN_USER = "1";
    /**
     * project principal role id
     */
    String PROJECT_USER = "2";
    /**
     * temp role id
     */
    String TEMP_USER = "4";
}
