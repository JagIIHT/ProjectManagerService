import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { TaskService } from '../task.service';

@Component({
  selector: 'app-viewtask',
  templateUrl: './viewtask.component.html',
  styleUrls: ['./viewtask.component.css']
})
export class ViewtaskComponent implements OnInit {
  private todayDate: any = new Date();
  private tasks: any[];
  constructor(private datePipe: DatePipe,
    private taskService: TaskService) { }

  ngOnInit() {
    this.todayDate = this.datePipe.transform(this.todayDate, 'yyyy-MM-dd');
    this.taskService.getTasks().subscribe(resp => this.tasks = resp);
  }

  endTask(task) {
    this.taskService.endTask(task.id).subscribe(
      resp => this.taskService.getTasks().subscribe(resp => this.tasks = resp)
    );
  }
}
