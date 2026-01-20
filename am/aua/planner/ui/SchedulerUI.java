package am.aua.planner.ui;

import am.aua.planner.core.*;
import am.aua.planner.exceptions.MalformedStringException;
import am.aua.planner.utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SchedulerUI {
    private static JFrame frame;
    private static Workweek workweek = new Workweek();
    private static JButton[][] gridButtons = new JButton[Days.values().length][Times.values().length];

    public static void launch() {
        frame = new JFrame("Workweek Scheduler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        JPanel schedulePanel = new JPanel(new GridLayout(Days.values().length, Times.values().length));
        for (int i = 0; i < Days.values().length; i++) {
            for (int j = 0; j < Times.values().length; j++) {
                Days day = Days.values()[i];
                Times time = Times.values()[j];
                JButton cell = new JButton(workweek.getTitleAt(day, time));
                gridButtons[i][j] = cell;
                cell.addActionListener(e -> showTaskMenu(day, time));
                schedulePanel.add(cell);
            }
        }

        JPanel controls = new JPanel();
        JButton loadBtn = new JButton("Load");
        JButton saveBtn = new JButton("Save");

        loadBtn.addActionListener(e -> {
            try {
                String path = JOptionPane.showInputDialog(frame, "Enter file path (leave blank for default):");
                String[] contents = path == null || path.isBlank()
                        ? FileUtils.loadStringsFromFile()
                        : FileUtils.loadStringsFromFile(path);
                workweek = Workweek.generateWorkweekFromStrings(contents);
                refreshButtons();
                JOptionPane.showMessageDialog(frame, "Schedule loaded.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Failed to load: " + ex.getMessage());
            }
        });

        saveBtn.addActionListener(e -> {
            try {
                String path = JOptionPane.showInputDialog(frame, "Enter file path (leave blank for default):");
                if (path == null || path.isBlank())
                    FileUtils.saveStringsToFile(workweek.convertToStringArray());
                else
                    FileUtils.saveStringsToFile(workweek.convertToStringArray(), path);
                JOptionPane.showMessageDialog(frame, "Schedule saved.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Failed to save: " + ex.getMessage());
            }
        });

        controls.add(loadBtn);
        controls.add(saveBtn);

        frame.add(schedulePanel, BorderLayout.CENTER);
        frame.add(controls, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private static void showTaskMenu(Days d, Times t) {
        if (workweek.isEmpty(d, t)) {
            int add = JOptionPane.showConfirmDialog(frame, "No task scheduled. Do you want to add a task?",
                    "Add Task", JOptionPane.YES_NO_OPTION);
            if (add == JOptionPane.YES_OPTION) {
                addTaskUI(d, t);
            }
            return;
        }

        Schedulable schedulable = workweek.getSchedulableAt(d, t);
        boolean isVideoCall = schedulable instanceof VideoCall;

        if (isVideoCall) {
            String[] options = {"View Details", "Add Participant", "Remove Participant", "Remove Task", "Cancel"};
            int choice = JOptionPane.showOptionDialog(frame,
                    "Choose an action for " + d + " " + t,
                    "VideoCall Options", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);

            switch (choice) {
                case 0:
                    JOptionPane.showMessageDialog(frame, schedulable.getFullDescription());
                    break;

                case 1:
                    addParticipantUI((VideoCall) schedulable);
                    break;
                case 2:
                    removeParticipantUI((VideoCall) schedulable);
                    break;
                case 3: {
                    workweek.removeFromSchedule(d, t);
                    refreshButtons();
                    break;
                }
            }
        } else {
            String[] options = {"View Details", "Remove Task", "Cancel"};
            int choice = JOptionPane.showOptionDialog(frame,
                    "Choose an action for " + d + " " + t,
                    "Task Options", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);

            switch (choice) {
                case 0:
                    JOptionPane.showMessageDialog(frame, schedulable.getFullDescription());
                    break;
                case 1: {
                    workweek.removeFromSchedule(d, t);
                    refreshButtons();
                    break;
                }
            }
        }
    }

    private static void removeParticipantUI(VideoCall call) {
        String email = JOptionPane.showInputDialog(frame, "Enter email of participant to remove:");
        if (email == null) return;

        Contact toRemove = null;
        for (Contact c : call.getCallers()) {
            if (c.getEmail().equals(email)) {
                toRemove = c;
                break;
            }
        }

        if (toRemove == null) {
            JOptionPane.showMessageDialog(frame, "No participant with that email found.");
        } else {
            if (call.removeParticipant(toRemove)) {
                JOptionPane.showMessageDialog(frame, "Participant removed.");
            } else {
                JOptionPane.showMessageDialog(frame, "Cannot remove the last participant.");
            }
        }
    }


    private static void addParticipantUI(VideoCall call) {
        String name = JOptionPane.showInputDialog(frame, "Enter participant name:");
        String email = JOptionPane.showInputDialog(frame, "Enter participant email:");
        if (name == null || email == null) return;

        try {
            call.addParticipant(new Contact(name, email));
            JOptionPane.showMessageDialog(frame, "Participant added.");
        } catch (MalformedStringException e) {
            JOptionPane.showMessageDialog(frame, "Invalid participant: " + e.getMessage());
        }
    }


    private static void addTaskUI(Days d, Times t) {
        String[] taskTypes = {"VideoCall", "Presentation"};
        String type = (String) JOptionPane.showInputDialog(frame, "Select Task Type:", "Add Task",
                JOptionPane.QUESTION_MESSAGE, null, taskTypes, taskTypes[0]);

        if (type == null) return;

        try {
            if (type.equals("VideoCall")) {
                String title = JOptionPane.showInputDialog(frame, "Enter title:");
                String name = JOptionPane.showInputDialog(frame, "Enter participant name:");
                String email = JOptionPane.showInputDialog(frame, "Enter email:");

                VideoCall call = new VideoCall(title, name, email);
                workweek.addToSchedule(call, d, t);
            } else {
                String title = JOptionPane.showInputDialog(frame, "Enter title:");
                double lat = Double.parseDouble(JOptionPane.showInputDialog(frame, "Latitude:"));
                double lon = Double.parseDouble(JOptionPane.showInputDialog(frame, "Longitude:"));
                workweek.addToSchedule(new Presentation(title, lat, lon), d, t);
            }

            refreshButtons();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to add task: " + e.getMessage());
        }
    }

    private static void refreshButtons() {
        for (int i = 0; i < Days.values().length; i++) {
            for (int j = 0; j < Times.values().length; j++) {
                gridButtons[i][j].setText(workweek.getTitleAt(Days.values()[i], Times.values()[j]));
            }
        }
    }
}