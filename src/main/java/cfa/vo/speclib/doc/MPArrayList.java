/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;

/**
 *  Extends the ArrayList class to implement the ModeledElement interface.
 *  This tags the ArrayList with a structured path representing the location
 *  of the instance within a data model node hierarchy.
 * 
 *  Contents of the MPArrayList may be of any type (T).
 *  If the type also implements the ModeledElement interface, the model path
 *  specification for the content is maintained/updated in coordination with
 *  updates to the MPArrayList.
 * 
 * @author mdittmar
 */
public class MPArrayList<T> extends ArrayList<T> implements ModeledElement {
    
    private String modelpath;

    // Constructors
    public MPArrayList()
    {
        super();
        this.modelpath = ModeledElement.MP_UNDEFINED;
    }
    
    public MPArrayList(int i) 
    {
        super( i );
        this.modelpath = ModeledElement.MP_UNDEFINED;
    }

    public MPArrayList(Collection<? extends T> clctn) 
    {
        super( clctn );
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

        // Set path and propagate to array content.
        this.modelpath = mp;
        this.rebaseContent();
    }
    
    /**
     * Re-base the model path associated with this Object.
     * Extracts the last node of the current model path and prepends the 
     * input base to define a new path for this Object.
     * 
     * Propagates the new base to ArrayList contents if they also implement
     * the ModeledElement interface.
     * 
     * @param base
     *   new base for element. A null base strips any path to the element, 
     *   leaving just the last node of current element path.
     */
    @Override
    public void rebaseModelpath( String base )
    {
        // Extract last node of existing model path.
        String last = this.modelpath.substring(this.modelpath.lastIndexOf("_")+1);
        String newpath;
        if ( base == null )
            newpath = last;
        else
            newpath = base + "_" + last;

        // reset path for the array, and propagate to contents.
        this.setModelpath( newpath );
    }

    // ArrayList methods

    @Override
    public boolean add( T obj )
    {
        this.updateModelpath(obj, this.modelpath+"[]");
        return super.add(obj);
    }
    
    @Override
    public void add(int i, T obj )
    {
        this.updateModelpath(obj, this.modelpath+"[]");
        super.add(i, obj);
    }
    
    @Override
    public boolean addAll( Collection<? extends T> clctn )
    {
        // TODO: Implement?
        throw new UnsupportedOperationException("Method not yet supported for MPArrayList class.");
    }
    @Override
    public boolean addAll(int i, Collection<? extends T> clctn )
    {
        // TODO: Implement?
        throw new UnsupportedOperationException("Method not yet supported for MPArrayList class.");
    }
    @Override
    public T set(int i, T obj )
    {
        this.updateModelpath( obj, this.modelpath+"[]" );
        return super.set(i, obj);
    }
    
    // Private Methods
    
    /**
     * Update model path of provided object, and initiate rebase of 
     * the object content to the provided model path.
     *   - Model path update occurs for Quantity and Proxy class objects only.
     *   - Rebase of object content occurs for Proxy objects only.
     *   - Other object types may be stored in MPArrayList.. they simply have
     *     no path to set/propagate
     *
     * @param obj
     * @param mp 
     */
    private void updateModelpath( Object obj, String mp )
    {
       ModeledElement elem = null;
       if ( Proxy.isProxyClass( obj.getClass()) ) {
          ModelProxy h = (ModelProxy)Proxy.getInvocationHandler( obj );
          elem = h.getDoc();
       }
       else if ( ModeledElement.class.isInstance( obj )) {
           elem = (ModeledElement)obj;
       }
       
       if ( elem == null )
           return;
       
       if ( MPNode.class.isInstance( elem ) ) {
         // Extract identity of the node from current path on node
         //    Strip base from path, including associated array annotation.
         String nodepath = elem.getModelpath();
         String nodetag  = nodepath.substring( nodepath.lastIndexOf("_")+1).replaceFirst( ".*\\[\\]\\.", "");

         mp = mp + "." + nodetag;
       }

       // Set new model path for the element... self propagating.
       elem.setModelpath( mp );
    }
    
    private void rebaseContent()
    {
       // Re-baseline the content of the array list to the current model path.
       // NOTE: Array elements get added notation of '[]' to the path.
       Object obj;
       String newbase = this.modelpath + "[]";
       for ( int ii=0; ii < this.size(); ii++ ) {
          obj = this.get(ii);
          this.updateModelpath( obj, newbase );
       }
    }
    
}
