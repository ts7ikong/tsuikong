package cc.mrbird.febs.common.core.entity;


import cc.mrbird.febs.common.core.entity.system.Menu;
import cc.mrbird.febs.common.core.entity.system.TButton;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author MrBird
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuTree extends Tree<Menu> {

    private String path;
    private String mobilePath;
    private String component;
    private String perms;
    private String icon;
    private String selectIcon;
    private String type;
    private Integer orderNum;
    private List<TButton> buttonList;
}
