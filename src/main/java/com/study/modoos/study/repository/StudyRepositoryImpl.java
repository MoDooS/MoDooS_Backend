package com.study.modoos.study.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.heart.entity.Heart;
import com.study.modoos.member.entity.Member;
import com.study.modoos.participant.entity.Participant;
import com.study.modoos.recruit.response.RecruitListInfoResponse;
import com.study.modoos.study.entity.Category;
import com.study.modoos.study.entity.Study;
import com.study.modoos.study.entity.StudyStatus;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.study.modoos.participant.entity.QParticipant.participant;
import static com.study.modoos.study.entity.QStudy.study;

@Repository
@RequiredArgsConstructor
public class StudyRepositoryImpl {
    private final JPAQueryFactory queryFactory;

    //no-offset 방식 처리
    private BooleanExpression ltStudyId(@Nullable Long studyId, Study lastStudy, String sortBy) {
        BooleanExpression condition = null;

        if (studyId == null) {
            return null;
        }

        if (sortBy.equals("createdAt")) {
            condition = study.createdAt.lt(lastStudy.getCreatedAt())
                    .or(study.createdAt.eq(lastStudy.getCreatedAt())
                            .and(study.id.gt(lastStudy.getId()))
                    );
        } else if (sortBy.equals("recruit_deadline")) {
            condition = study.recruit_deadline.gt(lastStudy.getRecruit_deadline())
                    .or(study.recruit_deadline.eq(lastStudy.getRecruit_deadline())
                            .and(study.id.gt(lastStudy.getId())));
        } else if (sortBy.equals("heart")) {
            condition = study.heart.lt(lastStudy.getHeart())
                    .or(study.heart.eq(lastStudy.getHeart())
                            .and(study.id.gt(lastStudy.getId()))
                    );
        }
        return condition;
    }

    public Slice<RecruitListInfoResponse> getSliceOfRecruit(Member member,
                                                            final String title,
                                                            final List<Category> categoryList,
                                                            final Long lastId,
                                                            Study lastStudy,
                                                            String sortBy,
                                                            List<Heart> hearts,
                                                            Pageable pageable) {
        /*
        if (order.equals("likeCount")) {
            content = queryFactory
                    .selectFrom(study)
                    .where(allCond(searchCondition))
                    .orderBy(study.createdAt)
                    .fetch();

        }
        */

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (sortBy.equals("createdAt")) {
            orderSpecifiers.add(study.createdAt.desc());
        } else if (sortBy.equals("recruit_deadline")) {
            orderSpecifiers.add(study.recruit_deadline.asc());
            orderSpecifiers.add(study.id.asc());
        } else if (sortBy.equals("heart")) {
            orderSpecifiers.add(study.heart.desc());
            orderSpecifiers.add(study.id.asc());
        }

        JPAQuery<Study> results = queryFactory.selectFrom(study)
                .where(
                        titleLike(title),
                        categoryEq(categoryList),
                        ltStudyId(lastId, lastStudy, sortBy))
                //.orderBy(study.createdAt.desc())
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        if (hearts == null) {
            hearts = new ArrayList<>();
        }

        List<Study> studies = hearts.stream().map(heart -> heart.getStudy())
                .collect(Collectors.toList());

        List<RecruitListInfoResponse> contents = results.fetch()
                .stream()
                .map(o -> RecruitListInfoResponse.of(o, studies.contains(o)))
                .collect(Collectors.toList());


        boolean hasNext = false;


        if (contents.size() > pageable.getPageSize()) {
            contents.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }

    public Study findMaxRecruitIdx() {
        return queryFactory.selectFrom(study)
                .orderBy(study.id.desc())
                .fetchFirst();
    }

    //제목 검색어로 검색
    private BooleanExpression titleLike(final String title) {
        return StringUtils.hasText(title) ? study.title.contains(title) : null;
    }

    //카테고리로 필터링
    private BooleanBuilder categoryEq(final List<Category> categoryList) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (categoryList.isEmpty()) return booleanBuilder;

        for (Category category : categoryList) {
            booleanBuilder.or(study.category.eq(category));
        }

        return booleanBuilder;
    }

    public Slice<RecruitListInfoResponse> getMyStudyList(Member member, StudyStatus status, List<Study> studies, Pageable pageable) {
        JPAQuery<Participant> results = queryFactory.selectFrom(participant)
                .join(participant.study, study)
                .where(participant.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        if (status != null) {
            if (status.equals(StudyStatus.RECRUITING) || status.equals(StudyStatus.RECRUIT_END)) {
                results.where(
                        study.status.eq(StudyStatus.RECRUITING).or(study.status.eq(StudyStatus.RECRUIT_END))
                );
                // 0: 모집중 상태
            } else if (status.equals(StudyStatus.ONGOING)) {
                results.where(study.status.eq(StudyStatus.ONGOING)); // 진행 중 상태
            } else if (status.equals(StudyStatus.STUDY_END)) {
                results.where(study.status.eq(StudyStatus.STUDY_END)); // 2: 스터디 생성 완료 상태
            } else {
                throw new ModoosException(ErrorCode.STUDY_STATUS);
            }
        }
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(study.getType(), study.getMetadata());
            results.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC :
                    Order.DESC, pathBuilder.get(o.getProperty())));
        }


        List<RecruitListInfoResponse> contents = results.fetch()
                .stream()
                .map(o -> RecruitListInfoResponse.of(o.getStudy(), studies.contains(o.getStudy())))
                .collect(Collectors.toList());


        boolean hasNext = false;

        if (contents.size() > pageable.getPageSize()) {
            contents.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }
}
