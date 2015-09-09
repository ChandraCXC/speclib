/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.Quantity;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mdittmar
 */
public class MPArrayListTest {
    static boolean verbose;
    
    public MPArrayListTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        verbose = false;
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
    public void testConstructors(){
        if (verbose){ System.out.println("Test Constructors");}
        
        MPArrayList<Double> instance;
        
        // simple constructor
        instance = new MPArrayList<Double>();
        assertNotNull( instance );
        assertEquals(0, instance.size());

        // constructor with preset capacity
        instance = new MPArrayList<Double>( 100 );
        assertNotNull( instance );
        assertEquals(0, instance.size());

        // constructor with Collection
        ArrayList<Double> data = new ArrayList<Double>();
        Double dval = 1.0;
        data.add( dval ); dval += 1.0;
        data.add( dval ); dval += 1.0;
        data.add( dval ); dval += 1.0;
        instance = new MPArrayList<Double>( data );
        assertNotNull( instance );
        assertEquals(3, instance.size());
        dval = 2.0;
        assertEquals( dval, instance.get(1));
        
    }
    
    // Test ModeledElement Interface.
    
    @Test
    public void testGetModelpath() {
        if (verbose){ System.out.println("Test getModelpath");}

        MPArrayList<Double> instance = new MPArrayList<Double>();
        String expResult = ModeledElement.MP_UNDEFINED; 
        String result = instance.getModelpath();
        assertEquals( expResult, result );
    }

    @Test
    public void testSetModelpath() {
        if (verbose){ System.out.println("Test setModelpath");}

        MPArrayList instance = new MPArrayList();
        Boolean ok;

        // Assign value.. check changes from default
        String mp = "Some_Path";
        instance.setModelpath(mp);
        String result = instance.getModelpath();
        assertEquals(mp, result);

        // Verify provided mp "must not be null"
        ok = false;
        mp = null;
        try{
          instance.setModelpath( mp );
          fail("Expected Exception not thrown.");
        }
        catch ( IllegalArgumentException ex ){ ok = true; }
        assertTrue( ok );
        
        // Verify provided mp "must not be empty"
        ok = false;
        mp = "";
        try{
          instance.setModelpath( mp );
          fail("Expected Exception not thrown.");
        }
        catch ( IllegalArgumentException ex ){ ok = true; }
        assertTrue( ok );
    }
    
    @Test
    public void testSetModelpathPropagation() {
        if (verbose){ System.out.println("Test setModelpathPropagation");}
        
        // Verify propagation of model path to child elements.
        String mp = "Some_Path";
        String expected;

        // MPQuantity Array
        MPArrayList<MPQuantity> qarr = this.createMPQArray();
        qarr.setModelpath(mp);
        
        // Check path of child elements.
        assertEquals( 3, qarr.size());
        expected = mp+"[]";
        for ( MPQuantity q: qarr ) {
           assertEquals( expected, q.getModelpath() );
        }

        // MPNode Array
        MPArrayList<MPNode> nodes = this.createMPNodeArray();
        nodes.setModelpath(mp);
        
        // Check path of child elements.
        assertEquals( 3, nodes.size());
        expected = mp+"[].Pos";
        for ( MPNode node: nodes ) {
           assertEquals( expected, node.getModelpath() );
        }
    }
    
    @Test
    public void testRebaseModelpath()
    {
        if (verbose){ System.out.println("Test rebaseModelpath");}
        
        String base = "Some_Path_To";
        String newbase = "Some_Other";
        String expected;

      // MPNode Array
        // Create Array of MPNodes containing MPQuantity elements.
        MPArrayList<MPNode> nodes = this.createMPNodeArray();
        nodes.setModelpath(base);
        
        // Check initial state
        assertEquals(base, nodes.getModelpath());
        assertEquals(base+"[].Pos", nodes.get(0).getModelpath());

        // Rebase array
        nodes.rebaseModelpath(newbase);

        // Check resulting state
        assertEquals(newbase+"_To", nodes.getModelpath());
        assertEquals(newbase+"_To[].Pos", nodes.get(0).getModelpath());

        // Strip base path from the array and contents
        nodes.rebaseModelpath( null );
        
        expected = "To";
        assertEquals(expected, nodes.getModelpath());
        assertEquals(expected+"[].Pos", nodes.get(0).getModelpath());

        
      // MPQuantity Array
        // Create Array of MPQuantity elements.
        MPArrayList<MPQuantity> qarr = this.createMPQArray();
        qarr.setModelpath(base);
        
        // Check initial state
        assertEquals(base, qarr.getModelpath());
        assertEquals(base+"[]", qarr.get(0).getModelpath());

        // Rebase array
        qarr.rebaseModelpath(newbase);

        // Check resulting state
        assertEquals(newbase+"_To", qarr.getModelpath());
        assertEquals(newbase+"_To[]", qarr.get(0).getModelpath());
        
        
        // Strip base path from the array and contents
        qarr.rebaseModelpath( null );
        
        expected = "To";
        assertEquals(expected, qarr.getModelpath());
        assertEquals(expected+"[]", qarr.get(0).getModelpath());
        
    }
    
