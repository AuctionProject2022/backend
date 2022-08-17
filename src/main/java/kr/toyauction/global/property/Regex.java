package kr.toyauction.global.property;

public class Regex {
    public static final String USERNAME = "^[a-zA-Z0-9가-힣]{2,10}$"; // 영문, 숫자, 한글로 이루어진 2~10글자로 작성해야 합니다.
    public static final String EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"; // EMAIL 정규식
    public static final String PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[-/:;()$&@\".,?!'#%^*+=_|~<>]).{8,30}$"; // 8~30 자리 영문자, 숫자, 특수 문자 1개 이상 반드시 포함
    public static final String PRODUCTNAME = "^[a-zA-Z0-9가-힣\\s]{1,40}$"; // 1~40 자리 영문자, 숫자, 한글, 공백
}