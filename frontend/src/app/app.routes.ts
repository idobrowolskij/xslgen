import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { TransformComponent } from './transform/transform.component';
import { PreviewComponent } from './preview/preview.component';
import { TemplateListComponent } from './template-list/template-list.component';
import { TemplateEditorComponent } from './template-editor/template-editor.component';
import { authGuard } from './auth/auth.guard';
import { LoginComponent } from './auth/login.component';
import { RegisterComponent } from './auth/register.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  { path: 'transform/preview/:id', component: PreviewComponent, canActivate: [authGuard] },
  { path: 'transform', component: TransformComponent, canActivate: [authGuard] },
  { path: 'templates/:id/edit', component: TemplateEditorComponent, canActivate: [authGuard] },
  { path: 'templates', component: TemplateListComponent, canActivate: [authGuard] },
  { path: 'home', component: HomeComponent, canActivate: [authGuard] },

  { path: '', pathMatch: 'full', redirectTo: 'home' },
  { path: '**', redirectTo: 'home' }
];
