package com.example.mobileapi.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public String extractDetailMessage(String errorMessage) {
        Pattern pattern = Pattern.compile("Detail: (.*?)]");
        Matcher matcher = pattern.matcher(errorMessage);

        if (matcher.find()) {
            return matcher.group(1); // ✅ "Key (email)=(leokang123@naver.com) already exists." 만 추출
        }
        return "Unknown error"; // ✅ 일치하는 내용이 없으면 기본 메시지 반환
    }
}
