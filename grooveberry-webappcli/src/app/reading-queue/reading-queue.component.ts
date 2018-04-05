import {Component, OnInit} from '@angular/core';

import {Song} from '../song';
import {ReadingQueueService} from '../reading-queue.service';

@Component({
  selector: 'app-reading-queue',
  templateUrl: './reading-queue.component.html',
  styleUrls: ['./reading-queue.component.css']
})
export class ReadingQueueComponent implements OnInit {
  currentTrack: Song;

  constructor(private readingQueueService: ReadingQueueService) {}

  ngOnInit(): void {
    this.getCurrentTrack();
  }

  getCurrentTrack(): void {
    this.readingQueueService.getCurrentTrack().subscribe(currentTrack => this.currentTrack = currentTrack);
  }

}
