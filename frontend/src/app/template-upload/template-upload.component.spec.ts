import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateUploadComponent } from './template-upload.component';

describe('TemplateUploadComponent', () => {
  let component: TemplateUploadComponent;
  let fixture: ComponentFixture<TemplateUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TemplateUploadComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TemplateUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
