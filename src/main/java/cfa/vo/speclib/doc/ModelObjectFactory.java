/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.*;        
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Generates instances of Spectral Model interfaces. 
 * 
 * @author mdittmar
 */
public class ModelObjectFactory {
    private ArrayList<Class> allowed;
    
    public ModelObjectFactory()
    {
        this.setClassList("SPECTRUM-2.0");
    }
        
    /**
     * Generate Proxy instance for specified interface class.
     * Throws IllegalArumentException for:
     *   - null input
     *   - input does not match class supported by the factory
     * 
     * @param type
     *   Class type of desired instance 
     * 
     * @return
     *   Object proxy instance implementing the specified interface.
     */
    public Object newInstance( Class type )
    {
        Object result;
        MPNode data;
        
        if ( type == null )
            throw new IllegalArgumentException( "Argument may not be null.");

        if ( ! allowed.contains( type ))
            throw new IllegalArgumentException( type.getSimpleName() + " is not a supported Class type");

        data = new MPNode(type.getSimpleName());
        result = Proxy.newProxyInstance( data.getClass().getClassLoader(),
                                         new Class[]{ type },
                                         new ModelProxy( data, type.getSimpleName()) );
                
        return result;
    }
    
//    /**
//     * Generate Proxy instance for specified ModeledDocument.
//     * Throws IllegalArumentException for:
//     *   - null input
//     *   - input does not match class supported by the factory
//     * 
//     * @param doc
//     *   ModeledDocument representing a proxy class 
//     * 
//     * @return
//     *   Object proxy instance implementing the specified interface.
//     */
//    public Object newInstance( ModeledDocument doc )
//    {
//        Object result;
//        
//        if ( doc == null )
//            throw new IllegalArgumentException( "Argument may not be null.");
//
//        String[] keys = doc.getChildrenMP();
//        String key = keys[0];
//        if ( (keys.length == 1)&&( ! key.contains("_") ) )
//            doc = (ModeledDocument)doc.getChildByMP(key);  // Top element.. send content to build.
//        String head = key.split("_")[0];
//        result = this.newInstanceByName( head );
//        
//        // Replace underlying storage for proxy with provided content.
//        ModelProxy h = (ModelProxy)Proxy.getInvocationHandler( result );
//        h.data = (MPNode) doc;
//        
//        return result;
//    }
    
    /**
     * Generate Proxy instance for class represented by the provided class name.
     * Name match is a case-insensitive comparison against the simpleName() value
     * of the Classes supported by the factory.
     * 
     * Throws IllegalArumentException for:
     *   - null input
     *   - input does not match class supported by the factory
     * 
     * @param className
     *   Class name of desired instance 
     * 
     * @return
     *   Object proxy instance implementing the specified interface.
     */
    public Object newInstanceByName( String className )
    {
       if ( className == null )
         throw new IllegalArgumentException( "Argument may not be null.");

       Class type = null;
       for ( Class ii : this.allowed ) {
         if (ii.getSimpleName().equalsIgnoreCase( className )) {
             type = ii;
             break;
         }
       }
       return this.newInstance( type );
    }

    /**
     * Traverse the provided source Object along the specified model path 
     * to retrieve the target Object.  Intermediate objects will be created
     * as needed along the way.  If an intermediate object is a List, a new
     * element will be created and added to the List.
     * 
     * If either argument is null, an IllegalArgumentException is thrown.
     * 
     * @param obj
     *    Proxy Instance to traverse to desired object. 
     *
     * @param mp
     *    Model Path to desired object.
     * @return
     *    Target Object of model path.
     */
    public Object newInstanceByModelPath( Object source, String mp )
    {
        Object item;
        Object next = null;
        Object elem;
        Method m;         // 'get' method for property
        String property;  // property to get

        if ( (source == null)||(mp == null) )
            throw new IllegalArgumentException( "Arguments may not be null.");

        item = source;
        for ( String node: mp.split("_")){
          if ( node.contains("[]") ){
            // Handle array element..
            String[] parts = node.split("\\[\\]\\.*");
            property = parts[0];
            if ( parts.length == 1 ) // No type specified.. should be Quantity
              elem = new MPQuantity();
            else  // Generate proxy instance of specified type.
              elem = this.newInstanceByName(parts[1]);
          }
          else{
            // Not an array node.
            elem = null;
            property = node;
          }
          // Get method to retrieve element.
          try {
            m = item.getClass().getMethod("get"+property, (Class<?>[]) null);
          }
          catch (NoSuchMethodException ex) {
            try { // Booleans do not have get method, try 'is' instead.
              m = item.getClass().getMethod("is"+property, (Class<?>[]) null);
            }
            catch (NoSuchMethodException ex2) {
              throw new IllegalArgumentException("Invalid model path.. issue at node "+property);
            }
          }
          // Invoke method to retrieve element.
          try {
             next = m.invoke( item, (Object[]) null);
           } catch (IllegalAccessException ex) {
                Logger.getLogger(ModelObjectFactory.class.getName()).log(Level.SEVERE, null, ex);
           } catch (InvocationTargetException ex) {
                Logger.getLogger(ModelObjectFactory.class.getName()).log(Level.SEVERE, null, ex);
           }
           item = next;
           
           // Add Array element, as needed
           if ( elem != null && item != null )
           {
               ((MPArrayList)item).add(elem);
               item = elem;
           }
        }
        
        return item;
    }

