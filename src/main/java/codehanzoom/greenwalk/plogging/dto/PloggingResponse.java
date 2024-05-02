package codehanzoom.greenwalk.plogging.dto;

import lombok.Getter;

@Getter
public class PloggingResponse {

    private int trashCount;
    private String imageUrl;

    public PloggingResponse(int trashCount, String imageUrl) {
        this.trashCount = trashCount;
        this.imageUrl = imageUrl;
    }
}
