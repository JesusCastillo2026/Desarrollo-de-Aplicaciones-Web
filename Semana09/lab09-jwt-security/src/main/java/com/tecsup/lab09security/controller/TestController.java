package com.tecsup.lab09security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/all")
    public Message all() { return new Message("Public Content"); }

    @GetMapping("/user")
    public Message user() { return new Message("User Content"); }

    @GetMapping("/admin")
    public Message admin() { return new Message("Admin Content"); }

    @GetMapping("/mod")
    public Message moderator() { return new Message("Moderator Content"); }

    public record Message(String message) { }
}
