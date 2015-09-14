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
import java.util.List;

/**
 * Dynamic proxy class to implement the various model interfaces.  Each instance
 * is given a MPNode object to hold the contents for that interface (eg. Target).
 * 
 * @author mdittmar
 */
public class ModelProxy implements InvocationHandler
{
    protected String type;         // Interface type that this proxy emulates.
    protected MPNode data;         // Node content.
    protected ModelObjectFactory mof;

    /**
     * Constructor, invokes new proxy instance using the provided MPNode 
     * object to hold content.
     * Throws IllegalArumentException for:
     *   - null storage input
     * 
     * @param storage 
     *    {@link MPNode}  object to hold content for the proxy. 
     * @param type 
     *    {@link String}  the class type that this proxy emulates. 
     * @param mof 
     *    {@link ModelObjectFactory}  facilitates generation of new proxies 
     *                                according to model classes. 
     */
    public ModelProxy( MPNode data, String type, ModelObjectFactory mof )
    {
        this.type = type;
        this.mof  = mof;
        
        if ( data == null )
            throw new IllegalArgumentException( "'data' argument may not be null.");
        else {
            this.data = data;
        }
    }
    
    /**
     * Invokes methods of the various model interfaces.  These are primarily
     * accessors/mutators to the properties, following the JavaBeans convention.
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
        
        // Check if arguments contain a List index (ie: getCollections( index ))
        if ((args != null)&&(args.length == 1 )&&(args[0].getClass().equals(Integer.class)))
            index = (Integer)args[0];

        // Get element from storage storage
        result = this.data.getChildByMP( newpath );
        if ( result == null ) {
            // Need to create the Property.  
            // If index is provided, create the enclosing List<rtype> itself.
            if ( index != null )
              result = newProperty( newpath, List.class, rtype );
            else
              result = newProperty( newpath, rtype, etype );
        }
        
        // If index is set, get requested element from List.
        // NOTE: may throw IndexOutOfBoundsException
        if ( index != null ) {
            Object tmp = ((List)result).get(index);
            result = tmp;
        }
        
        // Create Proxy for complex elements, provide node storage.
        if ( result.getClass() == MPNode.class ) {
            MPNode node = (MPNode)result;
            result = this.mof.newInstanceByModelPath(node.getModelpath(), node );
        }
        else if ( result.getClass() == MPArrayList.class ) {
        // Serve 'proxy' array list to user.
          result = new ProxyArrayList( (MPArrayList)result, this.mof );
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
        
        // handle arguments; setP( index, item ) vs setP(item)
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
        
        // Translate user input object (item) to ModeledElement for inclusion
        // in data storage node.
        ModeledElement elem = convertObjectToElement( item );
        if ( elem == null )
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
           elem = q;
        }
        
        // Update model path of the element
        elem.setModelpath( mp );
        elem.rebaseModelpath( this.data.getModelpath() );
        
        // Add property to document
        if ( index == null )
        {
          this.data.appendChild( elem );
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
           l.set( index, elem );
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
    
    /**
     *  Create specified property and add to the storage storage for this object.
     * 
     * @param mp        Model path of new element
     * @param type      Type of new element
     * @param etype     Parameter Type of new element (or null) (eg Quantity<T> )
     * @return 
     */
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
               // TODO: Can this be done so that it is defined as a specific type,
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

        // add new property to document.
        this.data.appendChild( result );
        
        return result;
    }

    /**
     * User input to methods will often be of non-ModeledElement types
     *   (the Interface, base Quantity, primitives, or Lists thereof)
     * This method handles converting the complex types to their 
     * ModeledElement equivalents.  The Primitive types need to be handled
     * by the caller, as the action is dependent on the situation.
     * 
     * @param item
     * @return
     */
    protected ModeledElement convertObjectToElement( Object item )
    {
        ModeledElement result = null;
        
        if ( ( item.getClass() == MPQuantity.class )   || 
             ( item.getClass() == MPArrayList.class )  ||
             ( item.getClass() == MPNode.class )  )
        {
           // item is already a ModeledElement, nothing to do here.
           result = (ModeledElement)item;
        }
        else if ( item.getClass() == Quantity.class )
        {
            // Convert input to MPQuantity
            MPQuantity mpq = new MPQuantity();
            mpq.fill( (Quantity)item );
            result = mpq;
        }
        else if ( Proxy.isProxyClass( item.getClass() ) )
        {
            // Get Proxy handler - and verify is one of OURs
            Object handler = Proxy.getInvocationHandler( item );
            if ( handler instanceof ModelProxy ){
                // Pull out underlying data storage.
                result = ((ModelProxy)handler).data;
            }
            else {
                throw new UnsupportedOperationException("Found unsupported Proxy type: "+ handler.getClass().getSimpleName() );
            }
        }
        else if ( item instanceof List )
        {
            // Input is List object.. not MPArrayList, or ListModelProxy which are handled above
            // Expecting List<Quantity>, List<ModelProxy>
            // NOTE: List<primitive> should be rejected as does single primitive.
            MPArrayList mparr = new MPArrayList();
            ModeledElement mpentry;
            for ( Object entry: (List)item ){
               mpentry = this.convertObjectToElement(entry);
               if ( mpentry == null ){ // List of Primitives
                   mparr = null;
                   break;
               }
               mparr.add(mpentry);
            }
            result = mparr;
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
