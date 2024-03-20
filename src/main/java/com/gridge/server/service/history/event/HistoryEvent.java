package com.gridge.server.service.history.event;

import com.gridge.server.service.history.entity.HistoryType;
import com.gridge.server.service.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HistoryEvent {
    private Member member;
    private HistoryType type;
}
