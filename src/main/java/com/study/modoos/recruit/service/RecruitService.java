package com.study.modoos.recruit.service;

import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.common.service.EntityFinder;
import com.study.modoos.heart.entity.Heart;
import com.study.modoos.heart.repository.HeartRepository;
import com.study.modoos.member.entity.Member;
import com.study.modoos.participant.entity.Participant;
import com.study.modoos.participant.repository.ParticipantRepository;
import com.study.modoos.recruit.request.ChangeRecruitRequest;
import com.study.modoos.recruit.request.RecruitRequest;
import com.study.modoos.recruit.request.TodoRequest;
import com.study.modoos.recruit.response.RecruitIdResponse;
import com.study.modoos.recruit.response.RecruitInfoResponse;
import com.study.modoos.recruit.response.RecruitListInfoResponse;
import com.study.modoos.recruit.response.TodoResponse;
import com.study.modoos.study.entity.Category;
import com.study.modoos.study.entity.Study;
import com.study.modoos.study.entity.StudyStatus;
import com.study.modoos.study.entity.Todo;
import com.study.modoos.study.repository.StudyRepository;
import com.study.modoos.study.repository.StudyRepositoryImpl;
import com.study.modoos.study.repository.TodoRepository;
import com.study.modoos.study.response.StudyParticipantResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitService {
    private final StudyRepository studyRepository;
    private final StudyRepositoryImpl studyRepositoryImpl;
    private final EntityFinder entityFinder;
    private final TodoRepository todoRepository;
    private final ParticipantRepository participantRepostiory;
    private final HeartRepository heartRepository;

    @Transactional
    public RecruitIdResponse postRecruit(Member currentMember, RecruitRequest request) {
        List<String> todos = request.getCheckList();

        Study study = request.createRecruit(currentMember);
        studyRepository.save(study);

        if (todos != null) {

            for (String todo : todos) {
                Todo t = Todo.builder().
                        study(study)
                        .content(todo)
                        .build();
                todoRepository.save(t);
            }
        }

        studyRepository.save(study);

        Participant participant = Participant.builder()
                .member(currentMember)
                .study(study)
                .build();

        participantRepostiory.save(participant);

        return RecruitIdResponse.of(study);
    }

    public RecruitInfoResponse oneRecruit(Member currentMember, Long id) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new ModoosException(ErrorCode.STUDY_NOT_FOUND));

        List<TodoResponse> checkList = todoRepository.findTodoByStudy(study)
                .stream().map(o -> TodoResponse.of(o))
                .collect(Collectors.toList());

        List<Participant> participantList = participantRepostiory.findByStudy(study);
        List<StudyParticipantResponse> participantResponseList = new ArrayList<>();

        for (Participant participant : participantList) {
            participantResponseList.add(StudyParticipantResponse.of(participant, null));
        }

        Optional<Heart> heart = heartRepository.findByMemberAndStudy(currentMember, study);
        boolean isHeart = false;

        if (heart.isPresent())
            isHeart = true;

        if (currentMember != null && study.getLeader().getId().equals(currentMember.getId())) {
            return RecruitInfoResponse.of(study, true, checkList, participantResponseList, isHeart);
        }
        return RecruitInfoResponse.of(study, false, checkList, participantResponseList, isHeart);
    }

    public Slice<RecruitListInfoResponse> getRecruitList(Member member, String search, List<String> categoryList, Long lastId, String sortBy, Pageable pageable) {

        List<Category> categories = new ArrayList<>();

        for (String s : categoryList) {
            categories.add(Category.resolve(s));
        }

        Study lastStudy;

        if (lastId == null) {
            lastStudy = null;
        } else {
            lastStudy = studyRepository.findById(lastId)
                    .orElse(null);
        }

        List<Heart> hearts = heartRepository.findByMember(member);
        return studyRepositoryImpl.getSliceOfRecruit(member, search, categories, lastId, lastStudy, sortBy, hearts, pageable);
    }

    @Transactional
    public RecruitIdResponse changeRecruit(Member currentMember, Long recruitId, ChangeRecruitRequest request) {
        Study study = entityFinder.findStudy(recruitId);

        if (!currentMember.getId().equals(study.getLeader().getId())) {
            throw new ModoosException(ErrorCode.FORBIDDEN_ARTICLE);
        }

        //스터디가 이미 생성된 모집공고면 삭제 불가능
        if (study.getStatus() == StudyStatus.ONGOING || study.getStatus() == StudyStatus.STUDY_END) {
            throw new ModoosException(ErrorCode.STUDY_NOT_EDIT);
        }

        //스터디 모집공고 내용 update
        study.update(request.getCampus(), request.getChannel(), request.getCategory(),
                request.getExpected_start_at(), request.getExpected_end_at(), request.getContact(),
                request.getLink(), request.getTitle(), request.getDescription(),
                request.getAbsent(), request.getLate(), request.getOut());

        //체크리스트 확인해서 update
        List<TodoRequest> checkList = request.getCheckList();

        List<Todo> todoList = todoRepository.findTodoByStudy(study);

        //스터디에 연결된 todoList 확인
        for (Todo todo : todoList) {
            boolean flag = false;

            for (TodoRequest todoRequest : checkList) {
                if (todoRequest.getId() != null && todo.getId().equals(todoRequest.getId())) {
                    todo.setContent(todoRequest.getContent());
                    flag = true;
                    todoRepository.save(todo);
                    break;
                }
            }

            //해당하는 값 없으면 삭제
            if (!flag) {
                todoRepository.delete(todo);
            }
        }

        for (TodoRequest todoRequest : checkList) {
            if (todoRequest.getId() == null) {
                Todo todo = Todo.builder()
                        .study(study)
                        .content(todoRequest.getContent())
                        .build();
                todoRepository.save(todo);
            }
        }

        studyRepository.save(study);
        return RecruitIdResponse.of(study);
    }

    @Transactional
    public void deleteRecruit(Member currentMember, Long recruitId) {
        Study study = entityFinder.findStudy(recruitId);

        //로그인한 유저와 글 작성자가 다르면 예외
        if (!currentMember.getId().equals(study.getLeader().getId())) {
            throw new ModoosException(ErrorCode.FORBIDDEN_ARTICLE);
        }

        //스터디가 이미 생성된 모집공고면 삭제 불가능
        if (study.getStatus() == StudyStatus.ONGOING) {
            throw new ModoosException(ErrorCode.STUDY_NOT_EDIT);
        }

        for (Todo todo : todoRepository.findTodoByStudy(study)) {
            todoRepository.delete(todo);
        }
        studyRepository.delete(study);
    }

    public Long findMaxRecruitIdx() {
        return studyRepositoryImpl.findMaxRecruitIdx().getId();
    }

    @Transactional
    public Slice<RecruitListInfoResponse> getMyStudyList(Member member, StudyStatus status, Pageable pageable) {
        List<Heart> hearts = heartRepository.findByMember(member);

        if (hearts == null) hearts = new ArrayList<>();

        List<Study> studies = hearts.stream().map(heart -> heart.getStudy())
                .collect(Collectors.toList());
        return studyRepositoryImpl.getMyStudyList(member, status, studies, pageable);
    }

    public Slice<RecruitListInfoResponse> getMyRecruit(Member member, Pageable pageable) {
        List<Heart> hearts = heartRepository.findByMember(member);
        return studyRepositoryImpl.getMyRecruitList(member, hearts, pageable);
    }
}
