import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, provideRouter, Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import { Login } from './login';
import { AuthService } from '../../../core/services/auth';

describe('Login', () => {
  let component: Login;
  let fixture: ComponentFixture<Login>;

  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let router: Router;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj<AuthService>(
      'AuthService',
      ['login', 'setUser']
    );

    await TestBed.configureTestingModule({
      imports: [Login],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authServiceSpy },
        { provide: ActivatedRoute, useValue: {} }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate').and.returnValue(Promise.resolve(true));

    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty fields', () => {
    expect(component.form.get('username')?.value).toBe('');
    expect(component.form.get('password')?.value).toBe('');
  });

  it('should not call login when form is invalid', () => {
    component.form.setValue({
      username: '',
      password: ''
    });

    component.login();

    expect(authServiceSpy.login).not.toHaveBeenCalled();
  });

  it('should call authService.login on valid submit', () => {
    const userResponse = {
      id: '1',
      username: 'breno',
      email: 'breno@email.com'
    };

    authServiceSpy.login.and.returnValue(of(userResponse));

    component.form.setValue({
      username: 'breno',
      password: '123456'
    });

    component.login();

    expect(authServiceSpy.login).toHaveBeenCalledWith({
      username: 'breno',
      password: '123456'
    });

    expect(authServiceSpy.setUser).toHaveBeenCalledWith(userResponse);
    expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
  });

  it('should set errorMessage on login error', () => {
    authServiceSpy.login.and.returnValue(
      throwError(() => new Error('Invalid credentials'))
    );

    component.form.setValue({
      username: 'breno',
      password: 'wrong'
    });

    component.login();

    expect(component.errorMessage()).toBe('Username or password invalid!');
  });
});