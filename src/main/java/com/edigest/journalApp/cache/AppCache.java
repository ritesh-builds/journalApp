package com.edigest.journalApp.cache;

import com.edigest.journalApp.entity.ConfigJournalAppEntity;
import com.edigest.journalApp.repository.ConfigJournalAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum keys {
         WEATHER_API
    }
    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;
    public Map<String, String> appCache = new HashMap<>();

    @PostConstruct()
    public void init() {
        appCache = new HashMap<>();
        List<ConfigJournalAppEntity> all = configJournalAppRepository.findAll();
        for(ConfigJournalAppEntity configJournalAppEntity : all) {
            appCache.put(configJournalAppEntity.getKey(), configJournalAppEntity.getValue());
        }
//        appCache = null;
    }
}
