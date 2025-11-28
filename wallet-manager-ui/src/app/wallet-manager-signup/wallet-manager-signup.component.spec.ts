import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WalletManagerSignupComponent } from './wallet-manager-signup.component';

describe('WalletManagerSignupComponent', () => {
  let component: WalletManagerSignupComponent;
  let fixture: ComponentFixture<WalletManagerSignupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WalletManagerSignupComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WalletManagerSignupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
