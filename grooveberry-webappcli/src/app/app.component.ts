import {Component, OnInit} from '@angular/core';
import {Stomp} from 'stompjs';
import {SockJS} from 'sockjs-client';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  title = 'GrooveBerry';
  imagePath = '/assets/img/logo.png';
  imageSize = 250;

  private serverUrl = 'http://localhost:8080/grooveberry/socket';
  private stompClient;


  ngOnInit(): void {
    this.initializeWebSocketConection();
  }

  constructor() {

  }

  initializeWebSocketConection(): any {
    const webSocket = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(webSocket);
    this.stompClient.connect({}, function(frame) {
      console.log('Connected: ' + frame);
      this.stompClient.subscribe('/events', (message) => console.log(message));
    });

  }

  sendMessage(message) {
    this.stompClient.send('/grooveberry/send/message', {}, message);
    console.log(message);
  }

}
