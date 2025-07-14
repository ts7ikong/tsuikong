package cc.mrbird.febs.common.core.options;

import com.wuwenze.poi.config.Options;

/**
 * Execl转换器
 *
 * @author 14059
 * @version V1.0
 * @date 2022/4/7 17:17
 */
public class MyOptions {
    public static class UserGroupNameOptions implements Options {
        @Override
        public String[] get() {
            return new String[]{"管理组", "普通会员组", "游客"};
        }
    }
    public static class SexOption implements Options {
        @Override
        public String[] get() {
            return new String[]{"未知", "男", "女"};
        }
    }
}
