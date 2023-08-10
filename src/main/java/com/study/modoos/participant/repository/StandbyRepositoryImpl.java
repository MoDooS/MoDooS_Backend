package com.study.modoos.participant.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.modoos.member.entity.Member;
import com.study.modoos.participant.response.AllApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.study.modoos.study.entity.QStudy.study;
import static com.study.modoos.participant.entity.QStandby.standby;


@Repository
@RequiredArgsConstructor
public class StandbyRepositoryImpl {
    private final JPAQueryFactory queryFactory;

    public Slice<AllApplicationResponse> getSliceOfAllApplication(Member member, Pageable pageable) {
        JPAQuery<Tuple> results = queryFactory
                .select(standby, standby.study)
                .from(standby)
                .join(standby.study, study)
                .where(study.leader.id.eq(member.getId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(standby.getType(), standby.getMetadata());
            results.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC :
                    Order.DESC, pathBuilder.get(o.getProperty())));
        }

        List<AllApplicationResponse> contents = results.fetch()
                .stream()
                .map(tuple -> AllApplicationResponse.of(tuple.get(standby)))
                .collect(Collectors.toList());

        boolean hasNext = false;

        if (contents.size() > pageable.getPageSize()) {
            contents.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }


}