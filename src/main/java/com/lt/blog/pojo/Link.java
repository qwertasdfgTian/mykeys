package com.lt.blog.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Link {

    private Integer linkId;
    private String linkName;
    private String linkUrl;
    private String createdTime;
    private String updateTime;
    private Integer version;
    private Integer deleted;

}
