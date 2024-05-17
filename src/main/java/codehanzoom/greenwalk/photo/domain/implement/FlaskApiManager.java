package codehanzoom.greenwalk.photo.domain.implement;

import codehanzoom.greenwalk.photo.domain.ImageName;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FlaskApiManager {

    @Value("${flask.url}")
    private String url;

    public int sendToFlaskReceiveCount(MultipartFile image) throws IOException
    {
        RestTemplate restTemplate = new RestTemplate();

        // Flask 서버의 엔드포인트
        String flaskUrl = url + "/receive_count";

        // 이미지 리사이즈
        File resizedImage = this.getImageResizer(image);

        // 요청 바디에 들어갈 데이터 설정
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("image", new FileSystemResource(resizedImage));

        MultiValueMap<String, HttpEntity<?>> body = builder.build();

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> requestEntity = new HttpEntity<>(body, headers);

        // 서버에 POST 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.exchange(flaskUrl, HttpMethod.POST, requestEntity, String.class);

        // File 삭제
        resizedImage.delete();

        // 쓰레기 갯수 반환
        //System.out.println(responseEntity.getBody());

        return Integer.parseInt(responseEntity.getBody());
    }

    //ImageResizer 객체 생성
    public File getImageResizer(MultipartFile file) throws IOException {
        ImageResizer resizer = new ImageResizer(file, new ImageName(file.getOriginalFilename()));
        File resizedfile = resizer.getResizedImage();
        return resizedfile;
        }
    }