    /**
     * Test of add(obj) method, of class MPArrayList.
     */
    @Test
    public void testAdd()
    {
        if (verbose){ System.out.println("Test add");}

        MPQuantity q = new MPQuantity("Name","CXC Help Desk","meta.bib.author;meta.curation",null);
        MPArrayList<MPQuantity> qarr = new MPArrayList<MPQuantity>();
        MPArrayList<Double> darr = new MPArrayList<Double>();

        String mp;
        String expected;
        
        // ++++++++++++++++++++++++++++++++++++++++++++++
        // add MPQuantity to MPArrayList.
        qarr.add(q);
        assertEquals( 1, qarr.size() );

        expected = "UNKNOWN[]";
        mp = ((MPQuantity)qarr.get(0)).getModelpath();
        assertEquals( expected, mp );

        // ++++++++++++++++++++++++++++++++++++++++++++++
        // add Double to MPArrayList.
        Double dval = 3.14159;
        darr.add( dval );
        assertEquals( 1, darr.size() );
        assertEquals( dval, darr.get(0) );        
    }

    /**
     * Test of add(int, obj) method, of class MPArrayList.
     */
    @Test
    public void testAddIndexed()
    {
        if (verbose){ System.out.println("Test Add(Indexed)");}
        
        MPArrayList<MPQuantity> qarr = new MPArrayList<MPQuantity>();
        qarr.setModelpath("QARR");
        qarr.add( new MPQuantity("A","a",null,null));
        qarr.add( new MPQuantity("B","b",null,null));
        qarr.add( new MPQuantity("C","c",null,null));

        MPQuantity q = new MPQuantity("Z","z",null,null);

        // ++++++++++++++++++++++++++++++++++++++++++++++
        // add Quantity within MPArrayList.
        qarr.add(1, q);
        assertEquals( 4, qarr.size() );
        assertEquals("A", qarr.get(0).getName());
        assertEquals("Z", qarr.get(1).getName());
        assertEquals("B", qarr.get(2).getName());

        String expected = "QARR[]";
        assertEquals( expected, qarr.get(1).getModelpath());
    }
    
    @Test
    public void testAddAll()
    {
        if (verbose){ System.out.println("Test addAll");}
        MPArrayList<Quantity> basearr = new MPArrayList<Quantity>();
        
        //Create collection to add
        MPArrayList<Quantity> qarr = new MPArrayList<Quantity>();
        qarr.add( new Quantity("A","a",null,null));
        qarr.add( new Quantity("B","b",null,null));
        qarr.add( new Quantity("C","c",null,null));

        boolean caught;

        //Add Collection to MPArray
        caught = false;
        try{
          basearr.addAll( qarr );
        }
        catch (UnsupportedOperationException ex){
            caught = true;
        }            
        assertTrue( caught );
        assertEquals( 0, basearr.size() );
    }
    
    @Test
    public void testAddAllIndexed()
    {
        if (verbose){ System.out.println("Test addAll(Indexed)");}
        MPArrayList<Quantity> basearr = new MPArrayList<Quantity>();
        basearr.add( new Quantity("A","a",null,null));
        basearr.add( new Quantity("B","b",null,null));
        basearr.add( new Quantity("C","c",null,null));
        
        //Create collection to add
        MPArrayList<Quantity> qarr = new MPArrayList<Quantity>();
        qarr.add( new Quantity("X","x",null,null));
        qarr.add( new Quantity("Y","y",null,null));
        qarr.add( new Quantity("Z","z",null,null));

        boolean caught;

        //Add Collection into MPArray
        caught = false;
        try{
          basearr.addAll( 1, qarr );
        }
        catch (UnsupportedOperationException ex){
            caught = true;
        }            
        assertTrue( caught );
        assertEquals( 3, basearr.size() );
        
    }
    
    @Test
    public void testSet()
    {
        if (verbose){ System.out.println("Test set");}
        MPArrayList<MPQuantity> qarr = new MPArrayList<MPQuantity>();
        qarr.add( new MPQuantity("A","a",null,null));
        qarr.add( new MPQuantity("B","b",null,null));
        qarr.add( new MPQuantity("C","c",null,null));

        MPQuantity q = new MPQuantity("Z","z",null,null);

        // ++++++++++++++++++++++++++++++++++++++++++++++
        // set Quantity to MPArrayList.
        qarr.set(1, q);
        assertEquals( 3, qarr.size() );
        assertEquals("Z", qarr.get(1).getName()); //no change

        String expected = "UNKNOWN[]";
        assertEquals( expected, qarr.get(1).getModelpath());

    }
    
    private MPArrayList<MPQuantity> createMPQArray() {
        
        MPArrayList<MPQuantity> qarr = new MPArrayList<MPQuantity>();
        Double dval = 7.0e+3;
        qarr.add( new MPQuantity("dist",dval,"km","a.b;c") );
        qarr.add( new MPQuantity("dist",dval+1000,"km","a.b;c") );
        qarr.add( new MPQuantity("dist",dval+1000,"km","a.b;c") );
        
        return qarr;
    }
    
    private MPArrayList<MPNode> createMPNodeArray() {
        
        MPArrayList<MPNode> nodes = new MPArrayList<MPNode>();
        for ( int ii=1; ii <= 3; ii++ ) {
          MPNode node = new MPNode("Pos");
          
          Double dval = 1e+03;
          for ( int jj=1; jj <=3; jj++) {
              dval += 1000.0;
              MPQuantity q = new MPQuantity("dist",dval,"km","a.b;c");
              q.setModelpath("Axis"+jj);
              node.appendChild(q);
          }
          
          nodes.add(node);
        }
        return nodes;
    }
    
}