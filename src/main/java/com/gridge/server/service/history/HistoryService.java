package com.gridge.server.service.history;

import com.gridge.server.dataManager.hisotry.HistoryRepository;
import com.gridge.server.service.common.SecurityService;
import com.gridge.server.service.history.dto.HistoryInfo;
import com.gridge.server.service.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final SecurityService securityService;

    @Transactional(readOnly = true)
    public List<HistoryInfo> getHistoryByAdmin(PageRequest pageRequest, Member member) {
        return historyRepository.findHistory(pageRequest).stream()
                .map(history -> HistoryInfo.builder()
                        .id(history.getId())
                        .nickname(securityService.twoWayDecrypt(history.getMember().getNickname()))
                        .createAt(history.getCreateAt())
                        .build())
                .toList();
    }
}
