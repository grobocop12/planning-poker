package com.grobocop.springscrumpoker.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Controller
class HomeController {

    @GetMapping("")
    fun home() : String = "index"

}