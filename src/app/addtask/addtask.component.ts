import { Component, OnInit } from '@angular/core';
import { TaskService } from '../task.service';

@Component({
  selector: 'app-addtask',
  templateUrl: './addtask.component.html',
  styleUrls: ['./addtask.component.css']
})
export class AddtaskComponent implements OnInit {

  private task: any = {
    task: '',
    parent: { task: '' },
    priority: 0,
    startDate: '',
    endDate: ''
  }
  private successMessage: string = '';
  private failureMessage: string = '';

  constructor(private taskService: TaskService) { }

  ngOnInit() {
  }

  save(event) {
    event.preventDefault();
    this.successMessage = '';
    this.failureMessage = '';
    this.taskService.addTask(this.task).subscribe(
      resp => this.successMessage = 'Task added successfully!',
      error => this.failureMessage = 'Add task failed. Try again later');
  }
}
