package com.lt.config.upload;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Description 文件上传属性
 * @Author Mr.Tian
 */
@ConfigurationProperties(prefix = "upload")
@Data
public class UploadProperties {

    private String baseUrl;

    private List<String> allowTypes;
}
