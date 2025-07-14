package cc.mrbird.febs.common.core.entity.system;

import lombok.Data;

import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/5/13 15:05
 */
@Data
public class MenuUserAuthDto {
    private String type;
    private List<MenuButtonDto> menus;

    @Data
    public static class MenuButtonDto {
        private String menuId;
        private String type;
        private String roleType;
        private List<ButtonDto> buttons;
        private String projectIds;
    }

    @Data
    public static class ButtonDto {
        private String type;
        private String buttonId;
        private String projectIds;
    }
}
