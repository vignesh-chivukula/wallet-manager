package com.backend.walletmanager.service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.backend.walletmanager.Model.ResponseModel;
import com.backend.walletmanager.Repository.TransactionRepository;
import com.backend.walletmanager.Repository.UserRepository;
import com.backend.walletmanager.entity.Transaction;
import com.backend.walletmanager.entity.TransactionObjects;
import com.backend.walletmanager.entity.TransactionType;
import com.backend.walletmanager.entity.User;
import com.backend.walletmanager.utils.PlaidTransactionHelper;
import com.plaid.client.model.TransactionsGetResponse;

@Service
public class TransactionService {
	
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    SplitwiseService splitwiseService;
    
    @Autowired
    PlaidApiService plaidApiService;
    
    @Autowired
    PlaidTransactionHelper plaidTransactionHelper;
	
	public ResponseModel<TransactionObjects> createTransaction(TransactionObjects transactionObject){
		
		User user = userRepository.getById(transactionObject.getUserId());
		if(user == null) {
			ResponseModel<TransactionObjects> responseModel = new ResponseModel<TransactionObjects>();
	    	responseModel.setHttpStatus(HttpStatus.BAD_REQUEST);
			responseModel.setResponseMessage("Unable to find the user to add the expense");
			responseModel.setResponseStatus("FAILED");
			responseModel.setResponseBody(null);
			return responseModel;
		}
    	
    	Transaction expenseRecord = new Transaction();
    	expenseRecord.setCategory(transactionObject.getTransactionCategory()== null ? "Miscellaneous":transactionObject.getTransactionCategory());
    	expenseRecord.setCost(transactionObject.getTrasactionCost());
    	expenseRecord.setDetail(transactionObject.getTransactionDetail());
    	expenseRecord.setTransactionDate(transactionObject.getTransactionDate());
    	expenseRecord.setTransactionType(transactionObject.getTransactionType());
    	expenseRecord.setUser(user);
        
        Transaction response = null;
    	try {
    		response = transactionRepository.save(expenseRecord);
    	}catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
        
        if(response == null) {
			ResponseModel<TransactionObjects> responseModel = new ResponseModel<TransactionObjects>();
	    	responseModel.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			responseModel.setResponseMessage("Unable to create the expense, failed while saving to DB");
			responseModel.setResponseStatus("FAILED");
			responseModel.setResponseBody(null);
			return responseModel;
		}
        TransactionObjects transactionObjects = new TransactionObjects();
        transactionObjects.setTransactionDetail(response.getDetail());
        transactionObjects.setTransactionId(BigInteger.valueOf(response.getId().intValue()));
        transactionObjects.setTransactionDate(response.getTransactionDate());
        transactionObjects.setTransactionType(response.getTransactionType());
        transactionObjects.setTransactionCategory(response.getCategory());
        transactionObjects.setTrasactionCost(response.getCost());
        transactionObjects.setUserId(user.getUserId());
        transactionObjects.setTransactionSource("Wallet Manager App");
    	
    	ResponseModel<TransactionObjects> responseModel = new ResponseModel<TransactionObjects>();
    	responseModel.setHttpStatus(HttpStatus.OK);
		responseModel.setResponseMessage("Successfully saved transaction details");
		responseModel.setResponseStatus("SUCCESS");
		responseModel.setResponseBody(transactionObjects);
		
		return responseModel;
	}
	
	public ResponseModel<TransactionObjects> updateTransaction(TransactionObjects transactionObject){
		User user = userRepository.getById(transactionObject.getUserId());
    	
		if(user == null) {
			ResponseModel<TransactionObjects> responseModel = new ResponseModel<TransactionObjects>();
	    	responseModel.setHttpStatus(HttpStatus.BAD_REQUEST);
			responseModel.setResponseMessage("Unable to find the user to add the expense");
			responseModel.setResponseStatus("FAILED");
			responseModel.setResponseBody(null);
			return responseModel;
		}
		
    	Transaction expenseRecord = new Transaction();
    	expenseRecord.setId(Integer.parseInt(String.valueOf(transactionObject.getTransactionId())));
    	expenseRecord.setCategory(transactionObject.getTransactionCategory());
    	expenseRecord.setCost(transactionObject.getTrasactionCost());
    	expenseRecord.setDetail(transactionObject.getTransactionDetail());
    	expenseRecord.setTransactionDate(transactionObject.getTransactionDate());
    	expenseRecord.setTransactionType(transactionObject.getTransactionType());
    	expenseRecord.setUser(user);
    	Transaction response = null;
    	try {
    		response = transactionRepository.saveAndFlush(expenseRecord);
    	}catch(Exception e) {
    		System.out.println(e.getMessage());
    	}
    	if(response == null) {
			ResponseModel<TransactionObjects> responseModel = new ResponseModel<TransactionObjects>();
	    	responseModel.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			responseModel.setResponseMessage("Unable to update the expense, failed while saving to DB");
			responseModel.setResponseStatus("FAILED");
			responseModel.setResponseBody(null);
			return responseModel;
		}
    	TransactionObjects transactionObjects = new TransactionObjects();
        transactionObjects.setTransactionDetail(response.getDetail());
        transactionObjects.setTransactionId(BigInteger.valueOf(response.getId().intValue()));
        transactionObjects.setTransactionDate(response.getTransactionDate());
        transactionObjects.setTransactionType(response.getTransactionType());
        transactionObjects.setTransactionCategory(response.getCategory());
        transactionObjects.setTrasactionCost(response.getCost());
        transactionObjects.setUserId(user.getUserId());
        transactionObjects.setTransactionSource("Wallet Manager App");
    	
    	ResponseModel<TransactionObjects> responseModel = new ResponseModel<TransactionObjects>();
    	responseModel.setHttpStatus(HttpStatus.OK);
		responseModel.setResponseMessage("Successfully saved expense details");
		responseModel.setResponseStatus("SUCCESS");
		responseModel.setResponseBody(transactionObjects);
		
		return responseModel;
	}
	
