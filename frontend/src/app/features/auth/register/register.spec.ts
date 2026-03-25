import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ActivatedRoute, provideRouter, Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import { Register } from './register';
import { AuthService } from '../../../core/services/auth';

describe('Register', () => {
  let component: Register;
  let fixture: ComponentFixture<Register>;

  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let router: Router;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj<AuthService>(
      'AuthService',
      ['register']
    );

    await TestBed.configureTestingModule({
      imports: [Register],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authServiceSpy },
        { provide: ActivatedRoute, useValue: {} }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate').and.returnValue(Promise.resolve(true));

    fixture = TestBed.createComponent(Register);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form correctly', () => {
    expect(component.form.get('username')?.value).toBe('');
    expect(component.form.get('email')?.value).toBe('');
    expect(component.form.get('password')?.value).toBe('');
  });

  it('should not call register when form is invalid', () => {
    component.form.setValue({
      username: '',
      email: '',
      password: ''
    });

    component.register();

    expect(authServiceSpy.register).not.toHaveBeenCalled();
  });

  it('should call authService.register on valid submit', fakeAsync(() => {
    const response = {
      id: '1',
      username: 'breno',
      email: 'breno@email.com'
    };

    authServiceSpy.register.and.returnValue(of(response));

    component.form.setValue({
      username: 'breno',
      email: 'breno@email.com',
      password: '123456'
    });

    component.register();

    expect(authServiceSpy.register).toHaveBeenCalledWith({
      username: 'breno',
      email: 'breno@email.com',
      password: '123456'
    });

    expect(component.successMessage()).toBe('Registration sucessful!');

    tick(1000);

    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  }));

  it('should handle error on register', () => {
    authServiceSpy.register.and.returnValue(
      throwError(() => new Error('error'))
    );

    component.form.setValue({
      username: 'breno',
      email: 'breno@email.com',
      password: '123456'
    });

    component.register();

    expect(authServiceSpy.register).toHaveBeenCalled();
    expect(component.errorMessage()).toBe('We were unable to register the user.');
  });
});