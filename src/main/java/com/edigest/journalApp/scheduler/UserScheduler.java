package com.edigest.journalApp.scheduler;

import com.edigest.journalApp.cache.AppCache;
import com.edigest.journalApp.entity.JournalEntry;
import com.edigest.journalApp.entity.User;
import com.edigest.journalApp.enums.Sentiment;
import com.edigest.journalApp.model.SentimentData;
import com.edigest.journalApp.repository.UserRepositoryImpl;
import com.edigest.journalApp.service.EmailService;
import com.edigest.journalApp.service.SentimentAnalysisService;
import com.edigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private AppCache appCache;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Scheduled(cron = "0 * * ? * *")
    public void fetchUsersAndSendSaMail(){
        List<User> users = userRepository.getUsersForSA();
        for(User user: users){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream()
                    .filter(entry -> entry.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(JournalEntry::getSentiment)   // ✅ correct
                    .collect(Collectors.toList());
            Map<Sentiment,Integer> sentimentCounts = new HashMap<>();
            for(Sentiment sentiment: sentiments){
                if(sentiment != null){
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment,0) + 1);
                }
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for(Map.Entry<Sentiment, Integer> entry: sentimentCounts.entrySet()){
                if(entry.getValue() > maxCount){
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if(mostFrequentSentiment != null){
                // 1. Sirf data set karo aur object build karo
                SentimentData sentimentData = SentimentData.builder()
                        .email(user.getEmail())
                        // Agar SentimentData class mein sentiment store karne ki field hai, toh usko yahan set kardo.
                        // Jaise: .sentiment(mostFrequentSentiment.toString())
                        .build();

                // 2. Data ko Kafka topic pe bhej do
                kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData);
            }
//            String sentiment = sentimentAnalysisService.getSentiment(entry);
//            emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days...", sentiment);
        }
    }

    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppChache(){
        appCache.init();
    }

}
