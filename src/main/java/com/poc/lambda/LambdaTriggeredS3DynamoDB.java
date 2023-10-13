package com.poc.lambda;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.poc.lambda.pojo.StudentBean;



public class LambdaTriggeredS3DynamoDB implements RequestHandler<S3Event, Boolean>{
    private final AmazonS3 s3Client = AmazonS3Client.builder()
    		.withCredentials(new DefaultAWSCredentialsProviderChain()).build();
    
    private DynamoDBMapper dynamoDBMapper;
	
	public Boolean handleRequest(S3Event input, Context context) {
		 final LambdaLogger logger = context.getLogger();
		 if(input.getRecords().isEmpty()) {
			 logger.log("LambdaTriggeredS3DynamoDB => Empty records");
			 return false;
		 }
		 
		 //process the record
		 for (S3EventNotification.S3EventNotificationRecord record : input.getRecords()) {
			
			 String bucketName = record.getS3().getBucket().getName();
			 String bucketKey = record.getS3().getObject().getKey();
			 
			S3Object s3Object =  s3Client.getObject(bucketName, bucketKey);
			InputStream s3ObjectIS = s3Object.getObjectContent();
			List<StudentBean> listObj = new ArrayList<>();
			try(final BufferedReader br=new BufferedReader(new InputStreamReader(s3ObjectIS, StandardCharsets.UTF_8))){
				br.lines().skip(1).forEach(line -> {
					String [] arr = line.split(",");
					StudentBean  stdObj = new StudentBean();
					stdObj.setStdName(arr[0]);					
					stdObj.setStdGrade(arr[1]);
					stdObj.setStdSubject(arr[2]);
					stdObj.setStdMark(Integer.parseInt(arr[3]));
					initDynamoDB();
					dynamoDBMapper.save(stdObj);
					//listObj.add(stdObj);
				});
				
				
				logger.log("Successfully inserted Student table.");
			}catch(Exception e) {
				logger.log("LambdaTriggeredS3DynamoDB => Exception"+e.getMessage());
				return false;
			}
			 
		}
		 
		return true;
	}
	
	private void initDynamoDB() {
		
		AmazonDynamoDB client = AmazonDynamoDBClient.builder().withCredentials(new DefaultAWSCredentialsProviderChain()).build();
		dynamoDBMapper = new DynamoDBMapper(client);
	}
}
