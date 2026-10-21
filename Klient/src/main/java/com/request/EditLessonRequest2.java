package com.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EditLessonRequest2 extends EditLessonRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
}