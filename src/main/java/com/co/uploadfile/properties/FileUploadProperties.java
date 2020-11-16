package com.co.uploadfile.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file.upload")
@Getter@Setter
public class FileUploadProperties {

    private String location;
    private String days;
    private String elements;
    private String weight;
    private String output;
    private String name;
}
