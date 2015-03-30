/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.*;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for SpectralFactory class.
 * 
 * @author mdittmar
 */
public class TestFactory {

   static SpectralFactory factory;
   static boolean verbose;

    public TestFactory() {
    }
    
    @BeforeClass
    public static void setUpClass() 
    {
        verbose = false;

        if ( verbose )
            System.out.println("Create New Factory.");

        // create a Spectral object factory
        factory = new SpectralFactory();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test basic functionality..
     *   - ability to generate Spectral objects
     */
    @Test
    public void testBasic()
    {
        Object obj;
                
        try {
          // instantiate interfaces for Spectral model nodes.
          if ( verbose ){ System.out.println(" Create Accuracy object");}
          obj = factory.newInstance( Accuracy.class );
          assertNotNull( obj.getClass().getMethod("getBinSize") );

          if ( verbose ){ System.out.println(" Create ApFrac object");}
          obj = factory.newInstance( ApFrac.class );
          assertNotNull( obj.getClass().getMethod("isApplied") );

          if ( verbose ){ System.out.println(" Create Bandpass object");}
          obj = factory.newInstance( Bandpass.class );
          assertNotNull( obj.getClass().getMethod("getName") );

          if ( verbose ){ System.out.println(" Create Bounds object");}
          obj = factory.newInstance( Bounds.class );
          assertNotNull( obj.getClass().getMethod("getExtent") );

          if ( verbose ){ System.out.println(" Create Characterization object");}
          obj = factory.newInstance( Characterization.class );
          assertNotNull( obj.getClass().getMethod("getCharacterizationAxes") );

          if ( verbose ){ System.out.println(" Create CharacterizationAxis object");}
          obj = factory.newInstance( CharacterizationAxis.class );
          assertNotNull( obj.getClass().getMethod("getName") );

          if ( verbose ){ System.out.println(" Create Contact object");}
          obj = factory.newInstance( Contact.class );
          assertNotNull( obj.getClass().getMethod("getEmail") );

          if ( verbose ){ System.out.println(" Create CoordFrame object");}
          obj = factory.newInstance( CoordFrame.class );
          assertNotNull( obj.getClass().getMethod("getName") );
          assertNotNull( obj.getClass().getMethod("getReferencePosition") );

          if ( verbose ){ System.out.println(" Create CoordSys object");}
          obj = factory.newInstance( CoordSys.class );
          assertNotNull( obj.getClass().getMethod("getTimeFrame") );

          if ( verbose ){ System.out.println(" Create Correction object");}
          obj = factory.newInstance( Correction.class );
          assertNotNull( obj.getClass().getMethod("isApplied") );

          if ( verbose ){ System.out.println(" Create Coverage object");}
          obj = factory.newInstance( Coverage.class );
          assertNotNull( obj.getClass().getMethod("getLocation") );

          if ( verbose ){ System.out.println(" Create Curation object");}
          obj = factory.newInstance( Curation.class );
          assertNotNull( obj.getClass().getMethod("getRights") );

          if ( verbose ){ System.out.println(" Create DataAxis object");}
          obj = factory.newInstance( DataAxis.class );
          assertNotNull( obj.getClass().getMethod("getCorrections") );
          
          if ( verbose ){ System.out.println(" Create DataID object");}
          obj = factory.newInstance( DataID.class );
          assertNotNull( obj.getClass().getMethod("getCreator") );

          if ( verbose ){ System.out.println(" Create DataModel object"); }
          obj = factory.newInstance( DataModel.class );
          assertNotNull( obj.getClass().getMethod("getPrefix") );

          if ( verbose ){ System.out.println(" Create DataSource object");}
          obj = factory.newInstance( DataSource.class );
          assertNotNull( obj.getClass().getMethod("getName") );

          if ( verbose ){ System.out.println(" Create Dataset object");}
          obj = factory.newInstance( Dataset.class );
          assertNotNull( obj.getClass().getMethod("getDataModel") );
                  
          if ( verbose ){ System.out.println(" Create Derived object"); }
          obj = factory.newInstance( Derived.class );
          assertNotNull( obj.getClass().getMethod("getSNR") );

          if ( verbose ){ System.out.println(" Create Facility object");}
          obj = factory.newInstance( Facility.class );
          assertNotNull( obj.getClass().getMethod("getName") );

          if ( verbose ){ System.out.println(" Create FluxDataAxis object");}
          obj = factory.newInstance( FluxDataAxis.class );
          assertNotNull( obj.getClass().getMethod("getResolution") );

          if ( verbose ){ System.out.println(" Create FluxFrame object");}
          obj = factory.newInstance( FluxFrame.class );
          assertNotNull( obj.getClass().getMethod("getReferencePosition") );
          assertNotNull( obj.getClass().getMethod("getRefID") );

          if ( verbose ){ System.out.println(" Create GenericCorr object");}
          obj = factory.newInstance( GenericCorr.class );
          assertNotNull( obj.getClass().getMethod("getValue") );
          
          if ( verbose ){ System.out.println(" Create Instrument object");}
          obj = factory.newInstance( Instrument.class );
          assertNotNull( obj.getClass().getMethod("getName") );

          if ( verbose ){ System.out.println(" Create Location object");}
          obj = factory.newInstance( Location.class );
          assertNotNull( obj.getClass().getMethod("getValue") );
          
          if ( verbose ){ System.out.println(" Create ObsConfig object");}
          obj = factory.newInstance( ObsConfig.class );
          assertNotNull( obj.getClass().getMethod("getObservingElements") );

          if ( verbose ){ System.out.println(" Create Proposal object");}
          obj = factory.newInstance( Proposal.class );
          assertNotNull( obj.getClass().getMethod("getIdentifier") );

          if ( verbose ){ System.out.println(" Create QualityCode object"); }
          obj = factory.newInstance( QualityCode.class );
          assertNotNull( obj.getClass().getMethod("getDefinition") );

          if ( verbose ){ System.out.println(" Create Redshift object"); }
          obj = factory.newInstance( Redshift.class );
          assertNotNull( obj.getClass().getMethod("getConfidence") );

          if ( verbose ){ System.out.println(" Create RedshiftFrame object");}
          obj = factory.newInstance( RedshiftFrame.class );
          assertNotNull( obj.getClass().getMethod("getReferencePosition") );
          assertNotNull( obj.getClass().getMethod("getDopplerDefinition") );
          
          if ( verbose ){ System.out.println(" Create ResolPower object"); }
          obj = factory.newInstance( ResolPower.class );
          assertNotNull( obj.getClass().getMethod("getRefVal") );

          if ( verbose ){ System.out.println(" Create Resolution object"); }
          obj = factory.newInstance( Resolution.class );
          assertNotNull( obj.getClass().getMethod("getRefVal") );

          if ( verbose ){ System.out.println(" Create SamplingPrecision object");}
          obj = factory.newInstance( SamplingPrecision.class );
          assertNotNull( obj.getClass().getMethod("getSampleExtent") );

          if ( verbose ){ System.out.println(" Create SPPoint object");}
          obj = factory.newInstance( SPPoint.class );
          assertNotNull( obj.getClass().getMethod("getFluxAxis") );
          
          if ( verbose ){ System.out.println(" Create SpaceFrame object");}
          obj = factory.newInstance( SpaceFrame.class );
          assertNotNull( obj.getClass().getMethod("getReferencePosition") );
          assertNotNull( obj.getClass().getMethod("getEquinox") );

          if ( verbose ){ System.out.println(" Create Spectral Data Axis object");}
          obj = factory.newInstance( SpectralDataAxis.class );
          assertNotNull( obj.getClass().getMethod("getAccuracy") );

          if ( verbose ){ System.out.println(" Create Spectral Dataset object");}
          obj = factory.newInstance( SpectralDataset.class );
          assertNotNull( obj.getClass().getMethod("getFluxSI") );
          
          if ( verbose ){ System.out.println(" Create SpectralFrame object");}
          obj = factory.newInstance( SpectralFrame.class );
          assertNotNull( obj.getClass().getMethod("getReferencePosition") );
          assertNotNull( obj.getClass().getMethod("getRedshift") );

          if ( verbose ){ System.out.println(" Create SpectralResolution object"); }
          obj = factory.newInstance( SpectralResolution.class );
          assertNotNull( obj.getClass().getMethod("getResolPower") );

          if ( verbose ){ System.out.println(" Create Support object");}
          obj = factory.newInstance( Support.class );
          assertNotNull( obj.getClass().getMethod("getArea") );
          
          if ( verbose ){ System.out.println(" Create Target object"); }
          obj = factory.newInstance( Target.class );
          assertNotNull( obj.getClass().getMethod("getTargetClass") );

          if ( verbose ){ System.out.println(" Create TimeFrame object");}
          obj = factory.newInstance( TimeFrame.class );
          assertNotNull( obj.getClass().getMethod("getReferencePosition") );
          assertNotNull( obj.getClass().getMethod("getZero") );

        } catch (Exception ex) {
           fail( ex.getMessage() );
        }
    }

    /**
     * Test that the factory only generates interfaces within the scope
     * of Spectral model objects.
     */
    @Test( expected = IllegalArgumentException.class )
    public void testScope()
    {
        if ( verbose )
            System.out.println(" Create List object");
        List l = (List)factory.newInstance( List.class );
    }
    
    /**
     * Test Equals method
     */
    @Test
    public void testEquals()
    {
        DataModel a, b;
        Dataset c;
        
        a = (DataModel)factory.newInstance( DataModel.class );
        b = (DataModel)factory.newInstance( DataModel.class );
        c = (Dataset)factory.newInstance( Dataset.class );

        assertTrue( a.equals(a) );
        assertTrue( a == a );
        if ( verbose )
            System.out.println("A.equals(A) && A == A");

        assertTrue( a.equals(b) );
        assertFalse( a == b );
        if ( verbose )
            System.out.println("A.equals(B) && A != B");

        assertFalse( a.equals(c) );
        if ( verbose )
            System.out.println("!A.equals(C)");

    }
}