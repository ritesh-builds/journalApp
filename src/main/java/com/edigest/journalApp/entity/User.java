package com.edigest.journalApp.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users") // MongoDB collection ka naam
@Data                           // YAHI THA MAIN CULPRIT! (Getters/Setters ke liye)
@NoArgsConstructor              // Spring Data MongoDB ko object banane ke liye chahiye hota hai
@AllArgsConstructor
@Builder
public class User {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    @NonNull
    private String userName;

    @NonNull
    private String password;

    @DBRef
    @Builder.Default
    private List<JournalEntry> journalEntries = new ArrayList<>();

    private List<String> roles;
}
