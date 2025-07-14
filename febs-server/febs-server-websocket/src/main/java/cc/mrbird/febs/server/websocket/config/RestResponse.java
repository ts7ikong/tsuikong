package cc.mrbird.febs.server.websocket.config;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/19 16:32
 */
@Data
public class RestResponse<T> {
    @ApiModelProperty(value = "总条数")
    private Long total;
    @ApiModelProperty(value = "返回对象")
    private Collection<T> data;
    @ApiModelProperty(value = "状态")
    private int state;

    public RestResponse() {}

    public RestResponse(IPage<T> page) {
        this.total = page.getTotal();
        this.data = page.getRecords();
    }

    public RestResponse<T> data(IPage<T> page) {
        return new RestResponse<>(page);
    }
}
