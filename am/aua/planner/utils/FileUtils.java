package am.aua.planner.utils;

import java.io.*;
import java.util.Scanner;

public class FileUtils {

    private static String defaultPath = "./myworkweek.txt";

    public static void saveStringsToFile(String[] content) throws IOException{
        saveStringsToFile(content, defaultPath);
    }

    public static void saveStringsToFile(String[] content, String path) throws IOException{
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(path));
            for(String s: content){
                pw.println(s);
            }
        }catch (IOException e){
            // What we want to do, is to pass along the same exception as is and inform the
            // caller about the problem, and also wrap up all file stream related activities.
            throw e;
        }finally{
            if(pw != null){
                pw.close();
            }
        }
    }

    public static String[] loadStringsFromFile() throws IOException{
        return loadStringsFromFile(defaultPath);
    }

    public static String[] loadStringsFromFile(String path){
        String[] result;
        Scanner sc = null;
        int i;

        try{
            sc = new Scanner(new FileInputStream(path));
            result =  new String[100];
            for(i = 0; sc.hasNextLine(); i++){
                result[i] = sc.nextLine();
                if(i == result.length){
                    String[] tmp = result;
                    result = new String[tmp.length * 2];
                    for(int j = 0; j < tmp.length; j++){
                        result[j] = tmp[j];
                    }
                }
            }
        }
        catch (Exception e){
            result = null;
        }
        finally {
            if(sc != null){
                sc.close();
            }
        }
        return result;
    }
}