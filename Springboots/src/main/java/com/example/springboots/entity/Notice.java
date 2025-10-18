package com.example.springboots.entity;


import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("\"notice\"")
public class Notice {
    @TableId(value= "id", type = IdType.AUTO)
    @Alias("序号")
    private Integer id;
    private String title;
    private String content;
    private Integer userid;
    private String time;
    private Boolean open;

    @TableField(exist = false)
    private String user;
}
