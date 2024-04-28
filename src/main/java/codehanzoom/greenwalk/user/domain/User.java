package codehanzoom.greenwalk.user.domain;

import codehanzoom.greenwalk.donation.domain.Donation;
import codehanzoom.greenwalk.plogging.domain.entity.Plogging;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotEmpty
    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private int totalPoint;

    private int totalDonation;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    private int totalStep;

    private int totalTrashCount;

    private float totalWalkingDistance;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Donation> donations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Plogging> ploggings = new ArrayList<>();

    //== 비밀번호 암호화 메소드 ==//
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    //== 유저 필드 업데이트 ==//
    public void updateName(String updatename) {
        this.name = updatename;
    }

    public void updatePassword(String updatePassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(updatePassword);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    /**
     * 포인트 증가
     */
    public void addTotalPoint(int point) {
        this.totalPoint += point;
    }

    /**
     * 포인트 감소
     */
    public void removeTotalPoint(int point) {
        int restPoint = this.totalPoint - point;
//        if (restPoint < 0) {
//            throw new NotEnoughStockException("need more stock");
//        }
        this.totalPoint  = restPoint;
    }

    public void addtotalStep(int step){this.totalStep += step;}
    public void removetotalStep(int step){this.totalStep -= step;}

    public void addtotalDonation(int donation){this.totalDonation += donation;}
    public void removetotalDonation(int donation){this.totalDonation -= donation;}

    public void addtotalTrashCount(int trashCount){this.totalTrashCount += trashCount;}
    public void removetotalTrashCount(int trashCount){this.totalTrashCount -= trashCount;}

    public void addtotalWalkingDistance(float walkingDistance){this.totalWalkingDistance += walkingDistance;}
    public void removetotalWalkingDistance(float walkingDistance){this.totalWalkingDistance -= walkingDistance;}
}
