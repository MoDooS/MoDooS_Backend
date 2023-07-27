package com.study.modoos.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Campus {
    SEOUL("인문",
            Arrays.asList(
                    Department.MANAGE_INFORMATION,
                    Department.BUSINESS,
                    Department.ECONOMICS,
                    Department.KOREAN,
                    Department.INTERNATIONAL_TRADE,
                    Department.GLOBAL_BUSINESS,
                    Department.GLOBAL_ASSIAN,
                    Department.GLOBAL_KOREAN,
                    Department.DIGITAL_MEDIA,
                    Department.DIGITAL_CONTENT,
                    Department.WRITING,
                    Department.LIBRARY_INFORMATION,
                    Department.ART_HISTORY,
                    Department.LEGAL_HISTORY,
                    Department.LAW,
                    Department.REAL_ESTATE,
                    Department.HISTORY,
                    Department.CHILD,
                    Department.ARABIC,
                    Department.ENGLISH,
                    Department.CONVERGENCE_SOFTWARE,
                    Department.CONVERGENCE_STUDY,
                    Department.JAPANESE,
                    Department.INTERDISCIPLINARY_SEOUL,
                    Department.POLITIC,
                    Department.CHINESE,
                    Department.PHILOSOPHY,
                    Department.YOUTH,
                    Department.ADMINISTRATION)),
    YONGIN("자연",
            Arrays.asList(
                    Department.ARCHITECTURE,
                    Department.TRANSPORTATION,
                    Department.MECHANICAL,
                    Department.DESIGN,
                    Department.PHYSICS,
                    Department.BADUK,
                    Department.SEMICONDUCTOR,
                    Department.INDUSTRIAL_MANAGEMENT,
                    Department.BIOINFORMATICS,
                    Department.MATH,
                    Department.SPORTS,
                    Department.FOOD,
                    Department.MATERIALS,
                    Department.ART,
                    Department.INTERDISCIPLINARY_YONGIN,
                    Department.ELECTRICAL,
                    Department.ELECTRONICS,
                    Department.COMPUTER,
                    Department.CIVIL_ENVIRONMENTAL,
                    Department.CHEMICAL,
                    Department.CHEMISTRY,
                    Department.ENVIRONMENTAL_ENERGY,
                    Department.INFORMATION_COMMUNICATION
            )),
    COMMON("공통", Collections.EMPTY_LIST);

    private static final Map<String, Campus> campusMap = Stream.of(values())
            .collect(Collectors.toMap(Campus::getCampusName, Function.identity()));

    @JsonValue
    private final String campusName;

    private List<Department> departmentList;

    Campus(String campusName, List<Department> departmentList) {
        this.campusName = campusName;
        this.departmentList = departmentList;
    }

    @JsonCreator
    public static Campus resolve(String campusName) {
        return Optional.ofNullable(campusMap.get(campusName))
                .orElseThrow(() -> new ModoosException(ErrorCode.VALUE_NOT_IN_OPTION));
    }

    public static Campus findByDepartment(Department department) {
        return Arrays.stream(Campus.values())
                .filter(d -> d.hasDepartment(department))
                .findAny()
                .orElseThrow(() -> new ModoosException(ErrorCode.VALUE_NOT_IN_OPTION));
    }

    public boolean hasDepartment(Department department) {
        return departmentList.stream()
                .anyMatch(d -> d == department);
    }
}
