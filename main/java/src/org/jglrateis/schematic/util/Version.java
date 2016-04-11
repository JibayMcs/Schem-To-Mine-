package org.jglrateis.schematic.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Version
{
    public static final String VERSION = "0.8";
    
    public Version()
    {
        try
        {
            File file = new File(Util.getWorkingDirectory() + "/.data/version.txt");
            
            if(!file.exists())
            {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(VERSION);
            bw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}