	public ResponseModel<String> deleteTransaction(TransactionObjects transactionObject){
		System.out.println("Expense ID to be deleted : " + String.valueOf(transactionObject.getTransactionId()));
		ResponseModel<String> responseModel = new ResponseModel<String>();
		try {
			transactionRepository.deleteById(Integer.parseInt(String.valueOf(transactionObject.getTransactionId())));
			responseModel.setHttpStatus(HttpStatus.OK);
			responseModel.setResponseMessage("Successfully deleted expense details");
			responseModel.setResponseStatus("SUCCESS");
			responseModel.setResponseBody("Deleted Expense");
		}catch(Exception e) {
			responseModel.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			responseModel.setResponseMessage("Failed to delete expense details with exception : "+ e.getMessage() );
			responseModel.setResponseStatus("FAILED");
			responseModel.setResponseBody(null);
		}    	
		return responseModel;
	}
	
	public ResponseModel<List<TransactionObjects>> getAllTransactions(Integer id) throws Exception{
		
		ResponseModel<List<TransactionObjects>> responseModel = new ResponseModel<List<TransactionObjects>>();
        User user = userRepository.getById(id);
        if(user == null) {			
	    	responseModel.setHttpStatus(HttpStatus.BAD_REQUEST);
			responseModel.setResponseMessage("Unable to find the user");
			responseModel.setResponseStatus("FAILED");
			responseModel.setResponseBody(null);
			return responseModel;
		}
        Date currentDate = new Date();
        int currentYear = currentDate.getYear();
        ResponseEntity<Object> spliwiseExpenses = null;
        List<LinkedHashMap> expensesList = new ArrayList<>();
        try {
            spliwiseExpenses = splitwiseService.getExpensesForUser(user);
             expensesList = (List<LinkedHashMap>) ((LinkedHashMap)spliwiseExpenses.getBody()).get("expenses");
           }catch(Exception e) {
           	System.out.println(e.getMessage());
           }
           List<TransactionObjects> transactionData = new ArrayList<>();
           for(LinkedHashMap expense : expensesList){
        	   if(expense.get("deleted_at") == null) {
	               TransactionObjects transactionObjects = new TransactionObjects();
	               transactionObjects.setTransactionId(BigInteger.valueOf((Long) expense.get("id")));
	               transactionObjects.setTransactionDetail((String) expense.get("description"));
	               transactionObjects.setTransactionType(TransactionType.EXPENSE);
	               final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	               transactionObjects.setTransactionDate(sdf.parse((String)expense.get("date")));
	               if(!(sdf.parse((String)expense.get("date")).getYear() == currentYear)) {
	            	   continue;
	               }
	               transactionObjects.setTrasactionCost(Double.valueOf( (String)expense.get("cost")));
	               transactionObjects.setTransactionCategory((String) ((LinkedHashMap)expense.get("category")).get("name"));
	               transactionObjects.setTransactionSource("Splitwise");
	               transactionObjects.setUserId(user.getUserId());
	               transactionData.add(transactionObjects);
        	   }
           }
           
           //Load info from Plaid
           List<TransactionsGetResponse> plaidResponse = plaidApiService.getTransactions(id);
           List<TransactionObjects> standardResponse = plaidTransactionHelper.convertPlaidToStandardTransactions(plaidResponse, id);
           List<TransactionObjects> filteredTransactions = standardResponse.stream()
        		   .filter(transaction -> transaction.getTransactionDate().getYear() == currentYear)
        		   .collect(Collectors.toList());
           transactionData.addAll(filteredTransactions);           
           
           List<Transaction> expenses = new ArrayList<Transaction>();
        try {
           expenses = transactionRepository.findByUserId(user.getUserId());
        }catch(Exception e) {
        	e.printStackTrace();
        }
        for(Transaction expense : expenses){
        	if(!(expense.getTransactionDate().getYear() == currentYear)) {
        		continue;
        	}
            TransactionObjects transactionObjects = new TransactionObjects();
            transactionObjects.setTransactionDetail(expense.getDetail());
            transactionObjects.setTransactionId(BigInteger.valueOf(expense.getId().intValue()));
            transactionObjects.setTransactionDate(expense.getTransactionDate());
            transactionObjects.setTransactionType(expense.getTransactionType());
            transactionObjects.setTransactionCategory(expense.getCategory());
            transactionObjects.setTrasactionCost(expense.getCost());
            transactionObjects.setTransactionSource("Wallet Manager App");
            transactionObjects.setUserId(user.getUserId());
            transactionData.add(transactionObjects);
        }
        responseModel.setHttpStatus(HttpStatus.OK);
		responseModel.setResponseMessage("Successfully retrieved user details");
		responseModel.setResponseStatus("SUCCESS");
		responseModel.setResponseBody(transactionData);
		
		return responseModel;
	}
	
