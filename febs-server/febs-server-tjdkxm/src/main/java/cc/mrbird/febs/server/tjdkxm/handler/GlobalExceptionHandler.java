package cc.mrbird.febs.server.tjdkxm.handler;

import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.constant.StringConstant;
import cc.mrbird.febs.common.core.handler.BaseExceptionHandler;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author MrBird
 */
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler extends BaseExceptionHandler {
    private static final Pattern P = Pattern.compile("(.*) '(.*)' to required type '(.*)' for property '(.*)'");

    @ExceptionHandler(value = UndeclaredThrowableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public FebsResponse undeclaredException(UndeclaredThrowableException e) {
        String message1 = e.getUndeclaredThrowable().getMessage();
        String message = FebsUtil.containChinese(message1) ? message1 : "系统内部异常";
        log.error(message, e);
        return new FebsResponse().message(message);
    }

    /**
     * 忽略参数异常处理器
     *
     * @param e 忽略参数异常
     * @return ResponseResult
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public FebsResponse parameterMissingExceptionHandler(MissingServletRequestParameterException e) {
        return new FebsResponse().message("请求参数不能为空");
    }

    /**
     * 缺少请求体异常处理器
     *
     * @param e 缺少请求体异常
     * @return ResponseResult
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public FebsResponse parameterBodyMissingExceptionHandler(HttpMessageNotReadableException e) {
        log.error("", e);
        return new FebsResponse().message("参数体不能为空");
    }

    /**
     * 参数效验异常处理器
     *
     * @param e 参数验证异常
     * @return ResponseInfo
     */
    @Override
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FebsResponse handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("", e);
        // 获取异常信息
        BindingResult exceptions = e.getBindingResult();
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (!errors.isEmpty()) {
                // 这里列出了全部错误参数，按正常逻辑，只需要第一条错误即可
                FieldError fieldError = (FieldError)errors.get(0);
                return new FebsResponse().message(fieldError.getDefaultMessage());
            }
        }
        return new FebsResponse().message("方法参数无效");
    }

    /**
     * 统一处理请求参数校验(实体对象传参)
     *
     * @param e BindException
     * @return FebsResponse
     */
    @Override
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FebsResponse handleBindException(BindException e) {
        String message = "";
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        message = fieldErrors.get(0).getDefaultMessage();
        if (P.matcher(message).matches()) {
            return new FebsResponse().message("格式错误");
        }
        return new FebsResponse().message(fieldErrors.get(0).getDefaultMessage());
    }
}
