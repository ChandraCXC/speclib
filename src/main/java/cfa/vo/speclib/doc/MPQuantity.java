/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.Quantity;

/**
 *  Extends the Quantity class to implement the ModeledElement interface.
 *  This tags the Quantity with a structured path representing the location
 *  of the instance within a data model node hierarchy.
 * 
 *  Quantity Types are expected to be base types, so no propagation of
 *  model path is needed.
 * 
 * @author mdittmar
 */
public class MPQuantity<T> extends Quantity<T> implements ModeledElement {
    
    private String modelpath;
    
    public MPQuantity()
    {
        this(null, null, null, null);
    }
    
    public MPQuantity( T value )
    {
        this(null, value, null, null );
    }

    public MPQuantity( String name, T value, String unit, String ucd )
    {
        super(name, value, unit, ucd );
        this.modelpath = ModeledElement.MP_UNDEFINED;
    }
    
    // ModeledElement interface methods
    @Override
    public String getModelpath()
    {
        return this.modelpath;
    }
    
    @Override
    public void setModelpath( String mp )
    {
        if ( mp == null || mp.isEmpty())
            throw new IllegalArgumentException("Model path must not be empty or null.");
        
        this.modelpath = mp;
    }
    
    @Override
    public void rebaseModelpath( String base )
    {
        // extract last node of existing model path.
        String last = this.modelpath.substring(this.modelpath.lastIndexOf("_")+1);
        if ( base == null )
            this.modelpath = last;
        else
            this.modelpath = base + "_" + last;
    }
    
    /**
     * Fill content of this MPQuantity object with the content of the provided
     * source MPQuantity.  Facilitates copying a Quantity to an existing object.
     * 
     * @param src
     *    Source Quantity whose content is to be copied.
     */
    public void fill( MPQuantity src )
    {
        this.modelpath = src.getModelpath();
        this.fill( (Quantity)src );
    }
    
     /**
     * Generate a string representation of the quantity
     *
     * @return 
     *     {@link String }
     */
    @Override public String toString()
    {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append("MPQuantity<").append(this.getDTypeStr()).append(">").append(newLine);
        result.append("  Model Path: ").append(this.modelpath).append(newLine);
        result.append("  ID: ").append(this.getID()).append(newLine);
        result.append("  Name: ").append(this.getName()).append(newLine);
        result.append("  Desc: ").append(this.getDescription()).append(newLine);
        result.append("  Unit: ").append(this.getUnit()).append(newLine);
        result.append("  UCD: ").append(this.getUCD()).append(newLine);
        result.append("  UType: ").append(this.getUtype()).append(newLine);
        result.append("  Value: ").append(this.getValStr()).append(newLine);
        return result.toString();
    }

}
