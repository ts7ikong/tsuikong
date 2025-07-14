package cc.mrbird.febs.common.core.entity.tjdkxm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/3/28 18:22
 */
@Data
@TableName("p_documentclass")
@ApiModel("资料分类")
public class DocumentClass implements Serializable {

    /**
     * 资料分类id
     */
    @TableId(value = "DOCUMENTCLASS_ID", type = IdType.AUTO)
    @ApiModelProperty("资料分类id")
    private Long documentclassId;

    /**
     * 资料分类父ID
     */
    @TableField("DOCUMENTCLASS_PID")
    @ApiModelProperty("资料分类父ID ")
    @NotNull(message = "资料分类大类不能为空")
    private Long documentclassPid;
    /**
     * 资料分类父ID
     */
    @TableField(exist = false)
    @ApiModelProperty("子类 ")
    private List<DocumentClass> documentclassChildren;

    /**
     * 资料分类父ID
     */
    @TableField("DOCUMENTCLASS_PROJECTID")
    @ApiModelProperty("资料分类项目ID ")
    private Long documentclassProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;

    /**
     * 资料名称
     */
    @TableField("DOCUMENTCLASS_NAME")
    @ApiModelProperty("资料分类名称")
    @NotNull(message = "资料分类名称不能为空")
    private String documentclassName;

    /**
     * 菜单信息
     */
    @TableField("DOCUMENTCLASS_MENU")
    @ApiModelProperty("菜单信息")
    private String documentclassMenu;
    /**
     * 菜单信息
     */
    @TableField("DOCUMENTCLASS_TIME")
    @ApiModelProperty("菜单时间")
    private Date documentclassTime;
    /**
     * 菜单信息
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("菜单时间")
    @JsonIgnore
    private Integer isDelete;

    @Data
    @ApiModel("新增")
    public static class Add {
        /**
         * 资料分类id
         */
        @TableId(value = "DOCUMENTCLASS_ID", type = IdType.AUTO)
        @ApiModelProperty("资料分类id")
        private Long documentclassId;
        /**
         * 资料分类父ID
         */
        @TableField("DOCUMENTCLASS_PID")
        @ApiModelProperty("资料分类父ID ")
        @NotNull(message = "资料分类大类不能为空")
        private Long documentclassPid;
        /**
         * 资料名称
         */
        @TableField("DOCUMENTCLASS_NAME")
        @ApiModelProperty("资料分类名称")
        @NotNull(message = "资料分类名称不能为空")
        private String documentclassName;
    }
}
