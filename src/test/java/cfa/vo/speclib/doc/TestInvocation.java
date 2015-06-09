/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.*;
import org.junit.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 * @author work
 */
public class TestInvocation {

    static ModelObjectFactory factory;
    static boolean verbose;

    public TestInvocation() {
    }

    /**
     * Start Spectral object factory once for all tests.
     */
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

    @Test
    public void testBasicGet()
    {
        SpectralDataset ds;
        DataID did;
        DataModel dm, dm2;
        Integer a;
        Quantity q;
        List<Quantity> qarr;
        
        if ( verbose )
            System.out.println("Test Basic Get:");

        // Create empty Dataset 
        ds = (SpectralDataset)factory.newInstance( SpectralDataset.class );
        assertNotNull(ds);
        
        // Create empty DataID 
        did = (DataID)factory.newInstance( DataID.class );
        assertNotNull(did);

        // show empty ds
        if ( verbose ){
            System.out.println("Empty Dataset");
            System.out.println(ds.toString());
        }
        
        try {

          if ( verbose ){ System.out.println("+ Extract Interface.");}
          dm = ds.getDataModel();
          assertNotNull(dm);

          // show empty dm added to ds
          if ( verbose ){ System.out.println(ds.toString()); }

          // Extract the same interface.. should pull from storage
          dm2 = ds.getDataModel();
          assertEquals(dm,dm2);
          assertTrue( dm == dm2);

          if ( verbose ){ System.out.println("+ Extract Quantity.");}
          q = dm.getURL();
          assertNotNull(q);

          if ( verbose ){ System.out.println("+ Extract 'Primitive'.");}
          a = ds.getLength();
          assertNotNull(a);

          if ( verbose ){ System.out.println("+ Extract Quantity List.");}
          qarr = did.getCollections();
          assertNotNull(qarr);

          // Add element to Collections
          qarr.add( new Quantity("WFC") );
          qarr.add( new Quantity("Sloan") );

          if ( verbose ){
              System.out.println("Full Tree");
              System.out.println(ds.toString());
              System.out.println(did.toString());
          }

        }
        catch ( UnsupportedOperationException e )
        {
            fail( e.getMessage());
        }
    }
    
    @Test
    public void testBasicSet()
    {
        Dataset ds;
        DataID did;
        DataModel dm;
        Quantity q;
        List qarr = new ArrayList<Quantity>();

        if ( verbose )
            System.out.println("Test Basic Set:");
        
        // Create empty Dataset 
        ds = (Dataset)factory.newInstance( Dataset.class );
        assertNotNull(ds);

        // Create empty DataModel
        dm = (DataModel)factory.newInstance( DataModel.class );
        assertNotNull(dm);

        // Create empty DataID
        did = (DataID)factory.newInstance( DataID.class );
        assertNotNull(did);

        // Set "Primitive"
        if ( verbose ){ System.out.println("+ Assign 'Primitive'");}
        assertFalse( ds.hasDataProductType());
        ds.setDataProductType("Spectrum");
        assertTrue( ds.hasDataProductType());
        
        // Set Quantity
        if ( verbose ){ System.out.println("+ Assign Quantity");}
        q = new Quantity("prefix", "spec", "", "" );
        assertFalse( dm.hasPrefix());
        dm.setPrefix( q );
        assertTrue( dm.hasPrefix());

        // Set Quantity List
        if ( verbose ){ System.out.println("+ Assign Quantity List");}
        qarr.add( new Quantity("Madame Curie" ));
        qarr.add( new Quantity("Albert Einstein" ));
        assertFalse( did.hasContributors());
        did.setContributors( qarr );
        assertTrue( did.hasContributors());
        
        // Set Interface
        if ( verbose ){ System.out.println("+ Assign Interface");}
        assertFalse( ds.hasDataModel());
        ds.setDataModel( dm );
        assertTrue( ds.hasDataModel());
        
        // show ds
        if ( verbose ){
            System.out.println("Full Tree");
            System.out.println(ds.toString()); 
            System.out.println(did.toString()); 
        }

        // Re-Assign property with Quantity
        if ( verbose ){ System.out.println("+ Re-Assign with Quantity"); }
        q = new Quantity("type","Photometry","","");
        assertTrue( ds.hasDataProductType());
        ds.setDataProductType( q );
        assertTrue( ds.hasDataProductType());
        assertEquals( ds.getDataProductType().getValue(), "Photometry");

        // Re-Assign property with Quantity
        if ( verbose ){ System.out.println("+ Re-Assign with 'Primitive'"); }
        assertTrue( dm.hasPrefix());
        dm.setPrefix( "phot" );
        assertTrue( dm.hasPrefix());
        assertEquals( dm.getPrefix().getValue(), "phot");
        assertEquals( dm.getPrefix().getName(), "prefix");

        // show ds
        if ( verbose ){
            System.out.println("Full Tree");
            System.out.println(ds.toString()); 
            System.out.println(did.toString());
        }

    }

