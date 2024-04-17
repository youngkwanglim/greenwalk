package codehanzoom.greenwalk.photo.service;

import codehanzoom.greenwalk.photo.domain.implement.ImageUploader;
import codehanzoom.greenwalk.photo.domain.implement.FlaskApiManager;
import codehanzoom.greenwalk.plogging.domain.implement.PloggingWriter;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final AmazonS3 amazonS3;
    private final ImageUploader imageUploader;
    private final FlaskApiManager flaskApiManager;
    private final PloggingWriter ploggingWriter;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    
    // 사진 S3에 업로드 및 포인트계산 
    public int calculatePoints(MultipartFile multipartFile, Long userId, Long step, float walkingDistance) throws IOException {

        // 변경된 파일로 업로드 실행
        String imageUrl = imageUploader.uploadImage(multipartFile, userId);
        System.out.println("이미지 S3에 저장됨");

        int trashCount = flaskApiManager.sendToFlaskReceiveCount(multipartFile);
        System.out.println("플라스크로도 잘 넘어감");

        ploggingWriter.plogging(userId, step, walkingDistance, trashCount, imageUrl);
        //플로깅 저장 찐막 끝

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