	public ResponseModel<List<TransactionObjects>> getCurrentMonthDetails(Integer id) throws Exception{
		
		ResponseModel<List<TransactionObjects>> responseModel = new ResponseModel<List<TransactionObjects>>();
        User user = userRepository.getById(id);
        if(user == null) {			
	    	responseModel.setHttpStatus(HttpStatus.BAD_REQUEST);
			responseModel.setResponseMessage("Unable to find the user");
			responseModel.setResponseStatus("FAILED");
			responseModel.setResponseBody(null);
			return responseModel;
		}
        Date currentDate = new Date();
        int currentMonth = currentDate.getMonth();
        System.out.println("currentmonth id : " + currentMonth);
        ResponseEntity<Object> spliwiseExpenses = null;
        List<LinkedHashMap> expensesList = new ArrayList<>();
        try {
            spliwiseExpenses = splitwiseService.getExpensesForUser(user);
             expensesList = (List<LinkedHashMap>) ((LinkedHashMap)spliwiseExpenses.getBody()).get("expenses");
           }catch(Exception e) {
           	System.out.println(e.getMessage());
           }
           List<TransactionObjects> transactionData = new ArrayList<>();
           for(LinkedHashMap expense : expensesList){
        	   if(expense.get("deleted_at") == null) {
               TransactionObjects transactionObjects = new TransactionObjects();
               final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
               transactionObjects.setTransactionDate(sdf.parse((String)expense.get("date")));
               if(!(sdf.parse((String)expense.get("date")).getMonth() == currentMonth)) {
            	   continue;
               }
               if(transactionObjects.getTransactionDate().getYear() != currentDate.getYear()) {
            	   continue;
               }
               transactionObjects.setTransactionId(BigInteger.valueOf((Long) expense.get("id")));
               transactionObjects.setTransactionDetail((String) expense.get("description"));
               transactionObjects.setTransactionType(TransactionType.EXPENSE);               
               transactionObjects.setTrasactionCost(Double.valueOf( (String)expense.get("cost")));
               transactionObjects.setTransactionCategory((String) ((LinkedHashMap)expense.get("category")).get("name"));
               transactionObjects.setTransactionSource("Splitwise");
               transactionObjects.setUserId(user.getUserId());
               transactionData.add(transactionObjects);
        	   }
           }
           
           //Load info from Plaid
           List<TransactionsGetResponse> plaidResponse = plaidApiService.getTransactions(id);
           List<TransactionObjects> standardResponse = plaidTransactionHelper.convertPlaidToStandardTransactions(plaidResponse, id);
           List<TransactionObjects> filteredTransactions = standardResponse.stream()
           .filter(transaction -> transaction.getTransactionDate().getYear() == currentDate.getYear())
           	.filter(transaction -> transaction.getTransactionDate().getMonth() == currentMonth)
           	.collect(Collectors.toList());
           transactionData.addAll(filteredTransactions); 
           
           
           List<Transaction> expenses = new ArrayList<>();
           try {
        	   expenses = transactionRepository.findByUserId(user.getUserId());
           }catch(Exception e) {
        	   e.printStackTrace();
           }
           
        for(Transaction expense : expenses){
        	if(!(expense.getTransactionDate().getMonth() == currentMonth)) {
        		continue;
        	}
            TransactionObjects transactionObjects = new TransactionObjects();
            transactionObjects.setTransactionDetail(expense.getDetail());
            transactionObjects.setTransactionId(BigInteger.valueOf(expense.getId().intValue()));
            transactionObjects.setTransactionDate(expense.getTransactionDate());
            transactionObjects.setTransactionType(expense.getTransactionType());
            transactionObjects.setTransactionCategory(expense.getCategory());
            transactionObjects.setTrasactionCost(expense.getCost());
            transactionObjects.setTransactionSource("Wallet Manager App");
            transactionObjects.setUserId(user.getUserId());
            transactionData.add(transactionObjects);
        }
        responseModel.setHttpStatus(HttpStatus.OK);
		responseModel.setResponseMessage("Successfully retrieved user details");
		responseModel.setResponseStatus("SUCCESS");
		responseModel.setResponseBody(transactionData);
		
		return responseModel;
	}
	
