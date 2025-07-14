package cc.mrbird.febs.common.datasource.starter.inteceptor;

import java.io.StringReader;
import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;

import cc.mrbird.febs.common.core.entity.CurrentUser;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.datasource.starter.annotation.DataPermission;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.Select;

/**
 * @author MrBird
 */
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataPermissionInterceptor extends AbstractSqlParserHandler implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        this.sqlParser(metaObject);
        MappedStatement mappedStatement = (MappedStatement)metaObject.getValue("delegate.mappedStatement");

        BoundSql boundSql = (BoundSql)metaObject.getValue("delegate.boundSql");
        Object paramObj = boundSql.getParameterObject();
        // 数据权限只针对查询语句
        if (SqlCommandType.SELECT == mappedStatement.getSqlCommandType()) {
            DataPermission dataPermission = getDataPermission(mappedStatement);
            if (shouldFilter(mappedStatement, dataPermission)) {
                String id = mappedStatement.getId();
                log.info("\n 数据权限过滤 method -> {}", id);
                String originSql = boundSql.getSql();
                String dataPermissionSql = dataPermissionSql(originSql, dataPermission);
                metaObject.setValue("delegate.boundSql.sql", dataPermissionSql);
                log.info("\n originSql -> {} \n dataPermissionSql: {}", originSql, dataPermissionSql);
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {}

    private String dataPermissionSql(String originSql, DataPermission dataPermission) {
        try {
            CurrentUser user = FebsUtil.getCurrentUser();
            if (user == null) {
                return originSql;
            }
            CCJSqlParserManager parserManager = new CCJSqlParserManager();
            Select select = (Select)parserManager.parse(new StringReader(originSql));
            return select.toString();
        } catch (Exception e) {
            log.warn("get data permission sql fail: {}", e.getMessage());
            return originSql;
        }
    }

    private DataPermission getDataPermission(MappedStatement mappedStatement) {
        String mappedStatementId = mappedStatement.getId();
        DataPermission dataPermission = null;
        try {
            String className = mappedStatementId.substring(0, mappedStatementId.lastIndexOf("."));
            final Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(DataPermission.class)) {
                dataPermission = clazz.getAnnotation(DataPermission.class);
            }
        } catch (Exception ignore) {
        }
        return dataPermission;
    }

    private Boolean shouldFilter(MappedStatement mappedStatement, DataPermission dataPermission) {
        if (dataPermission != null) {
            String methodName = StringUtils.substringAfterLast(mappedStatement.getId(), ".");
            String methodPrefix = dataPermission.methodPrefix();
            if (StringUtils.isNotBlank(methodPrefix) && StringUtils.startsWith(methodName, methodPrefix)) {
                return Boolean.TRUE;
            }
            String[] methods = dataPermission.methods();
            for (String method : methods) {
                if (StringUtils.equals(method, methodName)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
}
