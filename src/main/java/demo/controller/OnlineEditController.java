package demo.controller;

import java.io.IOException;
import java.net.URI;
import javax.servlet.http.HttpServletResponse;


import demo.domain.ViewRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import demo.config.CenterPlatformProperties;
import demo.domain.RestResponse;
import demo.domain.online.edit.GetOnlineEditUrlResponse;
import demo.utils.TokenUtils;

@RestController
public class OnlineEditController {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private CenterPlatformProperties centerPlatformProperties;

	@GetMapping("/files/{fileId}/get-online-edit-url")
    public RestResponse<Object, GetOnlineEditUrlResponse> getFileVersion(@PathVariable String fileId, HttpServletResponse response) throws IOException {
		UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(centerPlatformProperties.getUrl() + "/weboffice/v1/url");

        builder.queryParam("app_token", TokenUtils.getAppToken().getAppToken());
        builder.queryParam("file_id", fileId);
        builder.queryParam("type", "w");

        URI url = builder.build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        GetOnlineEditUrlResponse res = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GetOnlineEditUrlResponse.class).getBody();
        
        RestResponse<Object, GetOnlineEditUrlResponse> rr = new RestResponse<>();
        rr.setUrl(url.toString());
		rr.setMethod("GET");
        rr.setResponse(res);
		return rr;
	}

    @GetMapping("/files/{fileId}/get-online-view-url")
    public ViewRestResponse<Object, GetOnlineEditUrlResponse> ViewgetFileVersion(@PathVariable String fileId, HttpServletResponse response) throws IOException {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(centerPlatformProperties.getUrl() + "/preview/v2/url");

        builder.queryParam("app_token", TokenUtils.getAppToken().getAppToken());
        builder.queryParam("file_id", fileId);
        builder.queryParam("scene_id", "asdoiufiouasdifuio");

        URI url = builder.build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        GetOnlineEditUrlResponse res = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GetOnlineEditUrlResponse.class).getBody();

        ViewRestResponse<Object, GetOnlineEditUrlResponse> rr = new ViewRestResponse<>();
        rr.setUrl(url.toString());
        rr.setMethod("GET");
        rr.setResponse(res);
        return rr;
    }
	
}
