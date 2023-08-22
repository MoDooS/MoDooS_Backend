package com.study.modoos.heart.service;

import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.heart.entity.Heart;
import com.study.modoos.heart.repository.HeartRepository;
import com.study.modoos.heart.repository.HeartRepositoryImpl;
import com.study.modoos.heart.response.HeartClickResponse;
import com.study.modoos.heart.response.HeartResponse;
import com.study.modoos.member.entity.Member;
import com.study.modoos.member.repository.MemberRepository;
import com.study.modoos.study.entity.Category;
import com.study.modoos.study.entity.Study;
import com.study.modoos.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final HeartRepositoryImpl heartRepositoryImpl;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;


    public Slice<HeartResponse> getAllHeart(Member member, Pageable page) {

        return heartRepositoryImpl.getSliceOfHeartStudies(member, page);
    }

    public HeartClickResponse clickHeart(Member member, Long recruitId) {
        Member currentUser = memberRepository.findById(member.getId())
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));
        Study study = studyRepository.findById(recruitId)
                .orElseThrow(() -> new ModoosException(ErrorCode.STUDY_NOT_FOUND));
        Optional<Heart> existingHeart = heartRepository.findByMemberAndStudy(currentUser, study);


        if (existingHeart.isPresent()) {
            heartRepository.delete(existingHeart.get());
            study.deleteHeart();
        } else {
            Heart heart = new Heart(currentUser, study);
            heartRepository.save(heart);
            study.addHeart();
        }
        studyRepository.save(study);
        return HeartClickResponse.of(member, study, existingHeart == null);
    }

    public List<Category> findMostCommonCategoryForMember(Member member) {
        // 특정 멤버가 누른 Heart 엔티티들 조회
        List<Heart> hearts = heartRepository.findByMember(member);

        // Heart 엔티티들에 연결된 Study 엔티티들의 Category 정보 수집
        List<Category> categories = hearts.stream()
                .map(Heart::getStudy)
                .map(Study::getCategory)
                .collect(Collectors.toList());

        // 가장 많이 공통되는 Category 찾기
        Map<Category, Long> categoryCountMap = categories.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Map.Entry<Category, Long>> maxEntries = categoryCountMap.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue))
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .orElse(Collections.emptyList());

        List<Category> mostCommonCategories = maxEntries.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return mostCommonCategories;
    }

    public int countHeartStudy(Member member) {
        // 특정 멤버가 누른 Heart 엔티티들 조회
        List<Heart> hearts = heartRepository.findByMember(member);

        return hearts.size();
    }
}
