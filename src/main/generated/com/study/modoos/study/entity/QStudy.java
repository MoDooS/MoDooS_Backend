package com.study.modoos.study.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStudy is a Querydsl query type for Study
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudy extends EntityPathBase<Study> {

    private static final long serialVersionUID = 1338586190L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStudy study = new QStudy("study");

    public final com.study.modoos.common.entity.QBaseTimeEntity _super = new com.study.modoos.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Integer> absent = createNumber("absent", Integer.class);

    public final EnumPath<com.study.modoos.member.entity.Campus> campus = createEnum("campus", com.study.modoos.member.entity.Campus.class);

    public final EnumPath<Category> category = createEnum("category", Category.class);

    public final EnumPath<Channel> channel = createEnum("channel", Channel.class);

    public final StringPath contact = createString("contact");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final DatePath<java.time.LocalDate> end_at = createDate("end_at", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> expected_end_at = createDate("expected_end_at", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> expected_start_at = createDate("expected_start_at", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> late = createNumber("late", Integer.class);

    public final com.study.modoos.member.entity.QMember leader;

    public final StringPath link = createString("link");

    public final NumberPath<Integer> out = createNumber("out", Integer.class);

    public final NumberPath<Integer> participants_count = createNumber("participants_count", Integer.class);

    public final NumberPath<Integer> period = createNumber("period", Integer.class);

    public final DatePath<java.time.LocalDate> recruit_deadline = createDate("recruit_deadline", java.time.LocalDate.class);

    public final NumberPath<Integer> recruits_count = createNumber("recruits_count", Integer.class);

    public final StringPath rule_content = createString("rule_content");

    public final DatePath<java.time.LocalDate> start_at = createDate("start_at", java.time.LocalDate.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> total_turn = createNumber("total_turn", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QStudy(String variable) {
        this(Study.class, forVariable(variable), INITS);
    }

    public QStudy(Path<? extends Study> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStudy(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStudy(PathMetadata metadata, PathInits inits) {
        this(Study.class, metadata, inits);
    }

    public QStudy(Class<? extends Study> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.leader = inits.isInitialized("leader") ? new com.study.modoos.member.entity.QMember(forProperty("leader")) : null;
    }

}

