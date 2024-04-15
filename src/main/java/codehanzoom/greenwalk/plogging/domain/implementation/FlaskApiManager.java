package codehanzoom.greenwalk.plogging.domain.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FlaskApiManager {

    @Value("${flask.url}")
    private String url;

    public int sendToFlaskReceiveCount(MultipartFile image) throws JsonProcessingException
    {
        RestTemplate restTemplate = new RestTemplate();

        // Flask 서버의 엔드포인트
        String flaskUrl = url + "/receive_count";

        // 요청 바디에 들어갈 데이터 설정
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // 파일 등록
        body.add("image", image.getResource());

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 서버에 POST 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.exchange(flaskUrl, HttpMethod.POST, requestEntity, String.class);

        // 여기다 넣지 말고 PloggingWriter 여기서 넣는게 낫다는거지? ㅇㅋㅇㅋ

        // 쓰레기 갯수 반환
        return Integer.parseInt(responseEntity.getBody());
    }
}
