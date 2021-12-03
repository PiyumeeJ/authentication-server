package com.authentication.authentication.model;


import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class ValidatedToken implements Serializable {

    private String userId;
    private String role;
    private String validationStatus;
}
