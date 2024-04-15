package codehanzoom.greenwalk.photo.service;

import codehanzoom.greenwalk.photo.domain.implementation.ImageUploader;
import codehanzoom.greenwalk.plogging.domain.implementation.FlaskApiManager;
import codehanzoom.greenwalk.plogging.domain.implementation.PloggingWriter;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final AmazonS3 amazonS3;

    private final ImageUploader imageUploader;

    private final PloggingWriter ploggingWriter;

    private final FlaskApiManager flaskApiManager;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${flask.url}")
    private String url;
    
    // 사진 S3에 업로드 및 포인트계산 
    public int uploadImage(MultipartFile multipartFile) throws IOException {
        String imageUrl = imageUploader.uploadImage(multipartFile);

        // ploggingWriter.createPlogging(-,-,-);
        int trashCount = flaskApiManager.sendToFlaskReceiveCount(multipartFile);

        return trashCount;
    }

    // 사진 다운로드
    public ResponseEntity<UrlResource> downloadImage(String originalFilename) {
        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, originalFilename));

        String contentDisposition = "attachment; filename=\"" +  originalFilename + "\"";

        // header에 CONTENT_DISPOSITION 설정을 통해 클릭 시 다운로드 진행
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }
}