package me.jh.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.jh.board.dto.comment.CommentDTO;
import me.jh.board.entity.Comment;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    private long id;
    private String title;
    private String content;
    private LocalDateTime date;
    private String tabName;
    private String creatorId;
    private List<CommentDTO> comments;


    @Transactional
    public Map<String, Object> toObject(String userId) {
        Map<String, Object> response = new HashMap<>();
        response.put("board", this);
        response.put("isCreator", this.creatorId.equals(userId));
        response.put("currentUserId", userId);
        response.put("creator", this.creatorId);
        return response;
    }
}
