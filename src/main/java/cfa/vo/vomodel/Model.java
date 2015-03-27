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
     * Set configuration flag indicating whether or not the model prefix text 
     * is to be included in the Utype string handling.  This applies to both the
     * Utype strings being returned by methods and those being received as arguments.
     * 
     * The default setting is to include the prefix (true).
     * 
     * @param flag true=include; false=exclude
     */
    public void setIncludeModelPrefix( boolean flag );
    
    /**
     * Returns the defined name string for the model, typically in format 
     * "[name]-[version].[subversion]"
     * 
     * @return Data Model name.
     */
    public String getModelName();
    /**
     * Returns the defined prefix string for the model.  The prefix string is 
     * generally used as part of the Utype to indicate which model the element 
     * is from.
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
     * specified Utype string.  This index may be used to retrieve other fields 
     * associated with that model record.
     * <p>
     * The input Utype may or may not include the model prefix, but the user 
     * should be sure the configuration flag is set accordingly.
     * 
     * @see setIncludeModelPrefix()
     * @param utype  Utype string of interest.
     * @return Numerical index of the corresponding model record.
     */
    public Integer getUtypeNum( String utype );
    /**
     * Returns the Utype field from the specified model record.
     * 
     * @param utypenum Numerical index of desired model record.
     * @return Utype string for that record
     * @thows IllegalArgumentException if record does not exist.
     */
    public String getUtype( Integer utypenum );
    /**
     * Return the default Unit field from the specified model record.
     * 
     * @param utypenum Numerical index of desired model record.
     * @return Default unit string or empty string if none.
     * @thows IllegalArgumentException if record does not exist.
     */
    public String getUnit( Integer utypenum );
    public String getUnit( String utype );
    /**
     * Return the default UCD field from the specified model record.
     * 
     * @param utypenum Numerical index of desired model record.
     * @return Default UCD string or empty string if none.
     * @thows IllegalArgumentException if record does not exist.
     */
    public String getUCD( Integer utypenum );
    public String getUCD( String utype );
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
    public String getType( String utype );
    /**
     * Return the default value from the specified model record.  This is a 
     * string representation of the default value for that model element.
     * 
     * @param utypenum Numerical index of desired model record.
     * @return Default value string of model element or empty string if none.
     * @thows IllegalArgumentException if record does not exist.
     */
    public String getDefault( Integer utypenum );
    public String getDefault( String utype );
    /**
     * Returns Boolean flag indicating whether or not the model defines the 
     * element as mandatory.
     * 
     * @param utypenum Numerical index of desired model record.
     * @return true if mandatory; otherwise false.
     * @thows IllegalArgumentException if record does not exist.
     */
    public Boolean isMandatory( Integer utypenum );
    public Boolean isMandatory( String utype );
    
    /**
     * Evaluates the provide Utype string and returns true if the string matches
     * a record in the model.  By default, the model prefix is expected to be 
     * included in the Utype string.  This may be changed by using the 
     * setIncludeModelPrefix() method. 
     * <p>
     * Comparison is case-insensitive.
     * 
     * @param utype Utype string to be evaluated
     * @return true if match is found; otherwise false.
     */
    public Boolean isValidUtype( String utype );
    
    /**
     * Return the complete list of Utype strings associated with the model.
     * By default, the model prefix string will be included. 
     * @return Utype strings associated with the model.
     */
    public List<String> getUtypes();
    
}
