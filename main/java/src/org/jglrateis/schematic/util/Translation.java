package org.jglrateis.schematic.util;

import java.util.Locale;

import utybo.minkj.locale.MinkJ;

public class Translation
{
    public static final MinkJ LANG = new MinkJ();
    
    public static void init()
    {
        try
        {
            LANG.loadTranslationsFromFile(Locale.FRENCH, Translation.class.getClassLoader().getResourceAsStream("resources/lang/FR.lang"));
            LANG.loadTranslationsFromFile(Locale.ENGLISH, Translation.class.getClassLoader().getResourceAsStream("resources/lang/EN.lang"));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}