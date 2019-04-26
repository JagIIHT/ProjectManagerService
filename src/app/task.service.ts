import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private baseURI: String = "http://localhost:8081/taskservice/";
  constructor(private http: HttpClient) { }

  getTasks(): any {
    return this.http.get(this.baseURI + 'tasks');
  }

  getTaskById(id): any {
    return this.http.get(this.baseURI + 'task/' + id);
  }

  addTask(task): any {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET, POST, PATCH, PUT, DELETE, OPTIONS',
        'Access-Control-Allow-Headers': 'Origin, Content-Type, X-Auth-Token'
      })
    };
    return this.http.post(this.baseURI + 'add', task, httpOptions);
  }

  endTask(id): any {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET, POST, PATCH, PUT, DELETE, OPTIONS',
        'Access-Control-Allow-Headers': 'Origin, Content-Type, X-Auth-Token'
      })
    };
    return this.http.put(this.baseURI + 'endtask/' + id, httpOptions);
  }

  updateTask(task): any {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET, POST, PATCH, PUT, DELETE, OPTIONS',
        'Access-Control-Allow-Headers': 'Origin, Content-Type, X-Auth-Token'
      })
    };
    return this.http.put(this.baseURI + 'update/', task, httpOptions);
  }
}
