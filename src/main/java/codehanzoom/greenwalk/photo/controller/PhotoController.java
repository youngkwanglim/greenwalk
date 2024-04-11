package codehanzoom.greenwalk.photo.controller;

import codehanzoom.greenwalk.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping(value = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> photoUpload(@RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            if (image == null) {
                // 이미지가 전송되지 않은 경우에 대한 처리
                return ResponseEntity.badRequest().body("Image file is required.");
            }

            // 플라스크에 사진 전달


            // S3에 사진 저장
            String profileImage = photoService.uploadImage(image);
            return ResponseEntity.ok(profileImage);
        } catch (IOException e) {
            // IOException 발생 시 처리
            e.printStackTrace(); // 또는 로그에 기록하거나 예외를 다시 throw할 수 있습니다.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }


//    @GetMapping("/partners")
//    public List<PartnerDto> partner() {
//        List<Partner> partners = partnerService.findPartners();
//        List<PartnerDto> result = partners.stream()
//                .map(p -> new PartnerDto(p))
//                .collect(Collectors.toList());
//
//        return result;
//    }

//    @GetMapping("/partners/{id}"))
//    public void detailPartner(@RequestParam("sponsorId") Long sponsorId) {
//
//    }

}