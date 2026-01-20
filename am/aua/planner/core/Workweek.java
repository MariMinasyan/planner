package am.aua.planner.core;

import am.aua.planner.exceptions.MalformedStringException;


public class Workweek implements Comparable<Workweek>, Cloneable{
    private Schedulable[][] schedule;

    public Workweek(){
        this.schedule = new Schedulable[Days.values().length][Times.values().length];
    }

    public boolean isEmpty(Days d, Times t){
        return schedule[d.ordinal()][t.ordinal()] == null;
    }

    public void addToSchedule(Task k, Days d, Times t){
        schedule[d.ordinal()][t.ordinal()] = k;
    }

    public void removeFromSchedule(Days d, Times t){
        schedule[d.ordinal()][t.ordinal()] = null;
    }

    public String getTitleAt(Days d, Times t){
        Schedulable s = schedule[d.ordinal()][t.ordinal()];
        if(s == null) return "-";
        return s.getShortDescription();
    }

    public String getFullDetailsAt(Days d, Times t){
        Schedulable s = schedule[d.ordinal()][t.ordinal()];
        if(s == null) return "No activities scheduled";
        return s.getFullDescription();
    }

    public static Workweek generateWorkweekFromStrings(String[] arr)
            throws MalformedStringException {
        Workweek w = new Workweek();

        for(int i = 0; i < Days.values().length; i++){
            for(int j = 0; j < Times.values().length; j++){
                String s = arr[i * Times.values().length + j];
                if(s.equals("null")) continue;
                String type = s.split(Task.DELIMITER)[0];
                switch (type){
                    case "VIDEOCALL":
                        w.schedule[i][j] = new VideoCall(s);
                        break;
                    case "PRESENTATION":
                        w.schedule[i][j] = new Presentation(s);
                        break;
                    case "HORSETENNIS":
                        w.schedule[i][j] = new HorseTennis(s);
                        break;
                    case "PONYPINGPONG":
                        w.schedule[i][j] = new HorseTennis.PonyPingPong(s);
                        break;
                }
            }
        }

        return w;
    }

    public String[] convertToStringArray(){
        // This is the correct place to put this method, symmetric to generateWorkweekFromStrings().
        // It's a getter method that doesn't affect the state of the workweek and is safe to implement.

        String[] result = new String[Days.values().length * Times.values().length];
        for(int i = 0; i < Days.values().length; i++) {
            for (int j = 0; j < Times.values().length; j++) {
                if(this.schedule[i][j] != null)
                    result[i*Times.values().length + j] = this.schedule[i][j].toSaveString();
            }
        }
        return result;
    }

    public Schedulable getSchedulableAt(Days d, Times t) {
        return schedule[d.ordinal()][t.ordinal()];
    }



    private int caluclateNumberOfTasks(){
        int result = 0;
        for(int i = 0; i < Days.values().length; i++){
            for(int j = 0; j < Times.values().length; j++){
                if(schedule[i][j] != null) result++;
            }
        }
        return result;
    }
    public int compareTo(Workweek workweek){
        return this.caluclateNumberOfTasks() - workweek.caluclateNumberOfTasks();
    }

    public static Workweek leastBusyWorkWeek(Workweek[] weeks){
        Workweek least = weeks[0];
        for(int i = 1; i < weeks.length; i++){
            if(least.compareTo(weeks[i]) > 0){
                least = weeks[i];
            }
        }
        return least;
    }

    public Workweek clone(){
        try {
            Workweek copy = (Workweek) super.clone();
            copy.schedule = new Schedulable[Days.values().length][Times.values().length];
            for(int i = 0; i < Days.values().length; i++){
                for(int j = 0; j < Times.values().length; j++){
                    if(schedule[i][j] != null)
                        copy.schedule[i][j] = this.schedule[i][j].clone();
                }
            }
        }
        catch (CloneNotSupportedException e){}
        return null;
    }
}
