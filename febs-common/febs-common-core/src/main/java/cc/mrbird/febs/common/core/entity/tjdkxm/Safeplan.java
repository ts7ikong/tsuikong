package cc.mrbird.febs.common.core.entity.tjdkxm;

import cc.mrbird.febs.common.core.entity.tjdkxm.model.UserMode;
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
import java.util.List;

@Data
@TableName(value = "p_safeplan", autoResultMap = true)
@ApiModel("安全检查计划")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Safeplan implements Serializable {
    private static final long serialVersionUID = 116383328597004300L;

    /**
     * 安全检查计划表ID
     */
    @TableId(value = "SAFEPLAN_ID", type = IdType.AUTO)
    @ApiModelProperty("安全检查计划表ID")
    private Long safeplanId;

    /**
     * 对应项目ID
     */
    @TableField("SAFEPLAN_PROJECTID")
    @ApiModelProperty("对应项目ID")
    private Long safeplanProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;
    /**
     * 单位项目ID
     */
    @TableField("SAFEPLAN_UNITENGINEID")
    @ApiModelProperty("单位项目ID")
    private Long safeplanUnitengineid;

    @ApiModelProperty("单位项目名称")
    @TableField(exist = false)
    private String unitengineName;
    /**
     * 检查现场图片 ',' 分割 多个
     */
    @TableField("SAFEPLAN_CHECKPICTURES")
    @ApiModelProperty("检查现场图片 ',' 分割 多个")
    private String safeplanCheckpictures;
    /**
     * 检查备注
     */
    @TableField("SAFEPLAN_CHECKREMARK")
    @ApiModelProperty("检查备注")
    private String safeplanCheckremark;

    /**
     * 分部id
     */
    @TableField("SAFEPLAN_PARCELID")
    @ApiModelProperty("分部id")
    private Long safeplanParcelid;
    @TableField(exist = false)
    @ApiModelProperty("分部 name")
    private String parcelName;

    /**
     * 分项id
     */
    @TableField("SAFEPLAN_SUBITEMID")
    @ApiModelProperty("分项id")
    private Long safeplanSubitemid;
    @TableField(exist = false)
    @ApiModelProperty("分项 name")
    private String subitemName;

    /**
     * 计划检查日期
     */
    @TableField("SAFEPLAN_CHECKTIME")
    @ApiModelProperty("计划检查日期")
    @NotNull(message = "计划检查日期不能为空")
    private Date safeplanChecktime;

    /**
     * 检查类型tudo
     */
    @TableField("SAFEPLAN_CHECKTYPE")
    @ApiModelProperty("检查类型tudo")
    private String safeplanChecktype;
    /**
     * 检查人内容
     */
    @TableField("SAFEPLAN_CONTENT")
    @ApiModelProperty("检查人内容")
    private String safeplanContent;

    /**
     * 检查人用户ID
     */
    @ApiModelProperty("检查人用户ID ','分割")
    @TableField(value = "SAFEPLAN_CHECKUSERID")
    private String safeplanCheckuserid;

    /**
     * 验收人用户ID
     */
    @ApiModelProperty("验收人用户ID ','分割")
    @TableField(value = "SAFEPLAN_ACCEPTANCEUSERID")
    private String safeplanAcceptanceuserid;

    /**
     * 验收人姓名
     */
    @TableField(exist = false)
    @ApiModelProperty("验收人姓名 查询用")
    private String safeplanAcceptanceusername;

    /**
     * 检查人用户ID
     */
    @TableField("SAFEPLAN_CHECKFOREIGNUSER")
    @ApiModelProperty("检查人 外来人员 ”,“分割")
    private String safeplanCheckforeignuser;
    /**
     * 检查人用户ID
     */
    @TableField("SAFEPLAN_CHECKIMG")
    @ApiModelProperty("检查实施图片 ”，“分割")
    private String safeplanCheckimg;
    /**
     * 实际检查时间
     */
    @TableField("SAFEPLAN_ACTUALTIME")
    @ApiModelProperty("实际检查时间")
    private Date safeplanActualtime;

    /**
     * 检查实施文件
     */
    @TableField(value = "SAFEPLAN_CHECKFILE", typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("检查实施文件 [{name:'',addr:''}]")
    private Object safeplanCheckfile;
    /**
     * 状态 0 代实施 1 待验收 2 验收通过 3 验收不通过
     */
    @TableField("SAFEPLAN_CHECKSTATE")
    @ApiModelProperty("状态 0 代实施 1 待验收【谁创建 谁验收】 2 验收通过 3 验收不通过 ")
    private String safeplanCheckstate;
    /**
     * 检查人用户信息
     */
    @TableField(exist = false)
    @ApiModelProperty("检查人用户信息")
    private List<UserMode> safeplanCheckUserInfo;
    /**
     * 检查人姓名
     */
    @TableField(exist = false)
    @ApiModelProperty("检查人用户姓名 查询用")
    private String safeplanCheckUsername;

    /**
     * 备注
     */
    @TableField("SAFEPLAN_REMARK")
    @ApiModelProperty("备注")
    private String safeplanRemark;

    /**
     * 制定人用户ID
     */
    @TableField("SAFEPLAN_MAKERUSERID")
    @ApiModelProperty("制定人用户ID")
    private Long safeplanMakeruserid;

    @TableField(exist = false)
    @ApiModelProperty("制定人用户姓名")
    private String safeplanMakerusername;

    /**
     * 制定日期
     */
    @TableField("SAFEPLAN_MAKERTIME")
    @ApiModelProperty("制定日期")
    private Date safeplanMakertime;
    /**
     * 验收说明
     */
    @TableField("SAFEPLAN_ACCEPTINFO")
    @ApiModelProperty("验收说明")
    private String safeplanAcceptinfo;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

    /**
     * 是否就地整改
     */
    @TableField("SAFEPLAN_ISLOCAL")
    @ApiModelProperty("是否就地整改")
    private String safeplanIslocal;

    /**
     * 就地整改人ID
     */
    @TableField("SAFEPLAN_LOCALUSERID")
    @ApiModelProperty("就地整改人ID")
    private Long safeplanLocaluserid;

    /**
     * 就地整改人姓名
     */
    @TableField(exist = false)
    @ApiModelProperty("就地整改人姓名")
    private String safeplanLocalusername;

    /**
     * 就地整改时间
     */
    @TableField("SAFEPLAN_LOCALTIME")
    @ApiModelProperty("就地整改时间")
    private Date safeplanLocaltime;

    /**
     * 就地整改说明
     */
    @TableField("SAFEPLAN_LOCALINFO")
    @ApiModelProperty("就地整改说明")
    private String safeplanLocalinfo;

    /**
     * 就地整改整改后影像
     */
    @TableField("SAFEPLAN_LOCALIMG")
    @ApiModelProperty("就地整改整改后影像")
    private String safeplanLocalimg;

    @Data
    @ApiModel("安全检查计划查询")
    public static class Params {
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
        /**
         * 检查类型tudo
         */
        @TableField("SAFEPLAN_CHECKTYPE")
        @ApiModelProperty("检查类型tudo")
        private String safeplanChecktype;
        /**
         * 状态 0 代实施 1 待验收 2 验收通过 3 验收不通过
         */
        @TableField("SAFEPLAN_CHECKSTATE")
        @ApiModelProperty("状态 0 代实施 1 待验收【谁创建 谁验收】 2 验收通过 3 验收不通过 ")
        private String safeplanCheckstate;
        /**
         * 检查人姓名
         */
        @TableField(exist = false)
        @ApiModelProperty("检查人用户姓名 查询用")
        private String safeplanCheckUsername;
        @TableField(exist = false)
        @ApiModelProperty("查询用")
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
        if (obj instanceof Safeplan) {
            Safeplan safeplan = (Safeplan)obj;
            jsonObject.put("计划检查日期", safeplan.getSafeplanChecktime());
            jsonObject.put("检查类型", getType(safeplan.getSafeplanChecktype()));
            jsonObject.put("检查人ID", safeplan.getSafeplanCheckuserid());
            jsonObject.put("检查人外来人员", safeplan.getSafeplanCheckforeignuser());
            jsonObject.put("备注", safeplan.getSafeplanRemark());
            jsonObject.put("状态", getState(safeplan.getSafeplanCheckstate()));
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
