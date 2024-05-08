package codehanzoom.greenwalk.plogging.dto;

import lombok.Getter;

@Getter
public class PloggingResponse {

    private int trashCount;
    private String imageUrl;
    private int point;

    public PloggingResponse(int trashCount, String imageUrl, int point) {
        this.trashCount = trashCount;
        this.imageUrl = imageUrl;
        this.point = point;
    }
}
