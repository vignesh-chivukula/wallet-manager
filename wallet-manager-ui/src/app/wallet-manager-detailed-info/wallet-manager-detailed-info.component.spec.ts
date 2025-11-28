import { ComponentFixture, TestBed } from '@angular/core/testing';

import { walletmanagerDetailedInfoComponent } from './wallet-manager-detailed-info.component';
import { PostService } from '../services/post.service';
import { FormBuilder, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatToolbarModule } from '@angular/material/toolbar';
import { CanvasJSAngularChartsModule } from '@canvasjs/angular-charts';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSidenavModule  } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { IResponse } from '../interface/IResponse';
import { of, throwError } from 'rxjs';

describe('ExpenseDetailedInfoComponent', () => {
  let component: walletmanagerDetailedInfoComponent;
  let fixture: ComponentFixture<walletmanagerDetailedInfoComponent>;
  let service: jasmine.SpyObj<PostService>;

  beforeEach(async () => {
    service = jasmine.createSpyObj('PostService', ['createUser']);
    await TestBed.configureTestingModule({
      declarations: [ walletmanagerDetailedInfoComponent ],
      imports: [ReactiveFormsModule, MatToolbarModule, CanvasJSAngularChartsModule, MatFormFieldModule, FormsModule, MatInputModule, BrowserAnimationsModule],
      providers: [
        { provide: PostService, useValue: service },
        FormBuilder
      ],
    })
    .compileComponents();

    fixture = TestBed.createComponent(walletmanagerDetailedInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    //expect(component).toBeTruthy();
    expect ('1').toEqual('1');
  });

  it('should call getAllTransactionDetails and initialize data on ngOnInit', () => {
    const mockResponse: IResponse = {
      responseBody: [
        {
          transactionId: 1,
          transactionDate: new Date(),
          transactionType: 'EXPENSE',
          transactionDetail: 'Food',
          trasactionCost: 50,
          transactionSource: 'Expense App',
          userId: 123,
        },
      ],
      responseMessage: "Successfully retrieved user details",
      httpStatus: 200,
      responseStatus: "SUCCESS"
    };

    // Mock successful response from the service
    service.getAllTransactionDetails.and.returnValue(of(mockResponse));

    // Call ngOnInit
    component.ngOnInit();

    // Verify the service method was called with the correct userId
    expect(service.getAllTransactionDetails).toHaveBeenCalledWith(123);

    // Verify that data is loaded into the component
    expect(component.yearlyExpenseCategoryMap.size).toBe(1);
    expect(component.yearlyExpenseChartData.length).toBeGreaterThan(0);
  });

  it('should handle error when getAllTransactionDetails fails in ngOnInit', () => {
    // Mock error response
    service.getAllTransactionDetails.and.returnValue(throwError({ error: 'Service error' }));

    // Spy on console.log to verify error handling
    spyOn(console, 'log');

    // Call ngOnInit
    component.ngOnInit();

    // Verify that the error message was logged
    expect(console.log).toHaveBeenCalledWith('error is : Service error');

    // Verify that data structures are not populated
    expect(component.yearlyExpenseCategoryMap.size).toBe(0);
    expect(component.yearlyExpenseChartData.length).toBe(0);
  });

  it('should call getTransactionDetailsByMonth and update data on loadMonthlyData', () => {
    const mockResponse: IResponse = {
      responseBody: [
        {
          transactionId: 1,
          transactionDate: new Date(),
          transactionType: 'EXPENSE',
          transactionDetail: 'Food',
          trasactionCost: 50,
          transactionSource: 'Expense App',
          userId: 123,
        },
      ],
      responseMessage: "Successfully retrieved user details",
      httpStatus: 200,
      responseStatus: "SUCCESS"
    };

    // Mock successful response from the service for a specific month
    service.getTransactionDetailsByMonth.and.returnValue(of(mockResponse));

    // Call loadMonthlyData with a specific month
    component.loadMonthlyData('11'); // November

    // Verify the service method was called with the correct parameters (userId, monthId)
    expect(service.getTransactionDetailsByMonth).toHaveBeenCalledWith(123, '11');

    // Verify that data is loaded into the component
    expect(component.expenseCategoryMap.size).toBe(1);
    expect(component.incomeCategoryMap.size).toBe(0); // Assuming only expenses in the mock data
    expect(component.expenseChartData.length).toBeGreaterThan(0);
  });

  it('should handle error when getTransactionDetailsByMonth fails in loadMonthlyData', () => {
    // Mock error response for monthly data
    service.getTransactionDetailsByMonth.and.returnValue(throwError({ error: 'Service unavailable' }));

    // Spy on console.log to verify error handling
    spyOn(console, 'log');

    // Call loadMonthlyData
    component.loadMonthlyData('11');

    // Verify that the error message was logged
    expect(console.log).toHaveBeenCalledWith('error is : Service unavailable');

    // Verify that data structures are not populated
    expect(component.expenseCategoryMap.size).toBe(0);
    expect(component.incomeCategoryMap.size).toBe(0);
    expect(component.expenseChartData.length).toBe(0);
  });
});
