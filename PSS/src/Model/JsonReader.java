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
     * Read a json file and return a task list with both recurring and transient tasks
     * @param FilePath path of the json file
     * @return an ArrayList of Task, or an empty ArrayList if exception occurs
     */
    public static ArrayList<Task> readTaskList(String FilePath) {
        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(FilePath));
            ArrayList<Task> taskList = gson.fromJson(reader, new TypeToken<ArrayList<Recurring>>(){}.getType());
            return taskList;

        } catch (IOException e) {
            System.out.println("Error occurs!");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }



}
