/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.Quantity;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mdittmar
 */
public class ProxyArrayList<T> extends ArrayList<T> {

    private MPArrayList content;  // ArrayList of ModeledElement content
    private ModelObjectFactory mof;
    
    
    // Constructors
    public ProxyArrayList( MPArrayList data, ModelObjectFactory mof ) 
    {
        super();
        this.content = data;
        this.mof = mof;
        
        ModeledElement elem;
        T item;
        
        for (int ii=0; ii < data.size(); ii++) {
            elem = (ModeledElement)data.get(ii);
            item = (T)convertElementToObject( elem );
            super.add(item);
        }        
    }

    
    // ArrayList methods
    //  boolean          add(E e)                                               Override - DONE
    //  void             add(int index, E element )                             Override - DONE
    //  boolean          addAll(int index, Collection<? extends E> c)           Override - TODO
    //  void             clear()                                                Eval
    //  Object           clone()                                                Eval
    //  boolean          contains(Object o)                                     Eval
    //  void             ensureCapacity(int minCapacity)                        Eval
    //  E                get(int index)                                         OK
    //  int              indexOf(Object o)                                      OK
    //  boolean          isEmpty()                                              OK
    //  Iterator<E>      iterator()                                             OK
    //  int              lastIndexOf(Object o)                                  OK
    //  ListIterator<E>  listIterator()                                         Eval
    //  ListIterator<E>  listIterator(int index)                                Eval
    //  E                remove(int index)                                      Override - TODO
    //  boolean          remove(Object o)                                       Override - TODO
    //  boolean          removeAll(Collection<?> c)                             Override - TODO
    //  protected void   removeRange(int fromIndex, int toIndex)                Override - TODO
    //  boolean          retainAll(Collection<?> c)                             Override - TODO
    //  E                set(int index, E element)                              Override - TODO
    //  int              size()                                                 OK
    //  List<E>          subList(int fromIndex, int toIndex)                    Eval
    //  Object[]         toArray()                                              Eval
    //  <T> T[]          toArray(T[] a)                                         Eval
    //  void             trimToSize()                                           Eval
    //
    //  boolean          equals(Object o)                                       Eval
    //  int              hashCode()                                             Eval
    //  boolean          containsAll(Collection<?> c)                           Eval
    //  String           toString()                                             OK
                
    // Overrides:
    
    @Override
    public boolean add( T obj )
    {
        boolean result;
        ModeledElement elem;
        
        elem = convertObjectToElement( obj );
        result = this.content.add( elem );
        if ( result == true )
          result = super.add(obj);
        
        return result;
    }

    @Override
    public void add(int i, T obj )
    {
        ModeledElement elem;
        
        elem = convertObjectToElement( obj );
        this.content.add(i,elem );
        super.add(i, obj);
    }


    private Object convertElementToObject( ModeledElement elem )
    {
      Object result;
      
      // Create Proxy for complex elements, provide node storage.
      if ( elem.getClass() == MPNode.class ) {
        result = this.mof.newInstanceByModelPath( elem.getModelpath(), (MPNode)elem );
      }
      else if ( elem.getClass() == MPArrayList.class ) {
        // NOTE: This is not correct (type).. but we do not have list of lists yet.
        result = new ProxyArrayList<Object>( (MPArrayList)elem, this.mof );
      }
      else
          result = elem;
      
      return result;
}
    
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
    
    
}
