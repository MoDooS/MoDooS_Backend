package com.study.modoos.feedback.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFeedback is a Querydsl query type for Feedback
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeedback extends EntityPathBase<Feedback> {

    private static final long serialVersionUID = -1868734190L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFeedback feedback = new QFeedback("feedback");

    public final com.study.modoos.common.entity.QBaseTimeEntity _super = new com.study.modoos.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Integer> attend = createNumber("attend", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> deligence = createNumber("deligence", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<Negative> negative = createEnum("negative", Negative.class);

    public final NumberPath<Integer> participate = createNumber("participate", Integer.class);

    public final EnumPath<Positive> positive = createEnum("positive", Positive.class);

    public final com.study.modoos.study.entity.QParticipant receiver;

    public final com.study.modoos.study.entity.QParticipant sender;

    public final com.study.modoos.study.entity.QStudy study;

    public final NumberPath<Integer> times = createNumber("times", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFeedback(String variable) {
        this(Feedback.class, forVariable(variable), INITS);
    }

    public QFeedback(Path<? extends Feedback> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFeedback(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFeedback(PathMetadata metadata, PathInits inits) {
        this(Feedback.class, metadata, inits);
    }

    public QFeedback(Class<? extends Feedback> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.receiver = inits.isInitialized("receiver") ? new com.study.modoos.study.entity.QParticipant(forProperty("receiver"), inits.get("receiver")) : null;
        this.sender = inits.isInitialized("sender") ? new com.study.modoos.study.entity.QParticipant(forProperty("sender"), inits.get("sender")) : null;
        this.study = inits.isInitialized("study") ? new com.study.modoos.study.entity.QStudy(forProperty("study"), inits.get("study")) : null;
    }

}

