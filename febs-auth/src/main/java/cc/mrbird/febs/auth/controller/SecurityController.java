package cc.mrbird.febs.auth.controller;

import cc.mrbird.febs.auth.service.ValidateCodeService;
import cc.mrbird.febs.common.core.exception.ValidateCodeException;
import cc.mrbird.febs.common.core.utils.FebsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * @author MrBird
 */
@Controller
@RequiredArgsConstructor
public class SecurityController {

    private final ValidateCodeService validateCodeService;


    @ResponseBody
    @GetMapping("user")
    public Principal currentUser(Principal principal) {
        return principal;
    }

    @ResponseBody
    @GetMapping("captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
        validateCodeService.create(request, response);
    }

    @Autowired
    private TokenStore tokenStore;

    @RequestMapping(value = "/remove-token", method = RequestMethod.POST)
    @ResponseBody
    public String logout() {
//        OAuth2AccessToken accessToken = tokenStore.readAccessToken(FebsUtil.getCurrentTokenValue());
//        tokenStore.removeAccessToken(accessToken);
        System.out.println("执行了....");
        return null;
    }
}
