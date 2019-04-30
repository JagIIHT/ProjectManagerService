import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { TaskService } from '../task.service';

@Component({
  selector: 'app-updatetask',
  templateUrl: './updatetask.component.html',
  styleUrls: ['./updatetask.component.css']
})
export class UpdatetaskComponent implements OnInit {
  private todayDate: any = new Date();
  private id: any;
  private viewOnly: boolean = false;
  private task: any = {
    task: '',
    parent: { task: '' },
    priority: 0,
    startDate: '',
    endDate: ''
  }
  private successMessage: string = '';
  private failureMessage: string = '';

  constructor(private route: ActivatedRoute,
    private router: Router,
    private taskService: TaskService,
    private datePipe: DatePipe) {

  }

  ngOnInit() {
    this.todayDate = this.datePipe.transform(this.todayDate, 'yyyy-MM-dd');
    this.id = this.route.snapshot.paramMap.get('id');
    this.taskService.getTaskById(this.id).subscribe(resp => {
      this.task = resp;
      if (this.task.task) {
        this.viewOnly = (this.task.endDate <= this.todayDate);
      }
    });
  }

  updateTask(event) {
    event.preventDefault();
    this.successMessage = '';
    this.failureMessage = '';
    this.taskService.updateTask(this.task).subscribe(
      resp => this.successMessage = 'Task updated successfully!',
      error => this.failureMessage = 'Update failed. Try again later');
    // this.router.navigate(['/view']);
  }
}
