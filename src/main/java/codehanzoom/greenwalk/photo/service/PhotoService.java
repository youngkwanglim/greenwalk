package codehanzoom.greenwalk.photo.service;

import codehanzoom.greenwalk.photo.domain.implement.ImageUploader;
import codehanzoom.greenwalk.photo.domain.implement.FlaskApiManager;
import codehanzoom.greenwalk.plogging.domain.implement.PloggingWriter;
import codehanzoom.greenwalk.user.service.UserService;
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

    private final UserService userService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    
    // 사진 S3에 업로드 및 포인트계산 
    public int calculatePoints(MultipartFile multipartFile, Long step, double walkingDistance) throws IOException {

        // 회원 id 반환
        Long userId= userService.getUserId();

        // 변경된 파일로 업로드 실행
        String imageUrl = imageUploader.uploadImage(multipartFile, userId);

        // 플라스크로 사진 전달 후 쓰레기 갯수 반환
        int trashCount = flaskApiManager.sendToFlaskReceiveCount(multipartFile);
        // 플로깅 저장
        Long ploggingId = ploggingWriter.plogging(userId, step, walkingDistance, trashCount, imageUrl);

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