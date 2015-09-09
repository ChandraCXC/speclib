/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.Quantity;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * General storage for the content of a Modeled Document.
 * The MPNode represents one node of a Modeled Document. 
 * Nodes may contain ModeledElements (either quantities, arrays,
 * or other nodes) to form a hierarchy which fully represents
 * the Modeled Document.
 * 
 * This implementation stores the content in a LinkedHashMap
 *   Key<String>           = Model Path of the element
 *   Value<ModeledElement> = Element (MPQuantity, MPArray, MPNode)
 *
 * @author mdittmar
 */
public class MPNode implements ModeledElement, ModeledDocument {

    private String modelpath;  // Model path of this node
    private LinkedHashMap<String, ModeledElement> doc;  // Storage map.

    
    // constructors
    /**
     * Constructor
     *   Assigns model path to ModeledElement.MP_UNDEFINED
     */
    public MPNode(){
        this(ModeledElement.MP_UNDEFINED);
    }

    /**
     * Constructor
     * @param mp 
     *   Model path of the node.
     */
    public MPNode( String mp ){
        this.modelpath = mp;
        this.doc = new LinkedHashMap<String, ModeledElement>();
    }
    
    // ModeledElement Interface Methods
    
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
        
        this.modelpath = mp;
        this.rebaseContent();
   }
    
    /**
     * Re-base the model path property of this element.
     * Extracts last node of current element path and prepends the input base
     * to define a new path for this element.
     * 
     * Propagates the new base to MPNode contents.
     * 
     * @param base
     *     {@link String } - new base for element
     *                       null base strips any path to the element, leaving 
     *                       just the last node of current element path.
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
    
    // ModeledDocument Interface Methods
    @Override
    public void appendChild( ModeledElement elem )
    {
        if ( elem == null )
            return;
        
        // Update the model path for the element being added to the node.
        // prepends node path to element identity.
        elem.rebaseModelpath( this.modelpath );
        
        // Store in map.
        this.doc.put( elem.getModelpath(), elem );
    }

    @Override
    public ModeledElement getChildByMP( String mp )
    {
        ModeledElement result = null;
        
        if (this.doc.containsKey(mp))
            result = this.doc.get(mp);
        
        return result;
    }
    
    @Override
    public ModeledElement[] getChildren()
    {
        ModeledElement[] result;
        result = this.doc.values().toArray( new ModeledElement[0] );
        return result;
    }
    
    @Override
    public String[] getChildrenMP()
    {
        String[] result;
        result = this.doc.keySet().toArray( new String[0] );
        return result;
    }
    
    @Override
    public boolean isEmpty()
    {
        return this.doc.isEmpty();
    }
    
    @Override
    public void removeChild( ModeledElement elem )
    {
        String key;  // key corresponding to object in node.
        
        if ( this.isEmpty() )
          throw new IllegalArgumentException("Node is empty.");

        if ( this.doc.containsValue( elem ) ){
            // NOTE: had originally just gotten the model path from the object
            //       but for Quantities the initial mp is not equal to the key.
            for ( Entry entry: this.doc.entrySet()){
                if (entry.getValue().equals(elem)){
                    key = (String)entry.getKey();
                    this.doc.remove(key);
                    break;
                }
            }
        }
        else
            throw new IllegalArgumentException("Node does not contain child object.");
    }

    @Override
    public void build( ModeledElement elem )
    {
        String mp = elem.getModelpath();
        
        if ( mp.matches(".+\\[\\]\\.*[a-zA-Z]*$") ) {
          // Element is a List member.. put it in an MPArrayList.
          String arraymp = mp.substring(0, mp.lastIndexOf('[') );
          MPArrayList<ModeledElement> qarr = new MPArrayList();
          qarr.setModelpath(arraymp);
          qarr.add(elem);
          // Element to be incorporated becomes the List itself.
          mp = arraymp;
          elem = qarr;
        }
        
        // Incorporate element..
        if ( this.getChildByMP( mp ) == null ) {
          // node does not contain this element.. add it.
          this.appendChild( elem );
          return;
        }
        
        // node already contains the element..
        if ( elem instanceof ModeledDocument ){
          // Complex element.. needs to be merged
          ModeledDocument subnode  = (ModeledDocument)elem;
          ModeledDocument existing = (ModeledDocument)this.getChildByMP( mp ); // TODO: add addElement to ModeledDocument interface.
          for ( ModeledElement child : subnode.getChildren() ){
            existing.build( child );
          }
        }
        else if ( elem instanceof List ){
           // Add the List contents.. they will be appended to the existing array
           List existing = (List)this.getChildByMP( mp );
           for ( Object item: (List)elem )
             existing.add( item );
        }
        else {
          // Replace element in node.
          this.appendChild(elem);
        }
    }
    
    @Override
    public void build( List<ModeledElement> stack )
    {
        // map to hold set of subnode groupings from input parameter stack
        LinkedHashMap<String, List<ModeledElement> > subnodeMap = new LinkedHashMap<>();
        // individual subnode grouping of parameters from the input stack
        List<ModeledElement> substack;

        String base = this.modelpath;       // model path of the node.
        String subnodepath;                 // model path of the subnode.
        String mp;                          // model path of the node element.
        for ( ModeledElement entry: stack ){
          // Group entries into common subnodes
          // If entry has no subnode, it is an element of this node.
          subnodepath = null;
          mp = entry.getModelpath();
          if ( mp.contains("Custom")) // TODO: - handle custom items.
            continue;
            
          // check if entry mp has nodes beyond this node (ie: belongs to a subnode)
          if ( mp.indexOf("_",base.length()+1 ) > -1 )
            subnodepath = mp.substring(0, mp.indexOf("_",base.length()+1 ));

          if ( subnodepath == null ){
            // entry is an element of this node.. 
            this.build( entry );
          }
          else {
            // Entry is ungrouped element, add to appropriate subnode list
            substack = subnodeMap.get(subnodepath);
            if ( substack == null ) {
              substack = new ArrayList<ModeledElement>();
              subnodeMap.put(subnodepath, substack);
            }
            substack.add( entry );
          }            
        }
        // Process each Subnode group and add to this node.
        for ( Map.Entry<String, List<ModeledElement>> entry: subnodeMap.entrySet() ){
          subnodepath = entry.getKey();
          substack = entry.getValue();
          
          // Build Subnode object
          MPNode subnode = new MPNode( subnodepath );
          subnode.build( substack ); // Recursive processing.

          // Add subnode to this node. (fold in)
          this.build(subnode);
        }
    }
    
    // Additional Methods
    /**
     * Generate a string representation of the document node
     * as part of a hierarchy of nodes.
     *
     * @return 
     *     {@link String }
     */
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String key;
        Iterator iter;
        
        if (this.doc.isEmpty()){
            result.append("<empty>");
            return result.toString();
        }

        String indent;
        int n;

        Object item;
        Set keys = this.doc.keySet();
        iter = keys.iterator();
        while (iter.hasNext()) {
            key = (String) iter.next();
            String[] nodes = key.split("_");
            n = nodes.length;

            // Setup indentation level
            indent = "";
            if ( n > 1 )
               indent = String.format("%" + 3*(n-1) + "s", " ");

            // Get value
            item = this.doc.get(key);

            // Setup value as array (so all entries can be handled in 1 clause)
            List inarr;
            Iterator recIter;
            if ( item.getClass() == MPArrayList.class ) {
                inarr = (List)item;
            }
            else {
                inarr = new ArrayList<Object>();
                inarr.add(item);
            }   
            recIter = inarr.iterator();
            while ( recIter.hasNext())
            {
                Object elem = recIter.next();
                if ( elem.getClass().getSimpleName().startsWith("$Proxy"))
                {   // MCD NOTE: This will not happen after the conversion..
                    result.append( elem.toString());
                }
                else
                {
                    result.append(indent).append( nodes[n-1] ).append(": " );
                    if ( Quantity.class.isInstance(elem) )
                        result.append( ((Quantity)elem).toSimpleString() );
                    else if ( elem.getClass() == MPNode.class )
                        result.append(newLine).append(((MPNode)elem).toString() );
                    else
                        result.append(elem.getClass().getSimpleName());
                    result.append(newLine);
                }
            }
        }
        return result.toString();
    }

    /**
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
    
    // Private Methods
    private void rebaseContent() 
    {
        // Re-baseline the content of the MPNode to the current model path.
        ModeledElement obj;
        for (String key: this.getChildrenMP() ) {
            // get child object.
           obj = this.doc.get( key );

           // Remove current entry for this object in data
           this.doc.remove( key );

           // Update the model path of the child object, if possible
           if ( ModeledElement.class.isInstance( obj ) ) {
             ((ModeledElement)obj).rebaseModelpath( this.modelpath );
           }
           else if ( Proxy.isProxyClass( obj.getClass()) ) {
             ModelProxy h = (ModelProxy)Proxy.getInvocationHandler( obj );
              h.data.rebaseModelpath(this.modelpath);
           }

           // Replace entry with new key.
           this.doc.put( obj.getModelpath(), obj );
       }
    }
}
