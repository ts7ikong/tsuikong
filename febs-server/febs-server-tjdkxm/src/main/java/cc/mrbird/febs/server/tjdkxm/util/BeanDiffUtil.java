package cc.mrbird.febs.server.tjdkxm.util;

import cc.mrbird.febs.common.core.entity.tjdkxm.Askfleave;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class BeanDiffUtil {

    // 忽略字段列表（黑名单）
    private static final Set<String> IGNORE_FIELDS = new HashSet<>(Arrays.asList(
            "hibernateLazyInitializer", "handler", "fieldHandler",
            "askfleaveCreateusername", "askfleaveCheckusername",
            "modify", "task", "isDelete", "askfleaveCheckrecord"
    ));

    public interface ValueFormatter {
        String format(Object value, String fieldName);
    }

    public static Map<String, String> getDiff(Askfleave oldBean, Askfleave newBean, ValueFormatter formatter) {
        Map<String, String> diffMap = new LinkedHashMap<>();
        Field[] fields = Askfleave.class.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();

            if (IGNORE_FIELDS.contains(fieldName)) continue;

            try {
                field.setAccessible(true);
                Object oldValue = field.get(oldBean);
                Object newValue = field.get(newBean);

                if (oldValue != null || newValue != null) {
                    if (oldValue == null && newValue != null ||
                            newValue == null && oldValue != null ||
                            !Objects.equals(oldValue, newValue)) {

                        String label = getLabel(fieldName);
                        String formattedOld = formatter.format(oldValue, fieldName);
                        String formattedNew = formatter.format(newValue, fieldName);
                        diffMap.put(label, formattedOld + " → " + formattedNew);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("字段对比异常：" + field.getName(), e);
            }
        }
        return diffMap;
    }

    private static String getLabel(String fieldName) {
        switch (fieldName) {
            case "askfleaveCause": return "请假原因";
            case "askfleaveStarttime": return "开始时间";
            case "askfleaveEndtime": return "结束时间";
            case "askfleaveCheckreason": return "审批意见";
            case "askfleaveCheckstate": return "审批状态";
            case "askfleaveCheckuserid": return "审批人";
            default: return fieldName;
        }
    }

    public static String defaultFormatValue(Object value) {
        if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(value);
        }
        return value == null ? "空" : value.toString();
    }
}