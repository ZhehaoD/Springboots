package com.example.springboots.entity;


import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("\"news\"")
public class News {
    @TableId(value= "id", type = IdType.AUTO)
    @Alias("序号")
    private Integer id;
    private String title;
    private String content;
    private String description;
    private Integer authorid;
    private String time;
    @TableField(exist = false)
    private String author;
}
