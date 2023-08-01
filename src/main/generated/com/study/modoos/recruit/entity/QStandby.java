package com.study.modoos.recruit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStandby is a Querydsl query type for Standby
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStandby extends EntityPathBase<Standby> {

    private static final long serialVersionUID = -1259742435L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStandby standby = new QStandby("standby");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.study.modoos.member.entity.QMember member;

    public final com.study.modoos.study.entity.QStudy study;

    public QStandby(String variable) {
        this(Standby.class, forVariable(variable), INITS);
    }

    public QStandby(Path<? extends Standby> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStandby(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStandby(PathMetadata metadata, PathInits inits) {
        this(Standby.class, metadata, inits);
    }

    public QStandby(Class<? extends Standby> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.study.modoos.member.entity.QMember(forProperty("member")) : null;
        this.study = inits.isInitialized("study") ? new com.study.modoos.study.entity.QStudy(forProperty("study"), inits.get("study")) : null;
    }

}

