package com.blog.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
        private Long id;
        private String body;
        private String email;
        private String name;
        private Long postId;
}
