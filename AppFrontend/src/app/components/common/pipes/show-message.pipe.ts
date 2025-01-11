import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'showMessage'
})
export class ShowMessagePipe implements PipeTransform {

  transform(value: string): string {
    if (value.length > 33) {
      return value.substring(0, 33) + '...';
    } else {
      return value;
    }
  }
}
