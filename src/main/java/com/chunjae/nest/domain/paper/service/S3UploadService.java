package com.chunjae.nest.domain.paper.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadPaper(MultipartFile multipartFile) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        String contentType = metadata.getContentType();
        log.info("contentType: {}", contentType);

        String fileName = setFileName(multipartFile);

        amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
        return URLDecoder.decode(amazonS3.getUrl(bucket, fileName).toString(), "UTF-8");
    }


    public String setFileName(MultipartFile multipartFile) {
        return "static" + "/" + UUID.randomUUID() + "." + multipartFile.getOriginalFilename();
    }

    public String getFileName(String url) {
        String[] parts = url.split("/");
        return parts[parts.length - 1];
    }

}
