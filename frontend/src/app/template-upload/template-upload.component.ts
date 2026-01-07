import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TemplateService } from '../services/template.service';
import { Template } from '../models/template.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-template-upload',
  imports: [CommonModule, FormsModule],
  templateUrl: './template-upload.component.html',
  styleUrl: './template-upload.component.css'
})
export class TemplateUploadComponent {

  @Output() transform = new EventEmitter<Template>();

  constructor(private templateService: TemplateService, private router: Router) {}

  formData = {
    templateName: "",
    xslFile: null as File | null,
    xmlFile: null as File | null
  };

  onFileChange(event: Event, type: 'xsl' | 'xml') {
    const target = event.target as HTMLInputElement;
    const file = target.files?.[0];
    
    if(file) {
      if (type === 'xsl') this.formData.xslFile = file;
      if (type === 'xml') this.formData.xmlFile = file;
    }
  }

  onSubmit() {
    const form = new FormData();
    form.append('name', this.formData.templateName);
    if (this.formData.xslFile) form.append('xslFile', this.formData.xslFile);
    if (this.formData.xmlFile) form.append('xmlFile', this.formData.xmlFile);
    this.templateService.create(form).subscribe(response => {
      this.router.navigate(['transform/preview/', response.id]);
      this.transform.emit(response);
    })
  }

}
