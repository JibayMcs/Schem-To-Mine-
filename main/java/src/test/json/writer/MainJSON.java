package test.json.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainJSON
{
    
    public static void main(String[] args)
    {
        Country countryObj = new Country();
        List<String> listOfStates = new ArrayList<String>();
        listOfStates.add("Madhya Pradesh");
        listOfStates.add("Maharastra");
        listOfStates.add("Rajasthan");
        
        countryObj.setListOfStates(listOfStates);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        String json = gson.toJson(countryObj);
        try
        {
            FileWriter writer = new FileWriter("./test.json");
            writer.write(json);
            writer.close();
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        System.out.println(json);
    }
    
}