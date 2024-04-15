package codehanzoom.greenwalk.photo.dto;

import lombok.Getter;

@Getter
public class TrashCountDto {

    private int trashCount;

    public TrashCountDto(int trashCount) {
        this.trashCount = trashCount;
    }
}
