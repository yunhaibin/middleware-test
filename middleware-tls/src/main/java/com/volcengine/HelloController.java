package com.volcengine;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jorian
 */
@RestController
public class HelloController {
    @RequestMapping("/")
    public String hello() {
        return "Spring boot test!";
    }
}
