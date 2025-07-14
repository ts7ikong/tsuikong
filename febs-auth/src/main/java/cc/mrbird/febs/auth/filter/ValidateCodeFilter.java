package cc.mrbird.febs.auth.filter;

import cc.mrbird.febs.auth.mapper.UserMapper;
import cc.mrbird.febs.auth.service.ValidateCodeService;
import cc.mrbird.febs.common.core.entity.FebsResponse;
import cc.mrbird.febs.common.core.entity.constant.*;
import cc.mrbird.febs.common.core.exception.FebsException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import cc.mrbird.febs.common.core.utils.RSAUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.checkerframework.checker.units.qual.K;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * 验证码过滤器
 *
 * @author MrBird
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

    private final ValidateCodeService validateCodeService;

    private final UserMapper userMapper;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest httpServletRequest, @Nonnull HttpServletResponse httpServletResponse,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
        String header = httpServletRequest.getHeader(ParamsConstant.LOGIN_TYPE);
        RequestMatcher matcher = new AntPathRequestMatcher(EndpointConstant.OAUTH_TOKEN, HttpMethod.POST.toString());
        if (matcher.matches(httpServletRequest)
                && StringUtils.equalsIgnoreCase(httpServletRequest.getParameter(ParamsConstant.GRANT_TYPE), GrantTypeConstant.PASSWORD)) {
            try {
//                Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
//                Map<String, Object> result = Maps.newLinkedHashMap();
//                parameterMap.entrySet().stream().sorted(Map.Entry.<String, String[]>comparingByKey())
//                        .forEachOrdered(e -> result.put(e.getKey(), e.getValue()[0]));
//                JSONObject jsonObject = new JSONObject(result);
//                jsonObject.remove("sign");
////                System.out.println("RSAConstant.PUBLIC_KEY = " + RSAConstant.PUBLIC_KEY);
////                System.out.println("RSAConstant.PRIVATE_KEY = " + RSAConstant.PRIVATE_KEY);
//                System.out.println(jsonObject);
//                String encrypt = RSAUtils.encrypt(jsonObject.toJSONString());
//                System.out.println(encrypt);
//                System.out.println(parameterMap.get("sign")[0]);
//                if (Objects.equals(encrypt, parameterMap.get("sign")[0])){
//                    System.out.println(132456);
//                }
//              //  String decrypt = RSAUtils.decrypt(parameterMap.get("sign")[0]);
                if (!FebsConstant.APP.equals(header)) {
                    validateCode(httpServletRequest);
                }
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } catch (Exception e) {
                FebsResponse febsResponse = new FebsResponse();
                FebsUtil.makeFailureResponse(httpServletResponse, febsResponse.message(e.getMessage()));
                log.error(e.getMessage(), e);
            }
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }


    private void validateCode(HttpServletRequest httpServletRequest) throws FebsException {
        String code = httpServletRequest.getParameter(ParamsConstant.VALIDATE_CODE_CODE);
        String key = httpServletRequest.getParameter(ParamsConstant.VALIDATE_CODE_KEY);
        validateCodeService.check(key, code);
    }
}
