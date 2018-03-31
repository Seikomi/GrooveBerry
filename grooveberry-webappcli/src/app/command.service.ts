import {HttpClient} from '@angular/common/http';
import {HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable()
export class CommandService {

  constructor(private http: HttpClient) {}

  private userUrl = '/grooveberry/api';

  public play() {
    return this.http.get(this.userUrl + '/command?text=PLAY');
  }

  public pause() {
    return this.http.get(this.userUrl + '/command?text=PAUSE');
  }

  public next() {
    return this.http.get(this.userUrl + '/command?text=NEXT');
  }

  public prev() {
    return this.http.get(this.userUrl + '/command?text=PREV');
  }

}
