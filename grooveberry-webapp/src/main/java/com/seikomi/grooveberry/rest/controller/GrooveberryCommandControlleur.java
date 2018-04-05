package com.seikomi.grooveberry.rest.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seikomi.grooveberry.GrooveberryClient;

@RestController
@RequestMapping({ "/api" })
public class GrooveberryCommandControlleur {
	@Autowired
	private GrooveberryClient grooveberryClient;
	
	@PostMapping("/command")
    public String sendCommand(@RequestBody String command) throws InterruptedException {
		String response = null;
        try {
			response = grooveberryClient.executeCommand(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
    }
}
