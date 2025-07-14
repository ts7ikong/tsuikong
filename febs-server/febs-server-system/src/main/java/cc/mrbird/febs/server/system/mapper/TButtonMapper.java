package cc.mrbird.febs.server.system.mapper;

import cc.mrbird.febs.common.core.entity.system.TButton;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 按钮表(TButton)表数据库访问层
 *
 * @author zlkj_cg
 * @since 2021-04-22 10:48:09
 */
@Mapper
@Repository
public interface TButtonMapper extends BaseMapper<TButton> {

    List<TButton> getButtonByUserId(@Param("userId") Long currentUserId);

}
