/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.*;        
import java.lang.reflect.Proxy;
import java.util.ArrayList;

/**
 *  Generates instances of Spectral Model interfaces. 
 * 
 * @author mdittmar
 */
public class SpectralFactory {
    private ArrayList<Class> allowed;
    
    public SpectralFactory()
    {
        allowed = new ArrayList<Class>();
        allowed.add( Accuracy.class);
        allowed.add( ApFrac.class);
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
        allowed.add( Dataset.class);
        allowed.add( Derived.class);
        allowed.add( FluxFrame.class);
        allowed.add( GenericCorr.class);
        allowed.add( Location.class);
        allowed.add( Point.class);
        allowed.add( QualityCode.class);
        allowed.add( Redshift.class);
        allowed.add( RedshiftFrame.class);
        allowed.add( ResolPower.class);
        allowed.add( Resolution.class);
        allowed.add( SamplingPrecision.class);
        allowed.add( SamplingPrecisionRefVal.class);
        allowed.add( SpaceFrame.class);
        allowed.add( Spectral.class);
        allowed.add( SpectralFrame.class);
        allowed.add( SpectralResolution.class);
        allowed.add( Support.class);
        allowed.add( Target.class);
        allowed.add( TimeFrame.class);
    }
        
    public Object newInstance( Class type )
    {
        Object result;
        SpectralDocument data;
        
        if ( ! allowed.contains( type ))
            throw new IllegalArgumentException( type.getSimpleName() + " is not a Spectral Class type");

        data = new SpectralDocument();
        result = Proxy.newProxyInstance( data.getClass().getClassLoader(),
                                         new Class[]{ type },
                                         new SpectralProxy( data, type.getSimpleName()) );
                
        return result;
    }
}
