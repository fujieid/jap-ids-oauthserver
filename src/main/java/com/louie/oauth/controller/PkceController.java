package com.louie.oauth.controller;

import com.fujieid.jap.core.cache.JapCache;
import com.fujieid.jap.ids.JapIds;
import com.fujieid.jap.ids.exception.IdsException;
import com.fujieid.jap.ids.exception.InvalidClientException;
import com.fujieid.jap.ids.exception.UnsupportedGrantTypeException;
import com.fujieid.jap.ids.model.ClientDetail;
import com.fujieid.jap.ids.model.enums.ErrorResponse;
import com.fujieid.jap.ids.model.enums.GrantType;
import com.fujieid.jap.ids.util.OauthUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lapati5
 * @date 2021/8/9
 */
@Controller
@RequestMapping("/oauth/pkce")
public class PkceController {

    /**
     * 使用JapCache缓存code_verifier
     */
    private static JapCache codeVerifierCache;

    @RequestMapping("/authorize")
    public String pkce(@RequestParam("client_id") String clientId, String state) {
        ClientDetail clientDetail = getClientDetail(clientId);
        String codeVerifier = OauthUtil.generateCodeVerifier();
        long codeExpiresIn = OauthUtil.getCodeExpiresIn(clientDetail.getCodeExpiresIn());
        codeVerifierCache = JapIds.getContext().getCache();
        codeVerifierCache.set("JAPIDS:CODE:VERIFIER:" + clientId, codeVerifier, codeExpiresIn * 1000L);
        String codeChallengeMethod = clientDetail.getCodeChallengeMethod();
        String codeChallenge = OauthUtil.generateCodeChallenge(codeChallengeMethod, codeVerifier);
        System.out.println("codeVerifier = " + codeVerifier);
        System.out.println("codeChallenge = " + codeChallenge);
        System.out.println("codeChallengeMethod = " + codeChallengeMethod);
        return String.format("redirect:/oauth/authorize?client_id=%s&response_type=code&scope=%s&redirect_uri=%s&state=%s&code_challenge=%s&code_challenge_method=%s",
                clientDetail.getClientId(),
                clientDetail.getScopes(),
                clientDetail.getRedirectUri(),
                state,
                codeChallenge,
                codeChallengeMethod
        );
    }

    @RequestMapping("/token")
    public String pkce(@RequestParam("client_id") String clientId, String code, String state) {
        ClientDetail clientDetail = getClientDetail(clientId);
        codeVerifierCache = JapIds.getContext().getCache();
        return String.format("redirect:/oauth/token?grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s&code_verifier=%s",
                clientDetail.getClientId(),
                clientDetail.getRedirectUri(),
                code,
                codeVerifierCache.get("JAPIDS:CODE:VERIFIER:" + clientId)
        );
    }

    private ClientDetail getClientDetail(String clientId) {
        ClientDetail clientDetail = JapIds.getContext().getClientDetailService().getByClientId(clientId);
        if (null == clientDetail) {
            throw new InvalidClientException(ErrorResponse.INVALID_CLIENT);
        }
        if (!clientDetail.getGrantTypes().contains(GrantType.AUTHORIZATION_CODE.getType())) {
            throw new UnsupportedGrantTypeException(ErrorResponse.UNSUPPORTED_GRANT_TYPE);
        }
        if (null == clientDetail.getEnablePkce() || !clientDetail.getEnablePkce()) {
            throw new IdsException(ErrorResponse.AUTHORIZATION_FAILED);
        }
        return clientDetail;
    }
}
