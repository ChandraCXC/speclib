package cfa.vo.speclib;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * This is generic class to describe physical values.  
 * It holds the value itself, plus associated metadata such as 
 * ID, unit, ucd, and description.  
 * 
 * A VetoableChangeSupport object is assigned to the quantity in 
 * order to validate and control changes to the Quantity properties.
 */

//TODO:  add property change control
//TODO:  what should action of 'get' be for this object? return null,
//TODO:  instantiate an empty instance, or instantiate a 'default' value?
//TODO:  define hash code
//TODO:  equals()?

/**
 * @author mdittmar
 */

public class Quantity<T> {
    private String modelpath;
    private Class dtype;
    
    private String id;
    
    private String name;
    private String description;
    private String unit;
    private String ucd;
    private String utype;
    
    private T value;
    
    public Quantity()
    {
        this(null, null, null, null);
    }
    
    public Quantity( T value )
    {
        this(null, value, null, null );
    }

    public Quantity( String name, T value, String unit, String ucd )
    {
        this.modelpath = null;
        this.id = null;
        
        this.name = name;
        this.description = null;
        this.value = value;
        this.unit = unit;
        this.ucd = ucd;
         this.utype = null;
        if ( value != null )
          this.dtype = value.getClass();
        else
          this.dtype = null;
}

    // Accessor Methods (Get)

    /**
     * Get the value of the model path property.
     * 
     * @return
     *     either null or
     *     {@link String }
     */
    public String getModelpath()
    {
        return this.modelpath;
    }
    /**
     * Get the value of the id property.
     * 
     * @return
     *     either null or
     *     {@link String }
     */
    public String getID()
    {
        return this.id;
    }
    /**
     * Get the value of the name property.
     * 
     * @return
     *     either null or
     *     {@link String }
     */
    public String getName()
    {
        return this.name;
    }
    /**
     * Get the value of the description property.
     * 
     * @return
     *     either null or
     *     {@link String }
     */
    public String getDescription()
    {
        return this.description;
    }
    /**
     * Get the value of the unit property.
     * 
     * @return
     *     either null or
     *     {@link String }
     */
    public String getUnit()
    {
        return this.unit;
    }
    /**
     * Get the value of the ucd property.
     * 
     * @return
     *     either null or
     *     {@link String }
     */
    public String getUCD()
    {
        return this.ucd;
    }
    /**
     * Get the value of the utype property.
     * 
     * @return
     *     either null or
     *     {@link String }
     */
    public String getUtype()
    {
        return this.utype;
    }
    /**
     * Get the value of the value property.
     * 
     * @return
     *     either null or value
     */
    public T getValue()
    {
        return this.value;
    }

    // Accessor Methods (Set)
    
    /**
     * Set the model path property.
     * 
     * @param mp
     *     allowed object is
     *     {@link String }
     */
    public void setModelpath( String mp )
    {
        this.modelpath = mp;
    }
    /**
     * Set the ID property.
     * 
     * @param id
     *     allowed object is
     *     {@link String }
     */
    public void setID( String id )
    {
        this.id = id;
    }
    /**
     * Set the name property.
     * 
     * @param name
     *     allowed object is
     *     {@link String }
     */
    public void setName( String name )
    {
        this.name = name;
    }
    /**
     * Set the description property.
     * 
     * @param description
     *     allowed object is
     *     {@link String }
     */
    public void setDescription( String description )
    {
        this.description = description;
    }
    /**
     * Set the unit property.
     *   Input unit string must be compliant with VOUnit-1.0 standard.
     * 
     * @param unit
     *     allowed object is
     *     {@link String }
     */
    public void setUnit( String unit )
    {
        this.unit = unit;
    }
    /**
     * Set the UCD property.
     * 
     * @param ucd
     *     allowed object is
     *     {@link String }
     */
    public void setUCD( String ucd )
    {
        this.ucd = ucd;
    }
    /**
     * Set the utype property.
     * 
     * @param utype
     *     allowed object is
     *     {@link String }
     */
    public void setUtype( String utype )
    {
        this.utype = utype;
    }
    /**
     * Set the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     {@link Double }
     *     {@link Integer }
     */
    public void setValue( T inval )
    {
        // If value is set, preserve the data type
        if ( this.dtype == null )
            this.dtype = inval.getClass();
        
        if ( inval.getClass().equals(this.dtype) )
            this.value = inval;
        else if ( this.dtype.isArray() && this.dtype.getComponentType().equals(inval.getClass()) )
        {
          // Source is Scalar of same type as this Array.. load array from scalar value.
          Object arr = Array.newInstance( inval.getClass(), 1 );
          Array.set( arr, 0, inval );
          this.setValue((T)arr );
        }
        else
            throw new IllegalArgumentException("Value must be of type "+this.dtype.getSimpleName()+" on "+this.modelpath);
    }
   
