package cc.mrbird.febs.common.core.entity.tjdkxm;

import cc.mrbird.febs.common.core.converter.UserPunchType;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户打卡信息
 *
 * @author 14059
 * @version V1.0
 * @date 2022/3/9 17:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("p_userpunch")
@ApiModel("项目打卡区域")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class UserPunch implements Serializable {
    @TableId(value = "PUNCH_ID", type = IdType.AUTO)
    @ApiModelProperty("id")
    @ExcelIgnore
    private Long punchId;

    @TableField("PUNCH_USERID")
    @ApiModelProperty("用户id")
    @ExcelIgnore
    private Long punchUserid;

    @TableField(exist = false)
    @ApiModelProperty("查询用 打卡人")
    @ExcelProperty(value = "打卡人", index = 0)
    private String punchUserName;

    // @TableField("PUNCH_PROJECTID")
    // @ApiModelProperty("项目id")
    // private Long punchProjectid;

    @TableField("PUNCH_PUNCHAREAID")
    @ApiModelProperty("打卡区域id")
    @ExcelIgnore
    private Long punchPunchareaid;

    @TableField("PUNCH_PUNCHAREATIMEID")
    @ApiModelProperty("打卡区域时间段id")
    @ExcelIgnore
    private Long punchPunchareatimeid;

    @TableField(exist = false)
    @ApiModelProperty("打卡区域名称")
    @ExcelProperty(value = "考勤区域", index = 4)
    private String punchPunchareaname;

    @TableField("PUNCH_ADDR")
    @ApiModelProperty("打卡地址")
    @ExcelProperty(value = "打卡地址", index = 2)
    private String punchAddr;

    @TableField("PUNCH_PUNCHTYEPE")
    @ApiModelProperty("打卡类型 0“内勤打卡 1外勤打卡")
    @ExcelProperty(value = "打卡类型", converter = UserPunchType.class, index = 3)
    private Integer punchPunchtyepe;

    @TableField("PUNCH_TIME")
    @ApiModelProperty("打卡时间")
    @ExcelProperty(value = "打卡时间", index = 1)
    private Date punchTime;

    @TableField(exist = false)
    @ApiModelProperty("打卡时间")
    @ExcelIgnore
    private String dateStr;

    @Data
    @ApiModel("查询")
    public static class AreaByDay {

        @ApiModelProperty("打卡类型 1 节假日")
        @ExcelIgnore
        private int holiday = 0;

        @ApiModelProperty("打卡类型 1 已打卡 0 未打卡")
        @ExcelIgnore
        private int isPunch = 0;
    }

    @Data
    @ApiModel("查询")
    public static class Params {
        @TableField(exist = false)
        @ApiModelProperty("查询用 打卡人")
        @ExcelIgnore
        private String punchUserName;

        @TableField(exist = false)
        @ApiModelProperty("查询用 项目id")
        @ExcelIgnore
        private Long punchProjectid;

        @TableField("PUNCH_PUNCHTYEPE")
        @ApiModelProperty("打卡类型 0“内勤打卡 1外勤打卡")
        private Integer punchPunchtype;

        @ApiModelProperty("打卡时间 开始")
        @ExcelIgnore
        private String punchStartTime;

        @ApiModelProperty("打卡时间 结束")
        @ExcelIgnore
        private String punchEndTime;

        @TableField("PUNCH_TIME")
        @ApiModelProperty("打卡时间")
        @ExcelIgnore
        private Date punchTime;

    }
}
