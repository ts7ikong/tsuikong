package cc.mrbird.febs.common.core.entity.system;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设置表(Set)实体类
 *
 * @author makejava
 * @since 2021-03-03 16:44:15
 */
@Data
@ApiModel("设置表")
@TableName("P_SET")
public class Set implements Serializable {
    private static final long serialVersionUID = -24562830216857995L;

    /**
     * 主键
     */
    @TableId(value = "SET_ID", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long setId;


    /**
     * 设置-名称
     */
    @TableField("SET_NAME")
    @ApiModelProperty("设置-名称")
    private String setName;


    /**
     * 设置-类型  0线上1线下/以此类推
     */
    @TableField("SET_TYPE")
    @ApiModelProperty("设置-名称")
    private Integer setType;



    /**
     * 设置-说明
     */
    @TableField("SET_EXPLAIN")
    @ApiModelProperty("设置-说明")
    private String setExplain;

}
