package cc.mrbird.febs.common.core.entity.tjdkxm;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.yulichang.annotation.EntityMapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "p_safeproblem", autoResultMap = true)
@ApiModel("安全隐患清单")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})

public class Safeproblem implements Serializable {
    private static final long serialVersionUID = -28722480564006571L;

    /**
     * 安全隐患id
     */
    @TableId(value = "SAFEPROBLEN_ID", type = IdType.AUTO)
    @ApiModelProperty("安全隐患id")
    private Long safeproblenId;
    /**
     * 安全隐患id
     */
    @TableField("SAFEPROBLEN_CODE")
    @ApiModelProperty("安全隐患编码")
    private String safeproblenCode;

    /**
     * 单位项目ID
     */
    @TableField("SAFEPROBLEN_UNITENGINEID")
    @ApiModelProperty("单位项目ID")
    private Long safeproblenUnitengineid;

    @ApiModelProperty("单位项目名称")
    @TableField(exist = false)
    private String unitengineName;

    /**
     * 对应项目ID
     */
    @TableField("SAFEPROBLEN_PROJECTID")
    @ApiModelProperty("对应项目ID")
    private Long safeproblenProjectid;

    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;

    /**
     * 分部id
     */
    @TableField("SAFEPROBLEN_PARCELID")
    @ApiModelProperty("分部id")
    private Long safeproblenParcelid;
    @TableField(exist = false)
    @ApiModelProperty("分部 name")
    private String parcelName;

    /**
     * 分项id
     */
    @TableField("SAFEPROBLEN_SUBITEMID")
    @ApiModelProperty("分项id")
    private Long safeproblenSubitemid;

    @TableField(exist = false)
    @ApiModelProperty("分项 name")
    private String subitemName;

    /**
     * 安全隐患类型TUDO
     */
    @TableField("SAFEPROBLEN_TYPE")
    @ApiModelProperty("安全隐患类型TUDO")
    @NotNull(message = "安全隐患类型不能为空")
    private String safeproblenType;

    /**
     * 安全隐患级别TUDO
     */
    @TableField("SAFEPROBLEN_LEVEL")
    @ApiModelProperty("安全隐患级别TUDO")
    @NotNull(message = "安全隐患级别不能为空")
    private String safeproblenLevel;

    /**
     * 安全隐患说明
     */
    @TableField("SAFEPROBLEN_EXPLAIN")
    @ApiModelProperty("安全隐患说明")
    private String safeproblenExplain;

    /**
     * 问题图片地址
     */
    @ApiModelProperty("问题图片地址 [{\"addr\": \"123456\", \"name\": \"123456\"}]")
    @TableField(value = "SAFEPROBLEN_IMG", typeHandler = JacksonTypeHandler.class)
    private Object safeproblenImg;

    /**
     * 检查人用户ID
     */
    @TableField("SAFEPROBLEN_CHECKUSERID")
    @ApiModelProperty("检查人用户ID ',' 隔开")
    private String safeproblenCheckuserid;
    @TableField(exist = false)
    @ApiModelProperty("检查人用户姓名 ',' 隔开")
    private String safeproblenCheckusername;

    @TableField("SAFEPROBLEN_CHECKFOREIGNUSER")
    @ApiModelProperty("检查人 外来人员 ”,“分割")
    private String safeproblenCheckforeignuser;

    /**
     * 检查日期
     */
    @TableField("SAFEPROBLEN_CHECKTIME")
    @ApiModelProperty("检查日期")
    @NotNull(message = "检查日期不能为空")
    private Date safeproblenChecktime;

    /**
     * 分配整改人用户ID
     */
    @TableField("SAFEPROBLEN_RECTIFYUSERID")
    @ApiModelProperty("分配整改人用户ID")
    private Long safeproblenRectifyuserid;
    @TableField(exist = false)
    private String safeproblenRectifyusername;

    /**
     * 整改期限
     */
    @TableField("SAFEPROBLEN_RECTIFYTIME")
    @ApiModelProperty("整改期限")

    private Date safeproblenRectifytime;

    /**
     * 整改要求
     */
    @TableField("SAFEPROBLEN_RECTIFYFOR")
    @ApiModelProperty("整改要求")
    private String safeproblenRectifyfor;

    /**
     * 实际整改日期
     */
    @TableField("SAFEPROBLEN_RECTIFYACTIME")
    @ApiModelProperty("实际整改日期")

    private Date safeproblenRectifyactime;

    /**
     * 整改后图片
     */
    @ApiModelProperty("整改后图片")
    @TableField(value = "SAFEPROBLEN_RECTIFYIMG", typeHandler = JacksonTypeHandler.class)
    private Object safeproblenRectifyimg;

