package cc.mrbird.febs.common.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/5/12 11:13
 */
@Data
public class OrgStructureDto {
    /**
     * 组织结构ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Long id;
    /**
     * 父id
     */
    @TableField("PARENT_ID")
    @ApiModelProperty("父id")
    private Long parentId;
    /**
     * 名称
     */
    @TableField("NAME")
    @ApiModelProperty("名称")
    @NotNull(message = "名称不能为空")
    private String label;
    @TableField(exist = false)
    @ApiModelProperty("组织项目信息")
    private List<OrgStructureDto> children;
    private List<ProjectDto> projects;
}
