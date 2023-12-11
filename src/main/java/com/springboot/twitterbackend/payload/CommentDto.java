package com.springboot.twitterbackend.payload;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CommentDto {
    private long id;
    @NotEmpty
    private String commentMsg;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCommentMsg() {
        return commentMsg;
    }

    public void setCommentMsg(String commentMsg) {
        this.commentMsg = commentMsg;
    }


}