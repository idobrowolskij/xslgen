import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { Template } from '../models/template.model';
import { TemplateService } from '../services/template.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { LucideAngularModule, FilePenLine, Eye, Trash2 } from 'lucide-angular';

@Component({
  selector: 'app-template-list',
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './template-list.component.html',
  styleUrl: './template-list.component.css'
})
export class TemplateListComponent {
  templates$: Observable<Template[]>;
  readonly FilePenLine = FilePenLine;
  readonly Eye = Eye;
  readonly Delete = Trash2;

  constructor(private templateService: TemplateService, private router: Router) {
    this.templates$ = templateService.getAll();
  }

  preview(id: number) {
    this.router.navigate(['transform/preview', id]);
  }

  edit(id: number) {
    this.router.navigate([`templates/${id}/edit`]);
  }

  delete(id: number) {
    this.templateService.delete(id).subscribe();
    this.templates$ = this.templateService.getAll();
  }
}
