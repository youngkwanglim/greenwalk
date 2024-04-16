package codehanzoom.greenwalk.photo.domain.implement;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ImageUploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private LocalDateTime uploadDate;

    public String uploadImage(MultipartFile multipartFile, Long userId) throws IOException {
        uploadDate = LocalDateTime.now();

        String originalFilename = String.valueOf(userId) + "/" + String.valueOf(uploadDate);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);

        return amazonS3.getUrl(bucket, originalFilename).toString();
    }

}
