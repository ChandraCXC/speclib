/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.Quantity;
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
        
    public ModelObjectFactory( String modelname )
    {
        this.setClassList( modelname );
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
        String key = modelName.toUpperCase();
        if ( key.equals( "SPECTRUM-2.0" ) )
            setSpectralClassList();
        else if ( key.equals( "SPECTRUM-1.0" ))
            setSpectrum1ClassList();
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
        allowed.add( cfa.vo.speclib.spectral.v2.Accuracy.class);
        allowed.add( cfa.vo.speclib.spectral.v2.ApFrac.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Bandpass.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Bounds.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Characterization.class);
        allowed.add( cfa.vo.speclib.spectral.v2.CharacterizationAxis.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Contact.class);
        allowed.add( cfa.vo.speclib.spectral.v2.CoordFrame.class);
        allowed.add( cfa.vo.speclib.spectral.v2.CoordSys.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Correction.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Coverage.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Curation.class);
        allowed.add( cfa.vo.speclib.spectral.v2.DataAxis.class);
        allowed.add( cfa.vo.speclib.spectral.v2.DataID.class);
        allowed.add( cfa.vo.speclib.spectral.v2.DataModel.class);
        allowed.add( cfa.vo.speclib.spectral.v2.DataSource.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Dataset.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Derived.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Facility.class);
        allowed.add( cfa.vo.speclib.spectral.v2.FluxCharAxis.class);
        allowed.add( cfa.vo.speclib.spectral.v2.FluxDataAxis.class);
        allowed.add( cfa.vo.speclib.spectral.v2.FluxFrame.class);
        allowed.add( cfa.vo.speclib.spectral.v2.GenericCorr.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Instrument.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Interval.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Location.class);
        allowed.add( cfa.vo.speclib.spectral.v2.ObsConfig.class);
        allowed.add( cfa.vo.speclib.spectral.v2.ObservingElement.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Proposal.class);
        allowed.add( cfa.vo.speclib.spectral.v2.QualityCode.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Redshift.class);
        allowed.add( cfa.vo.speclib.spectral.v2.RedshiftFrame.class);
        allowed.add( cfa.vo.speclib.spectral.v2.ResolPower.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Resolution.class);
        allowed.add( cfa.vo.speclib.spectral.v2.SPPoint.class);
        allowed.add( cfa.vo.speclib.spectral.v2.SamplingPrecision.class);
        allowed.add( cfa.vo.speclib.spectral.v2.SamplingPrecisionRefVal.class);
        allowed.add( cfa.vo.speclib.spectral.v2.SpaceFrame.class);
        allowed.add( cfa.vo.speclib.spectral.v2.SpatialCharAxis.class);
        allowed.add( cfa.vo.speclib.spectral.v2.SpectralCharAxis.class);
        allowed.add( cfa.vo.speclib.spectral.v2.SpectralDataAxis.class);
        allowed.add( cfa.vo.speclib.spectral.v2.SpectralDataset.class);
        allowed.add( cfa.vo.speclib.spectral.v2.SpectralFrame.class);
        allowed.add( cfa.vo.speclib.spectral.v2.SpectralResolution.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Support.class);
        allowed.add( cfa.vo.speclib.spectral.v2.Target.class);
        allowed.add( cfa.vo.speclib.spectral.v2.TimeCharAxis.class);
        allowed.add( cfa.vo.speclib.spectral.v2.TimeFrame.class);
        allowed.add( cfa.vo.speclib.spectral.v2.PhotCal.class);          //TODO: Photometry Model
        allowed.add( cfa.vo.speclib.spectral.v2.ZeroPoint.class);        //TODO: Photometry Model
        allowed.add( cfa.vo.speclib.spectral.v2.MagnitudeSystem.class);  //TODO: Photometry Model
    }
    
    /**
     * Load the supported Class list with Spectrum-1.1 model interfaces.
     */
    private void setSpectrum1ClassList()
    {
        allowed = new ArrayList<Class>();
//        allowed.add( cfa.vo.speclib.spectral.v1.Accuracy.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.ApFrac.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Bandpass.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Bounds.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Characterization.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.CharacterizationAxis.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Contact.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.CoordFrame.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.CoordSys.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Correction.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Coverage.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Curation.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.DataAxis.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.DataID.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.DataModel.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.DataSource.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Dataset.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Derived.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Facility.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.FluxCharAxis.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.FluxDataAxis.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.FluxFrame.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.GenericCorr.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Instrument.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Interval.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Location.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.ObsConfig.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.ObservingElement.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Proposal.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.QualityCode.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Redshift.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.RedshiftFrame.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.ResolPower.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Resolution.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.SPPoint.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.SamplingPrecision.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.SamplingPrecisionRefVal.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.SpaceFrame.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.SpatialCharAxis.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.SpectralCharAxis.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.SpectralDataAxis.class);
        allowed.add( cfa.vo.speclib.spectral.v1.SpectralDataset.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.SpectralFrame.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.SpectralResolution.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Support.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.Target.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.TimeCharAxis.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.TimeFrame.class);
//        allowed.add( cfa.vo.speclib.spectral.v1.PhotCal.class);          //TODO: Photometry Model
//        allowed.add( cfa.vo.speclib.spectral.v1.ZeroPoint.class);        //TODO: Photometry Model
//        allowed.add( cfa.vo.speclib.spectral.v1.MagnitudeSystem.class);  //TODO: Photometry Model
    }

}
