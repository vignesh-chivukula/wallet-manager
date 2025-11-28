package com.backend.walletmanager.service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.backend.walletmanager.Model.PublicTokenRequest;
import com.backend.walletmanager.Model.ResponseModel;
import com.backend.walletmanager.Repository.ExternalAccountsRepository;
import com.backend.walletmanager.entity.ExternalAccounts;
import com.backend.walletmanager.entity.TransactionObjects;
import com.plaid.client.model.AccountBase;
import com.plaid.client.model.AccountsGetRequest;
import com.plaid.client.model.AccountsGetResponse;
import com.plaid.client.model.CountryCode;
import com.plaid.client.model.Item;
import com.plaid.client.model.ItemPublicTokenExchangeRequest;
import com.plaid.client.model.ItemPublicTokenExchangeResponse;
import com.plaid.client.model.LinkTokenCreateRequest;
import com.plaid.client.model.LinkTokenCreateRequestUser;
import com.plaid.client.model.LinkTokenCreateResponse;
import com.plaid.client.model.Products;
import com.plaid.client.model.TransactionsGetRequest;
import com.plaid.client.model.TransactionsGetResponse;
import com.plaid.client.request.PlaidApi;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

@Service
public class PlaidApiService {
	
	@Autowired
	private PlaidApi plaidApi;
	
	@Autowired
	private ExternalAccountsRepository externalAccountsRepository;
	
	public PlaidApiService() {
    }
	
	public ResponseModel<String> createLinkToken(String userId) throws Exception {
		ResponseModel<String> responseModel = new ResponseModel<String>();
        LinkTokenCreateRequestUser user = new LinkTokenCreateRequestUser()
                .clientUserId(userId);

        LinkTokenCreateRequest request = new LinkTokenCreateRequest()
                .user(user)
                .clientName("Wallet Manager")
                .products(List.of(Products.TRANSACTIONS))
                .language("en")
                .countryCodes(List.of(CountryCode.US));

        Response<LinkTokenCreateResponse> resp = plaidApi.linkTokenCreate(request).execute();
        if (!resp.isSuccessful()) {
            throw new RuntimeException("Error creating link token: " + resp.errorBody().string());
        }
        
        String linkToken = resp.body().getLinkToken(); 
        System.out.println("Public Token is : " + linkToken);
       
        
        responseModel.setHttpStatus(HttpStatus.OK);
		responseModel.setResponseMessage("Successfully retrieved link token");
		responseModel.setResponseStatus("SUCCESS");
		responseModel.setResponseBody(linkToken);
		return responseModel;
    }
	
	public ResponseModel<String> exchangePublicToken(PublicTokenRequest publicTokenRequest) throws Exception {
		
		ResponseModel<String> responseModel = new ResponseModel<String>();
		
		ItemPublicTokenExchangeRequest itemPublicTokenExchangeRequest = new ItemPublicTokenExchangeRequest().publicToken(publicTokenRequest.getPublicToken());
        Response<ItemPublicTokenExchangeResponse> response = plaidApi.itemPublicTokenExchange(itemPublicTokenExchangeRequest)
                .execute();

        if (!response.isSuccessful()) {
            throw new RuntimeException("Plaid error: " + response.errorBody().string());
        }
        
        String accessToken = response.body().getAccessToken();
        
        AccountsGetResponse linkedAccounts = getAccounts(accessToken);
        List<AccountBase> accountsList = linkedAccounts.getAccounts();
        Item itemDetail = linkedAccounts.getItem();
        
        String institutionName = itemDetail.getInstitutionName();
        String institutionId = itemDetail.getInstitutionId();
        
        for(AccountBase account : accountsList) {
        	
        	ExternalAccounts externalAccount = new ExternalAccounts();
        	
        	externalAccount.setUserId(publicTokenRequest.getUserId());
        	externalAccount.setAccountId(account.getAccountId());
        	externalAccount.setAccountName(account.getOfficialName());
        	externalAccount.setAccountType(account.getType());
        	externalAccount.setInstitutionId(institutionId);
        	externalAccount.setInstitutionName(institutionName);
        	externalAccount.setAccessKey(accessToken);
        	externalAccount.setIsActive(true);
        	
        	externalAccountsRepository.save(externalAccount);
        }
        
        
        responseModel.setHttpStatus(HttpStatus.OK);
		responseModel.setResponseMessage("Successfully saved accounts");
		responseModel.setResponseStatus("SUCCESS");
		responseModel.setResponseBody(accessToken);

        return responseModel;
    }
	
    public AccountsGetResponse getAccounts(String accessToken) throws Exception {
        AccountsGetRequest request = new AccountsGetRequest().accessToken(accessToken);

        Response<AccountsGetResponse> response = plaidApi.accountsGet(request).execute();
        if (!response.isSuccessful()) {
            throw new RuntimeException("Error fetching accounts: " + response.errorBody().string());
        }
        return response.body();
    }
    
    public List<TransactionsGetResponse> getTransactions(Integer userId) throws Exception {
    	List<ExternalAccounts> accountsDetails = externalAccountsRepository.findAllByUserId(userId);
    	
    	List<TransactionsGetResponse> responseData = new ArrayList<TransactionsGetResponse>();
    	if(accountsDetails == null) {
    		return responseData;
    	}
    	
    	System.out.println(accountsDetails);
    	
    	Set<String> accessTokenSet = new HashSet<String>();
    	for(ExternalAccounts account : accountsDetails) {
    		accessTokenSet.add(account.getAccessKey());
    	}  	
    	
    	for(String accessToken : accessTokenSet) {
    		
	        LocalDate startDate = LocalDate.now().minusDays(30);
	        LocalDate endDate = LocalDate.now();
	
	        TransactionsGetRequest request = new TransactionsGetRequest()
	                .accessToken(accessToken)
	                .startDate(startDate)
	                .endDate(endDate);
	
	        Response<TransactionsGetResponse> response = plaidApi.transactionsGet(request).execute();
	        if (!response.isSuccessful()) {
	            throw new RuntimeException("Error fetching transactions: " + response.errorBody().string());
	        }
	        
	        TransactionsGetResponse currentResponse = response.body();
	        System.out.println("Response for the accesstoken : "+ accessToken + "  is : "+ currentResponse.toString());
	        
	        responseData.add(currentResponse);
	        
    	}

        return responseData;
    }
	
    public ResponseModel<List<ExternalAccounts>> getLinkedAccounts(String userId){
    	Integer userIdValue = Integer.parseInt(userId);
    	ResponseModel<List<ExternalAccounts>> responseModel = new ResponseModel<List<ExternalAccounts>>();
    	
    	List<ExternalAccounts> accountsDetails = externalAccountsRepository.findAllByUserId(userIdValue);
    	
    	responseModel.setHttpStatus(HttpStatus.OK);
		responseModel.setResponseMessage("Successfully retrieved linked accounts");
		responseModel.setResponseStatus("SUCCESS");
		responseModel.setResponseBody(accountsDetails);
    	
    	
    	return responseModel;
    }
}
