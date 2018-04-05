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


@NgModule({
  declarations: [
    AppComponent,
    SongComponent,
    CommandComponent,
    ReadingQueueComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [SongService, CommandService, ReadingQueueService],
  bootstrap: [AppComponent]
})
export class AppModule { }
