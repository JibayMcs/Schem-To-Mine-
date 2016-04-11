package org.jglrateis.schematic.core;

import static org.jglrateis.schematic.util.Translation.LANG;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import com.mojang.nbt.NBTCompoundTag;
import com.mojang.nbt.NBTTag;

import org.jglrateis.schematic.json.JSONBlocksNames;
import org.jglrateis.schematic.ui.CreditFrame;
import org.jglrateis.schematic.util.Util;
import org.jglrateis.schematic.util.Version;

/**
 * 
 * @authors jglrxavpok & ZeAmateis
 *          
 */

public class Converter extends JFrame
{
    private static final long serialVersionUID = 1L;
    
    private static File generatedFile;
    private static JTextArea resultArea;
    
    private JButton convertButton, creditsButton, generatedFileButton;
    private JCheckBox skipAirBlocks;
    private JTextField field;
    
    public Converter()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(InstantiationException e)
        {
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }
        if(!Util.getWorkingDirectory().exists())
        {
            Util.getWorkingDirectory().mkdir();
        }
        File dataFolder = new File(Util.getWorkingDirectory() + "./.data/");
        
        if(!dataFolder.exists())
            dataFolder.mkdir();
            
        new Version();
        JSONBlocksNames.checkIDConflict();
        JSONBlocksNames.init();
        buildFrame(this);
        
        this.setTitle("Schem' To Mine'");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(400, 162);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.pack();
        this.setResizable(false);
        
