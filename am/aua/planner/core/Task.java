package am.aua.planner.core;

import am.aua.planner.exceptions.MalformedStringException;

public abstract class Task implements Schedulable, Cloneable{

    public abstract String getTitle();
    public abstract String getFullDetails();
    public abstract String toFullString();
    public Task clone(){
        try{
            Task copy = (Task) super.clone();
            return copy;
        }catch (CloneNotSupportedException e){}
        return null;
    }

    public String toString(){
        return this.getShortDescription(10);
    }

    public Task() throws MalformedStringException{}

    public Task(String s) throws MalformedStringException{
        if(s == null || s.isEmpty())
            throw new MalformedStringException("Give a proper String you dumdum.");
    }

    public String getShortDescription(){
        return getTitle();
    }


}
