package com.example.order2gatherBE.services;

import com.example.order2gatherBE.models.HistoryModel;
import com.example.order2gatherBE.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {
    @Autowired
    HistoryRepository historyRepository;

    public List<HistoryModel> getUserOrderEventHistory(Integer uid) {
        return historyRepository.getUserOrderEventHistory(uid);
    }
}
