import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import {
  LucideAngularModule,
  FileCode2,
  House,
  FileCog,
  Files,
  User,
  LogOut,
  ChevronDown
} from 'lucide-angular';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, LucideAngularModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  // icons
  readonly FileCode2 = FileCode2;
  readonly House = House;
  readonly FileCog = FileCog;
  readonly Files = Files;
  readonly User = User;
  readonly LogOut = LogOut;
  readonly ChevronDown = ChevronDown;

  open = false;

  constructor(public auth: AuthService, private router: Router) {}

  toggle() {
    this.open = !this.open;
  }

  logout() {
    this.auth.logout();
    this.open = false;
    this.router.navigateByUrl('/login');
  }
}
