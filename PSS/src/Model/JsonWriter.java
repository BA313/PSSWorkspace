package Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class JsonWriter {

    public void writeTaskList(String filePath, ArrayList<Task> taskArrayList) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Writer writer = Files.newBufferedWriter(Paths.get(filePath));
            gson.toJson(taskArrayList, writer);
            writer.close();

        } catch (Exception e) {
            System.out.println("Error occurs when writing Json!");
            e.printStackTrace();
        }

    }
}
