package com.study.modoos.study.service;

import com.study.modoos.common.exception.ErrorCode;
import com.study.modoos.common.exception.ModoosException;
import com.study.modoos.feedback.entity.*;
import com.study.modoos.feedback.repository.FeedbackRepository;
import com.study.modoos.feedback.repository.FeedbackTodoRepository;
import com.study.modoos.member.entity.Member;
import com.study.modoos.member.repository.MemberRepository;
import com.study.modoos.participant.entity.Participant;
import com.study.modoos.participant.repository.ParticipantRepository;
import com.study.modoos.recruit.request.TodoRequest;
import com.study.modoos.recruit.response.RecruitIdResponse;
import com.study.modoos.recruit.response.TodoResponse;
import com.study.modoos.study.entity.*;
import com.study.modoos.study.repository.*;
import com.study.modoos.study.request.*;
import com.study.modoos.study.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class StudyService {
    private final ParticipantRepository participantRepository;
    private final StudyRepository studyRepository;

    private final TodoRepository todoRepository;

    private final MemberRepository memberRepository;

    private final FeedbackRepository feedbackRepository;

    private final FeedbackTodoRepository feedbackTodoRepository;

    private final PositiveKeywordRepository positiveKeywordRepository;

    private final NegativeKeywordRepository negativeKeywordRepository;

    private final StudyHistoryRepository studyHistoryRepository;

    private final StudyHistoryTodoRepository studyHistoryTodoRepository;


    //스터디 생성 전 스터디 설정 정보 넘겨받기
    public CreateStudyResponse beforeCreate(Member currentMember, Long id) {
        Study study = findStudy(id);

        //study에 매핑된 todo 리스트 모두 가져오기
        List<TodoResponse> checkList = findTodoOfStudy(study);

        //리더면 생성 가능, 리더가 아니면 불가능
        if (isLeaderOfStudy(currentMember, study)) {
            return CreateStudyResponse.of(study, checkList);
        } else {
            throw new ModoosException(ErrorCode.FORBIDDEN_ARTICLE);
        }
    }

    //스터디 생성하기
    @Transactional
    public RecruitIdResponse createStudy(Member currentMember, CreateStudyRequest request) {
        Study study = findStudy(request.getId());

        if (!isLeaderOfStudy(currentMember, study)) {
            throw new ModoosException(ErrorCode.FORBIDDEN_ARTICLE);
        }

        //적어도 다음날 시작해야 함
        if (request.getStart_at().equals(LocalDate.now()))
            throw new ModoosException(ErrorCode.UNABLE_TO_START_ON_THE_DAY);

        study.setStudy(request.getStart_at(), request.getEnd_at(), request.getStudyTime(),
                request.getEndTime(), request.getPeriod(), request.getTotal_turn(),
                request.getAbsent(), request.getLate(), request.getOut());
        study.updateStatus(StudyStatus.ONGOING);  //스터디 생성 완료 상태로 변경
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

    //리더만 출석 체크
    @Transactional
    public RecruitIdResponse checkAttendance(Member currentMember, Long id, CreateAllAttendanceRequest request) {
        //스터디 찾기
        Study study = findStudy(id);

        //스터디 리더인지 확인
        if (!isLeaderOfStudy(currentMember, study))
            throw new ModoosException(ErrorCode.FORBIDDEN_STUDY_ACCEPT);

        //현재 시간 확인하고 출석체크 기간인지 확인
        LocalDateTime current = LocalDateTime.now();
        log.info(current + "시간입니다.");
        boolean isEvaluation = isAttendanceCheckPeriod(current, study);

        //평가 기간이 아니면 예외처리
        if (!isEvaluation)
            throw new ModoosException(ErrorCode.NOT_EVALUATION_PERIOD);

        log.info("평가기간입니다");


        //리더가 맞고 출석 체크 시간이 맞으면 출석 체크 저장
        for (CreateOneAttendanceRequest oneAttendance : request.getAttendanceList()) {
            Member tempMember = memberRepository.findById(oneAttendance.getId())
                    .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));

            Participant participant = participantRepository.findByMemberAndStudy(tempMember, study)
                    .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_IN_STUDY));

            //참가자의 출석 현황 리스트에 출석 저장
            participant.getAttendanceList().add(oneAttendance.getAttendance());

            log.info("출석을 저장했습니다.");

            //지각이거나 결석이면 지각수와 결석수 update
            if (oneAttendance.getAttendance().equals(Attendance.LATE))
                participant.updateLate(participant.getLate() + 1);  //지각 수 업데이트
            else if (oneAttendance.getAttendance().equals(Attendance.ABSENT))
                participant.updateAbsent(participant.getAbsent() + 1);  //결석 수 업데이트
            else if (oneAttendance.getAttendance().equals(Attendance.ATTEND)) {
                participant.getMember().updateScore(participant.getMember().getScore() + 5);    //출석이면 해당 멤버 점수 +5점
            }

            //지각부터 처리
            while (participant.getLate() >= study.getLate()) {
                participant.updateLate(participant.getLate() - study.getLate());
                participant.updateAbsent(participant.getAbsent() + 1);
            }

            //결석 처리
            while (participant.getAbsent() >= study.getAbsent()) {
                participant.updateAbsent(participant.getAbsent() - study.getAbsent());
                participant.updateOut(participant.getOut() + 1);
                //아웃카운트 하나 늘 때마다 -30점
                participant.getMember().updateScore(participant.getMember().getScore() - 30);
                participant.getMember().updateRanking();    //점수 변경됐으니 랭킹도 변경
            }

            memberRepository.save(participant.getMember());
            participantRepository.save(participant);

            //참가자의 outcount가 넘으면 퇴출
            if (participant.getOut() >= study.getOut()) {
                participant.getMember().updateScore(participant.getMember().getScore() - 200);  //퇴출자 점수 깎기
                memberRepository.save(participant.getMember());
                participantRepository.delete(participant);  //스터디에서 퇴출
                study.setParticipants_count(study.getParticipants_count() - 1); //스터디의 현재 참가자 수 변경
                studyRepository.save(study);
            }
        }

        return RecruitIdResponse.of(study);
    }


    //스터디 피드백 전에 피드백 해야 할 출석,지각한 참여자 정보, 체크리스트 정보 넘겨주기
    public BeforeFeedbackResponse beforeFeedback(Member currentMember, Long id, int turn) {
        //스터디 찾기
        Study study = findStudy(id);

        if (currentMember == null) {
            throw new ModoosException(ErrorCode.MEMBER_NOT_FOUND);
        }

        //평가 기간인지 확인
        LocalDateTime current = LocalDateTime.now();
        boolean isEvaluation = isEvalutationPeriod(current, study);

        //평가 기간 아니면 예외 처리
        if (!isEvaluation)
            throw new ModoosException(ErrorCode.NOT_EVALUATION_PERIOD);

        //스터디 참여자 리스트
        List<Participant> participantList = participantRepository.findByStudy(study);
        boolean isParticipant = false;


        //결석 인원 지우기
        for (int i = 0; i < participantList.size(); i++) {
            Participant participant = participantList.get(i);

            //결석인원이면 참여자 리스트에서 지우기
            if (participant.getAttendanceList().get(study.getCurrent_turn() - 1).equals(Attendance.ABSENT)) {
                participantList.remove(participant);
                i--;

                //결석인원이 현재 자기자신이면 에러 메시지
                if (participant.getMember().getId() == currentMember.getId())
                    throw new ModoosException(ErrorCode.MEMBER_IS_ABSENT);

            } else if (participant.getMember().getId() == currentMember.getId()) { //참여자이면
                isParticipant = true;
                participantList.remove(participant);    //참여자면 자기 자신도 지우기
                i--;
            }
        }

        //아예 해당 스터디에 참여자가 아닐경우 에러 메시지
        if (!isParticipant)
            throw new ModoosException(ErrorCode.MEMBER_NOT_IN_STUDY);

        //현재 로그인한 사용자로 참여자 정보 찾기
        Participant thisParticipant = participantRepository.findByMemberAndStudy(currentMember, study)
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_IN_STUDY));

        //현재 참여자가 이번 주차에 스터디에 피드백 했는지 확인
        List<Feedback> feedbackOptional = feedbackRepository.findBySenderAndStudyAndTimes(thisParticipant, study, turn);
        log.info(String.valueOf(feedbackOptional.size()));

        //만약 이미 이번 주차 피드백을 한 상태라면 예외 처리
        if (feedbackOptional.size() > 0) {
            throw new ModoosException(ErrorCode.ALREADY_FEEDBACK);
        }

        //참여자의 정보 확인해서 넣기
        List<StudyParticipantResponse> participantResponseList = new ArrayList<>();

        //지난 주차의 출석 정보까지만 넣기
        for (Participant participant : participantList) {
            ArrayList<Attendance> attendanceList = new ArrayList<>(participant.getAttendanceList());
            List<Attendance> subList = attendanceList.subList(0, study.getCurrent_turn());
            participantResponseList.add(StudyParticipantResponse.of(participant, subList));
        }

        //체크리스트 response 매핑
        List<TodoResponse> checkList = findTodoOfStudy(study);

        //리더면 리더라고 알려주기
        if (isLeaderOfStudy(currentMember, study))
            return BeforeFeedbackResponse.of(study, checkList, participantResponseList, true);
        return BeforeFeedbackResponse.of(study, checkList, participantResponseList, false);
    }

    //피드백 저장하기
    @Transactional
    public RecruitIdResponse createFeedback(Member currentMember, CreateAllFeedbackRequest request) {
        //스터디 있는지 확인
        Study study = findStudy(request.getId());

        if (currentMember == null)
            throw new ModoosException(ErrorCode.MEMBER_NOT_IN_STUDY);

        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));

        //현재 로그인한 사용자 정보로 스터디 참여자(지금 feedback 생성자) 정보 가져오기
        Participant sender = participantRepository.findByMemberAndStudy(member, study)
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_IN_STUDY));

        //평가 기간인지 확인
        LocalDateTime current = LocalDateTime.now();
        boolean isEvaluation = isEvalutationPeriod(current, study);

        //평가 기간이 아니면 예외 처리
        if (!isEvaluation) {
            throw new ModoosException(ErrorCode.NOT_EVALUATION_PERIOD);
        }

        //만약 생성자가 이번 주차 결석자면 평가 금지
        if (sender.getAttendanceList().get(request.getTurn() - 1).equals(Attendance.ABSENT))
            throw new ModoosException(ErrorCode.MEMBER_IS_ABSENT);


        //피드백 리스트에서 하나씩 확인
        List<CreateOneFeedbackRequest> feedbackRequestList = request.getFeedbackList();

        for (CreateOneFeedbackRequest feedbackRequest : feedbackRequestList) {
            Member tempMember = memberRepository.findById(feedbackRequest.getId())
                    .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));

            //피드백 receiver 확인
            Participant receiver = participantRepository.findByMemberAndStudy(tempMember, study)
                    .orElseThrow(() -> new ModoosException(ErrorCode.RECEIVER_NOT_IN_STUDY));

            //checkList 확인 -> 한 것 저장
            List<TodoIdResponse> todoList = feedbackRequest.getCheckList();

            Feedback feedback = Feedback.builder()
                    .study(study)
                    .receiver(receiver)
                    .sender(sender)
                    .times(request.getTurn())
                    .participate(feedbackRequest.getParticipate())
                    .positive(feedbackRequest.getPositive())
                    .negative(feedbackRequest.getNegative())
                    .build();

            for (TodoIdResponse todoResponse : todoList) {
                Todo todo = todoRepository.findById(todoResponse.getId())
                        .orElseThrow(() -> new ModoosException(ErrorCode.TODO_NOT_FOUND));
                FeedbackTodo feedbackTodo = FeedbackTodo.builder()
                        .todo(todo)
                        .feedback(feedback)
                        .build();
                feedbackTodoRepository.save(feedbackTodo);
            }
            feedbackRepository.save(feedback);

        }

        //피드백 작성 완료했으므로 점수 +5점
        member.updateScore(member.getScore() + 5);
        member.updateRanking();
        memberRepository.save(member);

        return RecruitIdResponse.of(study);
    }

    /* 스터디 관리 페이지 조회
     ** 만약 평가 기간이 끝났다면 -> 끝난 주차에 대한 값을 보여줘야 함
     ** 만약 평가 가긴 중이라면 -> 현재 평가 기간 이전 주차에 대한 값을 보여줘야 함
     */
    public StudyInfoResponse getStudyInfo(Member currentMember, Long id) {

        //스터디 id로 찾기
        Study study = findStudy(id);

        //로그인 한 사용자 검증
        if (currentMember == null)
            throw new ModoosException(ErrorCode.MEMBER_NOT_IN_STUDY);

        //회원인지 검증
        Member member = memberRepository.findById(currentMember.getId())
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_FOUND));

        //스터디 참여 인원인지 검증
        Participant thisParticipant = participantRepository.findByMemberAndStudy(currentMember, study)
                .orElseThrow(() -> new ModoosException(ErrorCode.RECEIVER_NOT_IN_STUDY));

        boolean isLeader = study.getLeader().equals(member);

        //현재까지의 스터디
        LocalDateTime current = LocalDateTime.now();

        //빈 출석체크 있으면 미리 채우기
        isEvalutationPeriod(current, study);


        LocalDateTime startTime = study.getStart_at().atTime(study.getEndTime()).plusDays(1);
        int idx = 0;

        //시작 전이면 idx = 0, 평가기간 주차와 idx는 같아짐
        for (LocalDateTime time = startTime; time.isEqual(study.getEnd_at().atTime(study.getEndTime()).plusDays(1)) || time.isBefore(study.getEnd_at().atTime(study.getEndTime()).plusDays(1));
             time = time.plusDays(study.getPeriod() * 7)) {

            LocalDateTime end = time.plusDays(study.getPeriod() * 7);
            idx++;

            if ((current.isEqual(time) || current.isAfter(time)) && current.isBefore(end)) {
                break;
            } else if (current.isBefore(time)) {
                idx--;
                break;
            }
        }

        log.info(String.valueOf(idx));

        //전체 todo 리스트(현재 로그인 사용자의 이행 여부와는 상관없음)
        List<Todo> todoList = todoRepository.findTodoByStudy(study);
        List<TodoResponse> todoResponseList = new ArrayList<>();

        for (Todo todo : todoList) {
            todoResponseList.add(TodoResponse.of(todo));
        }

        //전체 참여자 리스트
        List<Participant> participants = participantRepository.findByStudy(study);

        log.debug(String.valueOf(participants.size()));

        //전체 회원의 출석상태, 아웃, 정보 가져오기
        List<StudyParticipantResponse> studyParticipantResponseList = new ArrayList<>();

        for (Participant participant : participants) {
            ArrayList<Attendance> attendanceList = new ArrayList<>(participant.getAttendanceList());
            log.info(String.valueOf(attendanceList.size()));

            studyParticipantResponseList.add(StudyParticipantResponse.of(participant, attendanceList.subList(0, idx)));
        }

        //아직 시작하지 않은 스터디라면 피드백을 빈 값으로 보내주기
        if (idx == 0) {
            return StudyInfoResponse.of(study, isLeader, member, todoResponseList, null, studyParticipantResponseList);
        }


        //결석회원이면 받은 피드백 없으므로 피드백 안 보여줌
        if (thisParticipant.getAttendanceList().get(idx - 1).equals(Attendance.ABSENT)) {
            return StudyInfoResponse.of(study, isLeader, member, todoResponseList, null, studyParticipantResponseList);
        }

        //이번 주차 받은 스터디 피드백
        List<Feedback> feedbacks = feedbackRepository.findByReceiverAndStudyAndTimes(thisParticipant, study, idx);

        //받은 피드백이 없으면
        if (feedbacks.size() == 0) {
            //계산해야 함 -> 이번 주차 피드백
            reflectFeedbacksScore(study, thisParticipant, feedbacks);

            //null 값으로 저장
            studyHistoryRepository.save(StudyHistory.builder()
                    .study(study)
                    .current_turn(idx)
                    .participant(thisParticipant)
                    .participate(0)
                    .positiveKeywords(null)
                    .negativeKeywords(null)
                    .build());
            return StudyInfoResponse.of(study, isLeader, member, todoResponseList, null, studyParticipantResponseList);
        }

        List<StudyHistory> studyHistories = studyHistoryRepository.findStudyHistoryByStudyAndParticipant(study, thisParticipant);
        Collections.sort(studyHistories, (Comparator.comparingInt(StudyHistory::getCurrentTurn)));

        //비어있는 계산 결과가 있으므로
        int tempIdx = studyHistories.size();

        while (tempIdx < idx - 1) {
            //다 null 값으로 저장
            StudyHistory studyHistory = StudyHistory.builder()
                    .study(study)
                    .current_turn(tempIdx++)
                    .participant(thisParticipant)
                    .participate(0)
                    .positiveKeywords(null)
                    .negativeKeywords(null)
                    .build();

            studyHistoryRepository.save(studyHistory);
        }

        //만약 이번 주차 피드백 계산한 결과가 있다면 -> 계산 x, 이미 있는 결과로 response
        Optional<StudyHistory> optionalStudyHistory = studyHistoryRepository.findStudyHistoryByStudyAndParticipantAndCurrentTurn(study, thisParticipant, idx);
        StudyHistory studyHistory;

        if (!optionalStudyHistory.isPresent()) {  //계산 결과 없다면 계산
            //계산한 결과 없다면 여기에 cal -> 이 때 점수도 계산해줘야 함
            studyHistory = calFeedbacks(feedbacks, thisParticipant, study, idx, todoList);
            reflectFeedbacksScore(study, thisParticipant, feedbacks);
        } else {
            studyHistory = optionalStudyHistory.get();
        }

        List<StudyHistoryTodo> studyHistoryTodos = studyHistoryTodoRepository.findStudyHistoryTodoByStudyHistory(studyHistory);
        List<Todo> todos = studyHistoryTodos.stream().map(studyHistoryTodo -> studyHistoryTodo.getTodo())
                .collect(Collectors.toList());

        List<PositiveKeyword> positiveKeywords = studyHistory.getPositiveKeywords();
        List<NegativeKeyword> negativeKeywords = studyHistory.getNegativeKeywords();

        List<CheckTodoResponse> checkTodoResponses = new ArrayList<>();

        for (Todo todo : todoList) {
            if (todos.contains(todo)) {
                checkTodoResponses.add(CheckTodoResponse.of(todo, true));
            } else {
                checkTodoResponses.add(CheckTodoResponse.of(todo, false));
            }
        }

        List<PositiveKeywordResponse> positiveKeywordResponses = positiveKeywords == null ? null : positiveKeywords.stream().map(PositiveKeywordResponse::of).collect(Collectors.toList());
        List<NegativeKeywordResponse> negativeKeywordResponses = negativeKeywords == null ? null : negativeKeywords.stream().map(NegativeKeywordResponse::of).collect(Collectors.toList());

        FeedbackResponse feedbackResponse = FeedbackResponse.builder()
                .times(idx)
                .checkList(checkTodoResponses)
                .positiveList(positiveKeywordResponses)
                .negativeList(negativeKeywordResponses)
                .build();

        return StudyInfoResponse.of(study, isLeader, member, todoResponseList, feedbackResponse, studyParticipantResponseList);

    }

    public StudyHistory calFeedbacks(List<Feedback> feedbacks, Participant thisParticipant, Study study, int idx, List<Todo> todoList) {
        //이번 주차에 받은 피드백 리스트
        int len = feedbacks.size() / 2;

        HashMap<Long, Integer> countMap = new HashMap<>();
        HashMap<Positive, Integer> positiveMap = new HashMap<>();
        HashMap<Negative, Integer> negativeMap = new HashMap<>();
        int participateScore = 0;

        //미리 모든 규칙 넣어놓기
        for (Todo todo : todoList) {
            countMap.put(todo.getId(), 0);
        }

        if (feedbacks != null) {

            for (Feedback feedback : feedbacks) {
                List<FeedbackTodo> feedbackTodoList = feedbackTodoRepository.findFeedbackTodoByFeedback(feedback);

                //참여자가 한 피드백 count +1
                for (FeedbackTodo feedbackTodo : feedbackTodoList) {
                    countMap.put(feedbackTodo.getTodo().getId(), countMap.get(feedbackTodo.getTodo().getId()) + 1);
                }

                //positive도 하나씩 count
                if (feedback.getPositive() != null) {
                    if (positiveMap.containsKey(feedback.getPositive())) {
                        positiveMap.put(feedback.getPositive(), positiveMap.get(feedback.getPositive()) + 1);
                    } else {
                        positiveMap.put(feedback.getPositive(), 1);
                    }
                }

                //negative도 하나씩 count
                if (feedback.getNegative() != null) {
                    if (negativeMap.containsKey(feedback.getNegative())) {
                        negativeMap.put(feedback.getNegative(), negativeMap.get(feedback.getNegative()) + 1);
                    } else {
                        negativeMap.put(feedback.getNegative(), 1);
                    }
                }

                participateScore += feedback.getParticipate();
            }
        }

        boolean allTodo = false;

        List<Todo> todos = new ArrayList<>();

        //각 todo에 대하여 이행했다고 답한 참여자가 더 많다면 한 것으로 판정
        for (Todo todo : todoList) {
            if (countMap.get(todo.getId()) > len) {
                todos.add(todo);
            } else {
                allTodo = true; //하나라도 안 했으면
            }
        }

        //안 한 todo가 하나라도 있으면 1out
        if (allTodo) {
            thisParticipant.updateOut(thisParticipant.getOut() + 1);
            participantRepository.save(thisParticipant);
        }

        //아웃카운트 하나 늘 때마다 -30점
        thisParticipant.getMember().updateScore(thisParticipant.getMember().getScore() - 30);
        thisParticipant.getMember().updateRanking();    //점수 변경됐으니 랭킹도 변경

        //참가자의 outcount가 넘으면 퇴출
        if (thisParticipant.getOut() >= study.getOut()) {
            thisParticipant.getMember().updateScore(thisParticipant.getMember().getScore() - 200);  //퇴출자 점수 깎기
            memberRepository.save(thisParticipant.getMember());
            participantRepository.delete(thisParticipant);  //스터디에서 퇴출
            study.setParticipants_count(study.getParticipants_count() - 1); //스터디의 현재 참가자 수 변경
            studyRepository.save(study);
        }

        //positive, negative 계산
        Set<Positive> positiveSet = positiveMap.keySet();
        Set<Negative> negativeSet = negativeMap.keySet();

        List<PositiveKeyword> positiveKeywords = new ArrayList<>();
        List<NegativeKeyword> negativeKeywords = new ArrayList<>();

        StudyHistory studyHistory = StudyHistory.builder()
                .study(study)
                .current_turn(idx)
                .participant(thisParticipant)
                .participate(participateScore / feedbacks.size())
                .build();
        studyHistoryRepository.save(studyHistory);

        for (Positive positive : positiveSet) {
            PositiveKeyword keyword = PositiveKeyword.builder()
                    .positive(positive)
                    .count(positiveMap.get(positive))
                    .studyHistory(studyHistory)
                    .build();
            positiveKeywordRepository.save(keyword);
            positiveKeywords.add(keyword);
        }

        for (Negative negative : negativeSet) {
            NegativeKeyword keyword = NegativeKeyword.builder()
                    .negative(negative)
                    .count(negativeMap.get(negative))
                    .studyHistory(studyHistory)
                    .build();
            negativeKeywordRepository.save(keyword);
            negativeKeywords.add(keyword);
        }

        Collections.sort(positiveKeywords, (o1, o2) -> {
            return Long.compare(o2.getCount(), o1.getCount());
        });

        Collections.sort(negativeKeywords, (o1, o2) -> {
            return Long.compare(o2.getCount(), o1.getCount());
        });


        studyHistoryRepository.save(studyHistory);

        //participate 점수 계산
        if (participateScore / feedbacks.size() >= 5) {
            participateScore = 20;
        } else if (participateScore / feedbacks.size() >= 3) {
            participateScore = 0;
        } else {
            participateScore = -20;
        }

        thisParticipant.getMember().updateScore(thisParticipant.getMember().getScore() + participateScore);
        thisParticipant.getMember().updateRanking();
        memberRepository.save(thisParticipant.getMember());

        //studyHistory와 연관 todo 저장
        for (Todo todo : todos) {
            StudyHistoryTodo studyHistoryTodo = StudyHistoryTodo.builder()
                    .todo(todo)
                    .studyHistory(studyHistory)
                    .build();
            studyHistoryTodoRepository.save(studyHistoryTodo);
        }

        //완주인지 확인
        checkStudyIsEnd(idx, study);
        studyHistoryRepository.save(studyHistory);
        return studyHistory;
    }

    public void fillEmptyAttendance(Study study, int idx, boolean isEvaluation) {
        //지금 현재 turn으로 update 시켜주고 빈 곳이 있으면 채워주기
        Member leader = study.getLeader();

        Participant self = participantRepository.findByMemberAndStudy(leader, study)
                .orElseThrow(() -> new ModoosException(ErrorCode.MEMBER_NOT_IN_STUDY));

        List<Participant> participantList = participantRepository.findByStudy(study);

        //출결체크 못한 회차들 확인
        if (self.getAttendanceList().size() + 1 < idx) {
            int start = self.getAttendanceList().size();

            for (Participant participant : participantList) {
                for (int i = start + 1; i < idx; i++) {
                    participant.getAttendanceList().add(Attendance.ATTEND); //출석으로 인정해줌
                    participant.getMember().updateScore(participant.getMember().getScore() + 5);  //출석인정되었으므로 +5점
                }

                if (!isEvaluation) { //평가 기간이 아니면 더해줘야 함
                    participant.getAttendanceList().add(Attendance.ATTEND);
                    participant.getMember().updateScore(participant.getMember().getScore() + 5);
                }
                participantRepository.save(participant);
            }
        }

        //current_turn을 update
        study.updateCurrentTurn(idx);
        studyRepository.save(study);
    }

    public Study findStudy(Long id) {
        return studyRepository.findById(id)
                .orElseThrow(() -> new ModoosException(ErrorCode.STUDY_NOT_FOUND));
    }

    public boolean isLeaderOfStudy(Member currentMember, Study study) {
        return currentMember != null && study.getLeader().getId() == currentMember.getId();
    }

    public List<TodoResponse> findTodoOfStudy(Study study) {
        return todoRepository.findTodoByStudy(study)
                .stream().map(o -> TodoResponse.of(o))
                .collect(Collectors.toList());
    }

    public boolean isEvalutationPeriod(LocalDateTime current, Study study) {
        //평가 기간인지 확인
        LocalDateTime startTime = study.getStart_at().atTime(study.getEndTime());
        boolean isEvaluation = false;
        int idx = 0;

        for (LocalDateTime time = startTime; time.isEqual(study.getEnd_at().atTime(study.getEndTime())) || time.isBefore(study.getEnd_at().atTime(study.getEndTime()));
             time = time.plusDays(study.getPeriod() * 7)) {
            LocalDateTime end = time.plusDays(1);
            idx++;

            if ((current.isEqual(time) || current.isAfter(time)) && current.isBefore(end)) {
                isEvaluation = true;
                break;
            } else if (current.isBefore(time)) {
                idx--;
                break;
            }
        }

        fillEmptyAttendance(study, idx, isEvaluation);
        return isEvaluation;
    }

    public boolean isAttendanceCheckPeriod(LocalDateTime current, Study study) {
        LocalDateTime startTime = study.getStart_at().atTime(study.getStudyTime());
        boolean isEvaluation = false;
        int idx = 0;


        //평가 기간인지 확인
        for (LocalDateTime time = startTime; time.isEqual(study.getEnd_at().atTime(study.getStudyTime())) || time.isBefore(study.getEnd_at().atTime(study.getStudyTime()));
             time = time.plusDays(study.getPeriod() * 7)) {

            LocalDateTime end = time.withHour(study.getEndTime().getHour())
                    .withMinute(study.getEndTime().getMinute())
                    .withSecond(study.getEndTime().getSecond());

            log.info(time + "부터 " + end + "안인지 확인");

            idx++;

            if ((current.isEqual(time) || current.isAfter(time)) && current.isBefore(end)) {
                isEvaluation = true;
                break;
            } else if (current.isBefore(time)) {
                idx--;
                break;
            }
        }

        fillEmptyAttendance(study, idx, isEvaluation);
        return isEvaluation;
    }

    public void reflectFeedbacksScore(Study study, Participant thisParticipant, List<Feedback> feedbacks) {
        //그 주차 피드백 안 받았으면 안 준 사람들 다 점수 깎기
        List<Participant> participantList = participantRepository.findByStudy(study);

        if (feedbacks.size() == 0) {    //이번 주차 피드백 해 준 사람 없을 때
            for (Participant participant : participantList) {
                if (participant.equals(thisParticipant))
                    continue;
                Member member = participant.getMember();
                member.updateScore(member.getScore() - 20);
                member.updateRanking();
                memberRepository.save(member);
            }
        }

        //그 주차만 계산하면 됨
        for (Participant participant : participantList) {
            boolean flag = false;

            for (Feedback feedback : feedbacks) {
                if (participant.equals(feedback.getSender())) {
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                Member member = participant.getMember();
                member.updateScore(member.getScore() - 20);
                member.updateRanking();
                memberRepository.save(member);
            }
        }
    }

    public void checkStudyIsEnd(int idx, Study study) {
        if (idx == study.getTotal_turn()) { //마지막 주차이면 완주 반영했는지 확인
            if (!study.isEnd()) { //완주 반영 안 됐으면
                study.updateIsEnd();    //완주 반영
                study.updateStatus(StudyStatus.STUDY_END);

                List<Participant> participantList = participantRepository.findByStudy(study);

                //완주한 참가자들한테 +200점
                for (Participant participant : participantList) {
                    Member tempMember = participant.getMember();

                    tempMember.updateScore(tempMember.getScore() + 200);
                    tempMember.updateRanking();
                    memberRepository.save(tempMember);
                }
                studyRepository.save(study);
            }
        }
    }


}
