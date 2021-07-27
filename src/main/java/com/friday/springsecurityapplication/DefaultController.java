package com.friday.springsecurityapplication;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @GetMapping("/")
    public String defaultHome() {
        return "<h1>Home</h1>";
    }

    @GetMapping("/user")
    public String userHome() {
        return ("<h1>Home for admin and user</h1>");
    }

    @GetMapping("/admin")
    public String adminHome() {
        return ("<h1>Home for admin</h1>");
    }
}
