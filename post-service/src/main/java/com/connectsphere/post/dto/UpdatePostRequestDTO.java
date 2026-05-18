package com.connectsphere.post.dto;

import com.connectsphere.post.entity.Visibility;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * UpdatePostRequestDTO - Payload for PUT /posts/{postId}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePostRequestDTO {

    @Size(max = 5000, message = "Post content cannot exceed 5000 characters")
    private String content;

    private Visibility visibility;
}