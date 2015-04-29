/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.vomodel;

/**
 *  The Utype is defined as "a pointer into a data model".  It is a string
 *  indicating which element of a data model an object represents.  This tag
 *  is to be considered an arbitrary string, but is often assigned a value 
 *  which roughly corresponds to the model path.
 * 
 *  In serializations, the utype expression consists of 2 parts
 *    1) the model prefix
 *    2) the tag for the model element
 *  combined in the form
 *    "<prefix>:<tag>"
 * 
 *  This class forms a bridge connecting the model paths to their corresponding
 *  utype prefix and tag strings.
 * 
 * @author mdittmar
 */
public class Utype {
 
    private String mp;      // model path
    private String tag;     // utype tag
    private String prefix;  // model prefix

    // Constructor
    public Utype( String modelpath, String tag, String prefix )
    {
        String errmsg = null;
        if ( modelpath == null || modelpath.trim().isEmpty() )
            errmsg = "modelpath param may not be null or empty.";
        else if ( tag == null || tag.trim().isEmpty() )
            errmsg = "tag param may not be null or empty.";
        else if ( prefix == null || prefix.trim().isEmpty() )
            errmsg = "prefix param may not be null or empty.";
        if ( errmsg != null )
            throw new IllegalArgumentException( errmsg );
        
        this.mp = modelpath;
        this.tag = tag;
        this.prefix = prefix;
    }

    // ******   Accessors/Mutators   ******
    /**
     *  Accessor to the modelpath attribute.
     * 
     * @return
     *    The value of the modelpath attribute.
     */
    public String getModelPath()
    {
        return this.mp;
    }
    
    /**
     *  Accessor to the tag attribute.
     *  The resulting string is formated as:
     *     "<prefix>:<tag>"
     * 
     * @return
     *    The value of the tag attribute with prefix attached.
     */
    public String getTag()
    {
        String result = this.prefix + ":" + this.tag;

        return result;
    }
    
    /**
     *  Accessor to the tag attribute.
     *  The resulting string does not include the model prefix.
     *     "<tag>"
     * 
     * @return
     *    The value of the tag attribute without prefix attached.
     */
    public String getTagNoPrefix()
    {
        return this.tag;
    }
    
    // ******   Methods   ******    
    /**
     *  Performs case insensitive comparison of the input model path string
     *  against the Utype model path attribute.  null input returns false.
     * 
     * @param modelpath
     *    Input model path to be checked for match.
     * 
     * @return
     *    true if the input param is not null and matches the Utype attribute;
     *    false otherwise
     */
    public boolean matchModelPath( String modelpath )
    {
        boolean result;
        
        result = this.mp.equalsIgnoreCase(modelpath);
        
        return result;
    }
    
    /**
     * Performs case insensitive comparison of the input label against the
     * Utype definition.  Where the label is comprised of an optional 
     * prefix string, followed by a colon character (:), followed by the 
     * tag string.  A null input returns false.
     *    ie:  [<prefix>:]<tag>
     * 
     * @param label
     *    Label string to be compared.
     * 
     * @return
     *    true if the label is not null and matches the Utype definition;
     *    false otherwise
     */
    public boolean matchTag( String label )
    {
        boolean result = false;
        String tmpstr;
        
        if ( label != null )
        {
          // Strip any 'prefix' from input label, case-insensitive.
          tmpstr = label.toLowerCase().replaceFirst( this.prefix.toLowerCase().concat(":"),"");

          // Compare result against Utype tag..
          result = this.tag.equalsIgnoreCase( tmpstr );
        }
        
        return result;
    }
    
    /**
     * Two Utype objects are considered 'equal' if the values of their 
     * model path, tag and prefix attributes are equivalent.  In other 
     * words, if they contain equivalent model path and label expressions
     * as determined by a case-insensitive match of these strings.
     * 
     * @param another
     *    Utype object to check for equality
     * 
     * @return
     *    true if object is not null the content is equivalent;
     *    false otherwise
     */
    @Override
    public boolean equals( Object another )
    {
        boolean result = false;
        if(!(another instanceof Utype)) return false;
        Utype b = (Utype)another;

        // Build full label expression to compare.
        // Does not use getTag since flag may be false and we do not
        // want to change any settings.
        String tmpstr = this.prefix.concat(":").concat(this.tag);
        if ( this.matchModelPath(b.getModelPath()) && b.matchTag( tmpstr ) )
        {
          result = true;
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.mp != null ? this.mp.hashCode() : 0);
        hash = 37 * hash + (this.tag != null ? this.tag.hashCode() : 0);
        hash = 37 * hash + (this.prefix != null ? this.prefix.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String fmt = "   %s = '%s'";

        result.append("Utype:").append(newLine);
        result.append(String.format( fmt, "path", this.mp )).append(newLine);
        result.append(String.format( fmt, "tag ", this.tag )).append(newLine);
        result.append(String.format( fmt, "prefix", this.prefix )).append(newLine);
        
        return result.toString();
    }
}
