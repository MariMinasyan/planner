package am.aua.planner.core;

import am.aua.planner.exceptions.MalformedStringException;

import java.util.Objects;

import static am.aua.planner.core.Schedulable.DELIMITER;

public class Contact implements Comparable<Contact>, Cloneable {
    private String name;
    private String email;

    public Contact(String name, String email) throws MalformedStringException {
        if (!isValidEmail(email))
            throw new MalformedStringException("Invalid email");
        if (name == null || name.isEmpty())
            throw new MalformedStringException("Invalid name");
        this.name = name;
        this.email = email;
    }

    public Contact clone(){
        try{
            return (Contact) super.clone();
        }catch(CloneNotSupportedException e){
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && !email.contains(DELIMITER);
    }


    public int compareTo(Contact other) {
        int emailCompare = this.email.compareTo(other.email);
        if (emailCompare != 0) {
            return emailCompare;
        }
        return this.name.compareTo(other.name);
    }


    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj.getClass() != this.getClass()) return false;
        Contact other = (Contact) obj;
        return this.email.equals(other.email) && this.name.equals(other.name);
    }


    public int hashCode() {
        return Objects.hash(email, name);
    }



    public String toString() {
        return name + " \"" + email + "\"";
    }
}
