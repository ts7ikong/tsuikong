package cc.mrbird.febs.common.core.entity.system;

import cc.mrbird.febs.common.core.converter.MyConverter;
import cc.mrbird.febs.common.core.converter.TimeConverter;
import cc.mrbird.febs.common.core.options.MyOptions;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

/**
 * 导入导出测试，Eximport = export + import
 *
 * @author MrBird
 */
@Data
@TableName("t_eximport")
@Excel("测试导入导出数据")
public class Eximport {

    /**
     * 字段1
     */
    @ExcelField(value = "字段1", required = true, maxLength = 20,
            comment = "提示：必填，长度不能超过20个字符")
    private String field1;

    /**
     * 字段2
     */
    @ExcelField(value = "字段2", writeConverter = MyConverter.IntegerToStringWriteConverter.class)
    private Integer field2;

    /**
     * 字段3
     */
    @ExcelField(value = "字段3", required = true, maxLength = 50)
    private String field3;
    /**
     * 字段3
     */
    @ExcelField(value = "性别", writeConverter = MyConverter.SexWriteConverter.class, readConverter =
            MyConverter.SexReadConverter.class, options = MyOptions.SexOption.class)
    private Integer sex;

    /**
     * 创建时间
     */
    @ExcelField(value = "创建时间", writeConverter = MyConverter.DateToStringWriteConverter.class)
    private Date createTime;
}