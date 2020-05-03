package com.my.list.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Image extends Resource {

    private String pageTitle;
    private String pageUrl;

    private String imageTitle;
    private String imageUrl;
}
