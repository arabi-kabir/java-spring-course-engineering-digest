package com.arabi.journalApp.controller;

import com.arabi.journalApp.entity.JournalEntry;
import com.arabi.journalApp.entity.User;
import com.arabi.journalApp.service.JournalEntryService;
import com.arabi.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {
    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {
        User user = userService.findByUsername(userName);
       List<JournalEntry> all = user.getJournalEntries();
       if (all != null) {
           return new ResponseEntity<>(all, HttpStatus.OK);
       }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry journalEntry, @PathVariable String userName) {
        try {

            journalEntryService.saveEntry(journalEntry, userName);
            return new ResponseEntity<>(journalEntry, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getEntryById(@PathVariable ObjectId myId) {
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
        if (journalEntry.isPresent()) {
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> removeEntryById(@PathVariable ObjectId myId, @PathVariable String userName) {
        journalEntryService.deleteEntryById(myId, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateEntryById(
            @PathVariable ObjectId myId,
            @RequestBody JournalEntry updatedJournalEntry,
            @PathVariable String userName
    ) {
        JournalEntry oldJournalEntry = journalEntryService.findById(myId).orElse(null);
        if (oldJournalEntry != null) {
            oldJournalEntry.setTitle(updatedJournalEntry.getTitle() != null && !updatedJournalEntry.getTitle().equals("") ? updatedJournalEntry.getTitle() : oldJournalEntry.getTitle());
            oldJournalEntry.setContent(updatedJournalEntry.getContent() != null && !updatedJournalEntry.getContent().equals("") ? updatedJournalEntry.getContent() : oldJournalEntry.getContent());
            journalEntryService.saveEntry(oldJournalEntry);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
