package com.contentfilter.controller;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilterController {

    @Autowired
    private ProducerTemplate producerTemplate;

    @PostMapping("/filter")
    public ResponseEntity<Object> filterContent(@RequestBody String inputJson) {
        Object response = producerTemplate.requestBody("direct:filter", inputJson);
        return ResponseEntity.ok(response);
    }
}