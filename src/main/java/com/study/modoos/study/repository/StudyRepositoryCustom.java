package com.study.modoos.study.repository;

import com.study.modoos.recruit.response.RecruitInfoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepositoryCustom {

    Slice<RecruitInfoResponse> getRecruitScroll(Pageable pageable);
}
