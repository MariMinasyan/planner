package am.aua.planner.core;

import am.aua.planner.exceptions.MalformedStringException;

public class HorseTennis implements Schedulable{

    public static class PonyPingPong implements Schedulable{
        // Not inheritance, because if someone enjoys PonyPingPong, does
        // not automatically mean they enjoy HorseTennis and vice versa.
        //
        // The point of all this, is we're sharing and reusing code without
        // inheritance.
        //
        private HorseTennis ht;

        public PonyPingPong(Team a, Team b){
            this.ht = new HorseTennis(a,b);
        }

        public PonyPingPong(String jockeyA, String horseA, String jockeyB, String horseB){
            this.ht = new HorseTennis(jockeyA,horseA,jockeyB,horseB);
        }

        public PonyPingPong(String fromSave) throws MalformedStringException{
            fromSave = fromSave.replaceAll("PONYPINGPONG","HORSETENNIS");
            this.ht = new HorseTennis(fromSave);
        }

        public String getShortDescription() {
            return ht.getShortDescription();
        }

        public String getShortDescription(int length) {
            return ht.getShortDescription(length);
        }

        public String getFullDescription() {
            return "PonyPingPong: " + ht.teamA + " vs. " + ht.teamB;
        }

        public String toSaveString() {
            return "PONYPINGPONG" + DELIMITER + ht.teamsSavePart();
        }

        public PonyPingPong clone(){
            try {
                PonyPingPong copy = (PonyPingPong) super.clone();
                copy.ht = ht.clone();
                return copy;
            } catch (CloneNotSupportedException e) {

            }
            return null;
        }
    }

    public static class Team{
        private String jockey, horse;
        public Team(String jockey, String horse){
            this.jockey = jockey;
            this.horse = horse;
        }
        public String toString(){
            return this.jockey + " - " + this.horse;
        }
    }

    private Team teamA, teamB;

    public HorseTennis(Team teamA, Team teamB){
        this.teamA = teamA;
        this.teamB = teamB;
    }

    public HorseTennis(String riderA, String horseA, String riderB, String horseB){
        this(new Team(riderA,horseA), new Team(riderB, horseB));
    }

    public HorseTennis(String fromSaveString) throws MalformedStringException {
        String[] chunks = fromSaveString.split(DELIMITER);
        if(chunks.length != 5 || !chunks[0].equals("HORSETENNIS")){
            throw new MalformedStringException("Cannot construct sport object");
        }
        teamA = new Team(chunks[1],chunks[2]);
        teamB = new Team(chunks[3], chunks[4]);
    }

    public String getShortDescription() {
        return "Sports match";
    }

    public String getShortDescription(int length) {
        return getShortDescription().substring(0,length);
    }

    public String getFullDescription() {
        return "HorseTennis: " + teamA + " vs. " + teamB;
    }

    private String teamsSavePart(){
        return teamA.jockey + DELIMITER + teamA.horse +
                DELIMITER + teamB.jockey + DELIMITER + teamB.horse;
    }

    public String toSaveString() {
        return "HORSETENNIS"+DELIMITER+teamsSavePart();
    }

    public HorseTennis clone(){
        try{
            HorseTennis copy = (HorseTennis) super.clone();
            // teams are immutable, we're done.
            return copy;
        }catch (CloneNotSupportedException e){

        }
        return null;
    }

}
