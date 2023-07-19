package com.study.modoos.common.service;

import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.member.entity.Member;
import com.study.modoos.study.entity.Study;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntityFinder {
    private final EntityManager entityManager;

    private <T> T find(Long id, Class<T> tClass, ErrorCode errorCode) {
        T result = entityManager.find(tClass, id);
        if (result == null) throw new ModoosException(errorCode);
        return result;
    }

    public Member findMember(Long memberId) {
        return find(memberId, Member.class, ErrorCode.MEMBER_NOT_FOUND);
    }

    public Study findStudy(Long studyId) {
        return find(studyId, Study.class, ErrorCode.STUDY_NOT_FOUND);
    }

}
