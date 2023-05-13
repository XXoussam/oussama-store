package com.fsb.oussama_store.authentication.controller;

import com.fsb.oussama_store.authentication.Service.JwtService;
import com.fsb.oussama_store.authentication.entity.JwtRequest;
import com.fsb.oussama_store.authentication.entity.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
public class JwtController {
    @Autowired
    protected JwtService jwtService;

    @PostMapping({"/authenticate"})
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);
    }
}
