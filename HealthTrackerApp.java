/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package healthapp;

/**
 * @author arisha mirza
 * date: 30/11/24
 * MentalHealthTracker.java
 */

import java.time.LocalDate;
import java.util.ArrayList;

public class HealthTrackerApp {

    // Stores mood entries
    private ArrayList<MoodEntry> moodLog;

    // Constructor
    public HealthTrackerApp() {
        moodLog = new ArrayList<>();
    }

    // Add a mood entry
    public boolean addMoodEntry(LocalDate date, int mood) {
        if (mood < 1 || mood > 5 || date.isAfter(LocalDate.now())) {
            return false; // Invalid input
        }
        moodLog.add(new MoodEntry(date, mood));
        return true;
    }

    // Delete a mood entry by date
    public boolean deleteMoodEntry(LocalDate date) {
        return moodLog.removeIf(entry -> entry.getDate().equals(date));
    }

    // Get the mood log for the last 10 days
    public String getMoodLog() {
        StringBuilder log = new StringBuilder();
        LocalDate tenDaysAgo = LocalDate.now().minusDays(10);
        int totalMood = 0;
        int count = 0;

        for (MoodEntry entry : moodLog) {
            if (entry.getDate().isAfter(tenDaysAgo) || entry.getDate().isEqual(tenDaysAgo)) {
                log.append(entry).append("\n");
                totalMood += entry.getMood();
                count++;
            }
        }

        if (count > 0) {
            double averageMood = (double) totalMood / count;
            log.append("\nAverage Mood: ").append(String.format("%.2f", averageMood));
        } else {
            log.append("\nNo mood logs available for the last 10 days.");
        }

        return log.toString();
    }

    // Getter for all mood entries (if needed for advanced features)
    public ArrayList<MoodEntry> getMoodLogEntries() {
        return moodLog;
    }
}

// MoodEntry class remains the same
class MoodEntry {
    private LocalDate date;
    private int mood;

    public MoodEntry(LocalDate date, int mood) {
        this.date = date;
        this.mood = mood;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getMood() {
        return mood;
    }

    @Override
    public String toString() {
        return date + ": Mood " + mood;
    }
}
