package cc.mrbird.febs.common.core.entity.tjdkxm;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
@TableName(value = "p_learnrecord",autoResultMap = true)
@ApiModel("用户党建学习记录表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class LearnRecord implements Serializable {

    /**
     * 用户党建学习记录id
     */
    @TableId(value = "LEARNRECORD_ID", type = IdType.AUTO)
    @ApiModelProperty("党建学习id")
    private Long learnrecordId;
    /**
     * 党建学习id
     */
    @TableField(value = "PARTYLEARN_ID")
    @ApiModelProperty("党建学习id")
    private Long partylearnId;


    /**
     * 对应项目ID
     */
    @TableField("LEARNRECORD_PROJECTID")
    @ApiModelProperty("对应项目ID")
    private Long learnrecordProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;

    /**
     * 用户id
     */
    @TableField("LEARNRECORD_USERID")
    @ApiModelProperty("用户id")
    private Long learnrecordUserid;

    /**
     * 用户名
     */
    @TableField(exist = false)
    @ApiModelProperty("用户名")
    private String learnrecordUsername;
    /**
     * 用户姓名
     */
    @TableField(exist = false)
    @ApiModelProperty("用户姓名")
    private String learnrecordUserRealname;
    /**
     * 用户头像
     */
    @TableField(exist = false)
    @ApiModelProperty("用户头像")
    private String learnrecordUserAvatar;


    /**
     * 附件 [{"name":"","addr":""}]
     */
    @TableField(value = "LEARNRECORD_ANNX",typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("附件 [{\"name\":\"\",\"addr\":\"\"}]")
    private Object learnrecordAnnx;


    /**
     * 图片 “,”分割
     */
    @TableField("LEARNRECORD_ADDR")
    @ApiModelProperty("图片 “,”分割")
    private String learnrecordAddr;

    /**
     * 留言
     */
    @TableField("LEARNRECORD_MSG")
    @ApiModelProperty("留言")
    private String learnrecordMsg;
    /**
     * 留言
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty("创建时间")
    @JsonIgnore
    private Date createTime;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;


}
