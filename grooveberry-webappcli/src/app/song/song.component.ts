import {Component, OnInit} from '@angular/core';

import {Song} from '../song';
import {SongService} from '../song.service';
import { WebSocketService } from '../web-socket.service';

import {MatListModule} from '@angular/material/list';

@Component({
  selector: 'app-song',
  templateUrl: './song.component.html',
  styleUrls: ['./song.component.css']
})
export class SongComponent implements OnInit {
  songs: Song[];

  constructor(private songService: SongService, private webSocketService: WebSocketService ) {
  const stompClient = this.webSocketService.connect();

    stompClient.connect({}, frame => {
      stompClient.subscribe('/topic/notification', notifications => {
        this.getSongs();
      });
    });
  }

  ngOnInit() {
    this.getSongs();
  }

  getSongs(): void {
    this.songService.getSongs().subscribe(songs => this.songs = songs);
  }

}
