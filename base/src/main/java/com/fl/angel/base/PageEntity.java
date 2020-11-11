package com.fl.angel.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author fdm
 * @Date 2020/11/11 9:17
 * @description： 分页实体类
 */
@Data
public class PageEntity implements Serializable {

    /**
     * 页数
     */
    private int page;

    /**
     * 页大小
     */
    private int size;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序类型（0：升序 1：降序）
     */
    private int sortType;

}
