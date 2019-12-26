package com.multitenant.arc.config.multitenant;

import com.multitenant.arc.config.Constants;
import com.multitenant.arc.core.exceptions.TokenExpiredException;
import com.multitenant.arc.security.TokenHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestInterceptor.class);

    @Autowired
    private TokenHelper tokenHelper;

    /**
     * It will intercept request and will fetch tenant id from
     * token and will set it in TextContext for current thread.
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String apiUrl = request.getRequestURL().substring(request.getRequestURL().indexOf(Constants.API_URL) + Constants.API_URL.length());
        if(isUrlAllowedWithoutAuthorization(apiUrl)){
            return true;
        }

        String authToken = tokenHelper.getToken(request);
        if(authToken == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new AuthenticationException("Unauthorised access");
        }else{
            if(tokenHelper.isExpired(authToken)){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                throw new TokenExpiredException("Token expired. Please login again.");
            }
            String tenantId = tokenHelper.getTenantFromToken(authToken);
            TenantContext.setCurrentTenant(tenantId);
        }
        return true;
    }

    private boolean isUrlAllowedWithoutAuthorization(String requestUrl){
        for(String url : Constants.ALLOW_URL){
            if(requestUrl.contains(url)){
                return true;
            }
        }
        return false;
    }

    /**
     * After completing task this will clear the tenant id from TextContext for current thread.
     * */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        TenantContext.clearTenant();
        LOGGER.info("Cleared tenant value");
    }
}
