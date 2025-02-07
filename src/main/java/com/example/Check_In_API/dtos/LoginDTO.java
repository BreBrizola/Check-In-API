package com.example.Check_In_API.dtos;

import lombok.Data;
import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
    private Long id;
    private String username;
    private String password;
}
