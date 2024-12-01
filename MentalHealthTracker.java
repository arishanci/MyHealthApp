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
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MentalHealthTracker extends JFrame {

    private JTextField moodInputField;
    private JTextArea moodLogArea;
    private JButton findResourcesButton;
    private JSpinner dateSpinner;
    private HashMap<String, String> moodLogs;

    // constructor
    public MentalHealthTracker() {
        // Main frame
        setTitle("Mental Health Tracker");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        // stores mood log
        moodLogs = new HashMap<>();

        // Mood label
        JLabel moodLabel = new JLabel("How do you feel today? (1-5):");
        moodLabel.setBounds(50, 30, 200, 25);
        panel.add(moodLabel);

        // Mood input field
        moodInputField = new JTextField();
        moodInputField.setBounds(230, 30, 50, 25);
        panel.add(moodInputField);

        // Date selection spinner
        JLabel dateLabel = new JLabel("Select Date:");
        dateLabel.setBounds(50, 70, 100, 25);
        panel.add(dateLabel);

        // Spinner for date selection
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setBounds(150, 70, 150, 25);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy");
        dateSpinner.setEditor(dateEditor);
        panel.add(dateSpinner);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(140, 110, 100, 30);
        panel.add(submitButton);

        // Mood log label
        JLabel moodLogLabel = new JLabel("Mood Logs for the last 10 days:");
        moodLogLabel.setBounds(50, 150, 300, 25);
        panel.add(moodLogLabel);

        // Mood log area
        moodLogArea = new JTextArea();
        moodLogArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moodLogArea);
        scrollPane.setBounds(50, 180, 300, 150);
        panel.add(scrollPane);

        // Find resources button
        findResourcesButton = new JButton("Find resources based on your location");
        findResourcesButton.setBounds(50, 350, 300, 30);
        panel.add(findResourcesButton);

        // Back to Main Menu button
        JButton backButton = new JButton("Back to Main Menu");
        backButton.setBounds(125, 400, 150, 30);
        panel.add(backButton);

        add(panel);

        // Submit button functionality
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mood = moodInputField.getText();
                Date selectedDate = (Date) dateSpinner.getValue();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String date = dateFormat.format(selectedDate);

                if (mood.isEmpty() || !mood.matches("[1-5]")) {
                    JOptionPane.showMessageDialog(panel, "Please enter a valid mood rating (1-5).", "Input Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Save mood and date
                    moodLogs.put(date, mood);

                    // save to mood log area
                    moodLogArea.append("Date: " + date + " | Mood: " + mood + "\n");

                    // clear input
                    moodInputField.setText("");
                }
            }
        });

        // Back button
        backButton.addActionListener(e -> dispose()); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MentalHealthTracker().setVisible(true));
    }
}