    /**
     * 状态 1待分配 2待整改 3待验收 4验收合格 5验收不通过
     */
    @TableField("SAFEPROBLEN_STATE")
    @ApiModelProperty("状态 1待分配 2待整改 3待验收 4验收合格 5验收不通过")
    private String safeproblenState;

    /**
     * 验收人
     */
    @TableField("SAFEPROBLEN_ACCEPTUSERID")
    @ApiModelProperty("验收人")
    private Long safeproblenAcceptuserid;
    @TableField(exist = false)
    private String safeproblenAcceptusername;

    /**
     * 验收时间
     */
    @TableField("SAFEPROBLEN_ACCEPTTIME")
    @ApiModelProperty("验收时间")
    private Date safeproblenAccepttime;
    /**
     * 验收说明
     */
    @TableField("SAFEPROBLEN_ACCEPTINFO")
    @ApiModelProperty("验收说明")
    private String safeproblenAcceptinfo;

    /**
     * 日志
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "操作日志", hidden = true)
    @EntityMapping(thisField = "safeproblenId", joinField = "safeproblenId")
    private List<Safeproblemlog> safeproblemlogList;

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
     * 关联安全隐患计划ID
     */
    @TableField("SAFEPLAN_ID")
    @ApiModelProperty("关联安全隐患计划ID")
    private Long safeplanId;

    /**
     * 关联安全隐患计划内容
     */
    @TableField(exist = false)
    @ApiModelProperty("关联安全隐患计划内容")
    private String safeplanContent;

    @ApiModel("安全隐患清单查询")
    @Data
    public static class Params {
        /**
         * 安全隐患类型TUDO
         */
        @TableField("SAFEPROBLEN_TYPE")
        @ApiModelProperty("安全隐患类型TUDO")
        private String safeproblenType;
        /**
         * 状态 1待分配 2待整改 3待验收 4验收合格 5验收不通过
         */
        @TableField("SAFEPROBLEN_STATE")
        @ApiModelProperty("状态 1待分配 2待整改 3待验收 4验收合格 5验收不通过")
        private String safeproblenState;
        /**
         * 安全隐患级别TUDO
         */
        @TableField("SAFEPROBLEN_LEVEL")
        @ApiModelProperty("安全隐患级别TUDO")
        @NotNull(message = "安全隐患级别不能为空")
        private String safeproblenLevel;
        /**
         * 整改期限start start
         */
        @TableField(exist = false)
        @ApiModelProperty("整改期限start")
        private String startRectifytime;
        /**
         * 整改期限end end
         */
        @TableField(exist = false)
        @ApiModelProperty("整改期限end")
        private String endRectifytime;
        /**
         * 实际整改日期
         */
        @TableField(exist = false)
        @ApiModelProperty("查询用 实际整改日期 start")
        private String startRectifyactime;
        /**
         * 实际整改日期
         */
        @TableField(exist = false)
        @ApiModelProperty("查询用 实际整改日期 end")
        private String endRectifyactime;
        private Date safeproblenAccepttime;
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
        if (obj instanceof Safeproblem) {
            Safeproblem safeproblem = (Safeproblem)obj;
            jsonObject.put("安全隐患类型", getType(safeproblem.getSafeproblenType()));
            jsonObject.put("安全隐患状态", getState(safeproblem.getSafeproblenState()));
            jsonObject.put("安全隐患级别", getLevel(safeproblem.getSafeproblenLevel()));
            jsonObject.put("安全隐患说明", safeproblem.getSafeproblenExplain());
            jsonObject.put("问题图片", safeproblem.getSafeproblenImg());
            jsonObject.put("检查人ID", safeproblem.getSafeproblenCheckuserid());
            jsonObject.put("检查日期", safeproblem.getSafeproblenChecktime());
            jsonObject.put("整改人ID", safeproblem.getSafeproblenRectifyuserid());
            jsonObject.put("整改期限", safeproblem.getSafeproblenRectifytime());
            jsonObject.put("整改要求", safeproblem.getSafeproblenRectifyfor());
            jsonObject.put("实际整改日期", safeproblem.getSafeproblenRectifyactime());
            jsonObject.put("验收人ID", safeproblem.getSafeproblenAcceptuserid());
            jsonObject.put("验收时间", safeproblem.getSafeproblenAccepttime());
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
                return "人的不安全行为";
            case "2":
                return "物的不安全状态";
            case "3":
                return "环境的不安全条件";
            case "4":
                return "管理缺陷";
            default:
                return "其他";
        }
    }

}
