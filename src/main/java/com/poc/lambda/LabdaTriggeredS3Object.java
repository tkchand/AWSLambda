package com.poc.lambda;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;



public class LabdaTriggeredS3Object implements RequestHandler<S3Event, Boolean>{
    private final AmazonS3 s3Client = AmazonS3Client.builder()
    		.withCredentials(new DefaultAWSCredentialsProviderChain()).build();
	
	public Boolean handleRequest(S3Event input, Context context) {
		 final LambdaLogger logger = context.getLogger();
		 if(input.getRecords().isEmpty()) {
			 logger.log("LabdaTriggeredS3Object => Empty records");
			 return false;
		 }
		 
		 //process the record
		 for (S3EventNotification.S3EventNotificationRecord record : input.getRecords()) {
			
			 String bucketName = record.getS3().getBucket().getName();
			 String bucketKey = record.getS3().getObject().getKey();
			 
			S3Object s3Object =  s3Client.getObject(bucketName, bucketKey);
			InputStream s3ObjectIS = s3Object.getObjectContent();
			try(final BufferedReader br=new BufferedReader(new InputStreamReader(s3ObjectIS, StandardCharsets.UTF_8))){
				br.lines().skip(1).forEach(line -> logger.log(line +"\n"));
			}catch(Exception e) {
				logger.log("LabdaTriggeredS3Object => Exception"+e.getMessage());
				return false;
			}
			 
		}
		 
		return true;
	}
}
