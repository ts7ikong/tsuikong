package cc.mrbird.febs.common.core.utils;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.query.MPJQueryWrapper;
import org.apache.commons.lang3.StringUtils;

/**
 * 排序utils
 *
 * @author ZNKJ-R
 * @version V1.0
 * @date 2022/1/17 11:18
 */
public class OrderUtils {
    /**
     * 驼峰式的实体类属性名转换为数据表字段名
     * @param camelCaseStr  驼峰式的实体类属性名
     * @return  转换后的以"_"分隔的数据表字段名
     */
    private static String decamelize(String camelCaseStr){
        return StringUtils.isBlank(camelCaseStr) ? camelCaseStr : camelCaseStr.replaceAll("[A-Z]", "_$0").toUpperCase();
    }


    /**
     * QueryWrapper添加排序信息
     *
     * @param query
     * @param queryRequest
     */
    public static void setQuseryOrder(QueryWrapper query, QueryRequest queryRequest) {
        if (StringUtils.isNotBlank(queryRequest.getField())) {
            if (StringUtils.isNotBlank(queryRequest.getOrder())) {
                query.orderBy(true, queryRequest.getOrder().startsWith("asc"),
                        decamelize(queryRequest.getField()).split(","));
            } else {
                query.orderByAsc(decamelize(queryRequest.getField()).split(","));
            }
        }
    }
    /**
     * MPJQueryWrapper添加排序信息
     *
     * @param query
     * @param queryRequest
     */
    public static void setQuseryOrder(MPJQueryWrapper query, QueryRequest queryRequest) {
        if (StringUtils.isNotBlank(queryRequest.getField())){
            if (StringUtils.isNotBlank(queryRequest.getOrder())){
                query.orderBy(true,queryRequest.getOrder().startsWith("asc"),
                        decamelize(queryRequest.getField()).split(","));
            }else {
                query.orderByAsc(decamelize(queryRequest.getField()).split(","));
            }
        }
    }



}
