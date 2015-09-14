/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.*;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;

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
        return newInstance( type, null );
    }
    
    public Object newInstance( Class type, MPNode data )
    {
        Object result;
        
        if ( type == null )
            throw new IllegalArgumentException( "Class type may not be null.");

        if ( data == null )
          // Generate MPNode storage element for this proxy.
          data = new MPNode(type.getSimpleName());

        if ( ! allowed.contains( type ))
            throw new IllegalArgumentException( type.getSimpleName() + " is not a supported Class type");

        result = Proxy.newProxyInstance( data.getClass().getClassLoader(),
                                         new Class[]{ type },
                                         new ModelProxy( data, type.getSimpleName(), this ) );
                
        return result;
    }
    
    public Object newInstanceByModelPath( String mp )
    {
        return newInstanceByModelPath( mp, null );
    }
    
    public Object newInstanceByModelPath( String mp, MPNode data )
    {
      Object result;
      Method m;

      // Parse model path.. get last node
      Class type = null;
      String[] nodes = mp.split("_");
      for ( String node: nodes ){
        // Handle array element..
        // For these, we want to reset class to the specific type indicated
        // in the model path for this particular element.
        if ( node.contains("[]") ){
            String[] parts = node.split("\\[\\]\\.*");
            String property = parts[0];  // container 
            if ( parts.length == 1 ) {
              // No type specified.. should be Quantity..
              type = Quantity.class;
              continue;
            }
            else {
              // clear type.. for reset.
              type = null;
              node = parts[1];
            }
        }
        if ( type == null ) {
            type = matchClassName( node );
            continue;
        }

        // Get class type for this node by checking the return type of the 
        // 'get' method in the current class.
        try {
          m = type.getMethod("get"+node, (Class<?>[]) null);
        }
        catch (NoSuchMethodException ex) {
          try { // Booleans do not have get method, try 'is' instead.
            m = type.getMethod("is"+node, (Class<?>[]) null);
          }
          catch (NoSuchMethodException ex2) {
            throw new IllegalArgumentException("Invalid model path.. issue at node "+node);
          }
        }
        // Set type of next node as return type of the method.
        type = m.getReturnType();
      }

      // Generate instance of specified type.
      result = this.newInstance( type, data );
      
      return result;
    }
    
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
       return this.newInstanceByName( className, null );
    }

    public Object newInstanceByName( String className, MPNode data )
    {
       if ( className == null )
         throw new IllegalArgumentException( "Argument may not be null.");

       // Find class matching name.
       Class type = this.matchClassName(className);
       
       return this.newInstance( type, data );
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
    
    private Class matchClassName( String className )
    {
       Class type = null;
       
       for ( Class ii : this.allowed ) {
         if (ii.getSimpleName().equalsIgnoreCase( className )) {
             type = ii;
             break;
         }
       }
       return type; 
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
