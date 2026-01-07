import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Template } from '../models/template.model';
import { XmlValidationResult } from '../models/xmlvalidationresult.model';
import { environment } from '../../environment';

@Injectable({
  providedIn: 'root'
})
export class TemplateService {
  private base = `${environment.apiBaseUrl}/templates`;

  constructor(private http: HttpClient) { }

  create(formData: FormData): Observable<Template> {
    return this.http.post<Template>(`${this.base}`, formData);
  }

  getAll(): Observable<Template[]> {
    return this.http.get<Template[]>(`${this.base}`);
  }

  getById(id: number): Observable<Template> {
    return this.http.get<Template>(`${this.base}/${id}`);
  }

  getXml(id: number): Observable<string> {
    return this.http.get(`${this.base}/${id}/xml`, { responseType: 'text' });
  }

  getXsl(id: number): Observable<string> {
    return this.http.get(`${this.base}/${id}/xsl`, { responseType: 'text' });
  }

  updateXml(id: number, xml: string): Observable<void> {
    return this.http.put<void>(`${this.base}/${id}/xml`, xml);
  }

  updateXsl(id: number, xsl: string): Observable<void> {
    return this.http.put<void>(`${this.base}/${id}/xsl`, xsl);
  }

  transform(id: number): Observable<Template> {
    return this.http.post<Template>(`${this.base}/${id}/transform`, {});
  }

  validate(xml: string): Observable<XmlValidationResult> {
    const headers = new HttpHeaders({ 'Content-Type': 'text/plain' });
    return this.http.post<XmlValidationResult>(`${this.base}/validate`, xml, { headers });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  getPdfBlob(id: number) {
    return this.http.get(`${this.base}/${id}/pdf`, { responseType: 'blob' });
  } 
}