    public Object build( MPNode doc )
    {
        Object result;

        // Get keys from doc
        String[] keys = doc.getChildrenMP();
        String key = keys[0];
        if ( (keys.length == 1)&&( ! key.contains("_") ) )
            doc = (MPNode)doc.getChildByMP(key);  // Top element.. send content to build.

        result = this.build( null, doc );
        return result;
    }

    private Object build( String base, MPNode doc )
    {
        Object result;
        
        // Get First key from doc
        String[] keys = doc.getChildrenMP();
        String key = keys[0];

        // Strip base from path.. we want it relative to current node.
        String path;
        if ( base == null)
          path = key;
        else
        {
          String regex = "^".concat(base).concat("_*").replace("[]", "\\[\\]").replace("].","]\\.");
          path = key.replaceFirst(regex, "");

          // Handle array elements
          if ( path.startsWith("[]."))  // Complex Array element
          {
            base = base.concat(path.substring(0,3));
            path = path.substring(3);
          }
        }

        // Instantiate first 'node' as container for doc content.
        String head = path.split("_")[0];
        result = this.newInstanceByName( head );

        // Populate the container with doc content.
        String newbase;
        if ( base == null )
            newbase = head;
        else if ( base.endsWith("[]."))
            newbase = base + head; // Array elment
        else
            newbase = base+"_"+head; // Regular node
 
        this.build( result, newbase, doc );
        
        return(result);
    }
    
    private void build( Object parent, String base, MPNode doc )
    {
       String mp;       // path to element in doc.. relative to this node.
       Object obj;      // element from parent being added.
       Object value;    // value from document.
       MPArrayList srclist;
       MPArrayList destlist;

       for ( String key: doc.getChildrenMP() )
       {
          if ( key.equals("SpectralDataset_Length"))
              continue;  // TODO: How to handle this calculated element.. cannot be 'set'
          
          // Strip base from key, including associated array annotation.
          String regex = "^".concat(base).concat("_*").replace("[]", "\\[\\]").replace("].","]\\.");
          mp = key.replaceFirst( regex, "");

          // Pull object from parent node
          obj = this.newInstanceByModelPath(parent, mp);
          
          // Get value from doc mapping to this element.
          value = doc.getChildByMP(key);
          if ( (obj.getClass() == Boolean.class ) && ( value.getClass() == MPQuantity.class )){
              // Boolean elements should have set<Property>( Quantity ) method..
              // Pull container object and invoke set method.. Note: parent may be the container.
              Object container;
              String property;
              if ( mp.contains("_")){
                property = mp.substring(mp.lastIndexOf("_"));
                mp = mp.substring(0, mp.lastIndexOf("_"));
                container = this.newInstanceByModelPath(parent, mp);
              }
              else {
                property = mp;
                container = parent;
              }
              // Invoke set method using source Quantity to fill.
              Method m;
              try {
                m = container.getClass().getMethod("set"+property, new Class[]{Quantity.class} );
                m.invoke( container, new Object[]{value} );
              }
              catch (NoSuchMethodException ex2) {
                throw new IllegalArgumentException("Boolean element with no set method - "+key);
              }
              catch (IllegalAccessException ex) {
                  Logger.getLogger(ModelObjectFactory.class.getName()).log(Level.SEVERE, null, ex);
              } catch (IllegalArgumentException ex) {
                  Logger.getLogger(ModelObjectFactory.class.getName()).log(Level.SEVERE, null, ex);
              } catch (InvocationTargetException ex) {
                  Logger.getLogger(ModelObjectFactory.class.getName()).log(Level.SEVERE, null, ex);
              }

          }
          else if ( (obj.getClass() == MPQuantity.class ) && ( value.getClass() == MPQuantity.class ))
          {
            // Transfer quantity content
            ((MPQuantity)obj).fill( (MPQuantity)value );
          }
          else if ( Proxy.isProxyClass( obj.getClass() ) && value.getClass() == MPNode.class )
          {
              // Build child node onto object.
              this.build( obj, key, (MPNode)value);
          }
          else if ( (obj.getClass() == MPArrayList.class ) && (value.getClass() == MPArrayList.class ) )
          {
              srclist = (MPArrayList)value;
              destlist = (MPArrayList)obj;
              for ( Object elem: srclist )
              {
                  if ( elem.getClass() == MPQuantity.class )
                  {
                     destlist.add( (MPQuantity)elem );
                  }
                  else if ( elem.getClass() == MPNode.class )
                  {  // Array of complex objects..
                     Object entry = this.build( key, (MPNode)elem );
                     destlist.add( entry );
                  }                      
              }
          }
          else
          {
             throw new UnsupportedOperationException(" Value type ("+value.getClass().getSimpleName()+" is not compatible with Element type ("+obj.getClass().getSimpleName()+").  Element = "+key);
          }
       }
    }
            
