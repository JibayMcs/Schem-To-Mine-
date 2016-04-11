package org.jglrateis.schematic.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jglrateis.schematic.util.Util;

/**
 * 
 * @author jglrxavpok & ZeAmateis
 *         
 */

public class JSONBlocksNames
{
    private static Map<Byte, String> names;
    private static boolean areConflict;
    
    private static int blockModsIDConflict, blockVanillaIDConflict;
    private static String blockModsNameConflict, blockVanillaNameConflict;
    
    public static void init()
    {
        names = new HashMap<>();
        
        try
        {
            JsonParser parser = new JsonParser();
            
            File vanillaJsonFile = new File(Util.getWorkingDirectory() + "/.data/config/json/vanilla.json");
            Object obj = parser.parse(new FileReader(vanillaJsonFile));
            
            JsonObject jsonObject = (JsonObject)obj;
            
            JsonArray minecraftList = (JsonArray)jsonObject.get("minecraft");
            
            System.out.println("\nMinecraft Vanilla ID List:\n");
            
            for(int i = 0; i < minecraftList.size(); ++i)
            {
                System.out.println(i + " " + minecraftList.get(i).getAsString());
                addVanillaBlocks(i, minecraftList.get(i).getAsString());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        try
        {
            JsonParser parser = new JsonParser();
            
            File modsJsonFile = new File(Util.getWorkingDirectory() + "/.data/config/json/mods.json");
            Object obj1 = parser.parse(new FileReader(modsJsonFile));
            
            JsonObject jsonObjectMods = (JsonObject)obj1;
            JsonArray modsList = (JsonArray)jsonObjectMods.get("mods");
            
            if(modsList != null)
            {
                for(int i = 0; i < modsList.size(); i++)
                {
                    System.out.println("\nMinecraft Mods ID List:\n");
                    
                    String modIDJsonFile = modsList.getAsJsonArray().get(i).getAsString();
                    
                    // System.out.println(modIDJsonFile.replace(".json", ""));
                    
                    File customModsJsonFile = new File(Util.getWorkingDirectory() + "/.data/config/json/mods/" + modIDJsonFile);
                    Object obj = parser.parse(new FileReader(customModsJsonFile));
                    
                    JsonArray jsonArray = (JsonArray)obj;
                    if(jsonArray != null)
                    {
                        for(int j = 0; j < jsonArray.size(); ++j)
                        {
                            // System.out.println(jsonArray.get(j).getAsJsonObject().get("blockName").getAsString());
                            System.out.println(jsonArray.get(j).getAsJsonObject().get("blockID").getAsInt() + " " + jsonArray.get(j).getAsJsonObject().get("blockName").getAsString());
                            addModsBlocks(modIDJsonFile.replace(".json", ""), jsonArray.get(j).getAsJsonObject().get("blockID").getAsInt(), jsonArray.get(j).getAsJsonObject().get("blockName").getAsString());
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    // public static void initMods()
    // {
    // names = new HashMap<>();
    // try
    // {
    // JsonParser parser = new JsonParser();
    // InputStream inputModsJson = JSONBlocksNames.class.getClass().getResourceAsStream("/resources/json/mods.json");
    // Object obj1 = parser.parse(new InputStreamReader(inputModsJson));
    // JsonObject jsonObjectMods = (JsonObject)obj1;
    // JsonArray modsList = (JsonArray)jsonObjectMods.get("mods");
    // System.out.println("\nMinecraft Mods ID List:\n");
    // for(int i = 0; i < modsList.size(); i++)
    // {
    // String modIDJsonFile = modsList.getAsJsonArray().get(i).getAsString(); // System.out.println(modIDJsonFile.replace(".json", "")); InputStream in = JSONBlocksNames.class.getClass().getResourceAsStream("/resources/json/custom/" + modIDJsonFile); Object obj = parser.parse(new InputStreamReader(in));
    // JsonArray jsonArray = (JsonArray)obj;
    // if(jsonArray != null)
    // {
    // for(int j = 0; j < jsonArray.size(); ++j)
    // { // System.out.println(jsonArray.get(j).getAsJsonObject().get("blockName").getAsString()); System.out.println(jsonArray.get(j).getAsJsonObject().get("blockID").getAsInt() + " " + jsonArray.get(j).getAsJsonObject().get("blockName").getAsString());
    // addModsBlocks(modIDJsonFile.replace(".json", ""), jsonArray.get(j).getAsJsonObject().get("blockID").getAsInt(), jsonArray.get(j).getAsJsonObject().get("blockName").getAsString());
    // }
    // }
    // }
    // }
    // catch(Exception e)
    // {
    // e.printStackTrace();
    // }
    // }
    
    public static void checkIDConflict()
    {
        BufferedWriter writer = null;
        JsonParser parser = new JsonParser();
        try
        {
            setIsConflict(false);
            
            // Vanilla Blocks
            File inutJsonVanillaBlocks = new File(Util.getWorkingDirectory() + "/.data/config/json/vanilla.json");
            Object objectVanillaBlocks = parser.parse(new FileReader(inutJsonVanillaBlocks));
            JsonObject jsonObjectVanilla = (JsonObject)objectVanillaBlocks;
            JsonArray minecraftList = (JsonArray)jsonObjectVanilla.get("minecraft");
            
            // Custom Blocks ID
            File inutJsonModsBlocks = new File(Util.getWorkingDirectory() + "/.data/config/json/mods.json");
            Object objectModsBlocks = parser.parse(new FileReader(inutJsonModsBlocks));
            JsonObject jsonObjectMods = (JsonObject)objectModsBlocks;
            JsonArray modsList = (JsonArray)jsonObjectMods.get("mods");
            
            for(int i = 0; i < minecraftList.size(); ++i)
            {
                for(int j = 0; j < modsList.size(); j++)
                {
                    String modIDJsonFile = modsList.getAsJsonArray().get(j).getAsString();
                    
                    File inutJsonCustomBlocks = new File(Util.getWorkingDirectory() + "/.data/config/json/mods/" + modIDJsonFile);
                    Object obj = parser.parse(new FileReader(inutJsonCustomBlocks));
                    
                    JsonArray jsonArray = (JsonArray)obj;
                    if(jsonArray != null)
                    {
                        for(int k = 0; k < jsonArray.size(); ++k)
                        {
                            int blockModsID = jsonArray.get(k).getAsJsonObject().get("blockID").getAsInt();
                            setBlockModsIDConflict(blockModsID);
                            int blockVanillaID = i;
                            setBlockVanillaIDConflict(i);
                            
                            String blockModsName = jsonArray.get(k).getAsJsonObject().get("blockName").getAsString();
                            setBlockModsNameConflict(blockModsName);
                            
                            String blockVanillaName = minecraftList.get(i).getAsString();
                            setBlockVanillaNameConflict(blockVanillaName);
                            
                            if(getBlockVanillaIDConflict() == getBlockModsIDConflict())
                            {
                                setIsConflict(true);
                                System.out.println(blockVanillaID + " " + blockVanillaName + " -> " + blockModsID + " " + blockModsName);
                                if(isConflict())
                                {
                                    File logFile = new File(Util.getWorkingDirectory() + "/conflict-ID-Name.txt");
                                    writer = new BufferedWriter(new FileWriter(logFile));
                                    
                                    writer.write(blockVanillaID + " " + blockVanillaName + " -> " + blockModsID + " " + blockModsName + "\n");
                                    writer.flush();
                                }
                            }
                        }
                    }
                }
            }
            if(isConflict())
            {
                JOptionPane.showMessageDialog(null, "There are one or more ID conflict !", "IDs Conflict !", JOptionPane.WARNING_MESSAGE);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void addVanillaBlocks(int blockID, String name)
    {
        names.put((byte)blockID, "minecraft:" + name);
    }
    
    public static void addModsBlocks(String modid, int blockID, String name)
    {
        names.put((byte)blockID, modid + ":" + name);
    }
    
    public static String get(byte blockID)
    {
        return names.getOrDefault(blockID, blockID + "");
    }
    
    public static boolean isConflict()
    {
        return areConflict;
    }
    
    public static void setIsConflict(boolean conflict)
    {
        areConflict = conflict;
    }
    
    public static int getBlockModsIDConflict()
    {
        return blockModsIDConflict;
    }
    
    public static void setBlockModsIDConflict(int blockID)
    {
        blockModsIDConflict = blockID;
    }
    
    public static String getBlockModsNameConflict()
    {
        return blockModsNameConflict;
    }
    
    public static void setBlockModsNameConflict(String blockName)
    {
        blockModsNameConflict = blockName;
    }
    
    public static int getBlockVanillaIDConflict()
    {
        return blockVanillaIDConflict;
    }
    
    public static void setBlockVanillaIDConflict(int blockVanillaID)
    {
        blockVanillaIDConflict = blockVanillaID;
    }
    
    public static String getBlockVanillaNameConflict()
    {
        return blockVanillaNameConflict;
    }
    
    public static void setBlockVanillaNameConflict(String blockVanillaName)
    {
        blockVanillaNameConflict = blockVanillaName;
    }
}
