package cc.mrbird.febs.common.core.entity.tjdkxm;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.yulichang.annotation.EntityMapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "p_qualityproblem", autoResultMap = true)
@ApiModel("质量问题清单")
// @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Qualityproblem implements Serializable {
    private static final long serialVersionUID = 731020811854535883L;

    /**
     * 质量问题id
     */
    @TableId(value = "QUALITYPROBLEN_ID", type = IdType.AUTO)
    @ApiModelProperty("质量问题id")
    private Long qualityproblenId;

    /**
     * 对应项目ID
     */
    @TableField("QUALITYPROBLEN_PROJECTID")
    @ApiModelProperty("对应项目ID")
    private Long qualityproblenProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;
    /**
     * 单位项目ID
     */
    @TableField("QUALITYPROBLEN_UNITENGINEID")
    @ApiModelProperty("单位项目ID")
    private Long qualityproblenUnitengineid;

    @ApiModelProperty("单位项目名称")
    @TableField(exist = false)
    private String unitengineName;
    /**
     * 对应项目ID
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目Code")
    private String qualityproblenProjectCode;

    /**
     * 分部id
     */
    @TableField("QUALITYPROBLEN_PARCELID")
    @ApiModelProperty("分部id")
    private Long qualityproblenParcelid;

    @TableField(exist = false)
    @ApiModelProperty("分部 name")
    private String parcelName;

    /**
     * 分项id
     */
    @TableField("QUALITYPROBLEN_SUBITEMID")
    @ApiModelProperty("分项id")
    private Long qualityproblenSubitemid;

    @TableField(exist = false)
    @ApiModelProperty("分项 name")
    private String subitemName;

    /**
     * 质量问题类型TUDO
     */
    @TableField("QUALITYPROBLEN_TYPE")
    @ApiModelProperty("质量问题类型TUDO")
    @NotNull(message = "质量问题类型不能为空")
    private String qualityproblenType;

    /**
     * 质量问题级别TUDO
     */
    @TableField("QUALITYPROBLEN_LEVEL")
    @ApiModelProperty("质量问题级别TUDO")
    @NotNull(message = "质量问题级别不能为空")
    private String qualityproblenLevel;

    /**
     * 质量问题说明
     */
    @TableField("QUALITYPROBLEN_EXPLAIN")
    @ApiModelProperty("质量问题说明")
    private String qualityproblenExplain;

    /**
     * 问题图片地址
     */
    @ApiModelProperty("问题图片地址 [{\"addr\": \"123456\", \"name\": \"123456\"}]")
    @TableField(value = "QUALITYPROBLEN_IMG", typeHandler = JacksonTypeHandler.class)
    private Object qualityproblenImg;

    /**
     * 检查人用户ID
     */
    @TableField("QUALITYPROBLEN_CHECKUSERID")
    @ApiModelProperty("检查人用户ID ',' 隔开")
    private String qualityproblenCheckuserid;
    @TableField(exist = false)
    @ApiModelProperty("检查人用户姓名 ',' 隔开")
    private String qualityproblenCheckusername;
    @TableField("QUALITYPROBLEN_CHECKFOREIGNUSER")
    @ApiModelProperty("检查人 外来人员 ”,“分割")
    private String qualityproblenCheckforeignuser;

    /**
     * 检查日期
     */
    @TableField("QUALITYPROBLEN_CHECKTIME")
    @ApiModelProperty("检查日期")
    @NotNull(message = "计划检查日期不能为空")
    private Date qualityproblenChecktime;

    /**
     * 分配整改人用户ID
     */
    @TableField("QUALITYPROBLEN_RECTIFYUSERID")
    @ApiModelProperty("分配整改人用户ID")
    private Long qualityproblenRectifyuserid;
    @TableField(exist = false)
    private String qualityproblenRectifyusername;

    /**
     * 整改期限
     */
    @TableField("QUALITYPROBLEN_RECTIFYTIME")
    @ApiModelProperty("整改期限")
    private Date qualityproblenRectifytime;

    /**
     * 整改要求
     */
    @TableField("QUALITYPROBLEN_RECTIFYFOR")
    @ApiModelProperty("整改要求")
    private String qualityproblenRectifyfor;

    /**
     * 实际整改日期
     */
    @TableField("QUALITYPROBLEN_RECTIFYACTIME")
    @ApiModelProperty("实际整改日期")

    private Date qualityproblenRectifyactime;

    /**
     * 整改后图片
     */
    @ApiModelProperty("整改后图片")
    @TableField(value = "QUALITYPROBLEN_RECTIFYIMG", typeHandler = JacksonTypeHandler.class)
    private Object qualityproblenRectifyimg;

    /**
     * 状态 1待分配 2待整改 3待验收 4验收合格 5验收不通过
     */
    @TableField("QUALITYPROBLEN_STATE")
    @ApiModelProperty("状态 1待分配 2待整改  3待验收 4验收合格 5验收不通过")
    private String qualityproblenState;

    /**
     * 验收人
     */
    @TableField("QUALITYPROBLEN_ACCEPTUSERID")
    @ApiModelProperty("验收人")
    private Long qualityproblenAcceptuserid;
    @TableField(exist = false)
    private String qualityproblenAcceptusername;

    /**
     * 验收时间
     */
    @TableField("QUALITYPROBLEN_ACCEPTTIME")
    @ApiModelProperty("验收时间")
    private Date qualityproblenAccepttime;
    /**
     * 验收说明
     */
    @TableField("QUALITYPROBLEN_ACCEPTINFO")
    @ApiModelProperty("验收说明")
    private String qualityproblenAcceptinfo;

    /**
     * 日志
     */
    @TableField(exist = false)
    @ApiModelProperty("操作日志")
    @EntityMapping(thisField = "qualityproblenId", joinField = "qualityproblenId")
    private List<Qualityproblemlog> qualityProblemLogList;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;
    /**
     * 创建人
     */
    @TableField("CREATE_USERID")
    // @JsonIgnore
    private Long createUserid;
    /**
     * 创建人
     */
    @TableField(exist = false)
    @JsonIgnore
    private String createUsername;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @JsonIgnore
    private Date createTime;

    /**
     * 关联质量检查计划ID
     */
    @TableField("QUALITYPLAN_ID")
    @ApiModelProperty("关联质量检查计划ID")
    private Long qualityplanId;

    /**
     * 关联质量检查计划内容
     */
    @TableField(exist = false)
    @ApiModelProperty("关联质量检查计划内容")
    private String qualityplanContent;

    @Data
    @ApiModel("质量问题查询参数")
    public static class Params {
        /**
         * 质量问题级别TUDO
         */
        @TableField("QUALITYPROBLEN_LEVEL")
        @ApiModelProperty("质量问题级别TUDO")
        @NotNull(message = "质量问题级别不能为空")
        private String qualityproblenLevel;
        /**
         * 状态 1待分配 2待整改 3待验收 4验收合格 5验收不通过
         */
        @TableField("QUALITYPROBLEN_STATE")
        @ApiModelProperty("状态 1待分配 2待整改  3待验收 4验收合格 5验收不通过")
        private String qualityproblenState;
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
         * 计划检查日期 start
         */
        @TableField(exist = false)
        @ApiModelProperty("整改期限start")
        private String startRectifytime;
        /**
         * 计划检查日期 end
         */
        @TableField(exist = false)
        @ApiModelProperty("整改期限end")
        private String endRectifytime;
        @TableField(exist = false)
        @ApiModelProperty("检查人用户姓名 ',' 隔开")
        private String qualityproblenCheckusername;
        @TableField(exist = false)
        @ApiModelProperty("查询用")
        private String pscName;
        @TableField(exist = false)
        @ApiModelProperty("我的任务跳转传 1")
        private String task;
        @TableField(exist = false)
        @ApiModelProperty("用于区分我的/全部; 全部 0 我的 1")
        private String type;

    }

    public JSONObject getJSON(Object obj) {
        JSONObject jsonObject = new JSONObject();
        if (obj == null) {
            return null;
        }
        if (obj instanceof Qualityproblem) {
            Qualityproblem qualityproblem = (Qualityproblem)obj;
            jsonObject.put("质量问题类型", getType(qualityproblem.getQualityproblenType()));
            jsonObject.put("质量问题状态", getState(qualityproblem.getQualityproblenState()));
            jsonObject.put("质量问题级别", getLevel(qualityproblem.getQualityproblenLevel()));
            jsonObject.put("质量问题说明", qualityproblem.getQualityproblenExplain());
            jsonObject.put("问题图片", qualityproblem.getQualityproblenImg());
            jsonObject.put("检查人ID", qualityproblem.getQualityproblenCheckuserid());
            jsonObject.put("检查日期", qualityproblem.getQualityproblenChecktime());
            jsonObject.put("整改人ID", qualityproblem.getQualityproblenRectifyuserid());
            jsonObject.put("整改期限", qualityproblem.getQualityproblenRectifytime());
            jsonObject.put("整改要求", qualityproblem.getQualityproblenRectifyfor());
            jsonObject.put("实际整改日期", qualityproblem.getQualityproblenRectifyactime());
            jsonObject.put("验收人ID", qualityproblem.getQualityproblenAcceptuserid());
            jsonObject.put("验收时间", qualityproblem.getQualityproblenAccepttime());
        }
        return jsonObject;
    }

    private String getLevel(String level) {
        switch (level) {
            case "1":
                return "一般";
            case "2":
                return "较大";
            case "3":
                return "重大";
            default:
                return "其他";
        }
    }

    private String getState(String state) {
        // 1 待分配 2 待整改 3 待验收 4 验收合格 5 验收不通过
        switch (state) {
            case "1":
                return "待分配";
            case "2":
                return "待整改";
            case "3":
                return "待验收";
            case "4":
                return "验收通过";
            case "5":
                return "验收不通过";
            default:
                return "其他";
        }
    }

    private String getType(String type) {
        switch (type) {
            case "1":
                return "工程质量缺陷";
            case "2":
                return "工程质量通病";
            case "3":
                return "工程质量事故";
            default:
                return "其他";
        }
    }
}
