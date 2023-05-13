package com.fsb.oussama_store.authentication.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private User user;
    private String jwtToken;
}
