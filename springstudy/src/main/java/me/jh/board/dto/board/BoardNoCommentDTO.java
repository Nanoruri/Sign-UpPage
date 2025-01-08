package me.jh.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jh.springstudy.entity.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardNoCommentDTO {
    private long id;
    private String title;
    private String content;
    private LocalDateTime date;
    private String tabName;
    private User creator;

}
