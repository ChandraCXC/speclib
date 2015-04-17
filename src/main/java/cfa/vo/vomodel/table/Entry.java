/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.vomodel.table;

/**
 *  Class to hold a single record from a VO Data Model description file.
 * 
 * @author mdittmar
 */
public class Entry {
    public String modelpath; // Model path to element
    public String type;      // Data or Class Type as string of model element
    public String mult;      // Multiplicity indicator
    public String tag;       // Tag string for element (utype basis)
    public String unit;      // Default unit
    public String ucd;       // Default ucd
    public String defval;    // Default value
    public String descr;     // Description

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String fmt = "   %s = '%s'";

        result.append("Entry:").append(newLine);
        result.append(String.format( fmt, "path", modelpath )).append(newLine);
        result.append(String.format( fmt, "type", type )).append(newLine);
        result.append(String.format( fmt, "mult", mult )).append(newLine);
        result.append(String.format( fmt, "tag ", tag )).append(newLine);
        result.append(String.format( fmt, "unit", unit )).append(newLine);
        result.append(String.format( fmt, "ucd ", ucd )).append(newLine);
        result.append(String.format( fmt, "val ", defval )).append(newLine);
        result.append(String.format( fmt, "desc", descr ));
        
        return result.toString();
    }
}
