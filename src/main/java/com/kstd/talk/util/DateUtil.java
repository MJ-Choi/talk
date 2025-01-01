package com.kstd.talk.util;

import com.kstd.talk.api.error.ErrorCode;
import com.kstd.talk.api.error.ResponseException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class DateUtil {

    /**
     * 문자열을 날짜형으로 형변환
     *
     * @param strDateTime "yyyy-MM-dd HH:mm" 형식의 문자열
     * @return LocalDateTime
     */
    public static LocalDateTime strToDt(String strDateTime) {
        // 문자열을 파싱하기 위한 DateTimeFormatter 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            return LocalDateTime.parse(strDateTime, formatter);
        } catch (DateTimeParseException dateTimeParseException) {
            log.error("Invalid String: {}", strDateTime);
            throw new ResponseException(ErrorCode.INPUT_ERROR);
        }
    }

    public static String dtToStr(LocalDateTime dateTime) {
        // 문자열을 파싱하기 위한 DateTimeFormatter 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            return dateTime.format(formatter);
        } catch (DateTimeParseException dateTimeParseException) {
            log.error("failed to convert date: {}", dateTime);
            return "";
        }

    }
}
