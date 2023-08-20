package com.study.modoos.ranking.service;

import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.member.entity.Member;
import com.study.modoos.member.repository.MemberRepository;
import com.study.modoos.ranking.response.RankingResponse;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankingService {
    private final MemberRepository memberRepository;

    public RankingService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Slice<RankingResponse> getRanking(Member currentUser, Pageable pageable) {
        Member member = memberRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "score"));

        Slice<Member> rankedMembersSlice = memberRepository.findAll(sortedPageable);

        List<RankingResponse> rankingList = rankedMembersSlice.stream()
                .map(RankingResponse::of)
                .collect(Collectors.toList());


        int currentRank = rankedMembersSlice.getNumber() * rankedMembersSlice.getSize() + 1;
        long currentScore = rankingList.isEmpty() ? -1 : rankingList.get(0).getScore();
        int currentRankCnt = 0;

        for (RankingResponse response : rankingList) {
            if (response.getScore() < currentScore) {
                if (currentRankCnt != 0){
                    currentRank+=currentRankCnt;
                } else {
                    currentRank++;
                }
                currentScore = response.getScore();
            } else if (response.getScore() == currentScore) {
                currentRankCnt ++;
            } else {
                currentRankCnt = 0;
            }
            response.setRankingNumber((long) currentRank);
        }

        return new SliceImpl<>(rankingList, sortedPageable, rankedMembersSlice.hasNext());
    }
    }