    // Private Methods
    
    /**
     * Load the supported Class list for the specified model.
     * Throws IllegalArgumentException if the specified model is not supported.
     * 
     * @param modelName
     *    Model name and version (e.g. "SPECTRUM-2.0" )
     */
    private void setClassList( String modelName )
    {
        if ( modelName.equals( "SPECTRUM-2.0" ) )
            setSpectralClassList();
        else
          throw new IllegalArgumentException("Invalid or unrecognized Model name.");        
    }
    
    /**
     * Load the supported Class list with Spectral-2.0 model interfaces.
     * TODO: can this be more dynamic? maybe derived from model definition?
     */
    private void setSpectralClassList()
    {
        allowed = new ArrayList<Class>();
        allowed.add( Accuracy.class);
        allowed.add( ApFrac.class);
        allowed.add( Bandpass.class);
        allowed.add( Bounds.class);
        allowed.add( Characterization.class);
        allowed.add( CharacterizationAxis.class);
        allowed.add( Contact.class);
        allowed.add( CoordFrame.class);
        allowed.add( CoordSys.class);
        allowed.add( Correction.class);
        allowed.add( Coverage.class);
        allowed.add( Curation.class);
        allowed.add( DataAxis.class);
        allowed.add( DataID.class);
        allowed.add( DataModel.class);
        allowed.add( DataSource.class);
        allowed.add( Dataset.class);
        allowed.add( Derived.class);
        allowed.add( Facility.class);
        allowed.add( FluxCharAxis.class);
        allowed.add( FluxDataAxis.class);
        allowed.add( FluxFrame.class);
        allowed.add( GenericCorr.class);
        allowed.add( Instrument.class);
        allowed.add( Interval.class);
        allowed.add( Location.class);
        allowed.add( ObsConfig.class);
        allowed.add( ObservingElement.class);
        allowed.add( Proposal.class);
        allowed.add( QualityCode.class);
        allowed.add( Redshift.class);
        allowed.add( RedshiftFrame.class);
        allowed.add( ResolPower.class);
        allowed.add( Resolution.class);
        allowed.add( SPPoint.class);
        allowed.add( SamplingPrecision.class);
        allowed.add( SamplingPrecisionRefVal.class);
        allowed.add( SpaceFrame.class);
        allowed.add( SpatialCharAxis.class);
        allowed.add( SpectralCharAxis.class);
        allowed.add( SpectralDataAxis.class);
        allowed.add( SpectralDataset.class);
        allowed.add( SpectralFrame.class);
        allowed.add( SpectralResolution.class);
        allowed.add( Support.class);
        allowed.add( Target.class);
        allowed.add( TimeCharAxis.class);
        allowed.add( TimeFrame.class);
        allowed.add( PhotCal.class);          //TODO: Photometry Model
        allowed.add( ZeroPoint.class);        //TODO: Photometry Model
        allowed.add( MagnitudeSystem.class);  //TODO: Photometry Model
    }
}
