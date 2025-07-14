package cc.mrbird.febs.common.core.entity.tjdk;


import java.util.Date;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


@Data
@TableName("p_banner")
@ApiModel("banner管理")
public class Banner implements Serializable {
    private static final long serialVersionUID = -92508871129599768L;

    /**
     * bannerid
     */
    @TableId(value = "BANNER_ID", type = IdType.AUTO)
    @ApiModelProperty("bannerid")
    private Long bannerId;


    /**
     * 图片地址
     */
    @TableField("BANNER_ADDR")
    @ApiModelProperty("图片地址")
    private String bannerAddr;

    /**
     * 删除状态 是否已删除 未删除0 删除1
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("是否已删除 未删除0 删除1")
    @JsonIgnore
    private Integer isDelete;


}
