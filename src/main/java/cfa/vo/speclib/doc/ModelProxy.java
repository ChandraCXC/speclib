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
    protected String type;         // Interface type that this proxy emulates.
    protected MPNode data;  // Node content.

    /**
     * Constructor, invokes new proxy instance and MPNode segment to hold content.
     */
    public ModelProxy()
    {
        this( null, null );
    }

    /**
     * Constructor, invokes new proxy instance and stores the provided 
     * MPNode segment to hold content.
     * 
     * @param data 
     *    {@link SpectralDocument} segment to hold content for the proxy. 
     */
    public ModelProxy( MPNode data )
    {
        this( data, null );
    }


    public ModelProxy( MPNode data, String type )
    {
        this.type = type;
        
        if ( data == null )
            this.data = new MPNode();
        else
            this.data = data;

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
    @Override
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
            // JavaBean get<Property>
            result = this.getProperty( proxy, method, args );
        }
        else if ( method.getName().startsWith("set") )
        {
            // JavaBean set<Property>
            this.setProperty( proxy, method, args );
            result = null;
        }
        else if ( method.getName().startsWith("is"))
        {
            // JavaBean is<Property>
            result = this.isProperty( proxy, method, args );
        }
        else if ( method.getName().startsWith("has") )
        {
            result = this.hasProperty( method.getName().replaceFirst("has", "") );
        }
        else if ( method.getName().equals("toString") )
        {
            result = this.toString();
        }
        else if ( method.getName().equals("equals") )
        {
            result = this.equals( proxy, args[0] );
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

    /**
     * Generate a string representation of the Interface
     *
     * @return 
     *     {@link String }
     */
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        String[] nodes = this.data.getModelpath().split("_");
        String indent = "";
        int n = nodes.length;
        if ( n > 1 )
        {
          indent = String.format("%" + 3*(n-1) + "s", " ");
        }
        result.append(indent).append( nodes[n-1] ).append(newLine);
        if ( (this.data == null)||(this.data.isEmpty()) )
        {
            result.append(indent).append("   <empty>");
        }
        else
        {
           result.append(this.data.toString());
        }
        return result.toString();
    }
    
    // ***********************************************************************
    // Private Methods:
    
    /**
     *  Implementation of the "get<Property>" methods.
     **/
    private Object getProperty( Object proxy, Method method, Object[] args )
    {
        Object result;
        String property  = method.getName().replaceFirst("get","");
        String newpath = defineModelPath( property );
        Integer index = null;

        // Check if arguments contain a List index (ie: getCollections( index ))
        if ((args != null)&&(args.length == 1 )&&(args[0].getClass().equals(Integer.class)))
            index = (Integer)args[0];

        // Get Expected Type (Quantity) and Parameter type (T) of returned property (Quantity<T>)
        Class rtype = method.getReturnType();
        Class<?> etype;
        try {
            ParameterizedType gtype = (ParameterizedType)method.getGenericReturnType();
            etype = (Class<?>)gtype.getActualTypeArguments()[0];
        }
        catch ( ClassCastException e ) {
            etype = null;
        }

        result = this.data.getChildByMP( newpath );
        if ( result == null ) {
            // Need to create the Property.

            ModeledElement elem;
            if ( index != null ) {
                // create enclosing List<rtype>, assign model path
                elem = newProperty( newpath, List.class, rtype );
            }
            else {
                // New Quantity, List, or Node
                elem = newProperty( newpath, rtype, etype );
            }
            // add new property to document.
            this.data.appendChild( elem );
            
            result = elem;
        }
        
        // check for indexed getter, get requested element.
        if ( index != null ) {
            //NOTE: may throw IndexOutOfBoundsException
            Object tmp = ((List)result).get(index);
            result = tmp;
        }

        // If the return type is an interface, we need to create it, and 
        // supply it with the underlying MPNode content.
        if ( result.getClass() == MPNode.class ) {
            MPNode node = (MPNode)result;
            result = Proxy.newProxyInstance( data.getClass().getClassLoader(),
                                             new Class[]{ rtype },
                                             new ModelProxy( node, rtype.getSimpleName() ));
        }
        
        return result;
    }

    /**
     *  Implementation of the "set<Property>" methods.
     **/
    private void setProperty( Object proxy, Method method, Object[] args )
    {
        Integer index;
        Object item;
        
        // handle setP( index, item ) vs setP(item)
        if ( args.length == 2 ) {
            index = (Integer)args[0];
            item = args[1];
        }
        else {
            index = null;
            item = args[0];
        }

        // define model path to property
        String property  = method.getName().replaceFirst("set","");
        String mp = defineModelPath( property );
        
        // Handle input.. if list, make sure it is an Proxy, MPQuantity, or MPArrayList
        if ( ( item.getClass() == MPQuantity.class )   || 
             ( item.getClass() == MPArrayList.class )   )
        {
           // nothing to do here.
        }
        else if ( Proxy.isProxyClass( item.getClass() ) )
        {
          // Pull out underlying MPNode.
          ModelProxy h = (ModelProxy)Proxy.getInvocationHandler( item );
          item = h.data;
        }
        else if ( item.getClass() == Quantity.class )
        {
            // Convert input to MPQuantity
            MPQuantity mpq = new MPQuantity();
            mpq.fill( (Quantity)item );
            item = mpq;
        }
        else if ( ( item.getClass() == ArrayList.class ) || (item.getClass() == List.class))
        {
            // Convert input list to MPArrayList, for adding to property
            MPArrayList mparr = new MPArrayList( (List)item );
            for ( int ii = 0; ii < mparr.size(); ii++ ) {  // TODO - this should be MPArrayList task.
                // Convert content Quantities to MPQuantities.
                if ( mparr.get(ii).getClass() == Quantity.class )
                {
                    MPQuantity mpq = new MPQuantity();
                    mpq.fill( (Quantity)mparr.get(ii) );
                    mparr.set(ii, mpq);
                }
            }
            item = mparr;
        }
        else
        {
           // Assign value from Primitive
           // Values should be type safe via prototypes
           MPQuantity q;
           try {
              Method m = proxy.getClass().getMethod("get"+property, (Class<?>[])null);
              if ( index == null )
                q = (MPQuantity)this.getProperty( proxy, m, (Object[])null );
              else
                q = (MPQuantity)this.getProperty( proxy, m, new Object[]{index} );
                    
              q.setValue( item );
           } catch (NoSuchMethodException ex) {
              // Boolean attributes do not have 'get' method..
              // Check for is<P>() method.
              try{
                 Method m = proxy.getClass().getMethod("is"+property, (Class<?>[])null);
                 q = new MPQuantity( item );
              } catch (NoSuchMethodException exx) {
                 throw new UnsupportedOperationException("Cannot find get"+property+"() method in proxy.");
              }
           }
           item = q;
        }
        // MCD NOTE: At this point, item should be a ModeledElement.
        // Update model path of object/list
        ((ModeledElement)item).setModelpath( mp );
        ((ModeledElement)item).rebaseModelpath( this.data.getModelpath() );
        
        // Add property to document
        if ( index == null )
        {
          this.data.appendChild( (ModeledElement)item );
        }
        else
        {
           // Replace Indexed Property Element
           // Ensure property exists in document
           List l = (List)this.data.getChildByMP(mp);
           if ( l == null ) {
              try {
                Method m = proxy.getClass().getMethod("get"+property, (Class<?>[])null);
                l = (List)this.getProperty( proxy, m, args );
              } catch (NoSuchMethodException ex) {
                // TODO: This shouldn't happen.. maybe should be ERROR instead?
                l = new MPArrayList<Object>();
              }
           }                                   
           // NOTE: May throw IndexOutOfBoundsException
           l.set( index, item );
        }
    }

    /**
     *  Implementation of the "is<Property>" methods.
     **/
    private Object isProperty( Object proxy, Method method, Object[] args )
    {
        Object result = false;
        String property  = method.getName().replaceFirst("is","");
        String newpath = defineModelPath( property );

        // NOTE: Currently only supporting scalar boolean properties.
        
        // return existing property value (boolean)... or false        
        Quantity elem = ((Quantity)this.data.getChildByMP(newpath));
        if ( elem != null )
            result = elem.getValue();

        return result;
    }
    
    /**
     *  Implementation of the "has<Property>" methods.
     * 
     * @param property
     *    {@link String} Name of interface property to check.
     * 
     * @return 
     *    boolean - true if property exists (is not null)
     */
    private boolean hasProperty( String property )
    {
        // get model path for property
        String newpath = defineModelPath( property );
        
        // check if the document currently contains the property.
        boolean result = (this.data.getChildByMP(newpath) != null );
        return result;
    }
    
    private ModeledElement newProperty( String mp, Class type, Class etype )
    {
        ModeledElement result;

        if ( type == Quantity.class ) {
            MPQuantity q = new MPQuantity();
            q.setDType( etype );

            result = q;
        }
        else if ( type == List.class ) {
            MPArrayList arr;
            if ( etype == Quantity.class ){
               arr = new MPArrayList<MPQuantity>();
            }
            else {
               // Can this be done so that it is defined as a specific type,
               // in a generic fashion (ie ArrayList<SPPoint>) without specifically
               // using/importing the SPPoint class.. ie: get it from the etype arg.
               arr = new MPArrayList<Object>();
            }
            result = arr;
        }
        else if ( type.isInterface() ) {
            MPNode node = new MPNode();
            result = node;
        }
        else
        {
            throw new UnsupportedOperationException( type.getSimpleName() + " type not yet supported.");
        }
        
        // Set model path of new element.
        result.setModelpath(mp);
        
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
        // Get model path to this node.
        String mp = this.data.getModelpath();

        if ( (mp == null) || mp.isEmpty() ) {
           mp = property;
        }
        else {
            mp = mp + "_" + property;
        }

        return mp;
    }
    
    //TODO - TEMPORARY
    public MPNode getDoc()
    {
        return this.data;
    }
}
