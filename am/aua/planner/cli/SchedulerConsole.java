package am.aua.planner.cli;

import am.aua.planner.core.*;
import am.aua.planner.exceptions.MalformedStringException;
import am.aua.planner.utils.FileUtils;
import java.util.*;

public class SchedulerConsole {
    private static Scanner sc = null;
    // declaring the scanner within the scope of the class ensures
    // multiple methods can access it.

    private static Workweek ww = null;

    public static void test(){
        /*
        ww = new Workweek();
        sc = new Scanner(System.in);
        //test05(); //Uncomment to populate
        loadSchedule();
        printWorkweek();

         */

        // left here for your consideration
        Workweek w1 = new Workweek();
        Workweek w2 = new Workweek();
        Workweek w3 = new Workweek();
        try {
            w1.addToSchedule(new VideoCall("Working", "Mari", "mari@aua.am"), Days.THU, Times.MORNING);
            w2.addToSchedule(new VideoCall("Test", "Mari", "mari@aua.am"), Days.THU, Times.MORNING);
            w2.addToSchedule(new VideoCall("Test","Mari", "mari@aua.am"), Days.THU, Times.AFTERNOON);
            Workweek[] weeks = {w1,w2,w3};
            System.out.println(Workweek.leastBusyWorkWeek(weeks).getFullDetailsAt(Days.THU, Times.MORNING));

        }
        catch (Exception e){
            e.printStackTrace(); // prints out the red stuff :)
        }

    }

    private static void test05(){
        // Method that populates the workweek with some values.
        try {
            Task t1 = new Presentation("Lecture: File I/O", 101.1, 202.2);
            Task t2 = new VideoCall("Office Hour", "Mari", "mari@aua.am");
            Task t3 = new Presentation("Lecture: Interfaces", 101.1, 202.2);
            ww.addToSchedule(t1,Days.TUE,Times.MORNING);
            ww.addToSchedule(t2,Days.TUE,Times.AFTERNOON);
            ww.addToSchedule(t3,Days.THU,Times.MORNING);
        }catch (Exception e){

        }
    }

    public static void start(){
        sc = new Scanner(System.in);
        // Initializing the scanner inside the method ensures it only
        // happens when CLI is launched.
        // Initializing it right after the static declaration instead of null,
        // results in a Scanner being created when the program is launched.

        ww = new Workweek();
        // Initializing blank workweek

        mainLoop:
        do{
            printWorkweek();
            printOptions();
            System.out.println("Choose an option by number or by highlighted letter:");
            String s = sc.nextLine();
            switch (s){
                case "1","a","A":
                    addTask();
                    break;
                case "2","r","R":
                    removeTask();
                    break;
                case "3","d","D":
                    printDetails();
                    break;
                case "4","l","L":
                    loadSchedule();
                    break;
                case "5","s","S":
                    saveSchedule();
                    break;
                case "6","q","Q":
                    break mainLoop;
            }
        }while(true);

    }

    private static void printWorkweek(){
        for(int i = 0; i < Days.values().length; i++){
            for(int j = 0; j < Times.values().length; j++){
                if(j == 0){
                    System.out.print(Days.values()[i]);
                    System.out.printf("%12s", Times.values()[j]);
                }else{
                    System.out.printf("%15s", Times.values()[j]);
                }
                System.out.print("\t\t");
                System.out.println(ww.getTitleAt(Days.values()[i],Times.values()[j]));
            }
        }
        System.out.println();
    }

    private static void printOptions(){
        System.out.println("1. (A)dd a task.");
        System.out.println("2. (R)emove a task.");
        System.out.println("3. Print (d)etails.");
        System.out.println("4. (L)oad schedule from file.");
        System.out.println("5. (S)ave schedule to file.");
        System.out.println("6. (Q)uit.");
    }

    private static Days chooseDay() throws MalformedStringException{
        System.out.println("Enter day of week:");
        try{
            return Days.valueOf(sc.nextLine());
        }catch (Exception e){
            throw new MalformedStringException("A 3-lettered valid day of week needed, e.g. TUE.");
        }
    }

    private static Times chooseTime() throws MalformedStringException{
        System.out.println("Enter AM or PM:");
        try{
            String timeInput = sc.nextLine();
            if(timeInput.equals("AM")){
                return Times.MORNING;
            }
            if(timeInput.equals("PM")){
                return Times.AFTERNOON;
            }
            throw new MalformedStringException
                    ("This exception will be immediately caught, another will be thrown");
        }catch (Exception e){
            throw new MalformedStringException("Please enter either AM or PM");
        }
    }

    private static void addTask(){
        Task k; Times t; Days d;

        try{
            d = chooseDay();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }

        try{
            t = chooseTime();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Do you wish to add a (v)ideo call or a (p)resentation?");
        String taskType = sc.nextLine();
        taskType = taskType.trim().substring(0,1).toLowerCase();
        if(taskType.equals("v")){
            try {
                System.out.println("Enter a title:");
                String title = sc.nextLine();
                System.out.println("Enter a participant name:");
                String name = sc.nextLine();
                System.out.println("Enter a participant email:");
                String email = sc.nextLine();
                k = new VideoCall(title, name, email);
                ww.addToSchedule(k,d,t);
            }catch (MalformedStringException e){
                System.out.println(e.getMessage());
            }
        }else if(taskType.equals("p")){
            try {
                System.out.println("Enter a title:");
                String title = sc.nextLine();
                System.out.println("Enter coordinates, latitude and longitude:");
                double latitude = sc.nextDouble();
                double longitude = sc.nextDouble();
                k = new Presentation(title, latitude, longitude);
                ww.addToSchedule(k,d,t);
            }catch (MalformedStringException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private static void removeTask(){
        Times t; Days d;

        try{
            d = chooseDay();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }

        try{
            t = chooseTime();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Removing task on " + d + " at " + t + " with the title:");
        System.out.println(ww.getTitleAt(d,t));
        System.out.println();
        ww.removeFromSchedule(d,t);
    }

    private static void printDetails(){
        Times t; Days d;

        try{
            d = chooseDay();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }

        try{
            t = chooseTime();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }

        System.out.println(ww.getFullDetailsAt(d,t));
    }

    private static void loadSchedule(){
        System.out.println("Specify path or leave blank for the default path:");
        String currentPath = sc.nextLine();
        String[] filecontents = null;
        System.out.println("Loading workweek from file...");
        try{
            if(currentPath.isEmpty()){
                filecontents = FileUtils.loadStringsFromFile();
            }else{
                filecontents = FileUtils.loadStringsFromFile(currentPath);
            }
            ww = Workweek.generateWorkweekFromStrings(filecontents);
            System.out.println("Workweek loaded successfully.");
        }catch (Exception e) {
            System.out.println("Problem loading from file.");
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

    private static void saveSchedule(){
        System.out.println("Specify path or leave blank for the default path:");
        String currentPath = sc.nextLine();
        String[] contents = ww.convertToStringArray();

        try{
            System.out.println("Saving workweek to file...");
            if(currentPath.isEmpty()){
                FileUtils.saveStringsToFile(ww.convertToStringArray());
            }else{
                FileUtils.saveStringsToFile(ww.convertToStringArray(),currentPath);
            }
            System.out.println("Workweek saved successfully.");
        }catch (Exception e) {
            System.out.println("Problem saving to file.");
            System.out.println(e.getMessage());
        }
        System.out.println();
    }

}
