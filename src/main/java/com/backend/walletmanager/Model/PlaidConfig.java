package com.backend.walletmanager.Model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.plaid.client.ApiClient;
import com.plaid.client.request.PlaidApi;

@Configuration
public class PlaidConfig {
	
	private static final String PlaidClient = null;

	@Value("${plaid.client-id}")
	private String clientId;
	
	@Value("${plaid.secret}")
	private String plaidSecret;
	
	@Value("${plaid.environment}")
	private String environment;
	
	public static final String Production = "https://production.plaid.com";
	public static final String Sandbox = "https://sandbox.plaid.com";

	public PlaidConfig(String clientId, String plaidSecret, String environment) {
		super();
		this.clientId = clientId;
		this.plaidSecret = plaidSecret;
		this.environment = environment;
	}

	public PlaidConfig() {
		super();
	}
	
	@Bean
    public PlaidApi plaidApi() {
		Map<String,String> authMap = new HashMap<>(Map.of("clientId",clientId, "secret",plaidSecret));
        ApiClient client = new ApiClient(authMap);

        switch (environment.toLowerCase()) {
            case "production":
            	client.setPlaidAdapter(Production);
                break;
            default:
            	client.setPlaidAdapter(Sandbox);
        }
        client.createService(PlaidApi.class);
        return client.createService(PlaidApi.class);
    }
	
}
