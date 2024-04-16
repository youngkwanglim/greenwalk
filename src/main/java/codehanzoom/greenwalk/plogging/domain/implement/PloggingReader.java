package codehanzoom.greenwalk.plogging.domain.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PloggingReader {

//    private final PloggingRepository ploggingRepository;
//
//    public Plogging readPlogging(```) {
//        return ploggingRepository.findById();
//    }
}
