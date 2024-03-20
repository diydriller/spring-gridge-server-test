package com.gridge.server.service.history;

import com.gridge.server.dataManager.hisotry.HistoryRepository;
import com.gridge.server.service.history.entity.History;
import com.gridge.server.service.history.event.HistoryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class HistoryEventListener {
    private final HistoryRepository historyRepository;

    @Async
    @TransactionalEventListener
    public void saveHistory(HistoryEvent event) {
        historyRepository.save(History.builder()
                .type(event.getType())
                .member(event.getMember())
                .build());
    }
}
