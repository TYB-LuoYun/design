package top.anets.modules.minio;

import io.minio.MinioClient;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * minio配置类
 */
@Data
@RefreshScope
@Lazy
@Configuration
public class MinioConfig {

    @Value("${minio.endpoint:}")
    private String endpoint;
    @Value("${minio.accessKey:}")
    private String accessKey;
    @Value("${minio.secretKey:}")
    private String secretKey;
    @Value("${minio.bucketName:}")
    private String bucketName;
    @Value("${minio.digitalImgZipBucket:}")
    private String digitalImgZipBucket;

    @Bean
    @Lazy
    public MinioClient minioClient() {
        if(StringUtils.isBlank(endpoint)){
            return null;
        }
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
