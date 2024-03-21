package com.gridge.server.service.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class HistoryInfo {
    private Long id;
    private String nickname;
    private LocalDateTime createAt;
}
