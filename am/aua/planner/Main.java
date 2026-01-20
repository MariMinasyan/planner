package am.aua.planner;


import am.aua.planner.cli.SchedulerConsole;
import am.aua.planner.ui.SchedulerUI;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("-cli")) {
            SchedulerConsole.test(); // For debugging.
            SchedulerConsole.start(); //For normal start
        } else {
            SchedulerUI.launch();
        }


    }
}
