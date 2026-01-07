import { Injectable } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { environment } from '../../environment';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(private sanitizer: DomSanitizer) { }

  getSafeUrl(id: number, type: 'pdf' | 'xsl' | 'xml'): SafeResourceUrl {
    return this.sanitizer.bypassSecurityTrustResourceUrl(`${environment.apiBaseUrl}/templates/${id}/${type}`);
  }
}