    /**
     * Fill content of this Quantity object with the content of the provided
     * source Quantity.  Facilitates copying a Quantity to an existing object.
     * 
     * @param src
     *    Source Quantity whose content is to be copied.
     */
    public void fill( Quantity src )
    {
        if (src.hasID())
          this.id = src.getID();
        if (src.hasDescription())
          this.description = src.getDescription();
        if (src.hasModelpath())
          this.modelpath = src.getModelpath();
        if (src.hasName())
          this.name = src.getName();
        if (src.hasUCD())
          this.ucd = src.getUCD();
        if (src.hasUnit())
          this.unit = src.getUnit();
        if (src.hasUtype())
          this.utype = src.getUtype();
        if (src.hasValue())
          this.setValue( (T)src.getValue() );
    }
    
    public boolean hasModelpath()
    {
        return (this.modelpath != null);
    }    
    public boolean hasID()
    {
        return (this.id != null);
    }    
    public boolean hasName()
    {
        return (this.name != null);
    }    
    public boolean hasDescription()
    {
        return (this.description != null);
    }    
    public boolean hasUnit()
    {
        return (this.unit != null);
    }    
    public boolean hasUCD()
    {
        return (this.ucd != null);
    }    
    public boolean hasUtype()
    {
        return (this.utype != null);
    }    
    public boolean hasValue()
    {
        return (this.value != null);
    }    

    public String toSimpleString()
    {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(String.format("%-22s", "Quantity<"+this.getDTypeStr()+">: "));
        result.append(this.name).append("\t");
        result.append( this.getValStr() ).append("\t");
        result.append(this.unit).append("\t");
        result.append(this.ucd).append("\t");
        result.append(this.utype);

        return result.toString();
    }

    private String getDTypeStr()
    {
        String result;
        if( this.dtype == null )
            result = "null";
        else
            result = this.dtype.getSimpleName();

        return result;
    }
    private String getValStr()
    {
        String valstr;

        if ( this.value == null )
            valstr = "null";
        else if ( this.value.getClass().isArray() )
            valstr = Arrays.toString( (T[])this.value );
        else
            valstr = this.value.toString();

        return valstr;
    }
    //TODO: I would prefer this were not public.. but need SpectralProxy to call it.. maybe a Constructor? Quantity( Class type ) ?? 
    /**
     * Assign the internal datatype.  Enables type control on Generic Class.
     * The type may be the 'expected' type of the value property.
     * 
     * @param type 
     *     {@link Class }
     */
    public void setDType( Class type )
    {
        this.dtype = type;
    }
    
    /**
     * For Debugging:
     * Generate a string representation of the quantity
     *
     * @return 
     *     {@link String }
     */
    @Override public String toString()
    {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append("Quantity<").append(this.getDTypeStr()).append(">").append(newLine);
        result.append("  Model Path: ").append(this.modelpath).append(newLine);
        result.append("  ID: ").append(this.id).append(newLine);
        result.append("  Name: ").append(this.name).append(newLine);
        result.append("  Desc: ").append(this.description).append(newLine);
        result.append("  Unit: ").append(this.unit).append(newLine);
        result.append("  UCD: ").append(this.ucd).append(newLine);
        result.append("  UType: ").append(this.utype).append(newLine);
        result.append("  Value: ").append(this.getValStr()).append(newLine);
        return result.toString();
    }
    
      
    
}
