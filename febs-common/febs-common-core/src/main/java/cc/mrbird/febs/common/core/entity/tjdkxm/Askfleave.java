package cc.mrbird.febs.common.core.entity.tjdkxm;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

@Data
@TableName("p_askfleave")
@ApiModel("请假申请审批表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Askfleave implements Serializable {
    private static final long serialVersionUID = 764582240061672182L;

    /**
     * 请假申请审批表ID
     */
    @TableId(value = "ASKFLEAVE_ID", type = IdType.AUTO)
    @ApiModelProperty("请假申请审批表ID")
    private Long askfleaveId;

    /**
     * 对应项目ID
     */
    @TableField("ASKFLEAVE_PROJECTID")
    @ApiModelProperty("对应项目ID")
    private Long askfleaveProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;

    /**
     * 请假原因
     */
    @TableField("ASKFLEAVE_CAUSE")
    @ApiModelProperty("请假原因")
    @NotNull(message = "请假原因不能为空")
    private String askfleaveCause;

    /**
     * 请假开始时间
     */
    @TableField("ASKFLEAVE_STARTTIME")
    @ApiModelProperty("请假开始时间")
    @NotNull(message = "请假开始时间")
    private Date askfleaveStarttime;

    /**
     * 请假结束时间
     */
    @TableField("ASKFLEAVE_ENDTIME")
    @ApiModelProperty("请假结束时间")
    @NotNull(message = "请假结束时间")
    private Date askfleaveEndtime;

    /**
     * 请假申请时间
     */
    @TableField("ASKFLEAVE_CREATETIME")
    @ApiModelProperty("请假申请时间")
    private Date askfleaveCreatetime;
    /**
     * 请假申请时间 start
     */
    @TableField(exist = false)
    @ApiModelProperty("请假申请时间 start")
    private String startCreateTime;
    /**
     * 请假申请时间 end
     */
    @TableField(exist = false)
    @ApiModelProperty("请假申请时间 end")
    private String endCreateTime;

    /**
     * 请假申请用户ID
     */
    @TableField("ASKFLEAVE_CREATEUSERID")
    @ApiModelProperty("请假申请用户ID")
    private Long askfleaveCreateuserid;
    /**
     * 请假申请用户ID
     */
    @TableField(exist = false)
    @ApiModelProperty("请假申请用户姓名")
    private String askfleaveCreateusername;

    /**
     * 请假审批时间
     */
    @TableField("ASKFLEAVE_CHECKTIME")
    @ApiModelProperty("请假审批时间")
    private String askfleaveChecktime;

    /**
     * 请假审批用户ID
     */
    @TableField("ASKFLEAVE_CHECKUSERID")
    @ApiModelProperty("请假审批用户ID")
    private String askfleaveCheckuserid;
    /**
     * 请假审批用户ID
     */
    @TableField(exist = false)
    @ApiModelProperty("请假审批用户姓名")
    private String askfleaveCheckusername;

    /**
     * 请假审批状态 0待审批1已通过2未通过
     */
    @TableField("ASKFLEAVE_CHECKSTATE")
    @ApiModelProperty("请假审批状态 0待审批1已通过2未通过")
    private String askfleaveCheckstate;

    /**
     * 请假审批未通过原因
     */
    @TableField("ASKFLEAVE_CHECKREASON")
    @ApiModelProperty("请假审批未通过原因")
    private String askfleaveCheckreason;

    @TableField(exist = false)
    @ApiModelProperty("是否能修改删除 0 不能,1 可以删除")
    private Integer modify;

    @TableField(exist = false)
    @ApiModelProperty("我的任务跳转传 1")
    private String task;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1 撤销2")
    @JsonIgnore
    private Integer isDelete;

    /**
     * 请假审批节点 0审批结束1下一审批
     */
    @TableField("ASKFLEAVE_CHECKNODE")
    @ApiModelProperty("请假审批节点")
    private String askfleaveChecknode;

    /**
     * 请假审批记录
     */
    @TableField("ASKFLEAVE_CHECKRECORD")
    @ApiModelProperty("请假审批记录")
    private String askfleaveCheckrecord;

}
