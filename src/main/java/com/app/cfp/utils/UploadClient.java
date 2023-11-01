package com.app.cfp.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import static org.apache.naming.SelectorContext.prefix;

@Component
public class UploadClient {

    private static final String REGION = "eu-central-1";
    private static final String BUCKET_NAME = "cfp-images";
    private static final String DESTINATION_FOLDER = "cfp-images/";

    AmazonS3 s3Client = AmazonS3ClientBuilder
            .standard()
            .withRegion(REGION)
            .withCredentials(new EnvironmentVariableCredentialsProvider())
            .build();

    public void uploadFile(File file) {
        //Concatenate the folder and file name to get the full destination path
        String destinationPath = DESTINATION_FOLDER + file.getName();

        //Create a PutObjectRequest
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, destinationPath, file);

        //Perform the upload, and assign the returned result object for further processing
        PutObjectResult putObjectResult = s3Client.putObject(putObjectRequest);
    }

    public URL getURL(String fileName) {
        return s3Client.getUrl(BUCKET_NAME, DESTINATION_FOLDER + fileName);
    }

    public String generateImageName(String originalName, String additional) {
        if (originalName == null) {
            return "image_" + System.currentTimeMillis() + ".jpg";
        }
        return originalName.substring(0, originalName.length()-4) + '_' + additional + originalName.substring(originalName.length()-4);
    }

    public List<String> listObjects(){
        List<String> objects = new ArrayList<>();
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(BUCKET_NAME);
        ListObjectsV2Result listing = s3Client.listObjectsV2(req);
        for (S3ObjectSummary summary: listing.getObjectSummaries()) {
            if(summary.getKey().startsWith("images")) {
                objects.add(summary.getKey());
            }
        }
        return objects;
    }
}