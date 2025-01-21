package com.wan.commonservice.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVo<T> {
    private Integer total;
    private Integer pages;

    private List<T> records;
}
