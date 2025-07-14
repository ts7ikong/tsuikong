package cc.mrbird.febs.server.tjdkxm.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 全局 日期格式 转换器
 * @author 14059
 * @version V1.0
 * @date 2022/3/23 10:17
 */
@Component
public class DateConverterConfig implements Converter<String, Date> {

    // 日期格式
    private static final List<String> FORMARTS = new ArrayList<>(4);
    static{
        FORMARTS.add("yyyy-MM");
        FORMARTS.add("yyyy-MM-dd");
        FORMARTS.add("yyyy-MM-dd hh:mm");
        FORMARTS.add("yyyy-MM-dd hh:mm:ss");
        FORMARTS.add("yyyy-MM-dd hh");
    }

    @Override
    public Date convert(String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        if(value.matches("^\\d{4}-\\d{1,2}$")){
            return parseDate(value, FORMARTS.get(0));
        }else if(value.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
            return parseDate(value, FORMARTS.get(1));
        }else if(value.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")){
            return parseDate(value, FORMARTS.get(2));
        }else if(value.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")){
            return parseDate(value, FORMARTS.get(3));
        }else if(value.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}$")){
            return parseDate(value, FORMARTS.get(4));
        }else {
            throw new IllegalArgumentException("Invalid boolean value '" + value + "'");
        }
    }

    /**
     * 格式化日期
     * @param dateStr String 字符型日期
     * @param format String 格式
     * @return Date 日期
     */
    public  Date parseDate(String dateStr, String format) {
        Date date=null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {

        }
        return date;
    }

}