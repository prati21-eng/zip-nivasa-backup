package com.zipnivasa.backend.modules.mess.service;

import com.zipnivasa.backend.modules.mess.model.Mess;
import com.zipnivasa.backend.modules.mess.repository.MessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class MessDailyResetJob {

    private final MessRepository messRepository;

    /**
     * Cron: every night at midnight
     * Same as your node-cron job:
     * 0 0 * * *
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void resetDailySpecial() {

        messRepository.findAll().forEach(m -> {
            m.getSpecialToday().setLunch("");
            m.getSpecialToday().setDinner("");
            m.getSpecialToday().setImageUrl("");
            m.getSpecialToday().setDate(Instant.now());
            messRepository.save(m);
        });

        System.out.println("Daily specials reset successfully");
    }
}
