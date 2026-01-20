package am.aua.planner.core;

import am.aua.planner.exceptions.MalformedStringException;

public class Presentation extends Task{
    private String title;
    private double latitude, longitude;

    public String getTitle(){
        return this.title;
    }

    public String getFullDetails() {
        return this.title + " " + this.latitude + " " + this.longitude;
    }

    public String toFullString(){
        return "PRESENTATION" + DELIMITER + this.title + DELIMITER + this.latitude + DELIMITER + this.longitude;
    }
    public boolean equals(Object other){
        if(other == null || other.getClass() != this.getClass()) return false;
        Presentation p = (Presentation) other;
        return this.title.equals(p.title) && this.latitude == p.latitude &&
                this.longitude == p.longitude;
    }

    public Presentation(String title, double latitude, double longitude)
            throws MalformedStringException {
        // super(); // there is an implicit super call here
        if(title.contains(DELIMITER))
            throw new MalformedStringException("Title contains " + DELIMITER);

        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Presentation(String s) throws MalformedStringException {
        super(s);
        String[] splitArg = s.split(DELIMITER);

        if(splitArg.length != 4 || !splitArg[0].equals("PRESENTATION"))
            throw new MalformedStringException("Invalid String argument for Presentation");

        this.title = splitArg[1];
        this.latitude = Double.parseDouble(splitArg[2]);
        this.longitude = Double.parseDouble(splitArg[3]);

    }

    public String getShortDescription(int length) {
        if(length < 4){
            return "PRES";
        }
        return getShortDescription().substring(0,length);
    }

    public String getFullDescription() {
        return getFullDetails();
    }

    public String toSaveString() {
        return this.toFullString();
    }

    public Presentation clone(){
        // No need to handle the CloneNotSupportedException here
        // because it's taken care of in Task

        // there is one String, immutable so fine.
        // two primitive doubles, again fine.
        return (Presentation) super.clone();

    }
}
