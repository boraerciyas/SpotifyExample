package com.spotifyexample.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthenticationController {

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public void queryMethod(@RequestParam String code)  {
        System.out.println("Our Code is : " + code);

    }
}
