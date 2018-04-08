import {Component, OnInit} from '@angular/core';

import {Song} from '../song';
import {ReadingQueueService} from '../reading-queue.service';
import { WebSocketService } from '../web-socket.service';

@Component({
  selector: 'app-reading-queue',
  templateUrl: './reading-queue.component.html',
  styleUrls: ['./reading-queue.component.css']
})
export class ReadingQueueComponent implements OnInit {
  currentTrack: Song;

  constructor(private readingQueueService: ReadingQueueService, private webSocketService: WebSocketService) {
    const stompClient = this.webSocketService.connect();

    stompClient.connect({}, frame => {
      stompClient.subscribe('/topic/notification', notifications => {
        this.getCurrentTrack();
      });
    });
  }

  ngOnInit(): void {
    this.getCurrentTrack();
  }

  getCurrentTrack(): void {
    this.readingQueueService.getCurrentTrack().subscribe(currentTrack => this.currentTrack = currentTrack);
  }

}
