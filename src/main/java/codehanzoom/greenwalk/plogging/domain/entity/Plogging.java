package codehanzoom.greenwalk.plogging.domain.entity;

import codehanzoom.greenwalk.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Plogging {

    @Id
    @GeneratedValue
    @Column(name = "plogging_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long step;

    private Long walkingDistance;

    private int trashCount;

    private int point;

    private String imageUrl;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

//    /**
//     * 모금액 감소
//     */
//    public void removeDonationAmount(int money) {
//        int restDonationAmount = this.totalDonationAmount - money;
//        this.totalDonationAmount  = restDonationAmount;
//    }
//
//    /**
//     * 모금액 증가
//     */
//    public void addDonationAmount(int donateAmount){
//        this.totalDonationAmount += donateAmount;
//    }
}