/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

/**
 *  ModeledElement interface.
 *  Defines methods for tagging objects with a model path, indicating the 
 *  object's location within a data model hierarchy.
 * 
 *  The model path string should never be null.  The interface defines a
 *  constant MP_UNDEFINED to indicate that the model path has not yet been
 *  assigned.
 * 
 * @author mdittmar
 */
public interface ModeledElement {

    public final static String MP_UNDEFINED = "UNKNOWN";
    
   // ModeledElement Interface Methods
   /**
     * Extract the model path associated with this Object.
     * 
     * @return
     *   model path assigned to the node.
     */
   public String getModelpath();
   
   /**
     * Assign model path to this Object.
     * 
     * Propagates the new base to Object contents as needed.
     * 
     * @param mp
     *   model path string. Must not be null or empty.
     * 
     * @exception
     *   IllegalArgumentException if input model path string is null or empty.
     */
   public void setModelpath( String mp );
   
   
    /**
     * Re-base the model path associated with this Object.
     * Extracts the last node of the current model path and prepends the 
     * input base to define a new path for this Object.
     * 
     * Propagates the new base to Object contents as needed.
     * 
     * @param base
     *   new base for element. A null base strips any path to the element, 
     *   leaving just the last node of current element path.
     */
   public void rebaseModelpath( String base );

}
