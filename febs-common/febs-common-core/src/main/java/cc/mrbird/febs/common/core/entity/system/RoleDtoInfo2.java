package cc.mrbird.febs.common.core.entity.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/5/12 17:15
 */
@Data
public class RoleDtoInfo2 {
    private String ids;
    @ApiModelProperty(value = "菜单ids")
    private String menuIds;
    @ApiModelProperty(value = "菜单名称")
    private String menuNames;
    @ApiModelProperty(value = "按钮ids")
    private String buttonIds;
    @ApiModelProperty(value = "项目id")
    private Long projectId;
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    private Long roleId;

    @Data
    @ApiModel("参数")
    public static class Params {
        @ApiModelProperty(value = "菜单按钮ids")
        private String ids;
        @ApiModelProperty(value = "项目id")
        private Long projectId;
    }
}
