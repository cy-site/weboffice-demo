package demo.utils;

import demo.domain.AppToken;

public class TokenUtils {
    
    private static final String APP_TOKEN_KEY = "AppToken";
    
    public static void setAppToken(AppToken appToken) {
        RequestContextUtils.getRequest().getServletContext().setAttribute(APP_TOKEN_KEY, appToken);
    }
    
    public static AppToken getAppToken() {
        return (AppToken)RequestContextUtils.getRequest().getServletContext().getAttribute(APP_TOKEN_KEY);
    }
        
}
