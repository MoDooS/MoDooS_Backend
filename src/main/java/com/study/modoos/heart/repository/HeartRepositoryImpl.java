package com.study.modoos.heart.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.modoos.heart.response.HeartResponse;
import com.study.modoos.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.study.modoos.heart.entity.QHeart.heart;

@Repository
@RequiredArgsConstructor
public class HeartRepositoryImpl {
    private final JPAQueryFactory queryFactory;

    public Slice<HeartResponse> getSliceOfHeartStudies(Member member, Pageable pageable) {
        JPAQuery<Tuple> results = queryFactory
                .select(heart, heart.study) // Study 정보도 함께 선택
                .from(heart)
                .where(heart.member.eq(member))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(heart.getType(), heart.getMetadata());
            results.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC :
                    Order.DESC, pathBuilder.get(o.getProperty())));
        }

        List<HeartResponse> contents = results.fetch()
                .stream()
                .map(tuple -> HeartResponse.of(tuple.get(heart.study), true)) // Study 정보를 HeartResponse에 전달
                .collect(Collectors.toList());


        boolean hasNext = false;

        if (contents.size() > pageable.getPageSize()) {
            contents.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }
}
