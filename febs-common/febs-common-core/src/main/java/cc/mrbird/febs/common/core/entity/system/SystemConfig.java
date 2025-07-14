package cc.mrbird.febs.common.core.entity.system;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/6/2 16:18
 */
@Data
@TableName("p_system_config")
@ApiModel("系统配置信息")
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfig implements Serializable {
    /**
     * 类型 app
     */
    public static final Integer TYPE_APP = 1;
    /**
     * 类型 系统
     */
    public static final Integer TYPE_SYSTEM = 0;
    /**
     * 类型 通用 默认
     */
    public static final Integer TYPE_CURRENCY = 2;
    /**
     * json 数据
     */
    public static final Integer IS_JSON = 1;
    /**
     * 不是 json 数据
     */
    public static final Integer IS_NOT_JSON = 0;
    /**
     * 版本信息ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    @ApiModelProperty("系统配置信息ID")
    private Long id;
    /**
     * key
     */
    @TableField("`KEY`")
    @ApiModelProperty("key")
    private String key;
    /**
     * '内容'
     */
    @TableField("CONTENT")
    @ApiModelProperty("'内容'")
    private String content;

    /**
     * [类型 1 APP, 0 SYSTEM, 2{默认} CURRENCY 通用]
     */
    @TableField("TYPE")
    @ApiModelProperty("[类型 1 APP, 0 SYSTEM, 2{默认} CURRENCY 通用]")
    private Integer type = 2;
    /**
     * '说明'
     */
    @TableField("`EXPLAIN`")
    @ApiModelProperty("'说明'")
    private String explain;
    /**
     * CURRENT_TIMESTAMP
     */
    @TableField("CREATE_TIME")
    @ApiModelProperty("CURRENT_TIMESTAMP")
    private Date createTime;
    /**
     * 删除 1；0未删
     */
    @TableField("IS_DELETE")
    @ApiModelProperty("删除 1；0未删")
    @JsonIgnore
    private Integer isDelete;
}
