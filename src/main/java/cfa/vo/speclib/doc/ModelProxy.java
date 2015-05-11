/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.Quantity;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic proxy class to implement the various interfaces of the Spectral
 * library.  Each instance is given a SpectralDocument object to hold the 
 * contents for that interface (eg. Target).
 * 
 * @author mdittmar
 */
public class ModelProxy implements InvocationHandler
{
    // Interface type that this proxy emulates.
    protected String type;
    
    // Model path identifier for the interface (proxy instance).
    protected String modelpath;

    // Node content.
    protected ModelDocument data;
    
    /**
     * Constructor, invokes new proxy instance and SpectralDocument segment
     * to hold content.
     */
    public ModelProxy()
    {
        this.data = new ModelDocument();
    }

    /**
     * Constructor, invokes new proxy instance and stores the provided 
     * SpectralDocument segment to hold content.
     * 
     * @param data 
     *    {@link SpectralDocument} segment to hold content for the proxy. 
     */
    public ModelProxy( ModelDocument data )
    {
        this.data = data;
    }

    public ModelProxy( ModelDocument data, String type, String mp )
    {
        if ( data == null )
            this.data = new ModelDocument();
        else
            this.data = data;

        this.type = type;
        this.modelpath = mp;
    }

    /**
     * Method to invoke the various accessors associated with the 
     * interfaces the proxy represents.
     * 
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable 
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
    {
        Object result;
        
        if ( method.getName().equals("getLength") && 
                ( method.getReturnType() == Integer.class) )
        {
            // TODO: Proper handling to determine Length
            result = new Integer(0);
        }
        else if ( method.getName().startsWith("get") )
        {
            result = this.getProperty( proxy, method, args );
        }
        else if ( method.getName().startsWith("set") )
        {
            this.setProperty( proxy, method, args );
            result = null;
        }
        else if ( method.getName().startsWith("isSet") )
        {
            result = this.check( method.getName().replaceFirst("isSet", "") );
        }
        else if ( method.getName().startsWith("is"))
        {
            // JavaBean is<Property>
            result = this.isProperty( proxy, method, args );
        }
        else if ( method.getName().equals("toString") )
        {
            result = toString();
        }
        else if ( method.getName().equals("equals") )
        {
            result = equals( proxy, args[0] );
        }
        else
            throw new UnsupportedOperationException(method.getName()+": Not supported yet.");

        return result;
    }

     /**
     * Equality check for proxies.
     * 
     * @param a
     * @param b
     * @return 
     *    true if a and b both represent the same Class.
     */
    public boolean equals( Object a, Object b )
    {
        //System.out.println("Equals method: "+a.getClass().getSimpleName()+" and "+b.getClass().getSimpleName());
        return ( a.getClass() == b.getClass());
    }

    public ModelDocument getDoc()
    {
       return ( this.data );
    }

    // ***********************************************************************
    // Protected Methods: Visible to other members of this package.

    
    /** 
     * Assign value to model path attribute of this proxy.
     * If base ends with '[]', this indicates that the proxy is an element
     * of a List, in which case, we append the the proxy 'type' to the 
     * model path to distinguish various flavors of the List content.
     * 
     * @param base 
     */
    protected void setModelPath( String base )
    {
       if ( base.endsWith("[]") )
           base = base.concat(".").concat(this.type);

       this.modelpath = base;
    }
    
    /**
     * Primarily for use during assignment (set), this method (re)sets 
     * the model path in the provided object, then recursively re-bases
     * any children of the object.
     * 
     * @param obj
     *    Object to be updated.
     * @param base 
     *    New base model path to this node.
     */
    protected void updateModelPath( Object obj, String base )
    {
       if ( obj.getClass() == Quantity.class )
       {
         // set new model path 
         ((Quantity)obj).setModelpath( base );

         // No content to rebaseline.
       }
       else if ( obj.getClass() == MPArrayList.class )
       {
          // set new model path 
          ((MPArrayList)obj).setModelpath( base );
          
          // Rebase array elements
          ((MPArrayList)obj).rebaseContent();
          
       }
       else if ( Proxy.isProxyClass( obj.getClass() ) )
       {
          ModelProxy h = (ModelProxy)Proxy.getInvocationHandler( obj );

          // set new model path 
          h.setModelPath(base);

          // reset base model path for base and its children.
          h.rebaseContent();
        }
        else
          throw new UnsupportedOperationException("Proxy.updateModelPath().. Unrecognized type "+obj.getClass().getSimpleName());
    }

