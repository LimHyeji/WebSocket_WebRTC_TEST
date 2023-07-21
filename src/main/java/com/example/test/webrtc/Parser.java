package com.example.test.webrtc;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Parser {
    public Optional<Long> parseId(String sid){
        //System.out.println("PARSER CALL");
        //System.out.println(sid+" PARSING ...");
        Long id=null;
        try{
            id=Long.valueOf(sid);
            //System.out.println("AFTER PARSING: "+id);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return Optional.ofNullable(id);
    }

}
