/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

import java.util.ArrayList;

/**
 *
 * @author mdittmar
 */
public class MPArrayList<T> extends ArrayList<T> {
    
    private String modelpath;
    
    // Accessor methods
    
    /**
     * Return Model Path string.
     * 
     * @return
     */
    public String getModelpath()
    {
        return this.modelpath;
    }
    
    /**
     * Set the model path property.
     * 
     * @param mp
     */
    public void setModelpath( String mp )
    {
        this.modelpath = mp;
    }

}