        URL iconURL = ClassLoader.getSystemResource("resources/img/icon.png");
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage(iconURL);
        this.setIconImage(img);
    }
    
    private void buildFrame(JFrame frame)
    {
        JPanel mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
        JPanel sourcePane = new JPanel();
        sourcePane.setLayout(new BoxLayout(sourcePane, BoxLayout.X_AXIS));
        field = new JTextField(30);
        field.setMaximumSize(field.getPreferredSize());
        sourcePane.add(field);
        JButton parse = new JButton(LANG.getTranslation("browse"));
        
        parse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileFilter()
            {
                @Override
                public boolean accept(File f)
                {
                    if(f.isDirectory())
                    {
                        return true;
                    }
                    final String name = f.getName();
                    return name.endsWith(".schematic") || name.endsWith(".SCHEMATIC");
                }
                
                @Override
                public String getDescription()
                {
                    return "*.schematic,*.SCHEMATIC";
                }
            });
            if(JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(mainPane))
            {
                File file = chooser.getSelectedFile();
                if(file.exists())
                {
                    field.setText(file.getAbsolutePath());
                }
            }
        });
        sourcePane.add(parse);
        
        resultArea = new JTextArea(10, 45);
        convertButton = new JButton(LANG.getTranslation("convert"));
        
        skipAirBlocks = new JCheckBox(LANG.getTranslation("convertAirBlock"), true);
        convertButton.addActionListener(e -> {
            
            String path = field.getText();
            File file = new File(path);
            
            if(file.exists() && (file.getName().indexOf(".") > 0 && file.getName().contains("schematic")))
            {
                
                String intList = "";
                String blockNameList = "";
                
                try
                {
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    this.setTitle(LANG.getTranslation("convertInProgress"));
                    
                    boolean convertAir = !skipAirBlocks.isSelected();
                    resultArea.setText("");
                    NBTCompoundTag tag = NBTTag.readCompoundFromFile(file);
                    
                    byte[] blocks = tag.getByteArray("Blocks");
                    byte[] blocksData = tag.getByteArray("Data");
                    
                    int width = tag.getShort("Width"); // X
                    int height = tag.getShort("Height"); // Y
                    int length = tag.getShort("Length"); // Z
                    
                    for(int x = 0; x < width; x++)
                    {
                        for(int y = 0; y < height; y++)
                        {
                            for(int z = 0; z < length; z++)
                            {
                                int address = (y * length + z) * width + x;
                                byte blockID = blocks[address];
                                byte blockMetadataID = blocksData[address];
                                
                                if(blockID == 0 && !convertAir)
                                    continue;
                                String block = JSONBlocksNames.get(blockID);
                                // String blockMetadata = JSONBlocksNames.get(blockMetadataID);
                                
                                // String method = "getBlockFromName";
                                
                                // if(block.equals(blockID + ""))
                                // {
                                // method = "getBlockFromId";
                                // quote = "";
                                // }
                                
                                intList = intList + "{" + x + "," + y + "," + z + "," + blockMetadataID + "},";
                                blockNameList = blockNameList + "\"" + block + "\",";
                                
                                // if(blockMetadataID != 0)
                                // {
                                // resultArea.append("world.setBlockState(pos.add(" + x + ", " + y + ", " + z + "), Block." + "getBlockFromName" + "(" + quote + block + quote + ").getStateFromMeta(" + blockMetadataID + "));\n ");
                                // }
                                // else
                                // resultArea.append("world.setBlockState(pos.add(" + x + ", " + y + ", " + z + "), Block." + method + "(" + quote + block + quote + ").getDefaultState());\n ");
                            }
                        }
                    }
                }
                catch(IOException e1)
                {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, LANG.getTranslation("errorFileLoad") + " " + e1.getLocalizedMessage(), LANG.getTranslation("error"), JOptionPane.ERROR_MESSAGE);
                }
                try
                {
                    // String intList = resultArea.getText();
                    
                    String fileName = file.getName();
                    
                    if(fileName.indexOf(".") > 0)
                    {
                        fileName = fileName.substring(0, fileName.lastIndexOf("."));
                        
                        File dir = new File(Util.getWorkingDirectory() + "/output/");
                        
                        if(!dir.exists() && dir.mkdir())
                            generatedFile = new File(Util.getWorkingDirectory() + "/output/" + fileName + ".java");
                            
                        generatedFile = new File(Util.getWorkingDirectory() + "/output/" + fileName + ".java");
                        
                        if(generatedFile.exists())
                            generatedFile.delete();
                        else
                            generatedFile.createNewFile();
                            
                        try
                        {
                            InputStream inputResourceFile = Converter.class.getClassLoader().getResourceAsStream("resources/pattern/WorldGen.pattern");
                            
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputResourceFile));
                            
                            FileWriter fileWriter = new FileWriter(generatedFile, true);
                            BufferedWriter bufferedWritter = new BufferedWriter(fileWriter);
                            
                            String aLine = null;
                            while((aLine = bufferedReader.readLine()) != null)
                            {
                                if(!aLine.contains("#ClassName") && !aLine.contains("#intList") && !aLine.contains("#Version") && !aLine.contains("#blockNameList"))
                                {
                                    bufferedWritter.write(aLine);
                                    bufferedWritter.newLine();
                                }
                                if(aLine.contains("#ClassName"))
                                {
                                    bufferedWritter.write(aLine.replaceAll("#ClassName", fileName));
                                    bufferedWritter.newLine();
                                }
                                if(aLine.contains("#intList"))
                                {
                                    try
                                    {
                                        bufferedWritter.write(aLine.replaceAll("#intList", intList).replace(",};", "};"));
                                        bufferedWritter.newLine();
                                    }
                                    catch(OutOfMemoryError ex)
                                    {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(null, LANG.getTranslation("outOfMemory"), LANG.getTranslation("error"), JOptionPane.ERROR);
                                    }
                                }
                                if(aLine.contains("#blockNameList"))
                                {
                                    try
                                    {
                                        bufferedWritter.write(aLine.replaceAll("#blockNameList", blockNameList).replace(",};", "};"));
                                        bufferedWritter.newLine();
                                    }
                                    catch(OutOfMemoryError ex)
                                    {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(null, LANG.getTranslation("outOfMemory"), LANG.getTranslation("error"), JOptionPane.ERROR);
                                    }
                                }
                                if(aLine.contains("#Version"))
                                {
                                    bufferedWritter.write(aLine.replaceAll("#Version", Version.VERSION));
                                    bufferedWritter.newLine();
                                }
                            }
                            
                            bufferedReader.close();
                            bufferedWritter.close();
                            
                            this.setCursor(null);
                            this.setTitle(".Schematics To Java Converter");
                            
                            JOptionPane.showMessageDialog(null, LANG.getTranslation("successFileGen"), LANG.getTranslation("success"), JOptionPane.INFORMATION_MESSAGE);
                        }
                        catch(Exception ex1)
                        {
                            ex1.printStackTrace();
                            JOptionPane.showMessageDialog(null, LANG.getTranslation("errorFileGen") + " " + ex1.getLocalizedMessage(), LANG.getTranslation("error"), JOptionPane.ERROR_MESSAGE);
                        }
                        
                    }
                }
                catch(IOException ex)
                {
                    ex.printStackTrace();
                }
            }
            else
                JOptionPane.showMessageDialog(null, LANG.getTranslation("notASchematic"), LANG.getTranslation("error"), JOptionPane.ERROR_MESSAGE);
        });
        
        creditsButton = new JButton(LANG.getTranslation("credits"));
        creditsButton.setAlignmentX(frame.CENTER_ALIGNMENT);
        creditsButton.setAlignmentY(frame.CENTER_ALIGNMENT);
        creditsButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    new CreditFrame();
                }
                catch(Exception ex)
                {}
            }
        });
        
        JPanel twitterPanel = new JPanel();
        twitterPanel.setLayout(new BoxLayout(twitterPanel, BoxLayout.X_AXIS));
        twitterPanel.setAlignmentX(frame.CENTER_ALIGNMENT);
        twitterPanel.setAlignmentY(frame.CENTER_ALIGNMENT);
        twitterPanel.add(creditsButton);
        
        mainPane.add(sourcePane);
        JPanel convertPane = new JPanel();
        convertPane.add(convertButton);
        convertPane.add(skipAirBlocks);
        mainPane.add(convertPane);
        
        generatedFileButton = new JButton(LANG.getTranslation("openGeneratedFile"));
        generatedFileButton.setAlignmentX(mainPane.getAlignmentX());
        generatedFileButton.addActionListener(e -> {
            try
            {
                if(generatedFile != null)
                    Desktop.getDesktop().open(new File(generatedFile.getAbsolutePath()));
                else
                    JOptionPane.showMessageDialog(null, LANG.getTranslation("noFileGenerated"), LANG.getTranslation("warning"), JOptionPane.WARNING_MESSAGE);
            }
            catch(java.io.IOException ex)
            {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, LANG.getTranslation("errorOpeningFile") + " " + ex.getLocalizedMessage(), LANG.getTranslation("error"), JOptionPane.ERROR_MESSAGE);
            }
        });
        
        mainPane.add(generatedFileButton);
        mainPane.add(twitterPanel);
        
        mainPane.setDropTarget(new DropTarget()
        {
            private static final long serialVersionUID = -1L;
            
            public synchronized void drop(DropTargetDropEvent event)
            {
                try
                {
                    event.acceptDrop(DnDConstants.ACTION_COPY);
                    @SuppressWarnings("unchecked")
                    List<File> droppedFiles = (List<File>)event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for(File file : droppedFiles)
                    {
                        System.out.println(file.getName());
                        if(file.getName().indexOf(".") > 0 && file.getName().contains("schematic"))
                        {
                            System.out.println(file);
                            field.setText(file.getAbsolutePath());
                        }
                        else
                            JOptionPane.showMessageDialog(null, LANG.getTranslation("notASchematic"), LANG.getTranslation("information"), JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, LANG.getTranslation("errorDragDrop") + " " + ex.getLocalizedMessage(), LANG.getTranslation("error"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        frame.add(mainPane);
    }
}