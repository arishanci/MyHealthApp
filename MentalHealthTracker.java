/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package healthapp;

/**
 * @author arisha mirza
 * date: 16/11/24
 * MentalHealthTracker.java
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class MentalHealthTracker extends JFrame {

    private JTextField moodInputField, deleteDateField;
    private JTextArea moodLogArea;
    private JSpinner dateSpinner;
    private ArrayList<MoodEntry> moodLog = new ArrayList<>();

    public MentalHealthTracker() {
        // main frame
        setTitle("Mental Health Tracker");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Mood input
        JLabel moodLabel = new JLabel("How do you feel today? (1-5):");
        moodLabel.setBounds(30, 20, 200, 25);
        panel.add(moodLabel);
        
        //moodinput bounds
        moodInputField = new JTextField();
        moodInputField.setBounds(240, 20, 50, 25);
        panel.add(moodInputField);
        
        //date label
        JLabel dateLabel = new JLabel("Select Date:");
        dateLabel.setBounds(30, 60, 100, 25);
        panel.add(dateLabel);
        
        //date source: https://www.geeksforgeeks.org/how-to-create-a-date-object-using-the-calendar-class-in-java/
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(dateModel);
        dateSpinner.setBounds(140, 60, 150, 25);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(editor);
        panel.add(dateSpinner);
        
        //submission for mood
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(320, 20, 100, 30);
        panel.add(submitButton);

        // mood data deletion
        JLabel deleteLabel = new JLabel("Delete Log (yyyy-MM-dd):");
        deleteLabel.setBounds(30, 100, 200, 25);
        panel.add(deleteLabel);

        deleteDateField = new JTextField();
        deleteDateField.setBounds(200, 100, 150, 25);
        panel.add(deleteDateField);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(370, 100, 80, 25);
        panel.add(deleteButton);

        // Mood log display
        JLabel moodLogLabel = new JLabel("Mood Logs for the last 10 days:");
        moodLogLabel.setBounds(30, 160, 200, 25);
        panel.add(moodLogLabel);

        moodLogArea = new JTextArea();
        moodLogArea.setBounds(30, 190, 420, 200);
        moodLogArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moodLogArea);
        scrollPane.setBounds(30, 190, 420, 200);
        panel.add(scrollPane);

        // Back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setBounds(175, 500, 150, 30);
        panel.add(backButton);

        add(panel);

        // Button actions
        submitButton.addActionListener(e -> addMoodEntry());
        deleteButton.addActionListener(e -> deleteMoodEntry());
        backButton.addActionListener(e -> dispose());
    }

    private void addMoodEntry() {
        try {
            // Get mood input
            String moodInput = moodInputField.getText();
            int mood = Integer.parseInt(moodInput);

            if (mood < 1 || mood > 5) {
                JOptionPane.showMessageDialog(this, "Please enter a mood value between 1 and 5.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get date input
            Date selectedDate = (Date) dateSpinner.getValue();
            LocalDate selectedLocalDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = LocalDate.now();

            // Check if the selected date is in the future
            if (selectedLocalDate.isAfter(currentDate)) {
                JOptionPane.showMessageDialog(this, "You cannot select a future date.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add mood entry to the log
            moodLog.add(new MoodEntry(selectedLocalDate, mood));
            updateMoodLog();

            // Clear input fields
            moodInputField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for mood.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMoodEntry() {
        try {
            // Parse the date entered for deletion
            String deleteDateInput = deleteDateField.getText();
            LocalDate deleteDate = LocalDate.parse(deleteDateInput);

            boolean entryFound = false;

            // Search for and remove the matching log
            for (int i = 0; i < moodLog.size(); i++) {
                if (moodLog.get(i).getDate().equals(deleteDate)) {
                    moodLog.remove(i);
                    entryFound = true;
                    break;
                }
            }

            if (entryFound) {
                updateMoodLog();
                JOptionPane.showMessageDialog(this, "Mood log deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No mood log found for the entered date.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            deleteDateField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMoodLog() {
        moodLogArea.setText("");
        LocalDate tenDaysAgo = LocalDate.now().minusDays(10);
        int totalMood = 0;
        int count = 0;

        // Filter and display only the last 10 days of mood logs
        for (MoodEntry entry : moodLog) {
            if (entry.getDate().isAfter(tenDaysAgo) || entry.getDate().isEqual(tenDaysAgo)) {
                moodLogArea.append(entry + "\n");
                totalMood += entry.getMood();
                count++;
            }
        }

        // Calculate and display average mood
        if (count > 0) {
            double averageMood = (double) totalMood / count;
            moodLogArea.append("\nAverage Mood: " + String.format("%.2f", averageMood));
        } else {
            moodLogArea.append("\nNo mood logs available for the last 10 days.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MentalHealthTracker().setVisible(true));
    }
}

// MoodEntry class to hold mood and date
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
