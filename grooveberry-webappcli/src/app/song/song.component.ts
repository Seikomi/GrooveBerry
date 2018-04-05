import {Component, OnInit} from '@angular/core';

import {Song} from '../song';
import {SongService} from '../song.service';

@Component({
  selector: 'app-song',
  templateUrl: './song.component.html',
  styleUrls: ['./song.component.css']
})
export class SongComponent implements OnInit {
  songs: Song[];

  constructor(private songService: SongService) {}

  ngOnInit() {
    this.getSongs();
  }

  getSongs(): void {
    this.songService.getSongs().subscribe(songs => this.songs = songs);
  }

}
