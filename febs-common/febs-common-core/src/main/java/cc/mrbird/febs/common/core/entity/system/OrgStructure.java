package cc.mrbird.febs.common.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 组织结构
 *
 * @author 14059
 * @version V1.0
 * @date 2022/5/6 14:46
 */
@Data
@TableName("p_org_structure")
@ApiModel(value = "组织结构")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class OrgStructure implements Serializable {
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
    private String name;
    /**
     * 地址
     */
    @TableField("ADDR")
    @ApiModelProperty("地址")
    private String addr;
    /**
     * 说明
     */
    @TableField("EXPLAIN")
    @ApiModelProperty("说明")
    private String explain;
    /**
     * 扩展信息
     */
    @TableField("EXTEND_INFO")
    @ApiModelProperty("扩展信息")
    private Object extendInfo;
    /**
     * 创建人
     */
    @TableField("CREATE_USERID")
    @ApiModelProperty("创建人")
    @JsonIgnore
    private Long createUserid;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty("创建时间")
    @JsonIgnore
    private Date createTime;
    /**
     * 是否删除 1 删除 0 未删除
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否删除 1 删除 0 未删除")
    @JsonIgnore
    private Integer isDelete;
    @TableField(exist = false)
    @ApiModelProperty("组织项目信息")
    private List<OrgStructureProject> projects;

    @Data
    @ApiModel("查询")
    public static class Params {
        /**
         * 名称
         */
        @TableField("NAME")
        @ApiModelProperty("名称")
        private String name;
    }
}
