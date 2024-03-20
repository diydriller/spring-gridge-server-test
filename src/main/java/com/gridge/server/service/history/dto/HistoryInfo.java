package com.gridge.server.service.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
public class HistoryInfo {
    private Long id;
    private String nickname;
    private LocalDateTime createAt;
}
