import {Component, OnInit} from '@angular/core';

import {CommandService} from '../command.service';

@Component({
  selector: 'app-command',
  templateUrl: './command.component.html',
  styleUrls: ['./command.component.css']
})
export class CommandComponent implements OnInit {

  constructor(private commandService: CommandService) {

  }

  ngOnInit() {
  }

  sendCommand(command: string): void {
    this.commandService.sendCommand(command).subscribe();
  }


}
