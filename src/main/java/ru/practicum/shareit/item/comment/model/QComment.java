package ru.practicum.shareit.item.comment.model;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;
import org.springframework.lang.Nullable;

import javax.annotation.processing.Generated;

@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QComment extends EntityPathBase<Comment> {
    public QComment(Class<? extends Comment> type, String variable) {
        super(type, variable);
    }

    public QComment(Class<? extends Comment> type, PathMetadata metadata) {
        super(type, metadata);
    }

    public QComment(Class<? extends Comment> type, PathMetadata metadata,
                    @Nullable PathInits inits) {
        super(type, metadata, inits);
    }
}
