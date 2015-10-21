/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.spectral.v2.SamplingPrecision;
import cfa.vo.speclib.spectral.v2.Location;
import cfa.vo.speclib.spectral.v2.Characterization;
import cfa.vo.speclib.spectral.v2.SpectralDataAxis;
import cfa.vo.speclib.spectral.v2.SpectralResolution;
import cfa.vo.speclib.spectral.v2.ApFrac;
import cfa.vo.speclib.spectral.v2.SpectralDataset;
import cfa.vo.speclib.spectral.v2.Contact;
import cfa.vo.speclib.spectral.v2.FluxCharAxis;
import cfa.vo.speclib.spectral.v2.Resolution;
import cfa.vo.speclib.spectral.v2.SpectralFrame;
import cfa.vo.speclib.spectral.v2.Target;
import cfa.vo.speclib.spectral.v2.SPPoint;
import cfa.vo.speclib.spectral.v2.Bounds;
import cfa.vo.speclib.spectral.v2.CoordSys;
import cfa.vo.speclib.spectral.v2.CoordFrame;
import cfa.vo.speclib.spectral.v2.Derived;
import cfa.vo.speclib.spectral.v2.GenericCorr;
import cfa.vo.speclib.spectral.v2.FluxFrame;
import cfa.vo.speclib.spectral.v2.ObsConfig;
import cfa.vo.speclib.spectral.v2.RedshiftFrame;
import cfa.vo.speclib.spectral.v2.ObservingElement;
import cfa.vo.speclib.spectral.v2.Accuracy;
import cfa.vo.speclib.spectral.v2.TimeFrame;
import cfa.vo.speclib.spectral.v2.DataModel;
import cfa.vo.speclib.spectral.v2.Correction;
import cfa.vo.speclib.spectral.v2.SpaceFrame;
import cfa.vo.speclib.spectral.v2.Proposal;
import cfa.vo.speclib.spectral.v2.DataSource;
import cfa.vo.speclib.spectral.v2.Facility;
import cfa.vo.speclib.spectral.v2.DataID;
import cfa.vo.speclib.spectral.v2.Bandpass;
import cfa.vo.speclib.spectral.v2.FluxDataAxis;
import cfa.vo.speclib.spectral.v2.DataAxis;
import cfa.vo.speclib.spectral.v2.CharacterizationAxis;
import cfa.vo.speclib.spectral.v2.ResolPower;
import cfa.vo.speclib.spectral.v2.Redshift;
import cfa.vo.speclib.spectral.v2.Dataset;
import cfa.vo.speclib.spectral.v2.Curation;
import cfa.vo.speclib.spectral.v2.QualityCode;
import cfa.vo.speclib.spectral.v2.Support;
import cfa.vo.speclib.spectral.v2.Instrument;
import cfa.vo.speclib.spectral.v2.Coverage;
import cfa.vo.speclib.*;
import java.lang.reflect.Proxy;
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

   static ModelObjectFactory factory;
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
        factory = new ModelObjectFactory();
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
     * Test Model path updates
     */
    @Test
    public void testModelPathUpdates()
    {
       Contact contact;
       Curation curation;
       SpectralDataset ds;
       String mp;
       String expected;
       ModelProxy h;
       try 
       {
          // Create Contact object.
          contact = (Contact)factory.newInstance( Contact.class );
          contact.setName("CXC Help Desk");
          contact.setEmail("cxchelp@cfa.harvard.edu");
          h = (ModelProxy)Proxy.getInvocationHandler( contact );
          
          // Check model path.
          expected = "Contact";
          mp = h.data.getModelpath();
          assertEquals( expected, mp );
          assertEquals( expected, h.type );

          // Add to Curation
          expected = "Curation_Contact";
          curation = (Curation)factory.newInstance( Curation.class );
          curation.setContact( contact );
          mp = h.data.getModelpath();
          assertEquals( expected, mp );
          assertEquals( "Contact", h.type );

          // Add to Dataset
          expected = "SpectralDataset_Curation_Contact";
          ds = (SpectralDataset)factory.newInstance( SpectralDataset.class );
          ds.setCuration(curation);
          mp = h.data.getModelpath();
          assertEquals( expected, mp );
          assertEquals( "Contact", h.type );
          
       } catch (Exception ex) {
           fail( ex.getMessage() );
       }
    }

    /**
     * Test Object building with List elements
     */
    @Test
    public void testBuildList()
    {
        ModelObjectFactory factory = new ModelObjectFactory();
        String expected;

        ObsConfig oc;
        MPArrayList<ObservingElement> elarr = new MPArrayList<ObservingElement>();
        // ++++++++++++++++++++++++++++++++++++++++++++++
        // add Proxy to MPArrayList.
        Facility   f = (Facility)factory.newInstance( Facility.class );
        Instrument i = (Instrument)factory.newInstance( Instrument.class );
        Bandpass   b = (Bandpass)factory.newInstance( Bandpass.class );
        elarr.add(f);
        elarr.add(i);
        elarr.add(b);

        assertEquals( 3, elarr.size() );
        expected = "UNKNOWN[].Facility_Name";
        assertEquals( expected, ((MPQuantity)elarr.get(0).getName()).getModelpath() );
        expected = "UNKNOWN[].Instrument_Name";
        assertEquals( expected, ((MPQuantity)elarr.get(1).getName()).getModelpath() );
        expected = "UNKNOWN[].Bandpass_Name";
        assertEquals( expected, ((MPQuantity)elarr.get(2).getName()).getModelpath() );

        // add List to Proxy
        oc = (ObsConfig)factory.newInstance( ObsConfig.class );
        oc.setObservingElements(elarr);
        expected = "ObsConfig_ObservingElements[].Facility_Name";
        assertEquals( expected, ((MPQuantity)elarr.get(0).getName()).getModelpath() );
        expected = "ObsConfig_ObservingElements[].Instrument_Name";
        assertEquals( expected, ((MPQuantity)elarr.get(1).getName()).getModelpath() );
        expected = "ObsConfig_ObservingElements[].Bandpass_Name";
        assertEquals( expected, ((MPQuantity)elarr.get(2).getName()).getModelpath() );
        
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
    @Test
    public void testNewInstanceByName()
    {
       if (verbose)
         System.out.println("testNewInstanceByName");

       String className = null;
       Object result = null;

        // Null input
        boolean caught = false;
        try{
          result = factory.newInstanceByName( className );
        }
        catch (IllegalArgumentException ex ){
            caught = true;
        }
        assertTrue( caught );
        assertNull( result );
       
        // Regular Class
        className = "Accuracy";
        result = factory.newInstanceByName( className );
        assertNotNull( result );

        // No matching Class
        result = null;
        className = "Bob";
        caught = false;
        try{
          result = factory.newInstanceByName( className );
        }
        catch (IllegalArgumentException ex ){
            caught = true;
        }
        assertTrue( caught );
        assertNull( result );       
    }
    
    @Test
    public void testNewInstanceByModelPath2()
    {   
        Object result;
        String mp;
        boolean ok;
        
        result = (SpectralDataset)factory.newInstanceByModelPath( "SpectralDataset" );
        assertNotNull( result );
        assertTrue( result instanceof SpectralDataset );
        
        // Normal 
        mp = "SpectralDataset_Curation";
        result = factory.newInstanceByModelPath( mp );
        assertNotNull( result );
        assertTrue( result instanceof Curation );
        
        mp = "SpectralDataset_Curation_Contact";
        result = factory.newInstanceByModelPath( mp );
        assertNotNull( result );
        assertTrue( result instanceof Contact );

        // Array element
        mp = "SpectralDataset_Data[].SPPoint";
        result = factory.newInstanceByModelPath( mp );
        assertNotNull( result );
        assertTrue( result instanceof SPPoint );
        
        // Subclass
        mp = "SpectralDataset_Data[].SPPoint_FluxAxis";
        result = factory.newInstanceByModelPath( mp );
        assertNotNull( result );
        assertTrue( result instanceof FluxDataAxis );

        // Subclass
        mp = "SpectralDataset_Characterization_CharacterizationAxes[].FluxCharAxis";
        result = factory.newInstanceByModelPath( mp );
        assertNotNull( result );
        assertTrue( result instanceof FluxCharAxis );

        // partial model path (picking up from middle
        mp = "CharacterizationAxis_Coverage_Bounds";
        result = factory.newInstanceByModelPath( mp );
        assertNotNull( result );
        assertTrue( result instanceof Bounds );
        
        // Array
        mp = "SpectralDataset_Data";
        ok = false;
        try{
          result = factory.newInstanceByModelPath( mp );
        }
        catch (IllegalArgumentException ex ){
          assertEquals("List is not a supported Class type", ex.getMessage());
          ok = true;
        }
        assertTrue( ok );
        
        // Quantity Array element
        mp = "SpectralDataset_Curation_References[]";
        ok = false;
        try{
          result = factory.newInstanceByModelPath( mp );
        }
        catch (IllegalArgumentException ex ){
          assertEquals("Quantity is not a supported Class type", ex.getMessage());
          ok = true;
        }
        assertTrue( ok );
        
        // Leaf element
        mp = "SpectralDataset_Curation_Version";
        ok = false;
        try{
          result = factory.newInstanceByModelPath( mp );
        }
        catch (IllegalArgumentException ex ){
          assertEquals("Quantity is not a supported Class type", ex.getMessage());
          ok = true;
        }
        assertTrue( ok );
    }
    
}