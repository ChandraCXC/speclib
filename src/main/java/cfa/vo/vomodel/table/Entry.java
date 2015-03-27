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
    public String type;      // Name of data or class type
    public String utype;
    public String unit;
    public String ucd;
    public String defval;
    public String req;       // MAN|REC|OPT
    public String descr;     // Description
}
