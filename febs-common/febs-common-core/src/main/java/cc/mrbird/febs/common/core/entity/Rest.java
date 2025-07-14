package cc.mrbird.febs.common.core.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ZNKJ-R
 * @version V1.0
 * @date 2022/1/21 16:18
 */
@Data
public class Rest<T> {
    @ApiModelProperty(value = "编号")
    private int state;
    @ApiModelProperty(value = "返回对象")
    private T data;
//    @ApiModelProperty(value = "返回对象")
//    private T row;
//    @ApiModelProperty(value = "返回总条数")
//    private Long total;


    public static <T> Rest<T> message(T message) {
        Rest<T> rest = new Rest<>();
        rest.state = 0;
        rest.data = message;
        return rest;
    }

    public static <T> Rest<T> data(T message) {
        Rest<T> rest = new Rest<>();
        rest.state = 0;
        rest.data = message;
        return rest;
    }
    public static <T> Rest<List<T>> data(IPage<T> page) {
        Rest<List<T>> rest = new Rest<>();
//        rest.total = page.getTotal();
//        rest.row = page.getRecords();
        return rest;
    }
}
