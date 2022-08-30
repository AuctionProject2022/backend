package kr.toyauction.domain.alert.dto;

import kr.toyauction.domain.alert.entity.Alert;
import kr.toyauction.global.entity.AlertCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class AlertGetResponse {
    private Long alertId;
    private Long memberId;
    private String createDatetime;
    private String endDateTime;
    private String title;
    private String contents;
    private AlertCode code;
    private String url;
    private boolean alertRead;

    public AlertGetResponse(Alert alert) {
        this.alertId = alert.getId();
        this.memberId = alert.getMemberId();
        this.createDatetime = alert.getCreateDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.endDateTime = alert.getEndDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.title = alert.getTitle();
        this.contents = alert.getContents();
        this.code = alert.getCode();
        this.url = alert.getUrl();
        this.alertRead = alert.isAlertRead();
    }
}