	public ResponseModel<List<TransactionObjects>> getGivenMonthDetails(Integer id, Integer monthId) throws Exception{
		ResponseModel<List<TransactionObjects>> responseModel = new ResponseModel<List<TransactionObjects>>();
        User user = userRepository.getById(id);
        if(user == null) {			
	    	responseModel.setHttpStatus(HttpStatus.BAD_REQUEST);
			responseModel.setResponseMessage("Unable to find the user");
			responseModel.setResponseStatus("FAILED");
			responseModel.setResponseBody(null);
			return responseModel;
		}
        System.out.println("month id : " + monthId);
        Date currentDate = new Date();
        ResponseEntity<Object> spliwiseExpenses = null;
        List<LinkedHashMap> expensesList = new ArrayList<>();
        try {
         spliwiseExpenses = splitwiseService.getExpensesForUser(user);
         System.out.println("spliwise expense obj : " + spliwiseExpenses);
          expensesList = (List<LinkedHashMap>) ((LinkedHashMap)spliwiseExpenses.getBody()).get("expenses");
        }catch(Exception e) {
        	System.out.println(e.getMessage());
        }
           List<TransactionObjects> transactionData = new ArrayList<>();
           for(LinkedHashMap expense : expensesList){
        	   if(expense.get("deleted_at") == null) {
	               TransactionObjects transactionObjects = new TransactionObjects();
	               final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	               transactionObjects.setTransactionDate(sdf.parse((String)expense.get("date")));
	               if((sdf.parse((String)expense.get("date")).getMonth() != (monthId-1))) {
	            	   continue;
	               }
	               if(transactionObjects.getTransactionDate().getYear() != currentDate.getYear()) {
	            	   continue;
	               }
	               transactionObjects.setTransactionId(BigInteger.valueOf((Long) expense.get("id")));
	               transactionObjects.setTransactionDetail((String) expense.get("description"));
	               transactionObjects.setTransactionType(TransactionType.EXPENSE);               
	               transactionObjects.setTrasactionCost(Double.valueOf( (String)expense.get("cost")));
	               transactionObjects.setTransactionCategory((String) ((LinkedHashMap)expense.get("category")).get("name"));
	               transactionObjects.setTransactionSource("Splitwise");
	               transactionObjects.setUserId(user.getUserId());
	               transactionData.add(transactionObjects);
        	   }
           }
           
           //Load info from Plaid
           List<TransactionsGetResponse> plaidResponse = plaidApiService.getTransactions(id);
           List<TransactionObjects> standardResponse = plaidTransactionHelper.convertPlaidToStandardTransactions(plaidResponse, id);
           List<TransactionObjects> filteredTransactions = standardResponse.stream()
           .filter(transaction -> transaction.getTransactionDate().getYear() == currentDate.getYear())
           	.filter(transaction -> transaction.getTransactionDate().getMonth() == (monthId-1))
           	.collect(Collectors.toList());
           transactionData.addAll(filteredTransactions); 
           
           List<Transaction> expenses = new ArrayList<>();
           try {
        	   expenses = transactionRepository.findByUserId(user.getUserId());
           }catch(Exception e) {
        	   e.printStackTrace();
           }
        
        for(Transaction expense : expenses){
        	if((expense.getTransactionDate().getMonth() != (monthId-1))) {
        		continue;
        	}
            TransactionObjects transactionObjects = new TransactionObjects();
            transactionObjects.setTransactionDetail(expense.getDetail());
            transactionObjects.setTransactionId(BigInteger.valueOf(expense.getId().intValue()));
            transactionObjects.setTransactionDate(expense.getTransactionDate());
            transactionObjects.setTransactionType(expense.getTransactionType());
            transactionObjects.setTransactionCategory(expense.getCategory());
            transactionObjects.setTrasactionCost(expense.getCost());
            transactionObjects.setTransactionSource("Wallet Manager App");
            transactionObjects.setUserId(user.getUserId());
            transactionData.add(transactionObjects);
        }
        responseModel.setHttpStatus(HttpStatus.OK);
		responseModel.setResponseMessage("Successfully retrieved user details");
		responseModel.setResponseStatus("SUCCESS");
		responseModel.setResponseBody(transactionData);
		return responseModel;
	}
}
