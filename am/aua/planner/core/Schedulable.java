package am.aua.planner.core;

public interface Schedulable{
    public static final String DELIMITER = "%%";

    // interfaces can have multiple parents
    String getShortDescription();
    String getShortDescription(int length);
    String getFullDescription();
    String toSaveString();
    Schedulable clone();
}
