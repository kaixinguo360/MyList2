package com.my.list.module.common;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Resource {

    private Long id;
    private Long user;

    private Timestamp ctime;
    private Timestamp mtime;
    
    private List<String> tags;
    
}
