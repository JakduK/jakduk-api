package com.jakduk.api.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SimpleWriter {
    private String userId;
    private String username;

    public SimpleWriter(CommonWriter commonWriter) {
        this.userId = commonWriter.getUserId();
        this.username = commonWriter.getUsername();
    }
}
