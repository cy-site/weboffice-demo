package demo.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import demo.domain.online.edit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import demo.service.storage.StorageProperties;
import demo.service.storage.StorageService;
import demo.utils.UrlUtils;

@RestController
public class OnlineEditCallbackController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OnlineEditCallbackController.class);
	
	@Autowired
	private StorageProperties storageProperties;
	
	@Autowired
	private StorageService storageService;
	
	private String fileId = "F001";
	
	private String fileNameV1 = "online_edit_v1.docx";		
	private String fileNameV2 = "test04.docx";
	
	private String zhangsanId = "U001";
	private String zhangsanName = "张三";
	private String zhangsanAvatar = "zhangsan.jpg";
	
	private String lisiId = "U002";
	private String lisiName = "李四";
	private String lisiAvatar = "lisi.jpg";
	
	private long getSize(String fileName) {
		Path path = Paths.get(storageProperties.getLocation()).resolve(fileName);
		File file = new File(path.toUri());
		return file.length();
	}
	
	@GetMapping("/v1/3rd/file/info")
    public GetFileResponse getFile(

    		@RequestParam("_w_third_appid") String wThirdAppId,
    		@RequestParam("_w_third_file_id") String wThirdFileId,
    		@RequestHeader(name = "x-weboffice-file-id", required = false) String xWebofficeFileId, 
    		@RequestHeader(name = "x-wps-weboffice-token", required = false) String xWpsWebofficeToken, 
    		HttpServletRequest r) {
	    
		LOGGER.info("url:[{}], _w_third_appid:[{}], _w_third_file_id:[{}], x-weboffice-file-id:[{}], x-wps-weboffice-token:[{}]", 
				"/v1/3rd/file/info", wThirdAppId, wThirdFileId, xWebofficeFileId, xWpsWebofficeToken);
		
		GetFileResponse.File.UserAccessControlList userAcl = new GetFileResponse.File.UserAccessControlList();
		userAcl.setRename(1);
		userAcl.setHistory(1);
		userAcl.setCopy(1);
		userAcl.setExport(1);
		userAcl.setPrint(1);
		
		GetFileResponse.File.Watermark watermark = new GetFileResponse.File.Watermark();
		watermark.setType(1);
		watermark.setValue("测试");
		watermark.setFillstyle("rgba(192,192,192,0.2)");
			
		GetFileResponse.File file = new GetFileResponse.File();
		file.setId(fileId);
		file.setName(fileNameV2);
		file.setVersion(2);
		file.setSize(getSize(fileNameV2));
		file.setCreator(zhangsanId);
		file.setCreateTime(System.currentTimeMillis());
		file.setModifier(zhangsanId);
		file.setModifyTime(System.currentTimeMillis());
		file.setDownloadUrl(UrlUtils.getSampleDownloadUrl(fileNameV2, r));
		file.setPreview_pages(10);
		file.setUserAcl(userAcl);
		file.setWatermark(watermark);
		
		GetFileResponse.User user = new GetFileResponse.User();
		user.setId(lisiId);
		user.setName(lisiName);
		user.setPermission("write");
		user.setAvatarUrl(UrlUtils.getSampleDownloadUrl(lisiAvatar, r));
		
		GetFileResponse getFileResponse = new GetFileResponse();
		getFileResponse.setFile(file);
		getFileResponse.setUser(user);
		return getFileResponse;       
    }
	
	@PostMapping("/v1/3rd/user/info")
    public GetUserResponse getUser(
    		@RequestParam("_w_third_appid") String wThirdAppId,
    		@RequestParam("_w_third_file_id") String wThirdFileId,
    		@RequestHeader(name = "x-weboffice-file-id", required = false) String xWebofficeFileId, 
    		@RequestHeader(name = "x-wps-weboffice-token", required = false) String xWpsWebofficeToken,
    		@RequestBody GetUserInfoRequest getUserInfoRequest,
    		HttpServletRequest r) {
		
		LOGGER.info("url:[{}], _w_third_appid:[{}], _w_third_file_id:[{}], x-weboffice-file-id:[{}], x-wps-weboffice-token:[{}], ids:[{}]", 
				"/v1/3rd/user/info", wThirdAppId, wThirdFileId, xWebofficeFileId, xWpsWebofficeToken, Arrays.toString(getUserInfoRequest.getIds()));
		
		GetUserResponse.User zhangsan = new GetUserResponse.User();
		zhangsan.setId(zhangsanId);
		zhangsan.setName(zhangsanName);
		zhangsan.setAvatarUrl(UrlUtils.getSampleDownloadUrl(zhangsanAvatar, r));
		
		GetUserResponse.User lisi = new GetUserResponse.User();
		lisi.setId(lisiId);
		lisi.setName(lisiName);
		lisi.setAvatarUrl(UrlUtils.getSampleDownloadUrl(lisiAvatar, r));
		
		GetUserResponse getUserResponse = new GetUserResponse();
		getUserResponse.setUsers(Arrays.asList(lisi, zhangsan));
		
		return getUserResponse;
    }
	
	@PostMapping("/v1/3rd/file/online")
    public void online(
    		@RequestParam("_w_third_appid") String wThirdAppId,
    		@RequestParam("_w_third_file_id") String wThirdFileId,
    		@RequestHeader(name = "x-weboffice-file-id", required = false) String xWebofficeFileId, 
    		@RequestHeader(name = "x-wps-weboffice-token", required = false) String xWpsWebofficeToken,
    		@RequestBody GetUserInfoRequest getUserInfoRequest) {
		
		LOGGER.info("url:[{}], _w_third_appid:[{}], _w_third_file_id:[{}], x-weboffice-file-id:[{}], x-wps-weboffice-token:[{}], ids:[{}]", 
				"/v1/3rd/file/online", wThirdAppId, wThirdFileId, xWebofficeFileId, xWpsWebofficeToken, Arrays.toString(getUserInfoRequest.getIds()));	
	}
	
	@PostMapping("/v1/3rd/file/save")
    public SaveFileResponse save(
    		@RequestParam("_w_third_appid") String wThirdAppId,
    		@RequestParam("_w_third_file_id") String wThirdFileId,
    		@RequestHeader(name = "x-weboffice-file-id", required = false) String xWebofficeFileId, 
    		@RequestHeader(name = "x-wps-weboffice-token", required = false) String xWpsWebofficeToken,
    		@RequestParam("file") MultipartFile file, 
    		HttpServletRequest r) {
		
		LOGGER.info("url:[{}], _w_third_appid:[{}], _w_third_file_id:[{}], x-weboffice-file-id:[{}], x-wps-weboffice-token:[{}]", 
				"/v1/3rd/file/save", wThirdAppId, wThirdFileId, xWebofficeFileId, xWpsWebofficeToken);
					
		storageService.store(file);
					
		SaveFileResponse.File fileInfo = new SaveFileResponse.File();
		fileInfo.setId(wThirdFileId);
		fileInfo.setName(fileNameV2);
		fileInfo.setSize(getSize(fileNameV2));
		fileInfo.setVersion(2);
		fileInfo.setDownloadUrl(UrlUtils.getSampleDownloadUrl(fileNameV2, r));
		
		SaveFileResponse saveFileResponse = new SaveFileResponse();
		saveFileResponse.setFile(fileInfo);
		
		return saveFileResponse;
	}
	
	@GetMapping("/v1/3rd/file/version/{version}")
    public GetFileVersionResponse getFileVersion(
    		@RequestParam("_w_third_appid") String wThirdAppId,
    		@RequestParam("_w_third_file_id") String wThirdFileId,
    		@RequestHeader(name = "x-weboffice-file-id", required = false) String xWebofficeFileId, 
    		@RequestHeader(name = "x-wps-weboffice-token", required = false) String xWpsWebofficeToken,
    		@PathVariable int version, 
    		HttpServletRequest r) {
		
		LOGGER.info("url:[{}], _w_third_appid:[{}], _w_third_file_id:[{}], x-weboffice-file-id:[{}], x-wps-weboffice-token:[{}], version:[{}]", 
				"/v1/3rd/file/version", wThirdAppId, wThirdFileId, xWebofficeFileId, xWpsWebofficeToken, version);
		
		GetFileVersionResponse.File file = new GetFileVersionResponse.File();
		
		if (version == 1) { 
			file.setId(wThirdFileId);
			file.setName(fileNameV1);
			file.setVersion(1);
			file.setSize(getSize(fileNameV1));
			file.setCreator(zhangsanId);
			file.setCreateTime(System.currentTimeMillis());
			file.setModifier(zhangsanId);
			file.setModifyTime(System.currentTimeMillis());
			file.setDownloadUrl(UrlUtils.getSampleDownloadUrl(fileNameV1, r));
		} else {
			file.setId(wThirdFileId);
			file.setName(fileNameV2);
			file.setVersion(2);
			file.setSize(getSize(fileNameV2));
			file.setCreator(lisiId);
			file.setCreateTime(System.currentTimeMillis());
			file.setModifier(lisiId);
			file.setModifyTime(System.currentTimeMillis());
			file.setDownloadUrl(UrlUtils.getSampleDownloadUrl(fileNameV2, r));
		}
		
		GetFileVersionResponse getFileResponse = new GetFileVersionResponse();
		getFileResponse.setFile(file);
				
		return getFileResponse;
	}
	
	@PutMapping("/v1/3rd/file/rename")
    public void rename(
    		@RequestParam("_w_third_appid") String wThirdAppId,
    		@RequestParam("_w_third_file_id") String wThirdFileId,
    		@RequestHeader(name = "x-weboffice-file-id", required = false) String xWebofficeFileId, 
    		@RequestHeader(name = "x-wps-weboffice-token", required = false) String xWpsWebofficeToken,
    		@RequestBody GetFileRemaneRequest getFileRemaneRequest) {
		System.out.println("重命名："+getFileRemaneRequest.getName());
		LOGGER.info("url:[{}], _w_third_appid:[{}], _w_third_file_id:[{}], x-weboffice-file-id:[{}], x-wps-weboffice-token:[{}], name:[{}]", 
				"/v1/3rd/file/rename", wThirdAppId, wThirdFileId, xWebofficeFileId, xWpsWebofficeToken, getFileRemaneRequest.getName());
		
	}
	
	@PostMapping("/v1/3rd/file/history")
	public GetFileVersionsResponse getFileVersions(
			@RequestParam("_w_third_appid") String wThirdAppId,
    		@RequestParam("_w_third_file_id") String wThirdFileId,
    		@RequestHeader(name = "x-weboffice-file-id", required = false) String xWebofficeFileId, 
    		@RequestHeader(name = "x-wps-weboffice-token", required = false) String xWpsWebofficeToken,
    		@RequestParam(name = "id", required = false) String id,
    		@RequestParam(name = "offset", required = false) String offset,
    		@RequestParam(name = "count", required = false) String count, 
    		HttpServletRequest r) {
		
		LOGGER.info("url:[{}], _w_third_appid:[{}], _w_third_file_id:[{}], x-weboffice-file-id:[{}], x-wps-weboffice-token:[{}], id:[{}], offset:[{}], count:[{}]", 
				"/v1/3rd/file/history", wThirdAppId, wThirdFileId, xWebofficeFileId, xWpsWebofficeToken, id, offset, count);
		
		GetFileVersionsResponse.File.User zhangsan = new GetFileVersionsResponse.File.User();
		zhangsan.setId(zhangsanId);
		zhangsan.setName(zhangsanName);
		zhangsan.setAvatarUrl(UrlUtils.getSampleDownloadUrl(zhangsanAvatar, r));
		
		GetFileVersionsResponse.File file_v1 = new GetFileVersionsResponse.File();
		file_v1.setId(fileId);
		file_v1.setName(fileNameV1);
		file_v1.setVersion(1);
		file_v1.setSize(getSize(fileNameV1));
		file_v1.setCreateTime(System.currentTimeMillis());
		file_v1.setModifyTime(System.currentTimeMillis());
		file_v1.setDownloadUrl(UrlUtils.getSampleDownloadUrl(fileNameV1, r));
		file_v1.setCreator(zhangsan);
		file_v1.setModifier(zhangsan);
		
		GetFileVersionsResponse.File.User lisi = new GetFileVersionsResponse.File.User();
		lisi.setId(lisiId);
		lisi.setName(lisiName);
		lisi.setAvatarUrl(UrlUtils.getSampleDownloadUrl(lisiAvatar, r));
		
		GetFileVersionsResponse.File file_v2 = new GetFileVersionsResponse.File();
		file_v2.setId(fileId);
		file_v2.setName(fileNameV2);
		file_v2.setVersion(2);
		file_v2.setSize(getSize(fileNameV2));
		file_v2.setCreateTime(System.currentTimeMillis());
		file_v2.setModifyTime(System.currentTimeMillis());
		file_v2.setDownloadUrl(UrlUtils.getSampleDownloadUrl(fileNameV2, r));
		file_v2.setCreator(lisi);
		file_v2.setModifier(lisi);
		
		GetFileVersionsResponse getFileVersionsResponse = new GetFileVersionsResponse();
		getFileVersionsResponse.setFiles(Arrays.asList(file_v2, file_v1));
		return getFileVersionsResponse;
	}
	
	@PostMapping("/v1/3rd/onnotify")
	public void onNotify(
			@RequestParam("_w_third_appid") String wThirdAppId,
    		@RequestParam("_w_third_file_id") String wThirdFileId,
    		@RequestHeader(name = "x-weboffice-file-id", required = false) String xWebofficeFileId, 
    		@RequestHeader(name = "x-wps-weboffice-token", required = false) String xWpsWebofficeToken,
    		@RequestParam(name = "cmd", required = false) String cmd,
    		@RequestBody OnNotifyRequest onNotifyRequest) {
		
		LOGGER.info("url:[{}], _w_third_appid:[{}], _w_third_file_id:[{}], x-weboffice-file-id:[{}], x-wps-weboffice-token:[{}], cmd:[{}], body:[{}]", 
				"/v1/3rd/onnotify", wThirdAppId, wThirdFileId, xWebofficeFileId, xWpsWebofficeToken, cmd, onNotifyRequest);
		
	}
	
}
