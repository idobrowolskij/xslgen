import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Template } from '../models/template.model';
import { TemplateUploadComponent } from '../template-upload/template-upload.component';

@Component({
  selector: 'app-transform',
  imports: [CommonModule, FormsModule, TemplateUploadComponent],
  templateUrl: './transform.component.html',
  styleUrl: './transform.component.css'
})
export class TransformComponent {
  template?: Template;

  onTransform(template: Template) {
    this.template = template;
  } 
}
