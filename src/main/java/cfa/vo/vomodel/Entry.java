/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.vomodel;

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

    public Entry() {

    }

    public Entry(String modelpath,
                 String type,
                 String mult,
                 String tag,
                 String unit,
                 String ucd,
                 String defval,
                 String descr) {
        this.modelpath = modelpath;
        this.type = type;
        this.mult = mult;
        this.tag = tag;
        this.unit = unit;
        this.ucd = ucd;
        this.defval = defval;
        this.descr = descr;
    }

    public Entry (Entry entry) {
        this.modelpath = entry.modelpath;
        this.type = entry.type;
        this.mult = entry.mult;
        this.tag = entry.tag;
        this.unit = entry.unit;
        this.ucd = entry.ucd;
        this.defval = entry.defval;
        this.descr = entry.descr;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry)) return false;

        Entry entry = (Entry) o;

        if (defval != null ? !defval.equals(entry.defval) : entry.defval != null) return false;
        if (descr != null ? !descr.equals(entry.descr) : entry.descr != null) return false;
        if (!modelpath.equals(entry.modelpath)) return false;
        if (mult != null ? !mult.equals(entry.mult) : entry.mult != null) return false;
        if (!tag.equals(entry.tag)) return false;
        if (!type.equals(entry.type)) return false;
        if (ucd != null ? !ucd.equals(entry.ucd) : entry.ucd != null) return false;
        if (unit != null ? !unit.equals(entry.unit) : entry.unit != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = modelpath.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + (mult != null ? mult.hashCode() : 0);
        result = 31 * result + tag.hashCode();
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (ucd != null ? ucd.hashCode() : 0);
        result = 31 * result + (defval != null ? defval.hashCode() : 0);
        result = 31 * result + (descr != null ? descr.hashCode() : 0);
        return result;
    }
}
