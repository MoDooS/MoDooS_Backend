package com.study.modoos.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -256387972L;

    public static final QMember member = new QMember("member1");

    public final EnumPath<Campus> campus = createEnum("campus", Campus.class);

    public final EnumPath<Department> department = createEnum("department", Department.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isMember = createBoolean("isMember");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath ranking = createString("ranking");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final NumberPath<Long> score = createNumber("score", Long.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

