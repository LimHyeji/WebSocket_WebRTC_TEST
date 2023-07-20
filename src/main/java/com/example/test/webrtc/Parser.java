package com.example.test.webrtc;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Parser {
    public Optional<Long> parseId(String sid){
        Long id=null;
        try{
            id=Long.valueOf(sid);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return Optional.ofNullable(id);
    }

}
