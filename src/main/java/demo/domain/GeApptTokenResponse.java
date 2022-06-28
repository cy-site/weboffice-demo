package demo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeApptTokenResponse {

    @JsonProperty("result")
    private int result;
    
    @JsonProperty("token")
    private AppToken token;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public AppToken getToken() {
        return token;
    }

    public void setToken(AppToken token) {
        this.token = token;
    }
        
}
