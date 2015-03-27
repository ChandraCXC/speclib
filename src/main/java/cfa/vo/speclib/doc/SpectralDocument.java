/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.Quantity;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Container class for Spectral data.
 * Forms a structured hierarchy of model components and serves as a 
 * representation of a Spectral document.
 *
 * @author mdittmar
 */
public class SpectralDocument {
    private HashMap<String, Object> doc;
    
    public SpectralDocument(){
        this.doc = new HashMap<String, Object>();
    }
    
    /**
     * Get object from document.  
     * If the item does not already exist, it will be created.
     * 
     * @param mp
     *   Model path identifier of the requested object.
     * 
     * @return 
     *   Requested object.
     */
    public Object get(String mp )
    {
        Object retval;
        
        if (this.doc.containsKey(mp))
        {
            retval = this.doc.get(mp);
        }
        else 
        {
            //TODO: review action.. return NULL, throw Exception, or create from Factory?
            retval = new Integer(1);
            this.doc.put(mp, retval);
        }
        
        return retval;
    }
    
    /**
     * Add item to document.
     * 
     * @param mp
     *   Model path identifier of the item.
     * 
     * @param member
     *   Object being stored
     */
    public void put(String mp, Object member){
        //TODO: check input member and path not null.
        
        this.doc.put(mp, member);
    }

    public void remove(String mp)
    {
        if ( this.doc.containsKey(mp) )
            this.doc.remove(mp);
    }

     /**
     * Check if document contains the specified member.
     * 
     * @param mp
     *   Model path identifier of the member.
     */
    public boolean contains( String mp )
    {
        return this.doc.containsKey(mp);
    }

    public Set<String> getKeys()
    {
        return this.doc.keySet();
    }

    /**
     * For Debugging:
     * Generate a string representation of the document
     * as part of a hierarchy of documents.
     *
     * @return 
     *     {@link String }
     */
    @Override public String toString()
    {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String key;
        Iterator iter;
        
        if (this.doc.isEmpty())
        {
            result.append("<empty>");
        }
        else
        {
            String indent;
            int n;

            Object item;
            Set keys = this.doc.keySet();
            iter = keys.iterator();
            while (iter.hasNext())
            {
                key = (String) iter.next();
                String[] nodes = key.split("_");
                n = nodes.length;

                indent = "";
                if ( n > 1 )
                   indent = String.format("%" + 3*(n-1) + "s", " ");

                item = this.doc.get(key);
                
                List inarr;
                Iterator recIter;
                if ( item.getClass() == ArrayList.class )
                {
                    inarr = (List)item;
                }
                else
                {
                    inarr = new ArrayList<Object>();
                    inarr.add(item);
                }   
                recIter = inarr.iterator();
                while ( recIter.hasNext())
                {
                    Object elem = recIter.next();
                    if ( elem.getClass().getSimpleName().startsWith("$Proxy"))
                    {
                        result.append( elem.toString());
                    }
                    else
                    {
                        result.append(indent).append( nodes[n-1] ).append(": " );
                        if ( elem.getClass() == Quantity.class )
                            result.append( ((Quantity)elem).toSimpleString() );
                        else
                            result.append(elem.getClass().getSimpleName());
                        result.append(newLine);
                    }
                }
            }
        }
        return result.toString();
        
    }
    /**
     * For Debugging:
     * Generate a simple string representation of the document
     *
     * @return 
     *     {@link String }
     */
    public String toSimpleString()
    {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String key;
        Iterator iter;
        
        if (this.doc.isEmpty())
        {
            result.append("<empty>");
        }
        else
        {
            Set keys = this.doc.keySet();
            iter = keys.iterator();
            while (iter.hasNext())
            {
                key = (String) iter.next();
                result.append(key).append(": ");
                result.append(this.doc.get(key).getClass().getSimpleName());
                result.append(newLine);
            }
        }
        return result.toString();
        
    }
}
