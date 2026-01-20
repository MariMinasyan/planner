package am.aua.planner.core;

import am.aua.planner.exceptions.MalformedStringException;
import java.util.*;

public class VideoCall extends Task{
    private String title;
    private Set<Contact> callers = new HashSet<>();

    public String getTitle(){
        return this.title;
    }

    public String getFullDetails() {
        String result = title + " ";
        result += concatenateCallersWithDelimiter(" ");
        return result;
    }

    public String toFullString(){
        return "VIDEOCALL" + DELIMITER + this.title + DELIMITER + concatenateCallersWithDelimiter(DELIMITER);
    }

    public boolean equals(Object other){
        if(other == null || other.getClass() != this.getClass()) return false;
        if(other == this) return true;
        VideoCall v = (VideoCall) other;
        if(this.callers.size() != v.callers.size()) return false;
        return this.title.equals(v.title) && this.callers.equals(v.callers);
    }

    public VideoCall(String title, String name, String email) throws MalformedStringException {
        if (title.contains(DELIMITER)) throw new MalformedStringException("Title contains " + DELIMITER);
        this.title = title;
        callers.add(new Contact(name, email));
    }


    public VideoCall(String s) throws MalformedStringException {
        super(s);

        String[] splitArg = s.split(DELIMITER);

        if(splitArg.length < 3 || !splitArg[0].equals("VIDEOCALL"))
            throw new MalformedStringException("Invalid String argument for VideoCall");

        this.title = splitArg[1];
        for (int i = 2; i + 1 < splitArg.length; i += 2) {
            String name = splitArg[i];
            String email = splitArg[i + 1];
            callers.add(new Contact(name, email));
        }
    }

    public String getShortDescription(int length) {
        if(length < 4){
            return "CALL";
        }
        return getShortDescription().substring(0,length);
    }

    public String getFullDescription() {
        return getFullDetails();
    }

    public String toSaveString() {
        return this.toFullString();
    }


    public void addParticipant(Contact c) {
        callers.add(c);
    }

    public boolean removeParticipant(Contact c) {
        if (callers.size() <= 1) return false;
        return callers.remove(c);
    }

    private String concatenateCallersWithDelimiter(String delimiter) {
        String result = "";
        int size = callers.size();
        int count = 0;

        for (Contact c : callers) {
            result += c;
            count++;
            if (count < size) {
                result += delimiter;
            }
        }
        return result;
    }


    public VideoCall clone(){
        // No need to handle the CloneNotSupportedException here
        // because it's taken care of in Task
        VideoCall copy = (VideoCall) super.clone();
        HashSet<Contact> copiedCallers = new HashSet<>();
        for (Contact c : this.callers) {
            copiedCallers.add(c.clone());
        }
        copy.callers = copiedCallers;
        return copy;

    }

    public Set<Contact> getCallers() {
        Set<Contact> copy = new HashSet<>();
        for (Contact c : callers) {
            copy.add(c.clone());
        }
        return copy;
    }

}
