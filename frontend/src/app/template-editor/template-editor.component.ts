import { Component, Input, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule, FileJson, FileCode, Save, FileCheck, Eye, FileCog } from 'lucide-angular';
import {MonacoEditorModule } from 'ngx-monaco-editor-v2'
import { TemplateService } from '../services/template.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { XmlValidationResult } from '../models/xmlvalidationresult.model';
import { ValidationError } from '../models/validationerror.model';
import * as monaco from 'monaco-editor';

@Component({
  selector: 'app-template-editor',
  imports: [FormsModule, MonacoEditorModule, CommonModule, LucideAngularModule, RouterLink],
  templateUrl: './template-editor.component.html',
  styleUrl: './template-editor.component.css'
})
export class TemplateEditorComponent {
  editorInstance!: monaco.editor.IStandaloneCodeEditor;
  code: string = '';
  validationError?: ValidationError;
  currentMode: 'xml' | 'xsl' = 'xml';
  templateId: number;
  log: string[] = [];

  readonly xmlIcon = FileCode;
  readonly xslIcon = FileJson;
  readonly saveIcon = Save;
  readonly validateIcon = FileCheck;
  readonly previewPdfIcon = Eye;
  readonly transformIcon = FileCog;

  editorOptions = {
    theme: 'vs-dark',
    language: 'xml',
    automaticLayout: true
  };

  onEditorInit(editor: monaco.editor.IStandaloneCodeEditor) {
    this.editorInstance = editor;
  }

  constructor(private templateService: TemplateService, private activatedRoute: ActivatedRoute) {
    this.templateId = Number(this.activatedRoute.snapshot.paramMap.get('id'));
    this.loadCode();
  }

  loadCode() {
  if (this.currentMode === 'xml') {
    this.templateService.getXml(this.templateId).subscribe(xml => this.code = xml);
  } else {
    this.templateService.getXsl(this.templateId).subscribe(xsl => this.code = xsl);
  }
}

switchMode(mode: 'xml' | 'xsl') {
  this.currentMode = mode;
  this.loadCode();
}

onSave() {
  if(this.currentMode === "xml") {
    this.templateService.updateXml(this.templateId, this.code).subscribe();
    this.logMsg(`XML-File saved`, "info");
  }

  else {
    this.templateService.updateXsl(this.templateId, this.code).subscribe();
    this.logMsg(`XSL-File saved`, "info");
  }
}

onTransform() {
  this.templateService.transform(this.templateId).subscribe();
  this.logMsg(`PDF-File transformed`, "info");
}

onValidate() {
  this.templateService.validate(this.code).subscribe({
    next: (res: XmlValidationResult) => {
      if (res.valid) {
        monaco.editor.setModelMarkers(this.editorInstance.getModel()!, 'xml', []);
        this.logMsg(`Validation: File well-formed`, "info");
        return;
      }

      const err = res.error!;
      this.validationError = err;
      this.setEditorMarkers(err);
      this.logMsg(`Error in line ${err.line}, column ${err.column}: ${err.message}`, "error");
    },
    error: (e) => {
      this.logMsg(`Unknown error: ${e?.message ?? e}`, "error");
    }
  });
}

logMsg(msg: string, type: 'error' | 'info' | 'warning') {
  const now = new Date();

  const date = new Intl.DateTimeFormat('de-DE').format(now);
  const time = now.toLocaleTimeString('de-DE', {hour: '2-digit', minute: '2-digit'});

  const log = `${type.toUpperCase()} ${date} ${time}: ${msg}`;
  this.log.push(log);
}

setEditorMarkers(error: ValidationError) {
  const model = this.editorInstance.getModel();
  if(!model) return;

  monaco.editor.setModelMarkers(model, 'xml', [{
    message: error.message,
    severity: monaco.MarkerSeverity.Error,
    startLineNumber: error.line,
    startColumn: error.column,
    endLineNumber: error.line,
    endColumn: error.column + 1,
  }]);
}

}
