package com.baeldung.newstack.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baeldung.newstack.web.dto.CheckDto;

@RestController
@RequestMapping(value = "/check")
public class SuperUserController {
	
	
	  @GetMapping
	    public CheckDto check() {
	       CheckDto checkDto = new CheckDto();
	       checkDto.setName("baeldung");
	        return checkDto;
	    }

}
