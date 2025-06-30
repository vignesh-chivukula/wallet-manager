package com.backend.walletmanager.controller;

import com.backend.walletmanager.Model.ResponseModel;
import com.backend.walletmanager.Repository.TransactionRepository;
import com.backend.walletmanager.Repository.UserRepository;
import com.backend.walletmanager.entity.Transaction;
import com.backend.walletmanager.entity.TransactionObjects;
import com.backend.walletmanager.entity.TransactionType;
import com.backend.walletmanager.entity.User;
import com.backend.walletmanager.service.TransactionService;
import com.github.scribejava.core.model.Verb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

@CrossOrigin(
        origins = {
        		"http://localhost:4200",
        		"http://localhost:8080",
        		"https://c58c-68-66-175-202.ngrok-free.app"
        },
        methods = {
                RequestMethod.OPTIONS,
                RequestMethod.GET,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.POST
        })
@RestController
@RequestMapping("/v1/transactions")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    TransactionService transactionService;

    @PostMapping("/create")
    private ResponseEntity<ResponseModel<TransactionObjects>> createTransaction(@RequestBody TransactionObjects transactionObject){
    	System.out.println(transactionObject.toString());
    	ResponseModel<TransactionObjects> responseModel = transactionService.createTransaction(transactionObject);
    	return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        
        
    }
    
    @PutMapping("/update")
    private ResponseEntity<ResponseModel<TransactionObjects>> updateTransaction(@RequestBody TransactionObjects transactionObject){
    	System.out.println(transactionObject.toString());
    	ResponseModel<TransactionObjects> responseModel = transactionService.updateTransaction(transactionObject);
    	return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }
    
    @PutMapping("/delete")
    private ResponseEntity<ResponseModel> deleteTransaction(@RequestBody TransactionObjects transactionObject){
    	
    	System.out.println("Expense ID to be deleted : " + String.valueOf(transactionObject.getTransactionId()));
    	ResponseModel<String> responseModel = transactionService.deleteTransaction(transactionObject);
    	
    	return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    	
    }
    
    @GetMapping("/all/{id}")
    private ResponseEntity<ResponseModel> getTransactions(@PathVariable Integer id) throws Exception {

    	ResponseModel<List<TransactionObjects>> responseModel = transactionService.getAllTransactions(id);
    	
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
  
    }
    
    @GetMapping("/home/{id}")
    private ResponseEntity<ResponseModel> getCurrentMonthDetails(@PathVariable Integer id) throws Exception{

    	ResponseModel<List<TransactionObjects>> responseModel = transactionService.getCurrentMonthDetails(id);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }
    
//    @GetMapping("/month/{id}/{monthId}")
//    private ResponseEntity<ResponseModel> getGivenMonthDetails(@PathVariable Integer id, @PathVariable Integer monthId) throws Exception{
//
//    	
//    	ResponseModel<List<TransactionObjects>> responseModel = expenseService.getGivenMonthDetails(id, monthId);
//    	
//		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
//    }
}
