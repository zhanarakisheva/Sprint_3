package sprint3.dto;

public class NewOrderResponse {
    private Long track;

    public NewOrderResponse() {
    }

    public NewOrderResponse(Long track) {
        this.track = track;
    }

    public Long getTrack() {
        return track;
    }

    public void setTrack(Long track) {
        this.track = track;
    }
}
