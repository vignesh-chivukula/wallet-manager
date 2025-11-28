package com.backend.walletmanager.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.math.BigInteger;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.backend.walletmanager.entity.TransactionObjects;
import com.backend.walletmanager.entity.TransactionType;
import com.plaid.client.model.AccountBase;
import com.plaid.client.model.Item;
import com.plaid.client.model.Transaction;
import com.plaid.client.model.TransactionsGetResponse;

@Component
public class PlaidTransactionHelper {
	
	public List<TransactionObjects> convertPlaidToStandardTransactions(List<TransactionsGetResponse> plaidTransactions, Integer userId){
		List<TransactionObjects> response = new ArrayList<TransactionObjects>();
		
		for(TransactionsGetResponse plaidTransaction : plaidTransactions) {

			Map<String, AccountBase> accountMap = plaidTransaction.getAccounts().stream()
			        .collect(Collectors.toMap(AccountBase::getAccountId, account -> account));
			
			Item item = plaidTransaction.getItem();
			List<Transaction> transactions = plaidTransaction.getTransactions();
			
			for(Transaction plaidTransactionObject : transactions) {
				TransactionObjects appTransaction = new TransactionObjects();

				appTransaction.setTransactionDate(Date.from(plaidTransactionObject.getDate().atStartOfDay(ZoneId.of("America/New_York")).toInstant()));
				appTransaction.setTransactionType(plaidTransactionObject.getPersonalFinanceCategory().getPrimary().equalsIgnoreCase("INCOME")
						? TransactionType.INCOME : TransactionType.EXPENSE);
				appTransaction.setTransactionDetail(plaidTransactionObject.getName());
				appTransaction.setTransactionCategory(getTransactionCategory(plaidTransactionObject));
				appTransaction.setTrasactionCost(Math.abs(plaidTransactionObject.getAmount()));
				appTransaction.setTransactionSource(accountMap.get(plaidTransactionObject.getAccountId()).getName());
				appTransaction.setUserId(userId);
					
				response.add(appTransaction);
			}
			
			
		}
		
		return response;
	}
	
	public String getTransactionCategory(Transaction transaction) {
		String category = "General";
		String plaidCategory = transaction.getPersonalFinanceCategory().getDetailed();
		category = TransactionCategoryMapper.valueOf(plaidCategory).getCategory();
		
		
		return category == null ? "Miscellaneous" : category;
	}
}
