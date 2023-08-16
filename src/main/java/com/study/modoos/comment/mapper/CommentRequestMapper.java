package com.study.modoos.comment.mapper;

import com.study.modoos.comment.entity.Comment;
import com.study.modoos.comment.request.CommentRequest;
import com.study.modoos.common.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentRequestMapper extends GenericMapper<CommentRequest, Comment> {
    CommentRequestMapper INSTANCE = Mappers.getMapper(CommentRequestMapper.class);
}
