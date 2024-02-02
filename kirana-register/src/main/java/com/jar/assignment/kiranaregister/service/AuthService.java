package com.jar.assignment.kiranaregister.service;

import com.jar.assignment.kiranaregister.dto.AuthDto;
import com.jar.assignment.kiranaregister.exception.CustomException;
import com.jar.assignment.kiranaregister.utils.AuthSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AuthService {
    private final List<String> mobileApiKeys;
    private final List<String> mobileAuthKeys;
    private final List<String> internalApiKeys;
    private final List<String> internalAuthKeys;
    private final List<String> webApiKeys;
    private final List<String> webAuthKeys;

    private final Boolean isMobileKeyValidationEnabled;
    private final Boolean isInternalKeyValidationEnabled;
    private final Boolean isWebKeyValidationEnabled;


    @Autowired
    public AuthService(@Value("${ntb.mobile.api.keys}") List<String> mobileApiKeys,
                       @Value("${ntb.mobile.auth.keys}") List<String> mobileAuthKeys,
                       @Value("${ntb.internal.api.keys}") List<String> internalApiKeys,
                       @Value("${ntb.internal.auth.keys}") List<String> internalAuthKeys,
                       @Value("${ntb.web.api.keys}") List<String> webApiKeys,
                       @Value("${ntb.web.auth.keys}") List<String> webAuthKeys,
                       @Value("${ntb.mobile.key.validation.enabled}") Boolean isMobileKeyValidationEnabled,
                       @Value("${ntb.internal.key.validation.enabled}") Boolean isInternalKeyValidationEnabled,
                       @Value("${ntb.web.key.validation.enabled}") Boolean isWebKeyValidationEnabled) {
        this.mobileApiKeys = mobileApiKeys;
        this.mobileAuthKeys = mobileAuthKeys;
        this.internalApiKeys = internalApiKeys;
        this.internalAuthKeys = internalAuthKeys;
        this.webApiKeys=webApiKeys;
        this.webAuthKeys=webAuthKeys;
        this.isMobileKeyValidationEnabled = isMobileKeyValidationEnabled;
        this.isInternalKeyValidationEnabled = isInternalKeyValidationEnabled;
        this.isWebKeyValidationEnabled=isWebKeyValidationEnabled;
    }
    public void doHeaderKeyAuth(AuthDto authDto) {
        if (matchHeaderKeys(authDto)) {
            return;
        }
        throw new CustomException("401","Authorization failed.");
    }
    public boolean matchHeaderKeys(AuthDto ntbAuthDto) {
        AuthSource source = ntbAuthDto.getAuthSource();
        String headerApiKey = ntbAuthDto.getRequest().getHeader("X-API-KEY");
        String headerAuthKey = ntbAuthDto.getRequest().getHeader("X-AUTH-KEY");
        switch (source) {
            case INTERNAL:
                if (isInternalKeyValidationEnabled) {
                    return validateKeys(internalApiKeys, internalAuthKeys, headerApiKey, headerAuthKey);
                }
                break;
            case WEB:
                if (isWebKeyValidationEnabled) {
                    return validateKeys(webApiKeys,webAuthKeys, headerApiKey, headerAuthKey);
                }
                break;
            case MOBILE:
                if (isMobileKeyValidationEnabled) {
                    return validateKeys(mobileApiKeys,mobileAuthKeys, headerApiKey, headerAuthKey);
                }
                break;

            default:
                return true;
        }
        return true;
    }

    private boolean validateKeys(List<String> apiKeys, List<String> authKeys, String apiKey, String authKey) {
        if (!CollectionUtils.isEmpty(apiKeys) && !CollectionUtils.isEmpty(authKeys)) {
            return StringUtils.hasText(apiKey) && apiKeys.contains(apiKey) &&
                    StringUtils.hasText(authKey) && authKeys.contains(authKey);
        } else if (!CollectionUtils.isEmpty(apiKeys)) {
            return StringUtils.hasText(apiKey) && apiKeys.contains(apiKey);
        } else if (!CollectionUtils.isEmpty(authKeys)) {
            return StringUtils.hasText(authKey) && authKeys.contains(authKey);
        }
        return true;
    }
}
