package com.example.mybatisentity.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {

    private String username;
    private String password;
    private Integer age;

}
