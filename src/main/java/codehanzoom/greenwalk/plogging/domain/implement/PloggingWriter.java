package codehanzoom.greenwalk.plogging.domain.implement;

import codehanzoom.greenwalk.plogging.domain.entity.Plogging;
import codehanzoom.greenwalk.plogging.repository.PloggingRepository;
import codehanzoom.greenwalk.user.domain.User;
import codehanzoom.greenwalk.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class PloggingWriter {

    private final PloggingRepository ploggingRepository;
    private final UserRepository userRepository;

    public Long plogging(Long userId, Long step, double walkingDistance, int trashCount, String imageUrl) {

        // 회원 엔티티 조회
        User user = userRepository.findById(userId).get();

        // 플로깅 포인트 계산
        int plogginPoint = 0;
        if (step < 3000){
            plogginPoint = trashCount * 10;
        }
        else if  (step < 6000){
            plogginPoint = trashCount * 20;
        }
        else if (step < 9000) {
            plogginPoint = trashCount * 30;
        }
        else {
            plogginPoint = trashCount * 40;
        }

        // 플로깅 생성
        Plogging plogging = Plogging.createPlogging(user, step, walkingDistance, trashCount, imageUrl, plogginPoint);
        ploggingRepository.save(plogging);

        return plogging.getId();
    }
}
