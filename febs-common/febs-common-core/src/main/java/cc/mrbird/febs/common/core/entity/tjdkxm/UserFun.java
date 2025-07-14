package cc.mrbird.febs.common.core.entity.tjdkxm;

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
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * @author ZNKJ-R
 * @version V1.0
 * @date 2022/1/18 20:19
 */
@Data
@ApiModel(value = "安全隐患清单")
@TableName(value = "p_userfun",autoResultMap = true)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"})
public class UserFun implements Serializable {
    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Long id;
    /**
     * 用户ID
     */
    @TableField("USER_ID")
    @ApiModelProperty("用户ID")
    @Null
    private Long userId;
    /**
     * 用户ID
     */
    @TableField("PROJECT_ID")
    @ApiModelProperty("项目ID")
    @Null
    private Long projectId;
    /**
     * 默认： 0 不是 1 是
     */
    @ApiModelProperty(value = "组件属性")
    @TableField(value = "FEATURES",typeHandler = JacksonTypeHandler.class)
    private Object features;

    @TableField(exist = false)
    @JsonIgnore
    @ApiModelProperty(value = "添加修改时用这个")
    @NotNull
    private String featuresString;
}
