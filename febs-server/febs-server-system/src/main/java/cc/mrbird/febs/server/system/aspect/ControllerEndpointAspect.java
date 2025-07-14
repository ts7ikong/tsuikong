package cc.mrbird.febs.server.system.aspect;

import cc.mrbird.febs.common.core.annotation.OperateLog;
import cc.mrbird.febs.common.core.entity.Operate;
import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityplan;
import cc.mrbird.febs.common.core.entity.tjdkxm.Qualityproblem;
import cc.mrbird.febs.common.core.entity.tjdkxm.Safeplan;
import cc.mrbird.febs.common.core.entity.tjdkxm.Safeproblem;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.server.system.configure.LogRecordContext;
import cc.mrbird.febs.server.system.service.ILogService;
import cc.mrbird.febs.server.system.service.OperateService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author MrBird
 */
@Aspect
@Slf4j
@Order(2)
@Component
public class ControllerEndpointAspect extends BaseAspectSupport {
    @Autowired
    private OperateService operateService;
    @Autowired
    private ILogService logService;
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    private LogRecordContext logRecordContext;

    @Pointcut("execution(* cc.mrbird.febs.server.system.controller.*.*(..)) ")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws FebsException {
        Object result;
        Method targetMethod = resolveMethod(point);
        ApiOperation apiOperation = targetMethod.getAnnotation(ApiOperation.class);
        OperateLog operateLog = targetMethod.getAnnotation(OperateLog.class);
        String operation = null;
        if (apiOperation != null) {
            operation = apiOperation.value();
        }
        String id = null;
        Object projectId = null;
        Object mapper = null;
        Method selectInfoById = null;
        JSONObject param = null;
        Operate operate = new Operate();
        try {
            if (operateLog != null) {
                String type = operateLog.type();
                Class<?> aClass = operateLog.className();
                Class<?> mapper1 = operateLog.mapper();
                String fieldIdName = null;
                Field[] declaredFields = aClass.getDeclaredFields();
                //找到表id
                for (Field field : declaredFields) {
                    TableId annotation = field.getAnnotation(TableId.class);
                    if (annotation != null) {
                        fieldIdName = field.getName();
                    }
                }
                // 获取入参
                Object[] args = point.getArgs();
                for (Object arg : args) {
                    if (arg.getClass().equals(aClass)) {
                        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(arg));
                        id = jsonObject.getString(fieldIdName);
//                        if (type.equals(Operate.TYPE_MODIFY)) {
//                            type = getTypeByTYPE_MODIFY(arg, jsonObject);
//                        }
                    }
                }
                selectInfoById = mapper1.getMethod("selectInfoById", Long.class);
                mapper = sqlSession.getMapper(mapper1);
                if (id != null) {
                    param = (JSONObject) selectInfoById.invoke(mapper, Long.valueOf(id));
                    System.out.println("param = " + param);
                }
                operate = operateService.create(param, type, id, FebsUtil.getCurrentUserId());
            }
        } catch (Exception e) {
            selectInfoById = null;
            mapper = null;
        }
        long start = System.currentTimeMillis();
        try {
            result = point.proceed();
            String username = FebsUtil.getCurrentUsername();
            String ip = FebsUtil.getHttpServletRequestIpAddress();
            logService.saveLog(point, targetMethod, ip, operation, username, start);
            return result;
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
            String message = throwable.getMessage();
            operate.setOperateState(Operate.STATE_FAIL);
            operate.setOperateFailmsg(message);
            throw new FebsException(message);
        } finally {
            if (operateLog != null) {
                try {
                    if (operate.getOperateFailmsg() == null) {
                        if (id == null) {
                            id = logRecordContext.getVariable("id").toString();
                        }
                        assert mapper != null;
                        assert selectInfoById != null;
                        param = (JSONObject) selectInfoById.invoke(mapper, Long.valueOf(id));
                        try {
                            projectId = logRecordContext.getVariable("projectId");
                        } catch (Exception e) {
                            projectId = null;
                        }
                        operate.setOperateAfter(param);
                        operate.setOperateState(Operate.STATE_SUCCESS);
                    }
                } catch (Exception e) {
                    String message = e.getMessage();
                    operate.setOperateState(Operate.STATE_FAIL);
                    operate.setOperateFailmsg(message);
                }
                operateService.updateOperate(operate, id, projectId);
            }
        }
    }

    private String getTypeByTYPE_MODIFY(Object arg, JSONObject jsonObject) {
        String type = Operate.TYPE_MODIFY;
        if (arg instanceof Safeplan || arg instanceof Qualityplan) {
            String state = "";
            //安全计划
            if (arg instanceof Safeplan) {
                state = jsonObject.getString("safeplanCheckstate");
            }
            //质量计划
            if (arg instanceof Qualityplan) {
                state = jsonObject.getString("qualityplanCheckstate");
            }
            switch (state) {
                case "1":
                    type = Operate.TYPE_CHECK;
                    break;
                case "2":
                case "3":
                    type = Operate.TYPE_ACCEPTANCE;
                    break;
                default:
                    break;
            }
        }
        if (arg instanceof Qualityproblem || arg instanceof Safeproblem) {
            String state = "";
            //质量管理
            if (arg instanceof Qualityproblem) {
                state = jsonObject.getString("qualityproblenState");

            }
            //安全隐患
            if (arg instanceof Safeproblem) {
                state = jsonObject.getString("safeproblenState");
            }
            switch (state) {
                case "2":
                    type = Operate.TYPE_DISTRIBUTE;
                    break;
                case "3":
                    type = Operate.TYPE_RECTIFICATION;
                    break;
                case "4":
                case "5":
                    type = Operate.TYPE_ACCEPTANCE;
                    break;
                default:
                    break;
            }
        }

        return type;
    }

    public String captureName(String name) {
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        return name;
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "ex")
    public void aspectAfterThrowing(JoinPoint jp, Exception ex) {
        String methodName = jp.getSignature().getName();
        log.error("[异常通知]" + methodName + "方法异常：" + ex.getMessage());
    }
}



