package com.example.springboots.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("\"logs\"")
public class Logs {
    @TableId(value= "id", type = IdType.AUTO)
    @Alias("序号")
    private Integer id;
    private String operation;
    private String type;
    private String ip;
    @TableField(value = "\"user\"")
    private String user;
    private String time;
}
