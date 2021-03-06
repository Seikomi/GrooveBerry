import {HttpClient} from '@angular/common/http';
import {HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable()
export class CommandService {

  private commandUrl = '/grooveberry/api/command';

  constructor(private http: HttpClient) {}

  public sendCommand(command: string) {
    return this.http.post(this.commandUrl, command, httpOptions);
  }

}
