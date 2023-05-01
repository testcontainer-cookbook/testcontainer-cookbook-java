package com.example.mockserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private Client client;

    @GetMapping(value = "/get")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok().body(this.client.get("test"));
    }

    @GetMapping(value = "/foo")
    public ResponseEntity<String> foo() {
        return ResponseEntity.ok().body("foo");
    }
}
