package cc.mrbird.febs.server.tjdkxm.util;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.validation.constraints.NotEmpty;

import cc.mrbird.febs.common.core.utils.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 节日
 *
 * @author 14059
 * @version V1.0
 * @date 2022/6/1 11:17
 */
public class Holidays {

    private static final Set<String> TOTAL_SET = new CopyOnWriteArraySet<>();
    // private static final Set<String> HOLIDAYS_SET = new CopyOnWriteArraySet<>();
    private static final Set<String> YEAR_SET = new HashSet<>();

    /**
     * 是否是 国家法定节假日&(周六||周日)
     *
     * @param date yyyy-MM-dd
     * @return {@link boolean}
     */
    public static boolean isHolidays(@NotEmpty String date) {
        String[] stirs = date.split("-");
        setTotalSet(stirs[0]);
        return TOTAL_SET.contains(date);
    }


    /**
     * 计算当前月有多少天
     *
     * @return
     */
    public static int getDays(int year, int month) {
        int days = 0;
        if (month != 2) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    days = 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    days = 30;

            }
        } else {
            // 闰年
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
                days = 29;
            else
                days = 28;
        }
        return days;
    }

    /**
     * 获取当前月份有几天节假日
     * * 是否是 国家法定节假日&(周六||周日)
     */
    public static int dqyisHolidays(String date) {
        int days = getDays(Integer.parseInt(date.split("-")[0]), Integer.parseInt(date.split("-")[1]));
        int holidays = 0;
        for (int i = 1; i <= days; i++) {
            if (isHolidays(date.split("-")[0] + "-" + date.split("-")[1] + "-" + String.format("%02d", i))) {
                holidays++;
            }
        }
        return holidays;
    }

    public static void main(String[] args) throws ParseException {
        String startTime = "2022-10-01";
        int i = DateUtil.getDaysOfMonth(startTime) - 23 - dqyisHolidays(startTime);
        if (i < 0) {
        } else {
        }
        System.out.println(i);
    }

    /**
     * 设置年份
     *
     * @param year 年
     */
    private static void setTotalSet(Object year) {
        String strYear = year + "";
        if (!YEAR_SET.contains(strYear)) {
            YEAR_SET.add(strYear);
            for (int i = 1; i < 13; ) {
                loop(strYear, i + "");
                i += 2;
            }
        }
    }

    private static void loop(String year, String month) {
        try {
            String s = HttpUtil.get("https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=" + year + "%E5%B9%B4"
                    + month + "%E6%9C%88&co=&resource_id=39043&t=1617089428269&ie=utf8&oe=gbk"
                    + "&cb=op_aladdin_callback&format=json&tn=wisetpl&cb=jQuery110203576901702188473_1617089118772&_=1617089118776");
            s = s.substring(s.indexOf("("));
            s = s.substring(1, s.length() - 2);
            JSONObject json = JSON.parseObject(s);
            List<Map> list = JSON.parseArray(json.get("data").toString(), Map.class);
            List<Map> almanac = JSON.parseArray(list.get(0).get("almanac").toString(), Map.class);
            if (almanac == null || almanac.isEmpty()) {
                return;
            }
            almanac.parallelStream().forEach(value -> {
                int monthInt = Integer.parseInt((String) value.get("month"));
                int yearInt = Integer.parseInt((String) value.get("year"));
                int day = Integer.parseInt((String) value.get("day"));
                LocalDate now = LocalDate.of(yearInt, monthInt, day);
                String status = (String) value.get("status");
                String cnDay = (String) value.get("cnDay");
                if ("1".equals(status)) {
                    TOTAL_SET.add(now.toString());
                } else {
                    if ((!"2".equals(status)) && ("日".equals(cnDay) || "六".equals(cnDay))) {
                        TOTAL_SET.add(now.toString());
                        // HOLIDAY_SET.add(now.toString());
                    }
                }
            });
        } catch (Exception e) {
            return;
        }
    }
}
