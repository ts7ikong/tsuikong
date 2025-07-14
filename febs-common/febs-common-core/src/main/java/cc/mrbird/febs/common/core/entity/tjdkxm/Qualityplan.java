package cc.mrbird.febs.common.core.entity.tjdkxm;

import com.alibaba.fastjson.JSONObject;
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
@TableName("p_qualityplan")
@ApiModel("质量检查计划")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Qualityplan implements Serializable {
    private static final long serialVersionUID = -36302159815778146L;

    /**
     * 质量检查计划表
     */
    @TableId(value = "QUALITYPLAN_ID", type = IdType.AUTO)
    @ApiModelProperty("质量检查计划表")
    private Long qualityplanId;

    /**
     * 对应项目ID
     */
    @TableField("QUALITYPLAN_PROJECTID")
    @ApiModelProperty("对应项目ID")
    private Long qualityplanProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;

    /**
     * 单位项目id
     */
    @TableField("QUALITYPLAN_UNITENGINEID")
    @ApiModelProperty("单位项目id")
    private Long qualityplanUnitengineid;
    /**
     * 单位项目name
     */
    @TableField(exist = false)
    @ApiModelProperty("单位项目name")
    private String unitengineName;
    /**
     * 分部id
     */
    @TableField("QUALITYPLAN_PARCELID")
    @ApiModelProperty("分部id")
    private Long qualityplanParcelid;
    @TableField(exist = false)
    @ApiModelProperty("分部 name")
    private String parcelName;

    /**
     * 分项id
     */
    @TableField("QUALITYPLAN_SUBITEMID")
    @ApiModelProperty("分项id")
    private Long qualityplanSubitemid;
    @TableField(exist = false)
    @ApiModelProperty("分项 name")
    private String subitemName;

    /**
     * 计划检查日期
     */
    @TableField("QUALITYPLAN_CHECKTIME")
    @ApiModelProperty("计划检查日期")
    @NotNull(message = "计划检查日期不能为空")
    private Date qualityplanChecktime;

    /**
     * 检查类型tudo
     */
    @TableField("QUALITYPLAN_CHECKTYPE")
    @ApiModelProperty("检查类型tudo")
    @NotNull(message = "检查类型不能为空")
    private String qualityplanChecktype;
    /**
     * 检查现场图片 ',' 分割 多个
     */
    @TableField("QUALITYPLAN_CHECKPICTURES")
    @ApiModelProperty("检查现场图片 ',' 分割 多个")
    private String qualityplanCheckpictures;
    /**
     * 检查备注
     */
    @TableField("QUALITYPLAN_CHECKREMARK")
    @ApiModelProperty("检查备注")
    private String qualityplanCheckremark;

    /**
     * 检查人内容
     */
    @TableField("QUALITYPLAN_CONTENT")
    @ApiModelProperty("检查人内容")
    private String qualityplanContent;
    /**
     * 检查人用户ID
     */
    @TableField("QUALITYPLAN_CHECKUSERID")
    @ApiModelProperty("检查人用户ID")
    private String qualityplanCheckuserid;
    /**
     * 检查人用户ID
     */
    @TableField("QUALITYPLAN_CHECKFOREIGNUSER")
    @ApiModelProperty("检查人 外来人员 ”,“分割")
    private String qualityplanCheckforeignuser;

    /**
     * 检查人用户ID
     */
    @TableField("QUALITYPLAN_CHECKIMG")
    @ApiModelProperty("检查实施图片 ”，“分割")
    private String qualityplanCheckimg;
    /**
     * 实际检查时间
     */
    @TableField("QUALITYPLAN_ACTUALTIME")
    @ApiModelProperty("实际检查时间")
    private Date qualityplanActualtime;

    /**
     * 检查实施文件
     */
    @TableField(value = "QUALITYPLAN_CHECKFILE", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("检查实施文件 [{name:'',addr:''},{}]")
    private Object qualityplanCheckfile;
    /**
     * 状态 0 代实施 1 待验收 2 验收通过 3 验收不通过
     */
    @TableField("QUALITYPLAN_CHECKSTATE")
    @ApiModelProperty("状态 0 代实施 1 待验收【谁创建 谁验收】 2 验收通过 3 验收不通过")
    private String qualityplanCheckstate;

    @TableField(exist = false)
    @ApiModelProperty("检查人用户姓名 查询用")
    private String qualityplanCheckusername;

    /**
     * 备注
     */
    @TableField("QUALITYPLAN_REMARK")
    @ApiModelProperty("备注")
    private String qualityplanRemark;

    /**
     * 制定人用户ID
     */
    @TableField("QUALITYPLAN_MAKERUSERID")
    @ApiModelProperty("制定人用户ID")
    private Long qualityplanMakeruserid;
    @TableField(exist = false)
    @ApiModelProperty("制定人用户姓名")
    private String qualityplanMakerusername;

    /**
     * 制定日期
     */
    @TableField("QUALITYPLAN_MAKERTIME")
    @ApiModelProperty("制定日期")
    private Date qualityplanMakertime;
    /**
     * 验收说明
     */
    @TableField("QUALITYPLAN_ACCEPTINFO")
    @ApiModelProperty("验收说明")
    private String qualityplanAcceptinfo;

    /**
     * 是否就地整改
     */
    @TableField("QUALITYPLAN_ISLOCAL")
    @ApiModelProperty("是否就地整改")
    private String qualityplanIslocal;

    /**
     * 就地整改人ID
     */
    @TableField("QUALITYPLAN_LOCALUSERID")
    @ApiModelProperty("就地整改人ID")
    private Long qualityplanLocaluserid;

    /**
     * 就地整改人姓名
     */
    @TableField(exist = false)
    @ApiModelProperty("就地整改人姓名")
    private String qualityplanLocalusername;

    /**
     * 就地整改时间
     */
    @TableField("QUALITYPLAN_LOCALTIME")
    @ApiModelProperty("就地整改时间")
    private Date qualityplanLocaltime;

    /**
     * 就地整改说明
     */
    @TableField("QUALITYPLAN_LOCALINFO")
    @ApiModelProperty("就地整改说明")
    private String qualityplanLocalinfo;

    /**
     * 就地整改整改后影像
     */
    @TableField("QUALITYPLAN_LOCALIMG")
    @ApiModelProperty("就地整改整改后影像")
    private String qualityplanLocalimg;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

    /**
     * 验收人用户ID
     */
    @ApiModelProperty("验收人用户ID ','分割")
    @TableField(value = "QUALITYPLAN_ACCEPTANCEUSERID")
    private String qualityplanAcceptanceuserid;

    /**
     * 验收人姓名
     */
    @TableField(exist = false)
    @ApiModelProperty("验收人姓名 查询用")
    private String qualityplanAcceptanceusername;

    @ApiModel("质量检查计划查询")
    @Data
    public static class Params {
        /**
         * 状态 0 代实施 1 待验收 2 验收通过 3 验收不通过
         */
        @TableField("QUALITYPLAN_CHECKSTATE")
        @ApiModelProperty("状态 0 代实施 1 待验收【谁创建 谁验收】 2 验收通过 3 验收不通过")
        private String qualityplanCheckstate;
        /**
         * 检查类型tudo
         */
        @TableField("QUALITYPLAN_CHECKTYPE")
        @ApiModelProperty("检查类型tudo")
        @NotNull(message = "检查类型不能为空")
        private String qualityplanChecktype;
        @TableField(exist = false)
        @ApiModelProperty("检查人用户姓名 查询用")
        private String qualityplanCheckusername;
        /**
         * 计划检查日期 start
         */
        @TableField(exist = false)
        @ApiModelProperty("计划检查日期start")
        private String startChecktime;
        /**
         * 计划检查日期 end
         */
        @TableField(exist = false)
        @ApiModelProperty("计划检查日期end")
        private String endChecktime;
        @TableField(exist = false)
        @ApiModelProperty("查询用 项目相关")
        private String pscName;
        @TableField(exist = false)
        @ApiModelProperty("我的任务跳转传 1")
        private String task;
        /**
         * 实际检查时间 start
         */
        @ApiModelProperty("实际检查时间 start")
        private String startActualtime;
        /**
         * 实际检查时间 end
         */
        @ApiModelProperty("实际检查时间 end")
        private String endActualtime;
    }

    public JSONObject getJSON(Object obj) {
        JSONObject jsonObject = new JSONObject();
        if (obj == null) {
            return null;
        }
        if (obj instanceof Qualityplan) {
            Qualityplan qualityplan = (Qualityplan)obj;
            jsonObject.put("计划检查日期", qualityplan.getQualityplanChecktime());
            jsonObject.put("检查类型", getType(qualityplan.getQualityplanChecktype()));
            jsonObject.put("检查人ID", qualityplan.getQualityplanCheckuserid());
            jsonObject.put("检查人外来人员", qualityplan.getQualityplanCheckforeignuser());
            jsonObject.put("备注", qualityplan.getQualityplanRemark());
            jsonObject.put("状态", getState(qualityplan.getQualityplanCheckstate()));
        }
        return jsonObject;
    }

    private String getState(String state) {
        // 0 代实施 1 待验收【谁创建 谁验收】 2 验收通过 3 验收不通过
        switch (state) {
            case "0":
                return "代实施";
            case "1":
                return "待验收";
            case "2":
                return "验收通过";
            case "3":
                return "验收不通过";
            default:
                return "其他";
        }
    }

    private String getType(String type) {
        switch (type) {
            case "1":
                return "日周月检查";
            case "2":
                return "专项检查";
            default:
                return "其他";
        }
    }
}
