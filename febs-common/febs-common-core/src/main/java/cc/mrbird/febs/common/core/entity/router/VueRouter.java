package cc.mrbird.febs.common.core.entity.router;

import cc.mrbird.febs.common.core.entity.system.TButton;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 构建 Vue路由
 *
 * @author MrBird
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VueRouter<T> implements Serializable {

    private static final long serialVersionUID = -3327478146308500708L;

    @JsonIgnore
    private String id;
    @JsonIgnore
    private String parentId;

    private String path;
    private String mobilePath;
    private String name;
    private String routeName;
    private String component;
    private String redirect;
    private RouterMeta meta;
    private Boolean hidden = false;
    private Boolean alwaysShow = false;
    private List<VueRouter<T>> children;
    private List<TButton> buttonList;

    @JsonIgnore
    private Boolean hasParent = false;

    @JsonIgnore
    private Boolean hasChildren = false;

    public void initChildren() {
        this.children = new ArrayList<>();
    }

    /**
     * 全部的按钮 用于前端校验按钮权限给list第一个数据
     */
    private List<TButton> allButtonList;
    /**
     * 全部的按钮 用于前端校验按钮权限给list第一个数据
     */
    private List<Map<String, Object>> allButtonNewList;
}
