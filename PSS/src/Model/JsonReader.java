package Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class JsonReader {

    /***
     * Read a json file and return a recurring task list
     * @param filePath path of the json file
     * @return an ArrayList of Task, or an empty ArrayList if exception occurs
     */
    public static ArrayList<Task> readRecurTaskList(String filePath) {
        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(filePath));
            return gson.fromJson(reader, new TypeToken<ArrayList<Recurring>>(){}.getType());

        } catch (IOException e) {
            System.out.println("Error occurs!");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /***
     * Read a json file and return a transient task list
     * @param filePath path of the json file
     * @return an ArrayList of Task, or an empty ArrayList if exception occurs
     */
    public static ArrayList<Task> readTransientTaskList(String filePath) {
        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(filePath));
            return gson.fromJson(reader, new TypeToken<ArrayList<Transient>>(){}.getType());

        } catch (IOException e) {
            System.out.println("Error occurs!");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /***
     * Read a json file and return a task list with both recurring and transient tasks
     * @param recurTaskFilePath path of the json file
     * @param transientTaskFilePath path of the json file
     * @return an ArrayList of Task, or an empty ArrayList if exception occurs
     */
    public static ArrayList<Task> readTaskList(String recurTaskFilePath, String transientTaskFilePath) {
        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(recurTaskFilePath));
            ArrayList<Task> taskList = gson.fromJson(reader, new TypeToken<ArrayList<Recurring>>(){}.getType());
            reader = Files.newBufferedReader(Paths.get(transientTaskFilePath));
            taskList.addAll(gson.fromJson(reader, new TypeToken<ArrayList<Transient>>(){}.getType()));
            return taskList;

        } catch (IOException e) {
            System.out.println("Error occurs!");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }



}
