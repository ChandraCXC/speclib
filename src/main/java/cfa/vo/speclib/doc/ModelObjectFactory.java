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
        ModelDocument data;
        
        if ( type == null )
            throw new IllegalArgumentException( "Argument may not be null.");

        if ( ! allowed.contains( type ))
            throw new IllegalArgumentException( type.getSimpleName() + " is not a supported Class type");

        data = new ModelDocument();
        result = Proxy.newProxyInstance( data.getClass().getClassLoader(),
                                         new Class[]{ type },
                                         new ModelProxy( data, type.getSimpleName(), type.getSimpleName()) );
                
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
       if ( className == null )
         throw new IllegalArgumentException( "Argument may not be null.");

       Class type = null;
       for ( Class ii : this.allowed )
       {
         if (ii.getSimpleName().equalsIgnoreCase( className ))
         {
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
        for ( String node: mp.split("_"))
        {
           if ( node.contains("[]") )
           {
               // Handle array element..
               String[] parts = node.split("\\[\\]\\.*");
               property = parts[0];
               if ( parts.length == 1 )
               {
                   // No type specified.. should be Quantity
                   elem = new Quantity();
               }
               else
               {
                   // Generate proxy instance of specified type.
                   elem = this.newInstanceByName(parts[1]);
               }
           }
           else
           {
               // Not an array node.
               elem = null;
               property = node;
           }
           
           try {
             m = item.getClass().getMethod("get"+property, (Class<?>[]) null);
             next = m.invoke( item, (Object[]) null);
            } catch (NoSuchMethodException ex) {
                throw new IllegalArgumentException("Invalid model path.. issue at node "+property);
            } catch (SecurityException ex) {
                Logger.getLogger(ModelObjectFactory.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ModelObjectFactory.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
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
        allowed.add( SpectralDataAxis.class);
        allowed.add( SpectralDataset.class);
        allowed.add( SpectralFrame.class);
        allowed.add( SpectralResolution.class);
        allowed.add( Support.class);
        allowed.add( Target.class);
        allowed.add( TimeFrame.class);
    }
}
