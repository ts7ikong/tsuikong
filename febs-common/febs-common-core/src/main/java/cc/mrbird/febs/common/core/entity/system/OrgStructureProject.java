package cc.mrbird.febs.common.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/5/12 11:04
 */
@Data
@TableName("p_org_structure_project")
@ApiModel(value = "组织结构-项目")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class OrgStructureProject implements Serializable {
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField(value = "TABLE_ID")
    private Long tableId;
    @TableField(value = "PROJECT_ID")
    private Long projectId;
    @TableField(exist = false)
    private String projectName;

}
