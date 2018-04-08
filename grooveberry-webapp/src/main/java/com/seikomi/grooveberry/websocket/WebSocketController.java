package com.seikomi.grooveberry.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

	@Autowired
    private SimpMessagingTemplate template;
	
    public String sendNotification() {
    	
        template.convertAndSend("/topic/notification", "TOTO");

        return "Notifications successfully sent to Angular !";
    }
}
