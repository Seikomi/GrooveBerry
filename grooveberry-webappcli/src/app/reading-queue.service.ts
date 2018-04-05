import {Song} from './song';
import {HttpClient} from '@angular/common/http';
import {HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable()
export class ReadingQueueService {

  private readingQueueUrl = 'grooveberry/api/readingQueue';

  constructor(private http: HttpClient) {}

  getCurrentTrack(): Observable<Song> {
    return this.http.get<Song>(this.readingQueueUrl + '/currentTrack');
  }

}
