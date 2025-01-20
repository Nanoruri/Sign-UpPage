package me.jh.board.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private long id;
    private String content;
    private LocalDateTime date;
    private LocalDateTime updateDate;
    private String creator;

}