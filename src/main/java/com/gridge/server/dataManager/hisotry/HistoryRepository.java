package com.gridge.server.dataManager.hisotry;

import com.gridge.server.service.history.entity.History;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long>{
    @Query("SELECT h FROM History h JOIN FETCH h.member")
    List<History> findHistory(PageRequest pageRequest);
}
