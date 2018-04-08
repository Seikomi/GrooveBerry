import {Injectable} from '@angular/core';

import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Injectable()
export class WebSocketService {

  constructor() {}

  connect() {
    const socket = new SockJS(`http://localhost:8080/grooveberry/socket`);
    const stompClient = Stomp.over(socket);

    return stompClient;
  }

}
