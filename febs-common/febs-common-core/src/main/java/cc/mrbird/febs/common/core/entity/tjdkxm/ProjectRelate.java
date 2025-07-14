package cc.mrbird.febs.common.core.entity.tjdkxm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目单位信息
 *
 * @author 14059
 * @version V1.0
 * @date 2022/4/15 15:29
 */
@Data
@TableName("p_project_relate")
@ApiModel("项目单位信息")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class ProjectRelate {
    /**
     * 项目id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    @ApiModelProperty("项目id")
    private Long id;
    /**
     * 项目编码
     */
    @TableField("TABLE_ID")
    @ApiModelProperty("对应项目id")
    private String tableId;
    /**
     * 项目编码
     */
    @TableField("NAME")
    @ApiModelProperty("'单位名称'")
    private String name;
    /**
     * 项目编码
     */
    @TableField("PERSON")
    @ApiModelProperty("'单位负责人'")
    private String person;
    /**
     * 项目编码
     */
    @TableField("LINK")
    @ApiModelProperty("'单位电话'")
    private String link;
    /**
     * 项目编码
     */
    @TableField("TYPE")
    @ApiModelProperty("建设单位 1\\r\\n施工单位 2\\r\\n监理单位 3\\r\\n设计单位 4\\r\\n勘察单位 5\\r\\n审计单位 6\\r\\n")
    private String type;
}
