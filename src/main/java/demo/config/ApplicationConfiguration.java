package demo.config;

import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import demo.service.storage.StorageProperties;

@Configuration
@EnableConfigurationProperties({ HttpClientProperties.class, CenterPlatformProperties.class, StorageProperties.class })
public class ApplicationConfiguration {
	
    @Autowired
    private HttpClientProperties httpClientProperties;
    
    @Autowired
    private CenterPlatformProperties openPlatformProperties;
    
    @Bean
    public CloseableHttpClient closeableHttpClient() {
        X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        
        SSLContext sslContext = null;
        
        try {
            sslContext = SSLContext.getInstance("SSLv3");
            sslContext.init(null, new TrustManager[] { x509TrustManager }, null);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        
        Registry<ConnectionSocketFactory> connectionSocketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslConnectionSocketFactory).build();
        
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(connectionSocketFactoryRegistry);
        
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(httpClientProperties.getConnectionRequestTimeout())  // 从连接池中获取连接超时
                .setConnectTimeout(httpClientProperties.getConnectTimeout()) // 连接目标超时
                .setSocketTimeout(httpClientProperties.getSocketTimeout()) // 等待响应超时
                .build();
        
        return HttpClients.custom()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setMaxConnTotal(httpClientProperties.getMaxConnTotal()) // 连接池中的最大连接数
                .setMaxConnPerRoute(httpClientProperties.getMaxConnPerRoute()) // 分配给同一个route最大的并发数
                //.disableDefaultUserAgent()
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        
        RestTemplate restTemplate = new RestTemplate(
                new HttpComponentsClientHttpRequestFactory(closeableHttpClient()));
        
        for (HttpMessageConverter<?> converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
            }
        }
        
        restTemplate.setInterceptors(Arrays.asList(new WPS3AuthenticationInterceptor(openPlatformProperties.getAppId(), openPlatformProperties.getAppKey())));
        
        return restTemplate;
    }

}
