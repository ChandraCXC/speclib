/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

import uk.ac.starlink.votable.FieldElement;

/**
 *  Simple Container for Column data
 * 
 * @author mdittmar
 */
public class Column {
    public FieldElement info;
    public String[] data;
    
    public Column()
    {
        info = null;
        data = null;
    }
}
