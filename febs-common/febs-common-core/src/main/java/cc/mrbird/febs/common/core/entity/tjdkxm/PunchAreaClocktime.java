package cc.mrbird.febs.common.core.entity.tjdkxm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 项目打卡区域
 *
 * @author 14059
 * @version V1.0
 * @date 2022/3/9 16:51
 */

@Data
@TableName("p_puncharea_clocktime")
@ApiModel("打卡区域打卡时间段")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class PunchAreaClocktime implements Serializable {
    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Long id;
    @TableField(value = "TABLE_ID")
    private Long tableId;
    @TableField(exist = false)
    private String tableName;

    @TableField(value = "START_TIME")
    private String startTime;

    @TableField(value = "startTimeColumn")
    private Integer startTimeColumn = 0;

    @TableField(value = "endTimeColumn")
    private Integer endTimeColumn = 1440;

    @TableField(value = "END_TIME")
    private String endTime;
}
