import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WalletManagerHeaderComponent } from './wallet-manager-header.component';

describe('WalletManagerHeaderComponent', () => {
  let component: WalletManagerHeaderComponent;
  let fixture: ComponentFixture<WalletManagerHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WalletManagerHeaderComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WalletManagerHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