    /**
     * Re-baseline the content of the proxy to the current model path.
     */
    protected void rebaseContent()
    {
        if ( this.data != null )
        {
           Object[] keys = this.data.getKeys().toArray();
           for ( int ii = 0; ii < keys.length; ii++ )
           {
               String key = (String)keys[ii];
               Object obj = this.data.get( key );
               
               // Remove current entry for this object in data
               this.data.remove( key );
               
               String last = key.substring(key.lastIndexOf("_")+1);
               String newkey = this.modelpath + "_" + last;
               
               // Replace entry with new key.
               this.data.put( newkey, obj );
               
               // Update the model path of the child object.
               this.updateModelPath( obj, newkey );
           }
        }
    }
    
    // ***********************************************************************
    // Private Methods:
    
    private Object getProperty( Object proxy, Method method, Object[] args )
    {
        Object result;
        String property  = method.getName().replaceFirst("get","");
        String newpath = defineModelPath( property );
        Integer index = null;

        // Check arguments for List index 
        if ((args != null)&&(args.length == 1 )&&(args[0].getClass().equals(Integer.class)))
            index = (Integer)args[0];

        if ( this.data.contains( newpath ))
        {
            // return existing property.
            result = this.data.get(newpath);
        }
        else
        {
            // Need to create the Property.
            
            // Get Expected Type (Quantity) and Parameter type (T) of returned property (Quantity<T>)
            Class rtype = method.getReturnType();
            Class<?> etype;
            try {
                ParameterizedType gtype = (ParameterizedType)method.getGenericReturnType();
                etype = (Class<?>)gtype.getActualTypeArguments()[0];
            }
            catch ( ClassCastException e )
            {
                etype = null;
            }
            
            if ( index != null )
            {
                // create enclosing List<rtype>, assign model path
                result = newListProperty( rtype );
                ((MPArrayList)result).setModelpath(newpath);
            }
            else if ( rtype == List.class )
            {
                // create List, assign model path
                result = newListProperty( etype );
                ((MPArrayList)result).setModelpath(newpath);
            }
            else
            {
                result = newProperty( newpath, rtype, etype );
            }
            // add new property to document.
            this.data.put(newpath, result);
        }
        // check for indexed getter
        if ( index != null )
        {
            //NOTE: may throw IndexOutOfBoundsException
            Object tmp = ((List)result).get(index);
            result = tmp;
        }
        
        return result;
    }

    private void setProperty( Object proxy, Method method, Object[] args )
    {
        Integer index;
        Object item;
        
        // handle setP( index, item ) vs setP(item)
        if ( args.length == 2 )
        {
            index = (Integer)args[0];
            item = args[1];
        }
        else
        {
            index = null;
            item = args[0];
        }

        // get model path for property
        String property  = method.getName().replaceFirst("set","");
        String mp = defineModelPath( property ); // path for this object/List

        // Handle input.. if list, make sure it is an Proxy, Quantity, or MPArrayList
        if ( Proxy.isProxyClass( item.getClass() ) || 
           ( item.getClass() == Quantity.class )   || 
           ( item.getClass() == MPArrayList.class )   )
        {
           // nothing to do here.
        }
        else if ( ( item.getClass() == ArrayList.class ) || (item.getClass() == List.class))
        {
            // Convert input list to MPArrayList, for adding to property
            MPArrayList mparr = new MPArrayList( (List)item );
            item = mparr;
        }
        else
        {
           // Assign value from Primitive
           // Values should be type safe via prototypes
           Quantity q;
           try {
              Method m = proxy.getClass().getMethod("get"+property, (Class<?>[])null);
              if ( index == null )
                q = (Quantity)this.getProperty( proxy, m, (Object[])null );
              else
                q = (Quantity)this.getProperty( proxy, m, new Object[]{index} );
                    
              q.setValue( item );
           } catch (NoSuchMethodException ex) {
              // Boolean attributes do not have 'get' method..
              // Check for is<P>() method.
              try{
                 Method m = proxy.getClass().getMethod("is"+property, (Class<?>[])null);
                 q = new Quantity( item );
              } catch (NoSuchMethodException exx) {
                 throw new UnsupportedOperationException("Cannot find get"+property+"() method in proxy.");
              }
           }
           item = q;
        }

        // Update model path of object/list
        this.updateModelPath( item, mp );
        
        // Add property to document
        if ( index == null )
        {
          this.data.put( mp, item);
        }
        else
        {
           // Replace Indexed Property Element
           // Make sure property exists in document
           List l;
           if ( this.data.contains(mp))
             l = (List)this.data.get(mp);
           else
           {
              try {
                Method m = proxy.getClass().getMethod("get"+property, (Class<?>[])null);
                l = (List)this.getProperty( proxy, m, args );
              } catch (NoSuchMethodException ex) {
                // TODO: This shouldn't happen.. maybe should be ERROR instead
                l = new MPArrayList<Object>();
              }
           }                                   
           // NOTE: May throw IndexOutOfBoundsException
           l.set( index, item );
        }
    }

