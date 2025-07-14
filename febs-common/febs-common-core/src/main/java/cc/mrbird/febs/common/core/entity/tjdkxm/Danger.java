package cc.mrbird.febs.common.core.entity.tjdkxm;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
@TableName("p_danger")
@ApiModel("重大危险源记录表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class Danger implements Serializable {
    private static final long serialVersionUID = -34764720254475675L;

    /**
     * 重大危险源表ID
     */
    @TableId(value = "DANGER_ID", type = IdType.AUTO)
    @ApiModelProperty("重大危险源表ID")
    private Long dangerId;


    /**
     * 对应项目ID
     */
    @TableField("DANGER_PROJECTID")
    @ApiModelProperty("对应项目ID")
    private Long dangerProjectid;
    /**
     * 对应项目表
     */
    @TableField(exist = false)
    @ApiModelProperty("对应项目名称")
    private String projectName;

    /**
     * 单位项目id
     */
    @TableField("DANGER_UNITENGINEID")
    @ApiModelProperty("单位项目id")
    private Long dangerUnitengineid;
    /**
     * 单位项目name
     */
    @TableField(exist = false)
    @ApiModelProperty("单位项目name")
    private String unitengineName;

    /**
     * 分部id
     */
    @TableField("DANGER_PARCELID")
    @ApiModelProperty("分部id")
    private Long dangerParcelid;

    @TableField(exist = false)
    @ApiModelProperty("分部 name")
    private String parcelName;

    /**
     * 分项id
     */
    @TableField("DANGER_SUBITEMID")
    @ApiModelProperty("分项id")
    private Long dangerSubitemid;

    @TableField(exist = false)
    @ApiModelProperty("分项 name")
    private String subitemName;

    /**
     * 重大危险源名称
     */
    @TableField("DANGER_NAME")
    @ApiModelProperty("重大危险源名称")
    @NotNull(message = "重大危险源名称不能为空")
    private String dangerName;

    /**
     * 重大危险源位置
     */
    @TableField("DANGER_ADDR")
    @ApiModelProperty("重大危险源位置")
    @NotNull(message = "重大危险源位置不能为空")
    private String dangerAddr;

    /**
     * 重大危险源类型
     */
    @TableField("DANGER_TYPE")
    @ApiModelProperty("重大危险源类型")
    @NotNull(message = "重大危险源类型不能为空")
    private String dangerType;

    /**
     * 重大危险源级别 1 1级 2 2级 3 3级
     */
    @TableField("DANGER_LEVEL")
    @ApiModelProperty("重大危险源级别 1 1级 2 2级 3 3级")
    @NotNull(message = "重大危险源级别不能为空")
    private String dangerLevel;

    /**
     * 重大危险源说明
     */
    @TableField("DANGER_REMARK")
    @ApiModelProperty("重大危险源说明")
    private String dangerRemark;

    /**
     * 记录人用户ID
     */
    @TableField("DANGER_RECORDUSERID")
    @ApiModelProperty("记录人用户ID")
    private Long dangerRecorduserid;
    /**
     * 记录人用户ID
     */
    @TableField(exist = false)
    @ApiModelProperty("记录人用户ID")
    private String dangerRecordusername;

    /**
     * 记录日期
     */
    @TableField("DANGER_RECORDTIME")
    @ApiModelProperty("记录日期")

    private Date dangerRecordtime;


    /**
     * 记录备注
     */
    @TableField("DANGER_RECORDREMARK")
    @ApiModelProperty("记录备注")
    private String dangerRecordremark;


    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;

    @Data
    @ApiModel("危险源查询")
    public static class Params {
        /**
         * 记录日期start
         */
        @TableField(exist = false)
        @ApiModelProperty("记录日期start")
        private String startRecordtime;
        /**
         * 记录日期end
         */
        @TableField(exist = false)
        @ApiModelProperty("记录日期end")
        private String endRecordtime;
        /**
         * 重大危险源类型
         */
        @TableField("DANGER_TYPE")
        @ApiModelProperty("重大危险源类型")
        @NotNull(message = "重大危险源类型不能为空")
        private String dangerType;

        /**
         * 重大危险源级别 1 1级 2 2级 3 3级
         */
        @TableField("DANGER_LEVEL")
        @ApiModelProperty("重大危险源级别 1 1级 2 2级 3 3级")
        @NotNull(message = "重大危险源级别不能为空")
        private String dangerLevel;
        @TableField(exist = false)
        @ApiModelProperty("查询用")
        private String pscName;
    }

    public JSONObject getJSON(Object obj) {
        JSONObject jsonObject = new JSONObject();
        if (obj == null) {
            return null;
        }
        if (obj instanceof Danger) {
            Danger danger = (Danger) obj;
            jsonObject.put("重大危险源名称", danger.getDangerName());
            jsonObject.put("重大危险源位置", danger.getDangerAddr());
            jsonObject.put("重大危险源类型", getType(danger.getDangerType()));
            jsonObject.put("重大危险源级别", danger.getDangerLevel() + "级");
            jsonObject.put("重大危险源说明", danger.getDangerRecordremark());
            jsonObject.put("记录日期", danger.getDangerRecordtime());
            jsonObject.put("记录备注", danger.getDangerRemark());
        }
        return jsonObject;
    }

    private String getType(String type) {
        switch (type) {
            case "1":
                return "储罐区";
            case "2":
                return "库区";
            case "3":
                return "生产场所";
            case "4":
                return "压力管道";
            case "5":
                return "锅炉";
            case "6":
                return "压力容器";
            case "7":
                return "煤矿（井工开采）";
            case "8":
                return "金属非金属地下矿山";
            case "9":
                return "尾矿区";
            default:
                return "";
        }
    }
}
