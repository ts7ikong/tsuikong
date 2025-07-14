package cc.mrbird.febs.common.datasource.starter.configure;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author 14059
 * @version V1.0
 * @date 2022/4/20 11:49
 */
public class MyPaginationInnerInterceptor extends PaginationInnerInterceptor implements InnerInterceptor {
    private static final List<SelectItem> COUNT_SELECT_ITEM = Collections.singletonList(myCountSelectItem());

    private static SelectItem myCountSelectItem() {
        Function function = new MyFunction();
        function.setName("COUNT");
        return new SelectExpressionItem(function);
    }

    public MyPaginationInnerInterceptor(DbType dbType) {
        super(dbType);
    }

    public MyPaginationInnerInterceptor(IDialect dialect) {
        super(dialect);
    }

    @Override
    protected String autoCountSql(boolean optimizeCountSql, String sql) {
        if (!optimizeCountSql) {
            return lowLevelCountSql(sql);
        }
        try {
            Select select = (Select) CCJSqlParserUtil.parse(sql);
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
            Distinct distinct = plainSelect.getDistinct();
            GroupByElement groupBy = plainSelect.getGroupBy();
            List<OrderByElement> orderBy = plainSelect.getOrderByElements();

            if (CollectionUtils.isNotEmpty(orderBy)) {
                boolean canClean = true;
                if (groupBy != null) {
                    // 包含groupBy 不去除orderBy
                    canClean = false;
                }
                if (canClean) {
                    for (OrderByElement order : orderBy) {
                        // order by 里带参数,不去除order by
                        Expression expression = order.getExpression();
                        if (!(expression instanceof Column) && expression.toString().contains(StringPool.QUESTION_MARK)) {
                            canClean = false;
                            break;
                        }
                    }
                }
                if (canClean) {
                    plainSelect.setOrderByElements(null);
                }
            }
            //#95 Github, selectItems contains #{} ${}, which will be translated to ?, and it may be in a function: power(#{myInt},2)
            for (SelectItem item : plainSelect.getSelectItems()) {
                if (item.toString().contains(StringPool.QUESTION_MARK)) {
                    return lowLevelCountSql(select.toString());
                }
            }
            // 包含 distinct、groupBy不优化
//            if (distinct != null || null != groupBy) {
//                return lowLevelCountSql(select.toString());
//            }
            if (null != groupBy) {
                return lowLevelCountSql(select.toString());
            }
            // 包含 join 连表,进行判断是否移除 join 连表
            if (optimizeJoin) {
                List<Join> joins = plainSelect.getJoins();
                if (CollectionUtils.isNotEmpty(joins)) {
                    boolean canRemoveJoin = true;
                    String whereS = Optional.ofNullable(plainSelect.getWhere()).map(Expression::toString).orElse(StringPool.EMPTY);
                    // 不区分大小写
                    whereS = whereS.toLowerCase();
                    for (Join join : joins) {
                        if (!join.isLeft()) {
                            canRemoveJoin = false;
                            break;
                        }
                        FromItem rightItem = join.getRightItem();
                        String str = "";
                        if (rightItem instanceof Table) {
                            Table table = (Table) rightItem;
                            str = Optional.ofNullable(table.getAlias()).map(Alias::getName).orElse(table.getName()) + StringPool.DOT;
                        } else if (rightItem instanceof SubSelect) {
                            SubSelect subSelect = (SubSelect) rightItem;
                            /* 如果 left join 是子查询，并且子查询里包含 ?(代表有入参) 或者 where 条件里包含使用 join 的表的字段作条件,就不移除 join */
                            if (subSelect.toString().contains(StringPool.QUESTION_MARK)) {
                                canRemoveJoin = false;
                                break;
                            }
                            str = subSelect.getAlias().getName() + StringPool.DOT;
                        }
                        // 不区分大小写
                        str = str.toLowerCase();
                        String onExpressionS = join.getOnExpression().toString();
                        /* 如果 join 里包含 ?(代表有入参) 或者 where 条件里包含使用 join 的表的字段作条件,就不移除 join */
                        if (onExpressionS.contains(StringPool.QUESTION_MARK) || whereS.contains(str)) {
                            canRemoveJoin = false;
                            break;
                        }
                    }
                    if (canRemoveJoin) {
                        plainSelect.setJoins(null);
                    }
                }
            }
            // 优化 SQL
            plainSelect.setSelectItems(COUNT_SELECT_ITEM);
            return select.toString();
        } catch (JSQLParserException e) {
            // 无法优化使用原 SQL
            logger.warn("optimize this sql to a count sql has exception, sql:\"" + sql + "\", exception:\n" + e.getCause());
        } catch (Exception e) {
            logger.warn("optimize this sql to a count sql has error, sql:\"" + sql + "\", exception:\n" + e);
        }
        return lowLevelCountSql(sql);
    }

    @Override
    protected String lowLevelCountSql(String originalSql) {
        return String.format("SELECT COUNT(1) FROM (%s) TEMP", originalSql);
    }
    private static class MyFunction extends Function implements Expression{
        @Override
        public String toString() {
            String params;
            if (getParameters() != null || getNamedParameters() != null) {
                if (getParameters() != null) {
                    params = getParameters().toString();
                    if (isDistinct()) {
                        params = params.replaceFirst("\\(", "(DISTINCT ");
                    } else if (isAllColumns()) {
                        params = params.replaceFirst("\\(", "(ALL ");
                    }
                } else {
                    params = getNamedParameters().toString();
                }
            } else if (isAllColumns()) {
                params = "(*)";
            } else {
                params = "(1)";
            }

            String ans = getName() + "" + params + "";

            if (getAttribute() != null) {
                ans += "." + getAttribute().toString();
            } else if (getAttributeName() != null) {
                ans += "." + getAttributeName();
            }

            if (getKeep() != null) {
                ans += " " + getKeep().toString();
            }

            if (isEscaped()) {
                ans = "{fn " + ans + "}";
            }

            return ans;
        }
    }
}
