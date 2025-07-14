package cc.mrbird.febs.server.system.dto;

import cc.mrbird.febs.common.core.entity.tjdkxm.model.Add;
import cc.mrbird.febs.common.core.entity.tjdkxm.model.Delete;
import cc.mrbird.febs.common.core.entity.tjdkxm.model.Update;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/6/2 16:34
 */
@Data
@Accessors(chain = true)
public class SystemConfigDto implements Serializable {
    /**
     * 版本信息ID
     */
    @ApiModelProperty("系统配置信息ID")
    @NotNull(groups = {Update.class, Delete.class})
    private Long id;
    /**
     * key
     */
    @ApiModelProperty("key")
    @NotNull(groups = {Add.class, Update.class})
    private String key;
    /**
     * '内容'
     */
    @ApiModelProperty("'内容'")
    @NotNull(groups = {Add.class, Update.class})
    private String content;
    /**
     * [类型 1 APP, 0 SYSTEM, 2{默认} CURRENCY 通用]
     */
    @ApiModelProperty("[类型 1 APP, 0 SYSTEM, 2{默认} CURRENCY 通用]")
    private Integer type = 2;
    /**
     * 说明
     */
    @ApiModelProperty("说明")
    private String explain;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
}
