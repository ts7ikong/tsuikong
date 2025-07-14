package cc.mrbird.febs.common.core.entity.system;

import cc.mrbird.febs.common.core.converter.MyConverter;
import cc.mrbird.febs.common.core.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Data
@TableName("t_menu")
@Excel("菜单信息表")
@ApiModel("重大危险源记录表")
public class Menu implements Serializable {

    /**
     * 菜单 公司
     */
    public static final String MENU_PC_COMPANY = "0";
    /**
     * 菜单 项目
     */
    public static final String MENU_PC_PROJECT = "1";
    /**
     * 按钮
     */
    public static final String TYPE_BUTTON = "9";

    public static final Long TOP_MENU_ID = 0L;
    private static final long serialVersionUID = 7187628714679791771L;
    /**
     * 菜单/按钮ID
     */
    @TableId(value = "MENU_ID", type = IdType.AUTO)
    @ApiModelProperty("菜单/按钮ID")
    private Long menuId;

    @TableField(exist = false)
    private String id;

    /**
     * 上级菜单ID
     */
    @TableField("PARENT_ID")
    @ApiModelProperty("上级菜单ID")
    private Long parentId;

    /**
     * 菜单/按钮名称
     */
    @TableField("MENU_NAME")
    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    @ExcelField(value = "名称")
    @ApiModelProperty(value = "名称")
    private String menuName;

    /**
     * 菜单/按钮名称
     */
    @TableField("ROUTE_NAME")
    @ExcelField(value = "路由名称")
    @ApiModelProperty(value = "路由名称")
    private String routeName;

    /**
     * 菜单URL
     */
    @TableField("BELONG_MENU_ROUTE_NAME")
    @ExcelField(value = "详情页所属菜单路由名")
    @ApiModelProperty(value = "详情页所属菜单路由名")
    private String belongMenuRouteName;
    /**
     * 菜单URL
     */
    @TableField("PATH")
    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value = "URL")
    @ApiModelProperty(value = "URL")
    private String path;

    /**
     * 移动端菜单URL
     */
    @TableField("MOBILE_PATH")
    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value = "URL")
    @ApiModelProperty(value = "URL")
    private String mobilePath;

    /**
     * 对应 Vue组件
     */
    @TableField("COMPONENT")
    @Size(max = 100, message = "{noMoreThan}")
    @ExcelField(value = "对应Vue组件")
    @ApiModelProperty(value = "对应Vue组件")
    private String component;

    /**
     * 权限标识
     */
    @TableField("PERMS")
    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "权限")
    @ApiModelProperty(value = "权限")
    private String perms;

    /**
     * 图标
     */
    @TableField("ICON")
    @ExcelField(value = "图标")
    @ApiModelProperty(value = "图标")
    private String icon;

    /**
     * 图标
     */
    @TableField("SELECT_ICON")
    @ExcelField(value = "展开后图标")
    @ApiModelProperty(value = "展开后图标")
    private String selectIcon;
    /**
     * 图标
     */
    @TableField("CLASS_TYPE")
    @ExcelField(value = "1 公共 2 项目")
    @ApiModelProperty(value = "1 公共 2 项目")
    private Integer classType;

    /**
     * 类型/是否启用 1启用 11未启用
     */
    @TableField("TYPE")
    @NotBlank(message = "{required}")
    @ExcelField(value = "类型", writeConverter = MyConverter.IntegerToStringWriteConverter.class,
        writeConverterExp = "1=启用,11=未启用")
    @ApiModelProperty(value = "1启用 11未启用")
    private Integer type;

    /**
     * 排序
     */
    @TableField("ORDER_NUM")
    @ApiModelProperty("排序")
    private Integer orderNum;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("MODIFY_TIME")
    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    private Date modifyTime;

    private transient String createTimeFrom;
    private transient String createTimeTo;

    @TableField(exist = false)
    private List<TButton> tButtonList = new ArrayList<>();

    @TableField(exist = false)
    private List<Map<String, Object>> tButtonListNew = new ArrayList<>();
}