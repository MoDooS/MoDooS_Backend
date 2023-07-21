package com.study.modoos.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Department {
    MANAGE_INFORMATION("경영정보학과"),
    BUSINESS("경영학과"),
    ECONOMICS("경제학과"),
    KOREAN("국어국문학과"),
    INTERNATIONAL_TRADE("국제통상학과"),
    GLOBAL_BUSINESS("글로벌비즈니스전공"),
    GLOBAL_ASSIAN("글로벌아시아문화학과"),
    GLOBAL_KOREAN("글로벌한국어학과"),
    DIGITAL_MEDIA("디지털미디어학과"),
    DIGITAL_CONTENT("디지털콘텐츠디자인학과"),
    WRITING("문예창작학과"),
    LIBRARY_INFORMATION("문헌정보학과"),
    ART_HISTORY("미술사학과"),
    LEGAL_HISTORY("법무정책학과"),
    LAW("법학과"),
    REAL_ESTATE("부동산학과"),
    HISTORY("사학과"),
    CHILD("아동학과"),
    ARABIC("아랍지역학과"),
    ENGLISH("영어영문학과"),
    CONVERGENCE_SOFTWARE("융합소프트웨어학부"),
    CONVERGENCE_STUDY("융합전공학부"),
    JAPANESE("일어일문학과"),
    INTERDISCIPLINARY_SEOUL("전공자유학부(인문)"),
    INFORMATION_COMMUNICATION("정보통신공학과"),
    POLITIC("정치외교학과"),
    CHINESE("중어중문학과"),
    PHILOSOPHY("철학과"),
    YOUTH("청소년지도학과"),
    ADMINISTRATION("행정학과"),
    ARCHITECTURE("건축학부"),
    TRANSPORTATION("교통공학과"),
    MECHANICAL("기계공학과"),
    DESIGN("디자인학부"),
    PHYSICS("물리학과"),
    BADUK("바둑학과"),
    SEMICONDUCTOR("반도체공학과"),
    INDUSTRIAL_MANAGEMENT("산업경영공학과"),
    BIOINFORMATICS("생명과학정보학과"),
    MATH("수학과"),
    SPORTS("스포츠학부"),
    FOOD("식품영양학과"),
    MATERIALS("신소재공학과"),
    ART("예술학부"),
    INTERDISCIPLINARY_YONGIN("전공자유학부(자연)"),
    ELECTRICAL("전기공학과"),
    ELECTRONICS("전자공학과"),
    COMPUTER("컴퓨터공학과"),
    CIVIL_ENVIRONMENTAL("토목환경공학과"),
    CHEMICAL("화학공학과"),
    CHEMISTRY("화학과"),
    ENVIRONMENTAL_ENERGY("환경에너지공학과");

    private static final Map<String, Department> departmentMap = Stream.of(values())
            .collect(Collectors.toMap(Department::getTitle, Function.identity()));

    @JsonValue
    private final String title;

    @JsonCreator
    public static Department resolve(String title) {
        return Optional.ofNullable(departmentMap.get(title))
                .orElseThrow(() -> new ModoosException(ErrorCode.VALUE_NOT_IN_OPTION));
    }

}
