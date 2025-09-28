package com.example.springboots.common;


import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
    private Integer total;
    private List<T> list;
}
