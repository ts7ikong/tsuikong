package cc.mrbird.febs.server.tjdkxm.service;

import cc.mrbird.febs.common.core.entity.QueryRequest;
import cc.mrbird.febs.common.core.entity.tjdk.Dept;
import cc.mrbird.febs.common.core.entity.tjdkxm.ConferenceThere;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ZNKJ-R
 * @version V1.0
 * @date 2022/1/18 18:55
 */
@Service
public interface MaillistService {

    IPage<Map<String, Object>> findMailLists(QueryRequest request, String name, Long deptId);

}
