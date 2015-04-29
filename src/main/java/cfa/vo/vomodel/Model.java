/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.vomodel;

import java.net.URL;
import java.util.List;

/**
 *
 * @author mdittmar
 */
public interface Model {
    
    /**
     * Returns the title string associated with this model
     * 
     * @return Data Model title.
     */
    public String getTitle();
    /**
     * Returns the defined name string for the model, typically in format 
     * "[name]-[version].[subversion]"
     * 
     * @return Data Model name.
     */
    public String getModelName();
    /**
     * Returns the defined prefix string for the model.  The prefix string is 
     * used as part of the Utype to indicate which model the element is from.
     * 
     * @return Model prefix.
     */
    public String getPrefix();
    /**
     * Returns the {@link URL} pointer to the location containing reference 
     * information about the model.  This may be the location of the XML Schema
     * or other resource.
     * 
     * @return URL pointer to reference information.
     */
    public URL getReferenceURL();

    /**
     * Returns a numerical index of the model record corresponding to the 
     * specified Utype.  This index may be used to retrieve other fields 
     * associated with that model record.
     * 
     * @param utype  Utype of interest.
     * @return Numerical index of the corresponding model record.
     */
    public Integer getRecordIndex( Utype utype );

    /**
     * Returns a numerical index of the model record corresponding to the 
     * specified model path.  This index may be used to retrieve other fields 
     * associated with that model record.
     * 
     * @param modelpath  Model path of interest.
     * @return Numerical index of the corresponding model record.
     */
    public Integer getRecordIndexByPath( String modelpath );

    /**
     * Returns a numerical index of the model record corresponding to the 
     * specified Utype label string.  This index may be used to retrieve other
     * fields associated with that model record.
     * 
     * @param label  Utype label string of interest (with or without prefix).
     * @return Numerical index of the corresponding model record.
     */
    public Integer getRecordIndexByTag( String label );

    /**
     * Returns the Utype from the specified model record.
     * 
     * @param utypenum Numerical index of desired model record.
     * @return Utype for that record
     * @thows IllegalArgumentException if record does not exist.
     */
    public Utype getUtype( Integer utypenum );
    /**
     * Return the default Unit field from the specified model record.
     * 
     * @param utypenum Numerical index of desired model record.
     * @return Default unit string or empty string if none.
     * @thows IllegalArgumentException if record does not exist.
     */
    public String getUnit( Integer utypenum );
    public String getUnit( Utype utype );
    /**
     * Return the default UCD field from the specified model record.
     * 
     * @param utypenum Numerical index of desired model record.
     * @return Default UCD string or empty string if none.
     * @thows IllegalArgumentException if record does not exist.
     */
    public String getUCD( Integer utypenum );
    public String getUCD( Utype utype );
    /**
     * Return the type field from the specified model record.  The type field 
     * is generally the simple name of the class or data type of the model 
     * element.
     * 
     * @param utypenum Numerical index of desired model record.
     * @return Type string of model element.
     * @thows IllegalArgumentException if record does not exist.
     */
    public String getType( Integer utypenum );
    public String getType( Utype utype );
    /**
     * Return the default value from the specified model record.  This is a 
     * string representation of the default value for that model element.
     * 
     * @param utypenum Numerical index of desired model record.
     * @return Default value string of model element or empty string if none.
     * @thows IllegalArgumentException if record does not exist.
     */
    public String getDefault( Integer utypenum );
    public String getDefault( Utype utype );
    /**
     * Return the description value from the specified model record.
     * 
     * @param utypenum Numerical index of desired model record.
     * @return Description string of model element or empty string if none.
     * @thows IllegalArgumentException if record does not exist.
     */
    public String getDescription( Integer utypenum );
    public String getDescription( Utype utype );
    /**
     * Returns Boolean flag indicating whether or not the model defines the 
     * element as mandatory.
     * 
     * @param utypenum Numerical index of desired model record.
     * @return true if mandatory; otherwise false.
     * @thows IllegalArgumentException if record does not exist.
     */
    public Boolean isMandatory( Integer utypenum );
    public Boolean isMandatory( Utype utype );
    
    /**
     * Evaluates the provided Utype and returns true if it matches
     * a record in the model.  A match is determined by comparing 
     * the Utype model path, tag, and prefix attributes.  The 
     * comparison is case-insensitive.
     * 
     * @param utype Utype to be evaluated
     * @return true if match is found; otherwise false.
     */
    public Boolean isValidUtype( Utype utype );
    
    /**
     * Return the complete list of Utypes associated with the model.
     * 
     * @return Utypes associated with the model.
     */
    public List<Utype> getUtypes();

}
