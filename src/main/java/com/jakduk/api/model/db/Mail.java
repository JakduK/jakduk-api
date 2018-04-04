package com.jakduk.api.model.db;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@EqualsAndHashCode
@Document
public class Mail {

    @Id
    private String id;
    private String subject; // 제목
    private String templateName; // 템플릿 이름

}
