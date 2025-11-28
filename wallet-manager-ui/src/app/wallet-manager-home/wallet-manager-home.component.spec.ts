import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WalletManagerHomeComponent } from './wallet-manager-home.component';

describe('WalletManagerHomeComponent', () => {
  let component: WalletManagerHomeComponent;
  let fixture: ComponentFixture<WalletManagerHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WalletManagerHomeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WalletManagerHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
