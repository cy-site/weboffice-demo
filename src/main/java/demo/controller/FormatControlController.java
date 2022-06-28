package demo.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import demo.config.CenterPlatformProperties;
import demo.domain.RestResponse;
import demo.domain.format.control.CallImageOperateRequest;
import demo.domain.format.control.CallImageOperateResponse;
import demo.domain.format.control.CallOfficeConvertRequest;
import demo.domain.format.control.CallOfficeConvertResponse;
import demo.domain.format.control.CallOfficeMergeRequest;
import demo.domain.format.control.CallOfficeMergeResponse;
import demo.domain.format.control.CallOfficeOperateRequest;
import demo.domain.format.control.CallOfficeOperateResponse;
import demo.domain.format.control.CallOfficeSmartOfficialRequest;
import demo.domain.format.control.CallOfficeSmartOfficialResponse;
import demo.domain.format.control.CallOfficeSplitRequest;
import demo.domain.format.control.CallOfficeSplitResponse;
import demo.domain.format.control.CallOfficeWrapheaderRequest;
import demo.domain.format.control.CallOfficeWrapheaderResponse;
import demo.domain.format.control.GetTaskRequest;
import demo.domain.format.control.GetTaskResponse;
import demo.utils.TokenUtils;
import demo.utils.UrlUtils;

