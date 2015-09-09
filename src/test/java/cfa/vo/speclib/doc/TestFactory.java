/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

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
    public void testNewInstanceByModelPath()
    {
        //System.out.println("testNewInstanceByModelPath");
        
        String mp = null;
        SpectralDataset ds = (SpectralDataset)factory.newInstance( SpectralDataset.class );
        Object result = null;
        ModelProxy h;

        // NULL Argument
        boolean caught = false;
        try{
          result = factory.newInstanceByModelPath( null, mp );
        }
        catch (IllegalArgumentException ex ){
            caught = true;
        }
        assertTrue( caught );
        assertNull( result );
        
        // NULL Argument
        caught = false;
        try{
          result = factory.newInstanceByModelPath( ds, null );
        }
        catch (IllegalArgumentException ex ){
            caught = true;
        }
        assertTrue( caught );
        assertNull( result );
        
        //   - From top level dataset -> leaf
        mp = "Curation_Rights";
        result = factory.newInstanceByModelPath( ds, mp );
        assertTrue( result.getClass() == MPQuantity.class );
        assertEquals("SpectralDataset_Curation_Rights", ((MPQuantity)result).getModelpath());
        

        //   - From top level dataset -> Proxy
        mp = "Curation_Contact";
        result = factory.newInstanceByModelPath( ds, mp );
        h = (ModelProxy)Proxy.getInvocationHandler( result );
        assertEquals( h.type, Contact.class.getSimpleName() );
        assertEquals("SpectralDataset_Curation_Contact_Email", ((MPQuantity)((Contact)result).getEmail()).getModelpath());

        //   - From top level dataset -> List
        mp = "Curation_References";
        result = factory.newInstanceByModelPath( ds, mp );
        assertTrue( result.getClass() == MPArrayList.class );
        MPArrayList refs = (MPArrayList)result;
        assertEquals(0, refs.size());

        //   - From top level dataset -> Through List to leaf
        mp = "Curation_References[]";
        result = factory.newInstanceByModelPath( ds, mp );
        assertTrue( result.getClass() == MPQuantity.class );
        assertEquals("SpectralDataset_Curation_References[]", ((MPQuantity)result).getModelpath());
        assertEquals(1, refs.size());

        // Again.. should make another element on list
        result = factory.newInstanceByModelPath( ds, mp );
        assertTrue( result.getClass() == MPQuantity.class );
        assertEquals(2, refs.size());

        //   - From top level dataset -> Through List of Proxies to leaf
        mp = "Data[].SPPoint_FluxAxis_Corrections[].ApFrac_Name";
        result = factory.newInstanceByModelPath( ds, mp );
        assertTrue( result.getClass() == MPQuantity.class );
        assertEquals("SpectralDataset_Data[].SPPoint_FluxAxis_Corrections[].ApFrac_Name", ((MPQuantity)result).getModelpath());
        
        //   - From internal proxy    -> Leaf
        CharacterizationAxis axis = (CharacterizationAxis)factory.newInstance(CharacterizationAxis.class);
        mp = "Coverage_Bounds_Extent";
        result = factory.newInstanceByModelPath( axis, mp );
        assertTrue( result.getClass() == MPQuantity.class );
        assertEquals("CharacterizationAxis_Coverage_Bounds_Extent", ((MPQuantity)result).getModelpath());
        

        //   - Invalid model path
        result = null;
        caught = false;
        mp = "Coverage_Blah_Extent";
        try{
          result = factory.newInstanceByModelPath( axis, mp );
        }
        catch( IllegalArgumentException ex ){
            caught = true;
        }
        assertTrue(caught);
        assertNull(result);
        
    }
}