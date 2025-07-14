package cc.mrbird.febs.common.core.entity.tjdkxm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用于安全检查计划多选中间表
 *
 * @author 14059
 * @version V1.0
 * @date 2022/4/14 9:42
 */
@Data
@TableName("p_safeplan_user")
public class SafeplanUser implements Serializable {
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;
    @TableField(value = "TABLE_ID")
    private Long tableId;

    @TableField(value = "USER_ID")
    private Long userId;
}
