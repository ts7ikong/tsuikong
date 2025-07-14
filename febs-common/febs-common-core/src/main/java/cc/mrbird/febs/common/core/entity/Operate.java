package cc.mrbird.febs.common.core.entity;

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

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志类
 *
 * @author 14059
 * @version V1.0
 * @date 2022/3/30 18:39
 */
@Data
@TableName(value = "p_operate", autoResultMap = true)
@ApiModel("操作日志日志表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Operate implements Serializable {
    /**
     * 操作状态 成功
     */
    public static final String STATE_SUCCESS = "成功";
    /**
     * 操作状态 成功
     */
    public static final String STATE_FAIL = "失败";
    /**
     * 操作类型 新增
     */
    public static final String TYPE_ADD = "新增";
    /**
     * 操作类型 修改
     */
    public static final String TYPE_MODIFY = "修改";
    /**
     * 操作类型 审批
     */
    public static final String TYPE_APPROVAL = "审批";
    /**
     * 操作类型 删除
     */
    public static final String TYPE_DELETE = "删除";
    /**
     * 操作类型 分配
     */
    public static final String TYPE_DISTRIBUTE = "分配";
    /**
     * 操作类型 整改
     */
    public static final String TYPE_RECTIFICATION = "整改";
    /**
     * 操作类型 验收
     */
    public static final String TYPE_ACCEPTANCE = "验收";
    /**
     * 操作类型 实施
     */
    public static final String TYPE_CHECK = "实施";

    @TableId(value = "OPERATE_ID", type = IdType.AUTO)
    @ApiModelProperty("操作日志日志ID")
    private Long operateId;
    @TableField("OPERATE_TABLEID")
    @ApiModelProperty("对应表中的id")
    private Long operateTableid;
    @TableField("OPERATE_USERID")
    @ApiModelProperty("操作用户id")
    private Long operateUserid;
    @TableField(exist = false)
    @ApiModelProperty("操作用户姓名")
    private String operateUserName;
    @TableField("OPERATE_STATE")
    @ApiModelProperty("操作状态 成功 失败")
    private String operateState;
    @TableField("OPERATE_FAILMSG")
    @ApiModelProperty("失败原因")
    private String operateFailmsg;
    @TableField(value = "OPERATE_BEFORE", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("操作前内容")
    private Object operateBefore;
    @TableField(value = "OPERATE_AFTER", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("操作后内容")
    private Object operateAfter;
    @TableField("OPERATE_TYPE")
    @ApiModelProperty("操作类型")
    private String operateType;
    @TableField("OPERATE_PROJECTID")
    @ApiModelProperty("项目id")
    private Long operateProjectid;
    @TableField(exist = false)
    @ApiModelProperty("项目名称")
    private String operateProjectName;
    @TableField("OPERATE_MENU")
    @ApiModelProperty("菜单信息")
    private String operateMenu;
    @TableField("OPERATE_TIME")
    @ApiModelProperty("操作时间")
    private Date operateTime;
    @TableField(exist = false)
    @ApiModelProperty("操作时间 查询 开始")
    private String startOperateTime;
    @TableField(exist = false)
    @ApiModelProperty("操作时间 查询 结束")
    private String endOperateTime;
    @TableField("IS_DELETE")
    @ApiModelProperty("是否删除")
    @JsonIgnore
    private String isDelete;
}
