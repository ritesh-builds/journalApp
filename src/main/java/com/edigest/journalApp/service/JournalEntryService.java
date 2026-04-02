package com.edigest.journalApp.service;

import java.util.List;
import java.util.Optional;

import com.edigest.journalApp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.edigest.journalApp.entity.JournalEntry;
import com.edigest.journalApp.repository.JournalEntryRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@Service
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;



    @Transactional //----> yeh ek transactional context banata hai ya yeh kaho ki container banata hai joki aapas mein isolate hote hain...
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setDate(journalEntry.getDate());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("An error occurred while saving entry"+ e);
        }
    }
    public void saveEntry(JournalEntry journalEntry) {
        JournalEntry saved = journalEntryRepository.save(journalEntry);
    }
    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(@PathVariable ObjectId Id){
        return Optional.of(journalEntryRepository.findById(Id).get());
    }

    @Transactional
    public boolean deleteById(@PathVariable ObjectId Id, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(Id));
            if (removed) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(Id);
            }

        } catch (Exception e){
            log.error("Error",e);
            throw new RuntimeException("An error occurred while deleting entry"+ e);
        }
        return removed;
    }
}


// controller ---> service ---> repository