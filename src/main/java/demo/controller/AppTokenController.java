package demo.controller;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import demo.config.CenterPlatformProperties;
import demo.domain.GeApptTokenResponse;
import demo.domain.RestResponse;
import demo.utils.TokenUtils;

@RestController
public class AppTokenController {
	
	@Autowired
    private CenterPlatformProperties openPlatformProperties;
	
	@Autowired
    private RestTemplate restTemplate;

	@GetMapping("/app/token")
	public RestResponse<Object, GeApptTokenResponse> getAppToken() {

		URI url = UriComponentsBuilder
                .fromHttpUrl(openPlatformProperties.getUrl() + "/auth/v1/app/inscope/token")
                .queryParam("app_id", openPlatformProperties.getAppId())
                .queryParam("scope", "file_preview,file_edit,file_format_control")//file_preview,file_edit,file_format_control
                .build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        GeApptTokenResponse res = restTemplate.exchange(url, HttpMethod.GET, request, GeApptTokenResponse.class).getBody();
        
        TokenUtils.setAppToken(res.getToken());
        
        RestResponse<Object, GeApptTokenResponse> rr = new RestResponse<>();
        rr.setUrl(url.toString());
		rr.setMethod("GET");
        rr.setResponse(res);
		return rr;
	}
	
}
