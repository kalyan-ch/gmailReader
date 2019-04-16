package com.scriptNinja.gmailReader.controller;

import com.google.api.services.gmail.model.Label;
import com.scriptNinja.gmailReader.service.GmailReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class GmailReaderController {

    @Autowired
    private GmailReaderService gmailReaderService;

    @RequestMapping(value = "/hello")
    public ResponseEntity<String> sayHello(){
        return new ResponseEntity<>("Hey baby", HttpStatus.OK);
    }

    @RequestMapping(value = "/getEmails")
    public ResponseEntity<String> getEmails(){
        StringBuilder sb = new StringBuilder();
        try{
            gmailReaderService.searchEmails("");
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(sb.toString(), HttpStatus.OK);
    }

}
