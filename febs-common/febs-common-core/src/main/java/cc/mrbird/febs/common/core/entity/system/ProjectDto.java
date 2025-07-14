package cc.mrbird.febs.common.core.entity.system;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/5/12 11:17
 */
@Data
public class ProjectDto {
    private Long id;
    private String label;
    private List<String> children = Collections.emptyList();
}
