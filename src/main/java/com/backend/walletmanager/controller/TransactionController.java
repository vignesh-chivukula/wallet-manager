package com.backend.walletmanager.controller;


import com.backend.walletmanager.Model.PublicTokenRequest;
import com.backend.walletmanager.Model.ResponseModel;
import com.backend.walletmanager.Repository.TransactionRepository;
import com.backend.walletmanager.Repository.UserRepository;
import com.backend.walletmanager.entity.ExternalAccounts;
import com.backend.walletmanager.entity.TransactionObjects;
import com.backend.walletmanager.service.PlaidApiService;
import com.backend.walletmanager.service.TransactionService;
import com.github.scribejava.core.model.Verb;
import com.plaid.client.model.AccountsGetRequest;
import com.plaid.client.model.AccountsGetResponse;
import com.plaid.client.model.TransactionsGetResponse;

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
import java.util.Map;

@CrossOrigin(
        origins = {
        		"http://localhost:4200",
        		"http://localhost:8080"
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
    
    @Autowired
    PlaidApiService plaidApiService;

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
    
    @GetMapping("/month/{id}/{monthId}")
    private ResponseEntity<ResponseModel> getGivenMonthDetails(@PathVariable Integer id, @PathVariable Integer monthId) throws Exception{

    	
    	ResponseModel<List<TransactionObjects>> responseModel = transactionService.getGivenMonthDetails(id, monthId);
    	
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }
    
//    @GetMapping("/obtainPlaidToken")
//    private Map<String,String> createToken() throws Exception{
//    	String linkToken = plaidApiService.createLinkToken("user-123");
//        return Map.of("link_token", linkToken);
//    }
    
    @GetMapping("/obtainPlaidToken/{userId}")
    private ResponseEntity<ResponseModel> createToken(@PathVariable String userId) throws Exception{
    	ResponseModel<String> linkToken = plaidApiService.createLinkToken(userId);
    	return ResponseEntity.status(HttpStatus.OK).body(linkToken);
    }
    
    @PostMapping("/exchange_public_token")
    public ResponseEntity<ResponseModel> exchangePublicToken(@RequestBody PublicTokenRequest publicTokenRequest) throws Exception {
    	ResponseModel<String> accessTokenInfo = plaidApiService.exchangePublicToken(publicTokenRequest);
    	return ResponseEntity.status(HttpStatus.OK).body(accessTokenInfo);
    }
    
    @GetMapping("/get-accounts")
    public AccountsGetResponse getAccounts(@RequestParam String accessToken) throws Exception {
    	return plaidApiService.getAccounts(accessToken);
    }
    
    @GetMapping("/get-transactions")
    public List<TransactionsGetResponse> getExternalTransactions(@RequestParam Integer userId) throws Exception {
    	List<TransactionsGetResponse> responseData = plaidApiService.getTransactions(userId);
    	return responseData;
    }
    
    @GetMapping("/get-linked-accounts/{userId}")
    public ResponseEntity<ResponseModel> getLinkedAccounts(@PathVariable String userId){
    	ResponseModel<List<ExternalAccounts>> responseData = plaidApiService.getLinkedAccounts(userId);
    	return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
}
