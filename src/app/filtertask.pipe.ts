import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filtertask'
})
export class FiltertaskPipe implements PipeTransform {

  transform(items?: any, task?: any, parentTask?: any, startDate?: any, endDate?: any, priorityFrom?: any, priorityTo?: any): any {
    if (!items) {
      return [];
    }
    if (!task && !parentTask && !startDate && !endDate && !priorityFrom && !priorityTo) {
      return items;
    }

    return items.filter(item => {
      if (task && !item.task.toLowerCase().includes(task.toLowerCase())) {
        return false;
      }
      if (parentTask && item.parent && !item.parent.task.toLowerCase().includes(parentTask.toLowerCase())) {
        return false;
      }
      if (startDate && item.startDate < startDate) {
        return false;
      }
      if (endDate && item.endDate > endDate) {
        return false;
      }
      if (priorityFrom && item.priority < priorityFrom) {
        return false;
      }
      if (priorityTo && item.priority > priorityTo) {
        return false;
      }
      return true;
    });
  }
}
