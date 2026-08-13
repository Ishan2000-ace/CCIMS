package com.Forensics.CCIMS.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class Users {

    @Id
    private String id;

    private String username;

    private String password;

    private String role;
}
