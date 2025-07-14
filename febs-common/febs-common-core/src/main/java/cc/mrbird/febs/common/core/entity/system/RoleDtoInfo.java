package cc.mrbird.febs.common.core.entity.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/5/12 17:15
 */
@Data
public class RoleDtoInfo {
    @ApiModelProperty(value = "类型 1 公共 2 项目")
    private Integer type;

    private List<RoleDtoInfo2> projects;

    @Data
    @ApiModel("参数")
    public static class Params {
        @ApiModelProperty(value = "类型 1 公共 2 项目")
        private Integer type;

        private List<RoleDtoInfo2.Params> projects;
    }
}
