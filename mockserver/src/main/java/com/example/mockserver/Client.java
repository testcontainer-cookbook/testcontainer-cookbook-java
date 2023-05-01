package com.example.mockserver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "fooClient", url = "${foo-client.url}")
public interface Client {

    @GetMapping(value = "/foo")
    String get(@RequestParam("name") String name);
}
