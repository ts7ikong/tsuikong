package cc.mrbird.febs.common.core.entity.tjdkxm.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/5/27 17:37
 */
@Data
public class ProjectDTO {
    private Long projectId;
    private String projectName;
    private List<Map<String, Object>> users;
}
