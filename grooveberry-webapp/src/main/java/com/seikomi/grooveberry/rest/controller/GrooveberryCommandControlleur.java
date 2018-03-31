package com.seikomi.grooveberry.rest.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seikomi.grooveberry.GrooveberryClient;

@RestController
@RequestMapping({ "/api" })
public class GrooveberryCommandControlleur {
	@Autowired
	private GrooveberryClient grooveberryClient;
	
	@GetMapping("/command")
    public String sendCommand(@RequestParam(value="text") String text) throws InterruptedException {
		String response = null;
        try {
			response = grooveberryClient.executeCommand('#' + text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
    }
}
