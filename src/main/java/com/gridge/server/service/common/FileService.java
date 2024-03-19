package com.gridge.server.service.common;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.gridge.server.common.exception.BaseException;
import com.gridge.server.common.response.BaseResponseState;
import com.gridge.server.common.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

import static com.gridge.server.common.response.BaseResponseState.NOT_IMAGE_FILE;

@Component
@RequiredArgsConstructor
public class FileService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = TimeUtil.currentDateTimeString() + multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3Client.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3Client.getUrl(bucket, originalFilename).toString();
    }

    public void checkImageType(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        if(contentType == null){
            throw new BaseException(NOT_IMAGE_FILE);
        }

        String[] splitedString = contentType.split("/");
        if(splitedString.length < 1 || !splitedString[0].equals("image")){
            throw new BaseException(NOT_IMAGE_FILE);
        }
    }
}
