import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TemplateService } from '../services/template.service';
import { map, Observable, switchMap, shareReplay } from 'rxjs';
import { Template } from '../models/template.model';
import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { LucideAngularModule, CodeXml } from 'lucide-angular';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-preview',
  imports: [CommonModule, LucideAngularModule, RouterLink],
  templateUrl: './preview.component.html',
  styleUrl: './preview.component.css'
})
export class PreviewComponent {
  template$: Observable<Template>
  safePdfUrl$: Observable<SafeResourceUrl>

  readonly xmlIcon = CodeXml;

  private objectUrl: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private templateService: TemplateService,
    private sanitizer: DomSanitizer
  ) {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.template$ = this.templateService.getById(id).pipe(shareReplay(1));

    this.safePdfUrl$ = this.template$.pipe(
      switchMap(t =>
        this.templateService.getPdfBlob(t.id!).pipe(
          map(blob => {
            // Cleanup alte blob URL
            if (this.objectUrl) URL.revokeObjectURL(this.objectUrl);
            this.objectUrl = URL.createObjectURL(blob);

            return this.sanitizer.bypassSecurityTrustResourceUrl(this.objectUrl);
          })
        )
      )
    );
  }

  ngOnDestroy(): void {
    if (this.objectUrl) URL.revokeObjectURL(this.objectUrl);
  }

}
