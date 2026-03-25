import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    localStorage.clear();

    TestBed.configureTestingModule({
      providers: [
        AuthService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should call login endpoint', () => {
    const payload = { username: 'breno', password: '123456' };
    const response = { id: '1', username: 'breno', email: 'breno@email.com' };

    service.login(payload).subscribe((result) => {
      expect(result).toEqual(response);
    });

    const req = httpMock.expectOne('http://localhost:8080/auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);

    req.flush(response);
  });

  it('should call register endpoint', () => {
    const payload = {
      username: 'breno',
      email: 'breno@email.com',
      password: '123456'
    };

    const response = {
      id: '1',
      username: 'breno',
      email: 'breno@email.com'
    };

    service.register(payload).subscribe((result) => {
      expect(result).toEqual(response);
    });

    const req = httpMock.expectOne('http://localhost:8080/users');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);

    req.flush(response);
  });

  it('should set user in localStorage and signal', () => {
    const user = {
      id: '1',
      username: 'breno',
      email: 'breno@email.com'
    };

    service.setUser(user);

    expect(localStorage.getItem('user')).toBe(JSON.stringify(user));
    expect(service.user()).toEqual(user);
  });

  it('should remove user from localStorage and clear signal on logout', () => {
    const user = {
      id: '1',
      username: 'breno',
      email: 'breno@email.com'
    };

    localStorage.setItem('user', JSON.stringify(user));
    service.setUser(user);

    service.logout();

    expect(localStorage.getItem('user')).toBeNull();
    expect(service.user()).toBeNull();
  });

  it('should initialize user signal from localStorage', () => {
    const storedUser = {
      id: '1',
      username: 'breno',
      email: 'breno@email.com'
    };

    localStorage.setItem('user', JSON.stringify(storedUser));

    TestBed.resetTestingModule();

    TestBed.configureTestingModule({
      providers: [
        AuthService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    const freshService = TestBed.inject(AuthService);

    expect(freshService.user()).toEqual(storedUser);
  });
});