@RestController
public class FormatControlController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FormatControlController.class);
	
	private String imageFileName = "wps.png";
	private String officeOperateFileName = "office_operate.docx";
	private String officeWrapHeaderFileName = "office_wrapheader.docx";
	private String officeWrapHeaderTemplateFileName = "office_wrapheader_template.docx";
	private String officeSmartOfficialFileName = "office_smart_official.docx";
	private String officeMargePart1FileName = "office_marge_part1.docx";
	private String officeMargePart2FileName = "office_marge_part2.docx";
	private String officeSplitFileName = "office_split.docx";
	
	@Autowired
    private CenterPlatformProperties openPlatformProperties;
	
	@Autowired
    private RestTemplate restTemplate;
	
	@GetMapping("/office/operate")
    public RestResponse<CallOfficeOperateRequest, CallOfficeOperateResponse> callOfficeOperate(HttpServletRequest r) {
		URI url = UriComponentsBuilder
	            .fromHttpUrl(openPlatformProperties.getUrl() + "/cps/v1/office/operate")
	            .queryParam("app_token", TokenUtils.getAppToken().getAppToken())
	            .build()	            
	            .toUri();
	    
		String taskId = UUID.randomUUID().toString();
				
		CallOfficeOperateRequest.Step step1 = new CallOfficeOperateRequest.Step();
		step1.setOperate("OFFICE_CLEAN");
		
		CallOfficeOperateRequest.TextWatermark textWatermark = new CallOfficeOperateRequest.TextWatermark();
		textWatermark.setContent("WPS");
		
		CallOfficeOperateRequest.ImageWatermark imageWatermark = new CallOfficeOperateRequest.ImageWatermark();
		imageWatermark.setWatermarkUrl(UrlUtils.getSampleDownloadUrl(imageFileName, r));
		imageWatermark.setWatermarkFileName(imageFileName);
		
		CallOfficeOperateRequest.Args args = new CallOfficeOperateRequest.Args();
		args.setTextWatermark(textWatermark);
		args.setImageWatermark(imageWatermark);
		
		CallOfficeOperateRequest.Step step2 = new CallOfficeOperateRequest.Step();
		step2.setOperate("OFFICE_WATERMARK");
		step2.setArgs(args);
		
		CallOfficeOperateRequest req = new CallOfficeOperateRequest();
		req.setTaskId(taskId);
		req.setDocUrl(UrlUtils.getSampleDownloadUrl(officeOperateFileName, r));
		req.setDocFileName(officeOperateFileName);		
		req.setSteps(Arrays.asList(step1, step2));
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CallOfficeOperateRequest> requestEntity = new HttpEntity<>(req, headers);
        CallOfficeOperateResponse res = restTemplate.exchange(url, HttpMethod.POST, requestEntity, CallOfficeOperateResponse.class).getBody();
        
        RestResponse<CallOfficeOperateRequest, CallOfficeOperateResponse> rr = new RestResponse<>();
        rr.setUrl(url.toString());
		rr.setMethod("POST");
		rr.setRequest(req);
        rr.setResponse(res);
		return rr;
	}
	
	@GetMapping("/image/operate")
    public RestResponse<CallImageOperateRequest, CallImageOperateResponse> callImageOperate(HttpServletRequest r) {
		URI url = UriComponentsBuilder
	            .fromHttpUrl(openPlatformProperties.getUrl() + "/cps/v1/image/operate")
	            .queryParam("app_token", TokenUtils.getAppToken().getAppToken())
	            .build()	            
	            .toUri();
	    
		String taskId = UUID.randomUUID().toString();
		LOGGER.info(taskId);
				
		CallOfficeOperateRequest.Step step1 = new CallOfficeOperateRequest.Step();
		step1.setOperate("OFFICE_CLEAN");
		
		CallImageOperateRequest.TextWatermark textWatermark = new CallImageOperateRequest.TextWatermark();
		textWatermark.setContent("WPS");
		
		CallImageOperateRequest req = new CallImageOperateRequest();
		req.setTaskId(taskId);
		req.setImageUrl(UrlUtils.getSampleDownloadUrl(imageFileName, r));
		req.setImageFileName(imageFileName);
		req.setTextWatermark(textWatermark);
		
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CallImageOperateRequest> requestEntity = new HttpEntity<>(req, headers);
        CallImageOperateResponse res = restTemplate.exchange(url, HttpMethod.POST, requestEntity, CallImageOperateResponse.class).getBody();
        
        RestResponse<CallImageOperateRequest, CallImageOperateResponse> rr = new RestResponse<>();
        rr.setUrl(url.toString());
		rr.setMethod("POST");
		rr.setRequest(req);
        rr.setResponse(res);
		return rr;
	}
	
	@GetMapping("/office/wrapheader")
    public RestResponse<CallOfficeWrapheaderRequest, CallOfficeWrapheaderResponse> callOfficeWrapheader(HttpServletRequest r) {
		URI url = UriComponentsBuilder
	            .fromHttpUrl(openPlatformProperties.getUrl() + "/cps/v1/office/wrapheader")
	            .queryParam("app_token", TokenUtils.getAppToken().getAppToken())
	            .build()	            
	            .toUri();
	    
		String taskId = UUID.randomUUID().toString();
		LOGGER.info(taskId);
		
		CallOfficeWrapheaderRequest.Sample title1 = new CallOfficeWrapheaderRequest.Sample();
		title1.setBookmark("title1");
		title1.setType("TEXT");
		title1.setText("标题1");
		
		CallOfficeWrapheaderRequest.Sample title2 = new CallOfficeWrapheaderRequest.Sample();
		title2.setBookmark("title2");
		title2.setType("TEXT");
		title2.setText("标题2");

		CallOfficeWrapheaderRequest.Sample ctext = new CallOfficeWrapheaderRequest.Sample();
		ctext.setBookmark("ctext");
		ctext.setType("TEXT");
		ctext.setText("300.00");

		CallOfficeWrapheaderRequest.Sample content = new CallOfficeWrapheaderRequest.Sample();
		content.setBookmark("content");
		content.setType("DOCUMENT");
		content.setSampleUrl(UrlUtils.getSampleDownloadUrl(officeWrapHeaderFileName, r));
		content.setSampleFileName(officeWrapHeaderFileName);
		
		CallOfficeWrapheaderRequest.Sample image = new CallOfficeWrapheaderRequest.Sample();
		image.setBookmark("image");
		image.setType("IMAGE");
		image.setSampleUrl(UrlUtils.getSampleDownloadUrl(imageFileName, r));
		image.setSampleFileName(imageFileName);
				
		CallOfficeWrapheaderRequest req = new CallOfficeWrapheaderRequest();
		req.setTaskId(taskId);
		req.setTemplateUrl(UrlUtils.getSampleDownloadUrl(officeWrapHeaderTemplateFileName, r));
		req.setTemplateFileName(officeWrapHeaderTemplateFileName);		
		req.setSampleList(Arrays.asList(title1, title2,ctext, content, image));
		
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CallOfficeWrapheaderRequest> requestEntity = new HttpEntity<>(req, headers);
        CallOfficeWrapheaderResponse res = restTemplate.exchange(url, HttpMethod.POST, requestEntity, CallOfficeWrapheaderResponse.class).getBody();
        
        RestResponse<CallOfficeWrapheaderRequest, CallOfficeWrapheaderResponse> rr = new RestResponse<>();
        rr.setUrl(url.toString());
		rr.setMethod("POST");
		rr.setRequest(req);
        rr.setResponse(res);
		return rr;
	}
	
	@GetMapping("/office/merge")
	public RestResponse<CallOfficeMergeRequest, CallOfficeMergeResponse> callOfficeMerge(HttpServletRequest r) {
		URI url = UriComponentsBuilder
	            .fromHttpUrl(openPlatformProperties.getUrl() + "/cps/v1/office/merge")
	            .queryParam("app_token", TokenUtils.getAppToken().getAppToken())
	            .build()	            
	            .toUri();
	    
		String taskId = UUID.randomUUID().toString();
		
		CallOfficeMergeRequest.MergedFile part1 = new CallOfficeMergeRequest.MergedFile();
		part1.setDocUrl(UrlUtils.getSampleDownloadUrl(officeMargePart1FileName, r));
		part1.setDocFileName(officeMargePart1FileName);
		
		CallOfficeMergeRequest.MergedFile part2 = new CallOfficeMergeRequest.MergedFile();
		part2.setDocUrl(UrlUtils.getSampleDownloadUrl(officeMargePart2FileName, r));
		part2.setDocFileName(officeMargePart2FileName);		
		
		CallOfficeMergeRequest req = new CallOfficeMergeRequest();
		req.setTaskId(taskId);
		req.setMergedLileList(Arrays.asList(part1, part2));
		
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CallOfficeMergeRequest> requestEntity = new HttpEntity<>(req, headers);
        CallOfficeMergeResponse res = restTemplate.exchange(url, HttpMethod.POST, requestEntity, CallOfficeMergeResponse.class).getBody();
		
        RestResponse<CallOfficeMergeRequest, CallOfficeMergeResponse> rr = new RestResponse<>();
        rr.setUrl(url.toString());
		rr.setMethod("POST");
		rr.setRequest(req);
        rr.setResponse(res);
		return rr;
	}
	
	@GetMapping("/office/split")
	public RestResponse<CallOfficeSplitRequest, CallOfficeSplitResponse> callOfficeSplit(HttpServletRequest r) {
		URI url = UriComponentsBuilder
	            .fromHttpUrl(openPlatformProperties.getUrl() + "/cps/v1/office/split")
	            .queryParam("app_token", TokenUtils.getAppToken().getAppToken())            
	            .build()	            
	            .toUri();
	    
		String taskId = UUID.randomUUID().toString();
		
		CallOfficeSplitRequest.Range range = new CallOfficeSplitRequest.Range();
		range.setStart(1);
		range.setEnd(2);
		
		CallOfficeSplitRequest.Average average = new CallOfficeSplitRequest.Average(); 
		average.setSize(1);
		average.setSplitRange(range);
		
		CallOfficeSplitRequest req = new CallOfficeSplitRequest();
		req.setTaskId(taskId);
		req.setDocUrl(UrlUtils.getSampleDownloadUrl(officeSplitFileName, r));
		req.setDocFileName(officeSplitFileName);
		req.setType("AVERAGE");
		req.setAverage(average);
		
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CallOfficeSplitRequest> requestEntity = new HttpEntity<>(req, headers);
        CallOfficeSplitResponse res = restTemplate.exchange(url, HttpMethod.POST, requestEntity, CallOfficeSplitResponse.class).getBody();
        
        RestResponse<CallOfficeSplitRequest, CallOfficeSplitResponse> rr = new RestResponse<>();
        rr.setUrl(url.toString());
		rr.setMethod("POST");
		rr.setRequest(req);
        rr.setResponse(res);
		return rr;
	}
	
	@GetMapping("/office/convert")
    public RestResponse<CallOfficeConvertRequest, CallOfficeConvertResponse> callOfficeConvert(HttpServletRequest r) {
		URI url = UriComponentsBuilder
	            .fromHttpUrl(openPlatformProperties.getUrl() + "/cps/v1/office/convert")
	            .queryParam("app_token", TokenUtils.getAppToken().getAppToken())	            
	            .build()	            
	            .toUri();
	    
		String taskId = UUID.randomUUID().toString();
				
		CallOfficeConvertRequest req = new CallOfficeConvertRequest();
		req.setTaskId(taskId);
		req.setCallback("http://192.168.31.135:8086/cps/callback");
		req.setDocUrl(UrlUtils.getSampleDownloadUrl(officeOperateFileName, r));
		req.setDocFileName(officeOperateFileName);		
		req.setTargetFileFormat("pdf");
		
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CallOfficeConvertRequest> requestEntity = new HttpEntity<>(req, headers);
        CallOfficeConvertResponse res = restTemplate.exchange(url, HttpMethod.POST, requestEntity, CallOfficeConvertResponse.class).getBody();
		
        RestResponse<CallOfficeConvertRequest, CallOfficeConvertResponse> rr = new RestResponse<>();
        rr.setUrl(url.toString());
		rr.setMethod("POST");
		rr.setRequest(req);
        rr.setResponse(res);
		return rr;
	}
	@PostMapping("/cps/callback")
	public RestResponse<CallOfficeConvertRequest, CallOfficeConvertResponse> callcpscallback(HttpServletRequest r) {

		System.out.println("回调接口成功");
		return null;
	}
	
	
	
	@GetMapping("/office/smartofficial")
	public RestResponse<CallOfficeSmartOfficialRequest, CallOfficeSmartOfficialResponse> callOfficeSmartOfficial(HttpServletRequest r) {
		URI url = UriComponentsBuilder
	            .fromHttpUrl(openPlatformProperties.getUrl() + "/cps/v1/office/smartofficial")
	            .queryParam("app_token", TokenUtils.getAppToken().getAppToken())	            
	            .build()	            
	            .toUri();
	    
		String taskId = UUID.randomUUID().toString();
		
		CallOfficeSmartOfficialRequest req = new CallOfficeSmartOfficialRequest();
		req.setTaskId(taskId);
		req.setDocUrl(UrlUtils.getSampleDownloadUrl(officeSmartOfficialFileName, r));
		req.setDocFileName(officeSmartOfficialFileName);
		//req.setTemplateUrl("");
		//req.setTemplateFileName("");
		req.setPreInstallTemplate("announcement_ds_ls1");
		req.setFieldConfig("none");
		
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CallOfficeSmartOfficialRequest> requestEntity = new HttpEntity<>(req, headers);
        CallOfficeSmartOfficialResponse res = restTemplate.exchange(url, HttpMethod.POST, requestEntity, CallOfficeSmartOfficialResponse.class).getBody();
        
        RestResponse<CallOfficeSmartOfficialRequest, CallOfficeSmartOfficialResponse> rr = new RestResponse<>();
        rr.setUrl(url.toString());
		rr.setMethod("POST");
		rr.setRequest(req);
        rr.setResponse(res);
		return rr;
	}
	
	@GetMapping("/tasks/{taskId}")
    public RestResponse<GetTaskRequest, GetTaskResponse> getTask(@PathVariable String taskId) {
		URI url = UriComponentsBuilder
	            .fromHttpUrl(openPlatformProperties.getUrl() + "/cps/v1/task/query")
	            .queryParam("app_token", TokenUtils.getAppToken().getAppToken())
	            .build()	            
	            .toUri();
	    
		GetTaskRequest req = new GetTaskRequest();
		req.setTaskId(taskId);
		
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GetTaskRequest> requestEntity = new HttpEntity<>(req, headers);
        GetTaskResponse res = restTemplate.exchange(url, HttpMethod.POST, requestEntity, GetTaskResponse.class).getBody();
		
        RestResponse<GetTaskRequest, GetTaskResponse> rr = new RestResponse<>();
        rr.setUrl(url.toString());
		rr.setMethod("POST");
		rr.setRequest(req);
        rr.setResponse(res);
		return rr;
	}
	
	@GetMapping("/downloads/{downloadId}")
    public ResponseEntity<Resource> getDownloadUrl(@PathVariable String downloadId) {
		URI url = UriComponentsBuilder
	            .fromHttpUrl(openPlatformProperties.getUrl() + "/cps/v1/download/file/{downloadId}")
	            .queryParam("app_token", TokenUtils.getAppToken().getAppToken())
	            .build()
	            .expand(downloadId)
	            .toUri();
	    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        Resource resource = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Resource.class).getBody();
    
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
}
