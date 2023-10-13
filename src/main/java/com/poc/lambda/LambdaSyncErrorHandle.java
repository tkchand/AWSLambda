package com.poc.lambda;

import org.apache.http.HttpStatus;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.lambda.pojo.ErrorMgmt;
import com.poc.lambda.pojo.Response;
import com.poc.lambda.pojo.UserBean;

public class LambdaSyncErrorHandle implements
RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
	ObjectMapper mapper = new ObjectMapper();  
	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent reqEvent, Context context) {
        final LambdaLogger logger = context.getLogger();
        String body = reqEvent.getBody();
        
        try {
			UserBean user =  mapper.readValue(body, UserBean.class);
			if(StringUtils.isNullOrEmpty(user.getUserName()) || user.getId() == null) {
				return returnApiResponse(HttpStatus.SC_BAD_GATEWAY, "Error Request Body invalid","User name, id not valid","400", logger);
			}
			logger.log(user.toString());
			
		} catch (JsonProcessingException e) {
			 logger.log("LambdaSyncErrorHandle =>"+e.getMessage());
		}
		try {
			return returnApiResponse(HttpStatus.SC_OK, "Request Body Valid","","", logger);
		} catch (JsonProcessingException e) {
			 
			e.printStackTrace();
		}
		return null;
	}

    public APIGatewayProxyResponseEvent returnApiResponse(int statusCode, String responseBody,
														  String errorMessage, String errorCode, LambdaLogger logger ) throws JsonProcessingException{

		final ErrorMgmt error = new ErrorMgmt();
		if(!StringUtils.isNullOrEmpty(errorCode)){
			error.setErrorCode(errorCode);
			error.setErrorMessage(errorMessage);
		}
		
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
				.withStatusCode(statusCode).withBody(mapper.writeValueAsString(new Response<String>(statusCode, responseBody, error)));
		

		return response;
	}
}
