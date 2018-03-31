import {Component, OnInit} from '@angular/core';
import {CommandService} from './command.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'GrooveBerry';
  imagePath = '/assets/img/logo.png';
  imageSize = 250;

  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }

  constructor(private commandService: CommandService) {

  }

  play(): void {
    this.commandService.play().subscribe();
  }

  pause(): void {
    this.commandService.pause().subscribe();
  }

  next(): void {
    this.commandService.next().subscribe();
  }

  prev(): void {
    this.commandService.prev().subscribe();
  }
}
