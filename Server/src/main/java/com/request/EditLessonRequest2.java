package com.request;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
public class EditLessonRequest2 extends EditLessonRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
}
