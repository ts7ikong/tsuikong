package cc.mrbird.febs.common.core.converter;

import cc.mrbird.febs.common.core.utils.DateUtil;
import com.wuwenze.poi.convert.ReadConverter;
import com.wuwenze.poi.convert.WriteConverter;
import com.wuwenze.poi.exception.ExcelKitReadConverterException;
import com.wuwenze.poi.exception.ExcelKitWriteConverterException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/7 16:51
 */
@Slf4j
public class MyConverter {
    public static class IntegerToStringWriteConverter implements WriteConverter {
        /**
         * 写文件时，将值进行转换（此处示例为将数值拼接为指定格式的字符串）
         */
        @Override
        public String convert(Object o) throws ExcelKitWriteConverterException {
            return (o + "");
        }
    }

    public static class DateToStringWriteConverter implements WriteConverter {
        /**
         * 写文件时，将值进行转换（此处示例为将数值拼接为指定格式的字符串）
         */
        @Override
        public String convert(Object value) throws ExcelKitWriteConverterException {
            try {
                return DateUtil.formatCstTime(value.toString(), DateUtil.FULL_TIME_SPLIT_PATTERN);
            } catch (ParseException e) {
                String message = "时间转换异常";
                log.error(message, e);
                throw new ExcelKitWriteConverterException(message);
            }
        }
    }

    public static class CustomizeFieldReadConverter implements ReadConverter {

        /**
         * 读取单元格时，将值进行转换（此处示例为计算单元格字符串char的总和）
         */
        @Override
        public Object convert(Object o) throws ExcelKitReadConverterException {
            String value = (String) o;

            int convertedValue = 0;
            for (char c : value.toCharArray()) {
                convertedValue += Integer.valueOf(c);
            }
            return convertedValue;
        }
    }

    private static final Map<String, String> SEX = new HashMap<String, String>() {{
        put("1", "男");
        put("2", "女");
        put("0", "未知");
        put("男", "1");
        put("女", "2");
        put("未知", "0");
    }};

    public static class SexReadConverter implements ReadConverter {
        @Override
        public Object convert(Object o) throws ExcelKitReadConverterException {
            String s = SEX.get(o.toString());
            return s == null ? "0" : s;
        }
    }

    public static class SexWriteConverter implements WriteConverter {
        @Override
        public String convert(Object o) throws ExcelKitWriteConverterException {
            String s = SEX.get(o.toString());
            return s == null ? "未知" : s;
        }
    }
}

