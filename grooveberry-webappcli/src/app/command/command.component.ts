import {Component, OnInit} from '@angular/core';

import {CommandService} from '../command.service';

import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';

@Component({
  selector: 'app-command',
  templateUrl: './command.component.html',
  styleUrls: ['./command.component.css']
})
export class CommandComponent implements OnInit {
  imagePath = '/assets/img/logo.png';
  imageSize = 100;

  constructor(private commandService: CommandService) {

  }

  ngOnInit() {
  }

  sendCommand(command: string): void {
    this.commandService.sendCommand(command).subscribe();
  }


}
