/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.Quantity;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author mdittmar
 */
public class MPArrayList<T> extends ArrayList<T> {
    
    private String modelpath;

    // Constructors
    public MPArrayList()
    {
        super();
        this.modelpath = "UNKNOWN";
    }
    
    public MPArrayList(int i) 
    {
        super( i );
        this.modelpath = "UNKNOWN";
    }

    public MPArrayList(Collection<? extends T> clctn) 
    {
        super( clctn );
        this.modelpath = "UNKNOWN";
    }

    
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
        if ( mp == null )
            this.modelpath = "";
        else
          this.modelpath = mp;
    }

    @Override
    public boolean add( T obj )
    {
        this.updateModelPath(obj, this.modelpath+"[]");
        return super.add(obj);
    }
    
    @Override
    public void add(int i, T obj )
    {
        this.updateModelPath(obj, this.modelpath+"[]");
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
        this.updateModelPath( obj, this.modelpath+"[]" );
        return super.set(i, obj);
    }
    /**
     * Update model path of provided object, and initiate rebase of 
     * the object content to the provided model path.
     *   - Model path update occurs for Quantity and Proxy class objects only.
     *   - Rebase of object content occurs for Proxy objects only.
     *   - Other object types may be stored in MPArrayList.. they simply have
     *     no path to set/propogate
     *
     * @param obj
     * @param base 
     */
    private void updateModelPath( Object obj , String base )
    {
       // Set model path on list entry
       if ( obj.getClass() == Quantity.class )
         ((Quantity)obj).setModelpath( base );
       else if ( Proxy.isProxyClass( obj.getClass()) )
       {
          ModelProxy h = (ModelProxy)Proxy.getInvocationHandler( obj );
          h.setModelPath(base);
          h.rebaseContent();
       }
    }
    
    /**
     * Re-baseline the content of the array list to the current model path.
     * NOTE: Array elements get added notation of '[]' to the path.
     */
    protected void rebaseContent()
    {
        Object obj;
        String newbase = this.modelpath + "[]";
        for ( int ii=0; ii < this.size(); ii++ )
        {
           obj = this.get(ii);
           this.updateModelPath( obj, newbase );
        }
    }    
}
