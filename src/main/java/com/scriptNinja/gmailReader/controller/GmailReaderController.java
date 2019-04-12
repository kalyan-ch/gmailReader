package com.scriptNinja.gmailReader.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GmailReaderController {

    @RequestMapping(value = "/hello")
    public ResponseEntity<String> sayHello(){
        return new ResponseEntity<>("Hey baby", HttpStatus.OK);
    }

}