    private Object isProperty( Object proxy, Method method, Object[] args )
    {
        Object result;
        String property  = method.getName().replaceFirst("is","");
        String newpath = defineModelPath( property );

        // return existing property value (boolean)... or false        
        if ( this.data.contains( newpath ))
            result = ((Quantity)this.data.get(newpath)).getValue();
        else
            result = false;

        return result;
    }
    
    /**
     *  Implementation of the "isSet<Property>" methods.
     * 
     * @param property
     *    {@link String} Name of interface property to check.
     * 
     * @return 
     *    boolean - true if property exists (is not null)
     */
    private boolean check( String property )
    {
        // get model path for property
        String newpath = defineModelPath( property );
        
        // check if the document currently contains the property.
        return this.data.contains( newpath );
    }
    
    private Object newProperty( String mp, Class type, Class etype )
    {
        Object result;

        if ( type == Quantity.class )
        {
            //if ( etype != null)
            //    System.out.println("MCD TEMP: New Q.. type == "+type.getSimpleName()+" etype == "+etype.getSimpleName());
            Quantity q = new Quantity();
            q.setModelpath( mp );
            q.setDType( etype );

            result = q;
        }
        else if ( type.isInterface() )
        {
            // create the requested interface and add to document.
            result = Proxy.newProxyInstance( data.getClass().getClassLoader(),
                                             new Class[]{ type },
                                             new ModelProxy( null, type.getSimpleName(), mp ));
        }
        else
        {
            throw new UnsupportedOperationException( type.getSimpleName() + " type not yet supported.");
        }
        return result;
    }

    private List newListProperty( Class type )
    {
        List result;
        
        if ( type == Quantity.class )
        {
            result = new MPArrayList<Quantity>();
        }
        else
        {
           // Can this be done so that it is defined as a specific type,
           // in a generic fashion (ie ArrayList<SPPoint>) without specifically
           // using/importing the SPPoint class.. ie: get it from the type arg.
           result = new MPArrayList<Object>();
        }
        return result;
    }

    /**
     * Generates the model path for the requested property.
     * The Model Path is a String containing "_" delimited set of 'nodes'
     * representing the path through the model objects for the property.
     * 
     * @param property
     *   @link{String} Name of the class property
     * 
     * @return
     *   Full model path to the property
     */
    private String defineModelPath( String property )
    {
        String mp;

        if ( (this.modelpath == null) || this.modelpath.isEmpty() )
        {
           mp = property;
        }
        else
        {
            mp = this.modelpath + "_" + property;
        }

        return mp;
    }
    
    /**
     * For Debugging:
     * Generate a string representation of the Interface
     *
     * @return 
     *     {@link String }
     */
    @Override public String toString()
    {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        String[] nodes = modelpath.split("_");
        String indent = "";
        int n = nodes.length;
        if ( n > 1 )
        {
          indent = String.format("%" + 3*(n-1) + "s", " ");
        }
        result.append(indent).append( nodes[n-1] ).append(newLine);
        if ( (this.data == null)||(this.data.getKeys().isEmpty()) )
        {
            result.append(indent).append("   <empty>");
        }
        else
        {
           result.append(this.data.toString());
        }
        return result.toString();
    }
}
