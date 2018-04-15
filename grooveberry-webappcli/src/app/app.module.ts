import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';


import { AppComponent } from './app.component';
import { CommandService } from './command.service';
import { SongService } from './song.service';
import { SongComponent } from './song/song.component';
import { CommandComponent } from './command/command.component';
import { ReadingQueueService } from './reading-queue.service';
import { ReadingQueueComponent } from './reading-queue/reading-queue.component';
import { WebSocketService } from './web-socket.service';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MatListModule} from '@angular/material/list';
import {MatIconModule} from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


@NgModule({
  declarations: [
    AppComponent,
    SongComponent,
    CommandComponent,
    ReadingQueueComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatListModule,
    MatIconModule,
    MatCardModule,
    MatToolbarModule
  ],
  providers: [SongService, CommandService, ReadingQueueService, WebSocketService],
  bootstrap: [AppComponent, SongComponent]
})
export class AppModule { }
