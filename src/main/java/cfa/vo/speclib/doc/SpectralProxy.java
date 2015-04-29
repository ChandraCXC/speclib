/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.Quantity;
import cfa.vo.speclib.SPPoint;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Dynamic proxy class to implement the various interfaces of the Spectral
 * library.  Each instance is given a SpectralDocument object to hold the 
 * contents for that interface (eg. Target).
 * 
 * @author mdittmar
 */
public class SpectralProxy implements InvocationHandler
{
    // Model path identifier for the interface (proxy instance).
    protected String modelpath;

    protected SpectralDocument data;
    
    /**
     * Constructor, invokes new proxy instance and SpectralDocument segment
     * to hold content.
     */
    public SpectralProxy()
    {
        this.data = new SpectralDocument();
    }

    /**
     * Constructor, invokes new proxy instance and stores the provided 
     * SpectralDocument segment to hold content.
     * 
     * @param data 
     *    {@link SpectralDocument} segment to hold content for the proxy. 
     */
    public SpectralProxy( SpectralDocument data )
    {
        this.data = data;
    }

    public SpectralProxy( SpectralDocument data, String mp )
    {
        if ( data == null )
            this.data = new SpectralDocument();
        else
            this.data = data;
        
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
            result = this.check( method.getName().replaceAll("isSet", "") );
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

    public SpectralDocument getDoc()
    {
       return ( this.data );
    }

    // ***********************************************************************
    // Private Methods:
    
    private Object getProperty( Object proxy, Method method, Object[] args )
    {
        Object result;
        String property  = method.getName().replaceAll("get","");
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
                // create enclosing List<rtype>
                result = newListProperty( rtype );
            }
            else if ( rtype == List.class )
            {
                result = newListProperty( etype );
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
        String property  = method.getName().replaceAll("set","");
        String newpath = defineModelPath( property );

        // Setup for updating model path of input objects
        List inarr;
        Iterator iter;
        if ( ( item.getClass() == ArrayList.class ) || (item.getClass() == List.class))
        {
            inarr = (List)item;
        }
        else
        {
            inarr = new ArrayList<Object>();
            inarr.add(item);
        }   

        iter = inarr.iterator();
        while ( iter.hasNext())
        {
            Object element = iter.next();
            if ( Proxy.isProxyClass( element.getClass() ) )
            {
                // reset base model path for base and its children.
                SpectralProxy h = (SpectralProxy)Proxy.getInvocationHandler( element );
                h.updateModelPath( this.modelpath );
            }
            else if ( element.getClass() == Quantity.class )
            {
                //TODO: Type check generic Quantity type against expected.
                // assign new model path 
                ((Quantity)element).setModelpath( newpath );
            }
            else
            {
                // Assign value from Primitive
                // Values should be type safe via prototypes
                Quantity tmp;
                try {
                    Method m = proxy.getClass().getMethod("get"+property, (Class<?>[])null);
                    if ( index == null )
                        tmp = (Quantity)this.getProperty( proxy, m, (Object[])null );
                    else
                        tmp = (Quantity)this.getProperty( proxy, m, new Object[]{index} );
                    
                    tmp.setValue(element);
                } catch (NoSuchMethodException ex) {
                    // TODO: This shouldn't happen.. maybe should be ERROR instead
                    tmp = new Quantity( element );
                }
                item = tmp;
            }
        }
        // Add property to document
        if ( index == null )
        {
            this.data.put(newpath, item);
        }
        else
        {
            // Replace Indexed Property Element
            // Make sure property exists in document
            List l;
            if ( this.data.contains(newpath))
                l = (List)this.data.get(newpath);
            else
            {
                try {
                    Method m = proxy.getClass().getMethod("get"+property, (Class<?>[])null);
                    l = (List)this.getProperty( proxy, m, args );
                } catch (NoSuchMethodException ex) {
                    // TODO: This shouldn't happen.. maybe should be ERROR instead
                    l = new ArrayList<Object>();
                }
            }                                   
            // NOTE: May throw IndexOutOfBoundsException
            l.set( index, item );
        }

    }
    private Object isProperty( Object proxy, Method method, Object[] args )
    {
        Object result;
        String property  = method.getName().replaceAll("is","");
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
                                             new SpectralProxy( null, mp ));
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
            result = new ArrayList<Quantity>();
        }
        else
        {
           // Can this be done so that it is defined as a specific type,
           // in a generic fashion (ie ArrayList<SPPoint>) without specifically
           // using/importing the SPPoint class.. ie: get it from the type arg.
           result = new ArrayList<Object>();
        }
        return result;
    }

    /**
     * Primarily for u
     * se during assignment (set), this method resets the 
     * base model path for the interface, as well as for all of its
     * elements.  
     * 
     * @param base 
     *    New base model path to this node.
     */
    private void updateModelPath( String base )
    {
        // Establish new path to this node.
        String last = this.modelpath.substring(this.modelpath.lastIndexOf("_")+1); 
        this.modelpath = base + "_" + last;
        
        if ( this.data != null )
        {
            Set keys = this.data.getKeys();
            Iterator iter =  keys.iterator();
            String mp;
            Object item;

            while (iter.hasNext())
            {
                // replace the item in Document with new key reference.
                mp = (String)iter.next();
                item = this.data.get(mp);
                this.data.remove(mp);
                last = mp.substring(mp.lastIndexOf("_")+1);
                mp = this.modelpath + "_" + last;
                this.data.put(mp, item);

                // if the element is itself a proxy, update recursively
                if ( Proxy.isProxyClass( item.getClass() ) )
                {
                    // reset base model path for base and its children.
                    SpectralProxy h = (SpectralProxy)Proxy.getInvocationHandler( item );
                    h.updateModelPath( mp );
                }
                else if ( item.getClass() == Quantity.class )
                {
                    // assign new model path 
                    ((Quantity)item).setModelpath( mp );
                }
            }
            
        }
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
