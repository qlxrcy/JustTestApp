package com.justtest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by le.qi on 11/1/2016.
 */
@Controller
public class TestController {
    @RequestMapping(value="/index")
    public String index(){
        return"/index";
    }
}
