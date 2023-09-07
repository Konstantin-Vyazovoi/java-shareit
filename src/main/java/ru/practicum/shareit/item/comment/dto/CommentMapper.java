package ru.practicum.shareit.item.comment.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(CommentDto commentDto, Item item, User author) {
        return new Comment(
            null,
            commentDto.getText(),
            item, author,
            LocalDateTime.now());
    }

    public static CommentDtoResponse toCommentDtoResponse(Comment comment) {
        return CommentDtoResponse.builder()
            .id(comment.getId())
            .text(comment.getText())
            .authorName(comment.getAuthor().getName())
            .created(comment.getCreated())
            .build();
    }

    public static List<CommentDtoResponse> toCommentList(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentDtoResponse).collect(Collectors.toList());
    }
}
