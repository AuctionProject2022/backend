package kr.toyauction.domain.alert.controller;

import kr.toyauction.global.property.Url;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlertController {

    @GetMapping(value= Url.ALERT, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAlerts(){
        return "{\n" +
                "\t\"success\" : \"true\",\n" +
                "\t\"data\" : {\n" +
                "\t\t\"content\" : [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"alertId\" : 12,\n" +
                "\t\t\t\t\"alertTitle\" : \"경매 성공\",\n" +
                "\t\t\t\t\"alertContents\" : \"<auctionTitle>이케아에서 산 홀더</auctionTitle> 경매에\\n <auctionColor>내 경매가 38,000원 낙찰완료</auctionColor> 되었습니다.\",\n" +
                "\t\t\t\t\"createDatetime\" : \"2022-08-03 22:03:16\",\n" +
                "\t\t\t\t\"url\" : \"/프론트url/product/12\",\n" +
                "                \"remainingTime\" : null,\n" +
                "\t\t\t\t\"alertRead\" : false\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"alertId\" : 15,\n" +
                "\t\t\t\t\"alertTitle\" : \"경매 실패\",\n" +
                "\t\t\t\t\"alertContents\" : \"<auctionTitle>이케아에서 산 홀더</auctionTitle> 경매에\\n <auctionColor>내 경매가 38,000원 낙찰실패<auctionColor> 하였습니다.\\n<auctionColor>최종 낙찰가는 56,000원</auctionColor> 입니다.\",\n" +
                "\t\t\t\t\"createDatetime\" : \"2022-08-03 22:03:16\",\n" +
                "\t\t\t\t\"url\" : \"/프론트url/product/123\",\n" +
                "                \"remainingTime\" : null,\n" +
                "\t\t\t\t\"alertRead\" : false\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"alertId\" : 112,\n" +
                "\t\t\t\t\"alertTitle\" : \"입찰 참여 완료\",\n" +
                "\t\t\t\t\"alertContents\" : \"<auctionTitle>이케아에서 산 홀더</auctionTitle> 경매에\\n <auctionColor>내 경매가 38,000원 응찰완료</auctionColor> 되었습니다.\",\n" +
                "\t\t\t\t\"createDatetime\" : \"2022-08-03 22:03:16\",\n" +
                "                \"remainingTime\" : \"238704\",\n" +
                "\t\t\t\t\"url\" : \"/프론트url/product/122\",\n" +
                "                \"remainingTime\" : null,\n" +
                "\t\t\t\t\"alertRead\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"alertId\" : 152,\n" +
                "\t\t\t\t\"alertTitle\" : \"경매 성공\",\n" +
                "\t\t\t\t\"alertContents\" : \"<auctionTitle>이케아에서 산 홀더</auctionTitle> 경매가\\n <auctionColor>내 경매가 38,000원 낙찰완료</auctionColor> 되었습니다.\",\n" +
                "\t\t\t\t\"createDatetime\" : \"2022-08-03 22:03:16\",\n" +
                "\t\t\t\t\"url\" : \"/프론트url/product/112\",\n" +
                "                \"remainingTime\" : null,\n" +
                "\t\t\t\t\"alertRead\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"alertId\" : 222,\n" +
                "\t\t\t\t\"alertTitle\" : \"경매 실패\",\n" +
                "\t\t\t\t\"alertContents\" : \"<auctionTitle>이케아에서 산 홀더</auctionTitle> 경매가\\n <auctionColor>응찰자 0명으로 낙찰실패</auctionColor> 하였습니다.\\n<auctionColor>재판매 2회 가능</auctionColor>합니다.\",\n" +
                "\t\t\t\t\"createDatetime\" : \"2022-08-03 22:03:16\",\n" +
                "\t\t\t\t\"url\" : \"/프론트url/product/152\",\n" +
                "                \"remainingTime\" : null,\n" +
                "\t\t\t\t\"alertRead\" : true\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"alertId\" : 333,\n" +
                "\t\t\t\t\"alertTitle\" : \"판매 등록 완료\",\n" +
                "\t\t\t\t\"alertContents\" : \"<auctionTitle>이케아에서 산 홀더</auctionTitle> 경매가\\n <auctionColor>최소 구매가 38,000원으로 판매 등록 완료</auctionColor> 되었습니다.\",\n" +
                "\t\t\t\t\"createDatetime\" : \"2022-08-03 22:03:16\",\n" +
                "                \"remainingTime\" : \"238704\",\n" +
                "\t\t\t\t\"url\" : \"/프론트url/product/182\",\n" +
                "                \"remainingTime\" : null,\n" +
                "\t\t\t\t\"alertRead\" : true\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"pageable\": {\n" +
                "\t\t\t\"sort\": {\n" +
                "\t\t\t\t\"sorted\": false,\n" +
                "\t\t\t\t\"unsorted\": true,\n" +
                "\t\t\t\t\"empty\": true\n" +
                "\t\t\t},\n" +
                "\t\t\t\"pageSize\": 6,\n" +
                "\t\t\t\"pageNumber\": 2,\t\t\n" +
                "\t\t\t\"offset\": 11,\t\t\t\n" +
                "\t\t\t\"paged\": true,\n" +
                "\t\t\t\"unpaged\": false\n" +
                "\t\t},\n" +
                "\t\t\"number\": 2,\t\t\t\t\n" +
                "\t\t\"sort\": {\n" +
                "\t\t\t\"sorted\": false,\n" +
                "\t\t\t\"unsorted\": true,\n" +
                "\t\t\t\"empty\": true\n" +
                "\t\t},\n" +
                "\t\t\"first\": false,\t\t\t\t\n" +
                "\t\t\"last\": false,\n" +
                "\t\t\"numberOfElements\": 5,\n" +
                "\t\t\"size\": 15,\n" +
                "\t\t\"empty\": false\n" +
                "\t}\n" +
                "}";
    }

    @PostMapping(Url.ALERT +"/{alertId}")
    public String getAlert(@PathVariable final Long alertId){
        return "{\n" +
                "\t\"success\" : true\n" +
                "}";
    }
}
