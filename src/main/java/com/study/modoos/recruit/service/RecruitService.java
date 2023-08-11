package com.study.modoos.recruit.service;

import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.common.service.EntityFinder;
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
import com.study.modoos.study.entity.Todo;
import com.study.modoos.study.repository.StudyRepository;
import com.study.modoos.study.repository.StudyRepositoryImpl;
import com.study.modoos.study.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    @Transactional
    public RecruitIdResponse postRecruit(Member currentMember, RecruitRequest request) {
        List<String> todos = request.getCheckList();

        Study study = request.createRecruit(currentMember);
        studyRepository.save(study);

        for (String todo : todos) {
            Todo t = Todo.builder().
                    study(study)
                    .content(todo)
                    .build();
            todoRepository.save(t);
        }

        studyRepository.save(study);
      
        Participant participant = new Participant(currentMember, study);
        participantRepostiory.save(participant);

        return RecruitIdResponse.of(study);
    }

    public RecruitInfoResponse oneRecruit(Member currentMember, Long id) {
        Study study = studyRepository.findById(id)
                .orElseThrow(() -> new ModoosException(ErrorCode.STUDY_NOT_FOUND));

        List<TodoResponse> checkList = todoRepository.findTodoByStudy(study)
                .stream().map(o -> TodoResponse.of(o))
                .collect(Collectors.toList());

        if (study.getLeader().getId().equals(currentMember.getId())) {
            return RecruitInfoResponse.of(study, true, checkList);
        }
        return RecruitInfoResponse.of(study, false, checkList);
    }

    public Slice<RecruitListInfoResponse> getRecruitList(Member member, String search, List<String> categoryList, Pageable pageable) {

        List<Category> categories = new ArrayList<>();

        for (String s : categoryList) {
            categories.add(Category.resolve(s));
        }
        return studyRepositoryImpl.getSliceOfRecruit(member, search, categories, pageable);
    }

    @Transactional
    public RecruitIdResponse changeRecruit(Member currentMember, Long recruitId, ChangeRecruitRequest request) {
        Study study = entityFinder.findStudy(recruitId);

        if (!currentMember.getId().equals(study.getLeader().getId())) {
            throw new ModoosException(ErrorCode.FORBIDDEN_ARTICLE);
        }

        //스터디가 이미 생성된 모집공고면 삭제 불가능
        if (study.getStatus() == 2) {
            throw new ModoosException(ErrorCode.STUDY_NOT_EDIT);
        }

        //스터디 모집공고 내용 update
        study.update(request.getCampus(), request.getChannel(), request.getCategory(),
                request.getExpected_start_at(), request.getExpected_end_at(), request.getContact(),
                request.getRule_content(), request.getTitle(), request.getDescription(),
                request.getAbsent(), request.getLate(), request.getOut(), request.getRule_content());

        //체크리스트 확인해서 update
        List<TodoRequest> checkList = request.getCheckList();
        Todo todo;

        for (TodoRequest todoRequest : checkList) {
            if (todoRequest.getRequestType().equals("없음"))
                continue;

            if (todoRequest.getRequestType().equals("추가")) {
                todo = todoRequest.createTodo(study);
                todoRepository.save(todo);
            } else if (todoRequest.getRequestType().equals("수정")) {
                todo = todoRepository.findById(todoRequest.getId())
                        .orElseThrow(() -> new ModoosException(ErrorCode.TODO_NOT_FOUND));
                todo.setContent(todoRequest.getContent());
                todoRepository.save(todo);
            } else if (todoRequest.getRequestType().equals("삭제")) {
                todo = todoRepository.findById(todoRequest.getId())
                        .orElseThrow(() -> new ModoosException(ErrorCode.TODO_NOT_FOUND));
                todoRepository.delete(todo);
            } else {
                throw new ModoosException(ErrorCode.VALUE_NOT_IN_OPTION);
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
        if (study.getStatus() == 2) {
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
    public Slice<RecruitListInfoResponse> getMyStudyList(Member member, Pageable pageable) {
        return studyRepositoryImpl.getMyStudyList(member, pageable);
    }
}
