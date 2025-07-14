package cc.mrbird.febs.common.core.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author MrBird
 */
@Data
@ToString
@ApiModel(value = "分页对象")
public class QueryRequest implements Serializable {

    private static final long serialVersionUID = -4869594085374385813L;
    /**
     * 当前页面数据量
     */
    @ApiModelProperty("当前页面数据量，默认为10")
    private int pageSize = 20;
    /**
     * 当前页码
     */
    @ApiModelProperty("当前页码，默认为1")
    private int pageNum = 1;
    /**
     * 排序字段
     */
    @ApiModelProperty("需要排序的字段名")
    private String field;
    /**
     * 排序规则，ascending升序，descending降序
     */
    @ApiModelProperty("排序规则，ascending升序，descending降序")
    private String order;
}
