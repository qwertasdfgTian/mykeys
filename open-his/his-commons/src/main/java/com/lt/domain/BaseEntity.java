package com.lt.domain;

import java.io.Serializable;

//所有pojo的父类，这样pojo可以不用实现序列化
public class BaseEntity implements Serializable {
    private static final long serialVersionUID=1L;
}
