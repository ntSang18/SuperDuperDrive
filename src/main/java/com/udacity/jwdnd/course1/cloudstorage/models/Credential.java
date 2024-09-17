package com.udacity.jwdnd.course1.cloudstorage.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Credential {

    private Integer CredentialId;

    private String url;

    private String username;

    private String key;

    private String password;

    private Integer userId;
}
