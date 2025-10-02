package com.example.springboots.entity;

import lombok.Data;
@Data

public class User {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String avatar;

    private String token;
}
