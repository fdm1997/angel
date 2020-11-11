package com.fl.angel.base;

import lombok.Data;

import java.util.Date;

/**
 * @Author fdm
 * @Date 2020/11/11 9:38
 * @description：  DO实体公共父类
 */
@Data
public class Entity {

    private Date addTime;

    private Date updateTime;

}
