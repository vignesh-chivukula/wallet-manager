import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WalletManagerLoginComponent } from './wallet-manager-login.component';

describe('WalletManagerLoginComponent', () => {
  let component: WalletManagerLoginComponent;
  let fixture: ComponentFixture<WalletManagerLoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WalletManagerLoginComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WalletManagerLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
