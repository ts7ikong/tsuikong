package cc.mrbird.febs.common.core.entity.tjdkxm;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.yulichang.annotation.EntityMapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Data
@TableName("p_project")
@ApiModel("项目信息")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Project implements Serializable {
    private static final long serialVersionUID = -39656887183565405L;

    /**
     * 项目id
     */
    @TableId(value = "PROJECT_ID", type = IdType.AUTO)
    @ApiModelProperty("项目id")
    private Long projectId;


    /**
     * 项目编码
     */
    @TableField("PROJECT_CODE")
    @ApiModelProperty("项目编码")
    private String projectCode;

    /**
     * 项目名称
     */
    @TableField("PROJECT_NAME")
    @ApiModelProperty("项目名称")
    @NotNull(message = "项目名称不能为空")
    private String projectName;

    /**
     * 项目对应的用户ID
     */
    @TableField("PROJECT_USERID")
    @ApiModelProperty("项目对应的用户ID")
    private Long projectUserid;
    /**
     * 项目对应的用户密码 前端md5加密
     */
    @TableField(exist = false)
    @ApiModelProperty("项目对应的用户密码 前端md5加密")
    @JsonIgnore
    private String password;


    /**
     * 项目对应的用户信息
     */
    @TableField(exist = false)
    @ApiModelProperty("项目对应的用户")
    private String projectUserName;


    /**
     * 项目工程类型（todo暂时未定）
     */
    @TableField("PROJECT_TYPE")
    @ApiModelProperty("项目工程类型（todo暂时未定）")
    @NotNull(message = "项目工程类型不能为空")
    private String projectType;

    /**
     * 项目地址
     */
    @TableField("PROJECT_ADDR")
    @ApiModelProperty("项目地址")
    private String projectAddr;

    /**
     * 经度
     */
    @TableField("PROJECT_LONGITUDE")
    @ApiModelProperty("经度")
    private Double projectLongitude;

    /**
     * 纬度
     */
    @TableField("PROJECT_LATITUDE")
    @ApiModelProperty("纬度")
    private Double projectLatitude;

    /**
     * 工程造价
     */
    @TableField("PROJECT_COST")
    @ApiModelProperty("工程造价")
    private Double projectCost;

    /**
     * 规模
     */
    @TableField("PROJECT_SCALE")
    @ApiModelProperty("规模")
    private String projectScale;

    /**
     * 负责人
     */
    @TableField("PROJECT_PERSON")
    @ApiModelProperty("项目负责人")
    private String projectPerson;

    /**
     * 负责人联系方式
     */
    @TableField("PROJECT_LINK")
    @ApiModelProperty("项目负责人联系方式")
    private String projectLink;

    /**
     * 项目描述
     */
    @TableField("PROJECT_BASUA")
    @ApiModelProperty("项目描述")
    private String projectBasua;

    /**
     * 项目施工进度
     */
    @TableField("PROJECT_PROGRESS")
    @ApiModelProperty("项目施工进度 （%）")
    private String projectProgress;
    /**
     * 项目施工进度
     */
    @TableField("PROJECT_SCHEDULE")
    @ApiModelProperty("项目进度 （%）")
    private String projectSchedule;

    /**
     * 行业主管部门
     */
    @TableField("PROJECT_CDEPT")
    @ApiModelProperty("行业主管部门")
    private String projectCdept;

    /**
     * 建设单位名称
     */
    @TableField("PROJECT_JSDWNAME")
    @ApiModelProperty("建设单位名称")
    private String projectJsdwname;

    /**
     * 建设单位负责人
     */
    @TableField("PROJECT_JSDWPERSON")
    @ApiModelProperty("建设单位负责人")
    private String projectJsdwperson;

    /**
     * 建设单位联系电话
     */
    @TableField("PROJECT_JSDWLINK")
    @ApiModelProperty("建设单位联系电话")
    private String projectJsdwlink;

    @TableField(exist = false)
    @ApiModelProperty("建设单位信息")
    private String jsdw;

    /**
     * 施工单位单位名称
     */
    @TableField("PROJECT_SGDWNAME")
    @ApiModelProperty("施工单位单位名称")
    private String projectSgdwname;

    /**
     * 施工单位负责人
     */
    @TableField("PROJECT_SGDWPERSON")
    @ApiModelProperty("施工单位负责人")
    private String projectSgdwperson;

    /**
     * 施工单位联系方式
     */
    @TableField("PROJECT_SGDWLINK")
    @ApiModelProperty("施工单位联系方式")
    private String projectSgdwlink;

    @TableField(exist = false)
    @ApiModelProperty("施工单位信息")
    private String sgdw;

    /**
     * 监理单位名称
     */
    @TableField("PROJECT_JLDWNAME")
    @ApiModelProperty("监理单位名称")
    private String projectJldwname;

    /**
     * 监理单位负责人
     */
    @TableField("PROJECT_JLDWPERSON")
    @ApiModelProperty("监理单位负责人")
    private String projectJldwperson;

    /**
     * 监理单位联系方式
     */
    @TableField("PROJECT_JLDWLINK")
    @ApiModelProperty("监理单位联系方式")
    private String projectJldwlink;

    @TableField(exist = false)
    @ApiModelProperty("监理单位信息")
    private String jldw;

    /**
     * 设计单位名称
     */
    @TableField("PROJECT_SJNAME")
    @ApiModelProperty("设计单位名称")
    private String projectSjname;

    /**
     * 设计单位负责人
     */
    @TableField("PROJECT_SJPERSON")
    @ApiModelProperty("设计单位负责人")
    private String projectSjperson;

    /**
     * 设计单位联系方式
     */
    @TableField("PROJECT_SJLINK")
    @ApiModelProperty("设计单位联系方式")
    private String projectSjlink;

    @TableField(exist = false)
    @ApiModelProperty("设计单位信息")
    private String sjdw;

    /**
     * 勘察单位名称
     */
    @TableField("PROJECT_KCNAME")
    @ApiModelProperty("勘察单位名称")
    private String projectKcname;

    /**
     * 勘察单位负责人
     */
    @TableField("PROJECT_KCPERSON")
    @ApiModelProperty("勘察单位负责人")
    private String projectKcperson;

    /**
     * 勘察单位联系方式
     */
    @TableField("PROJECT_KCLINK")
    @ApiModelProperty("勘察单位联系方式")
    private String projectKclink;

    @TableField(exist = false)
    @ApiModelProperty("设计单位信息")
    private String kcdw;

//    @TableField(exist = false)
//    @EntityMapping(joinField = "parcelProjectid")
//    @ApiModelProperty(hidden = true)
//    private List<Parcel> parcelList;

    /**
     * 项目开始时间
     */
    @TableField("PROJECT_STARTIME")
    @ApiModelProperty("项目开始时间")
    private Date projectStartime;
    /**
     * 项目截至时间
     */
    @TableField("PROJECT_ENDTIME")
    @ApiModelProperty("项目开始时间")
    private Date projectEndtime;
    /**
     * 工地人数
     */
    @TableField("PROJECT_PEOPLENUMBER")
    @ApiModelProperty("工地人数")
    private Integer projectPeoplenumber;
    /**
     * 工地人数
     */
    @TableField("PROJECT_PICTURES")
    @ApiModelProperty("项目图片  ‘，’ 分割")
    private String projectPictures;
    /**
     * 项目分包单位  ‘，’ 分割
     */
    @TableField("PROJECT_PARCELUNITID")
    @ApiModelProperty("项目分包单位  ‘，’ 分割")
    private String projectParcelunitid;
    /**
     * 项目分包单位  ‘，’ 分割
     */
    @TableField("PROJECT_PARCELUNITID")
    @ApiModelProperty("项目分包单位  ‘，’ 分割")
    private String projectParcelunitname;
    /**
     * 项目分包单位  ‘，’ 分割
     */
    @TableField(exist = false)
    @ApiModelProperty("项目分包单位信息 id 负责人 负责人电话 JSON")
    private List<Map<String, Object>> projectParceluniinfo;
    /**
     * 审计单位
     */
    @TableField("PROJECT_AUDIT")
    @ApiModelProperty("审计单位")
    private String projectAudit;
    /**
     * 审计单位
     */
    @TableField("PROJECT_AUDITPERSON")
    @ApiModelProperty("审计单位")
    private String projectAuditperson;
    /**
     * 审计单位
     */
    @TableField("PROJECT_AUDITLINK")
    @ApiModelProperty("审计单位")
    private String projectAuditlink;
    @TableField(exist = false)
    @ApiModelProperty("设计单位信息")
    private String auditdw;
    /**
     * 收款进度
     */
    @TableField("PROJECT_RECEIVABLE")
    @ApiModelProperty("收款进度--应收（万元）")
    private String projectReceivable;
    /**
     * 收款进度
     */
    @TableField("PROJECT_RECEIVED")
    @ApiModelProperty("收款进度--已收（万元）")
    private String projectReceived;
    /**
     * 收款进度
     */
    @TableField("PROJECT_UNCOLLECTED")
    @ApiModelProperty("收款进度--未收（万元）")
    private String projectUncollected;


    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    private Integer isDelete;
    /**
     * 创建人
     */
    @TableField("CREATE_USERID")
//    @JsonIgnore
    private Long createUserid;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @JsonIgnore
    private Date createTime;

    @Data
    @ApiModel("项目查询")
    public static class Params {
        /**
         * 项目编码
         */
        @TableField("PROJECT_CODE")
        @ApiModelProperty("项目编码/名称/地址/负责人")
        private String projectCode;
        /**
         * 项目id
         */
        @TableId(value = "PROJECT_ID", type = IdType.AUTO)
        @ApiModelProperty("项目id")
        private Long projectId;
    }

    @Data
    @ApiModel("项目负责人")
    public static class UserModel implements Serializable{
        /**
         * 项目id
         */
        @TableId(value = "PROJECT_ID", type = IdType.AUTO)
        @ApiModelProperty("项目id")
        @NotNull(message = "请选择项目")
        private Long projectId;
        /**
         * 负责人
         */
        @TableField("PROJECT_PERSON")
        @ApiModelProperty("项目负责人")
        @NotNull(message = "请选择项目负责人")
        private String projectPerson;
        /**
         * 项目对应的用户ID
         */
        @TableField("PROJECT_USERID")
        @ApiModelProperty("项目对应的用户ID")
        @NotNull(message = "请选择项目负责人")
        private Long projectUserid;

        /**
         * 负责人联系方式
         */
        @TableField("PROJECT_LINK")
        @ApiModelProperty("项目负责人联系方式")
//        @NotNull(message = "请选择项目负责人联系方式")
        private String projectLink;
    }

}
