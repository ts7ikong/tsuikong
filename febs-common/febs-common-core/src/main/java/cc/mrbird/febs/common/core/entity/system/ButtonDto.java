package cc.mrbird.febs.common.core.entity.system;

import lombok.Data;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/5/18 16:04
 */
@Data
public class ButtonDto {
    /**
     * 按钮id
     */
    private String buttonId;
    /**
     * 按钮对应的项目id
     */
    private String projectIds;
    /**
     * 按钮类型-->菜单类型
     */
    private String type;
    /**
     * role 类型
     */
    private String roleType;
}
