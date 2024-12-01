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

import org.jdatepicker.impl.*;
import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

public class MentalHealthTracker extends JFrame {

    private JTextField moodInputField;
    private JTextArea moodLogArea;
    private JDatePickerImpl datePicker;
    private MHTrackerApp trackerApp;

    public MentalHealthTracker() {
        // Initialize app class
        trackerApp = new MHTrackerApp();

        // Main frame settings
        setTitle("Mental Health Tracker");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Mood input
        JLabel moodLabel = new JLabel("How do you feel today? (1-5):");
        moodLabel.setBounds(50, 30, 200, 25);
        panel.add(moodLabel);

        moodInputField = new JTextField();
        moodInputField.setBounds(250, 30, 50, 25);
        panel.add(moodInputField);

        // Date input using JDatePicker
        JLabel dateLabel = new JLabel("Select Date:");
        dateLabel.setBounds(50, 70, 100, 25);
        panel.add(dateLabel);

        datePicker = createDatePicker();
        datePicker.setBounds(150, 70, 200, 25);
        panel.add(datePicker);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(50, 110, 100, 30);
        panel.add(submitButton);

        // Mood log display
        JLabel moodLogLabel = new JLabel("Mood Logs for the last 10 days:");
        moodLogLabel.setBounds(50, 160, 200, 25);
        panel.add(moodLogLabel);

        moodLogArea = new JTextArea();
        moodLogArea.setBounds(50, 190, 380, 200);
        moodLogArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moodLogArea);
        scrollPane.setBounds(50, 190, 380, 200);
        panel.add(scrollPane);

        // Delete button
        JButton deleteButton = new JButton("Delete Log");
        deleteButton.setBounds(50, 410, 120, 30);
        panel.add(deleteButton);

        // Back button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setBounds(200, 410, 200, 30);
        panel.add(backButton);

        add(panel);

        // Button actions
        submitButton.addActionListener(e -> addMoodEntry());
        deleteButton.addActionListener(e -> deleteMoodEntry());
        backButton.addActionListener(e -> dispose());
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    private void addMoodEntry() {
        try {
            String moodInput = moodInputField.getText();
            int mood = Integer.parseInt(moodInput);

            Date selectedDate = (Date) datePicker.getModel().getValue();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Please select a valid date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            boolean success = trackerApp.addMoodEntry(localDate, mood);

            if (!success) {
                JOptionPane.showMessageDialog(this, "Invalid input. Ensure mood is 1-5 and date is not in the future.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            updateMoodLog();
            moodInputField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for mood.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMoodEntry() {
        try {
            Date selectedDate = (Date) datePicker.getModel().getValue();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Please select a valid date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            boolean deleted = trackerApp.deleteMoodEntry(localDate);

            if (!deleted) {
                JOptionPane.showMessageDialog(this, "No log found for the selected date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            updateMoodLog();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while deleting the log.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMoodLog() {
        moodLogArea.setText(trackerApp.getMoodLog());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MentalHealthTracker().setVisible(true));
    }
}

// Utility class for formatting date picker dates
class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parse(text);
    }

    @Override
    public String valueToString(Object value) {
        if (value != null) {
            return dateFormatter.format((Date) value);
        }
        return "";
    }
}
