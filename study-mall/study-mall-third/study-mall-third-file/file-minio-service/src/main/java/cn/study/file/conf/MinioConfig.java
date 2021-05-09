package cn.study.file.conf;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
public class MinioConfig {

    @Value("${minio.endpoint:192.168.56.10}")
    private String endpoint;

    @Value("${minio.port::9000}")
    private Integer port;

    @Value("${minio.accessKey:dev}")
    private String accessKey;

    @Value("${minio.secretKey:c~Qo1~qa)b&m@}")
    private String secretKey;

    @Value("${minio.bucketName:default}")
    private String bucketName;

    /**
     * 注入minio 客户端
     */
    @Bean
    public MinioClient minioClient(){
        try {
            return new MinioClient(endpoint,port,accessKey,secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