    @Test
    public void testBasicIs()
    {
        Correction corr;
        Quantity q;
        
        if ( verbose )
            System.out.println("Test Basic Is:");

        // Create empty Correction 
        corr = (Correction)factory.newInstance( GenericCorr.class );
        assertNotNull(corr);

        if ( verbose ){ System.out.println("+ Assert initially false");}
        assertFalse(corr.isApplied());

        if ( verbose ){ System.out.println("+ Assign Applied flag = true");}
        corr.setApplied( true );

        assertTrue(corr.isApplied());

        q = new Quantity("apflag", false, "", "");
        if ( verbose ){ System.out.println("+ Assign Applied flag = false");}
        corr.setApplied( q );
        if ( verbose ){System.out.println(q.toString());}
        
        assertFalse(corr.isApplied());
        
    }
        
    @Test
    public void testIndexedPropertyHandling()
    {
        DataID did;
        Quantity q;
        List qarr;
        Quantity a,b;

        if ( verbose )
            System.out.println("Test Indexed Property Handling:");
        
        // Create empty DataID
        if ( verbose ){ System.out.println("+ Create empty DataID "); }
        did = (DataID)factory.newInstance( DataID.class );
        assertNotNull(did);

        if ( verbose ){ System.out.println("+ Get Indexed Property"); }
        qarr = did.getCollections();

        // Add Quantities to list
        if ( verbose ){ System.out.println("+ Add records to extracted List"); }
        qarr.add( new Quantity("WFC") );
        qarr.add( new Quantity("Sloan") );
        
        // Extract specific Collection getCollection(1)
        if ( verbose ){ System.out.println("+ Get Indexed Property Element"); }
        q = did.getCollections( 1 );
        if ( verbose ){System.out.println(q.toString());}
        
        // verify Quantities are the same object
        assertEquals(q, qarr.get(1));
        
        // replace that member with new Quantity setCollection(1, Q)
        if ( verbose ){ System.out.println("+ Replace Indexed Property Element"); }
        a = new Quantity("SDSS");
        did.setCollections(1, a);

        // Extract that quantity
        if ( verbose ){ System.out.println("+ Verify replaced Indexed Property Element"); }
        b = did.getCollections( 1 );
        if ( verbose ){ System.out.println(b.toString());}

        // verify new is same as set, and no longer same as previous.
        assertSame( a, b );
        assertNotSame( b, q );
        
        // Get DataID.Contributors( 2 )..
        //   Contributors not set so s/b Index Exception..
        if ( verbose ){ System.out.println("+ Get Indexed Property Element from unset property"); }
        try{
            q = did.getContributors(2);
        }
        catch (IndexOutOfBoundsException ex ){
            // expected
            q = null;
        }
        assertNull( q );
        
        if ( verbose ){ System.out.println("+ Create Indexed Property List"); }
        qarr = new ArrayList<Quantity>();
        qarr.add( new Quantity("Madame Curie" ));
        qarr.add( new Quantity("Albert Einstein" ));

        if ( verbose ){ System.out.println("+ Assign Indexed Property List"); }
        did.setContributors( qarr );

        if ( verbose ){ System.out.println("+ Re-Assign value of Indexed Property Element"); }
        did.setContributors(1, "Isaac Newton");

        if ( verbose ){ System.out.println("+ Verify Re-Assign value"); }
        q = (Quantity)qarr.get(1);
        assertEquals( q.getValue(), "Isaac Newton" );
        if ( verbose){ System.out.println( q.toString()); }
        
        // Get element with index out or range
        if ( verbose ){ System.out.println("+ Get Bad Indexed Property Element from existing property"); }
        try{
            q = did.getCollections( 5 );
        }
        catch (IndexOutOfBoundsException ex ){
            // expected
            q = null;
        }
        assertNull( q );

        // Either one.  Set element with index out or range
        if ( verbose ){ System.out.println("+ Set Bad Indexed Property Element to existing property"); }
        q = new Quantity("BFS");
        try{
            did.setCollections( 5, q );
        }
        catch (IndexOutOfBoundsException ex ){
            // expected
            q = null;
        }
        assertNull( q );

    }

    @Test
    public void testTypeSafety(){

        DataModel dm;
        Quantity q;
        Quantity p;
        URL uval;
        String sval;
        int count = 0;

        if ( verbose )
            System.out.println("Test Type Safety:");

        sval = "http://www.ivoa.net";
        try {
            uval = new URL(sval);
        } catch (MalformedURLException ex) {
            uval = null;
        }

        // Create empty DataModel object 
        dm = (DataModel)factory.newInstance( DataModel.class );
        assertNotNull(dm);

        if ( verbose ){ System.out.println("  + Assign with value.");}
        p = new Quantity( uval );
 
        // Test assigment of values allowed by the interface (since Generic), 
        // but should be rejected by the set method since data type of value 
        // does not match.
        if ( verbose ){ System.out.println("    - attempt to set different type");}
        try{
            p.setValue(sval);
        }catch ( IllegalArgumentException ex){ count++;}

        if ( verbose ){ System.out.println("  + Extract EMPTY quantity.");}
        p = dm.getURL();
        if ( verbose ){ System.out.println("    - attempt to set different type");}
        try{
            p.setValue(sval);
        }catch ( IllegalArgumentException ex){ count++;}
 
        // Should have had 1 errors.
        if ( verbose ){ System.out.println("  + Number of Exceptions caught = "+count);}
        assertEquals( count, 2 );

        //TODO: This should fail, Prefix defined type is String.
        q = new Quantity();
        q.setValue(Double.NaN);
        dm.setPrefix( q );

    }
}