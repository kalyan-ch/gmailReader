package com.scriptNinja.gmailReader.controller;

import com.google.api.services.gmail.model.Label;
import com.scriptNinja.gmailReader.service.GmailReaderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class GmailReaderController {

    @Autowired
    private GmailReaderService gmailReaderService;

    @ApiOperation(
            value = "/hello",
            httpMethod = "GET",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "Success")
    })
    @RequestMapping(value = "/hello")
    public ResponseEntity<String> sayHello(){
        return new ResponseEntity<>("Hey baby", HttpStatus.OK);
    }

    @ApiOperation(
            value = "/getEmails",
            httpMethod = "GET",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "Success")
    })
    @RequestMapping(value = "/getEmails", method = RequestMethod.GET)
    public ResponseEntity<String> getEmails(){
        String emails = "";
        try{
            emails = gmailReaderService.searchEmails("");
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(emails, HttpStatus.OK);
    }

    @ApiOperation(
            value = "/hello",
            httpMethod = "GET",
            produces = "application/json",
            response = String.class
    )
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "Success")
    })
    @RequestMapping(value = "/getLabels")
    public ResponseEntity<String> getLabels(){
        StringBuilder sb = new StringBuilder();
        try{
            List<Label> labels = gmailReaderService.readLabels();
            for(Label s: labels){
                sb.append(s.getName());
                sb.append(", ");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(sb.toString(), HttpStatus.OK);
    }

}
