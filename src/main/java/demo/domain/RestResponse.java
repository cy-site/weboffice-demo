package demo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse<X, Y> {
		
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("method")
	private String method;
	
	@JsonProperty("request")
	private X request;
	
	@JsonProperty("response")
	private Y response;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public X getRequest() {
		return request;
	}

	public void setRequest(X request) {
		this.request = request;
	}

	public Y getResponse() {
		return response;
	}

	public void setResponse(Y response) {
		this.response = response;
	}
	
}
