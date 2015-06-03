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
    
    /**
     * Test of getModelpath method, of class MPArrayList.
     */
    @Test
    public void testGetModelpath() {
        if (verbose){ System.out.println("Test getModelpath");}

        MPArrayList<Double> instance = new MPArrayList<Double>();
        String expResult = "UNKNOWN";
        String result = instance.getModelpath();
        assertEquals( expResult, result );
    }

    /**
     * Test of setModelPath method, of class MPArrayList.
     */
    @Test
    public void testSetModelpath() {
        if (verbose){ System.out.println("Test setModelpath");}

        MPArrayList instance = new MPArrayList();

        // Assign value.. check changes from default
        String mp = "Some_Path";
        instance.setModelpath(mp);
        String result = instance.getModelpath();
        assertEquals(mp, result);

        // Assign null.. check sets to empty 
        mp = null;
        instance.setModelpath( mp );
        result = instance.getModelpath();
        assertEquals( "", result );
        
    }
    
    /**
     * Test of add(obj) method, of class MPArrayList.
     */
    @Test
    public void testAdd()
    {
        if (verbose){ System.out.println("Test add");}

        Quantity q = new Quantity("Name","CXC Help Desk","meta.bib.author;meta.curation",null);
        MPArrayList<Quantity> qarr = new MPArrayList<Quantity>();
        MPArrayList<Double> darr = new MPArrayList<Double>();

        String mp;
        String expected;
        
        // ++++++++++++++++++++++++++++++++++++++++++++++
        // add Quantity to MPArrayList.
        qarr.add(q);
        assertEquals( 1, qarr.size() );

        expected = "UNKNOWN[]";
        mp = qarr.get(0).getModelpath();
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
        
        MPArrayList<Quantity> qarr = new MPArrayList<Quantity>();
        qarr.setModelpath("QARR");
        qarr.add( new Quantity("A","a",null,null));
        qarr.add( new Quantity("B","b",null,null));
        qarr.add( new Quantity("C","c",null,null));

        Quantity q = new Quantity("Z","z",null,null);

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
        MPArrayList<Quantity> qarr = new MPArrayList<Quantity>();
        qarr.add( new Quantity("A","a",null,null));
        qarr.add( new Quantity("B","b",null,null));
        qarr.add( new Quantity("C","c",null,null));

        Quantity q = new Quantity("Z","z",null,null);

        // ++++++++++++++++++++++++++++++++++++++++++++++
        // set Quantity to MPArrayList.
        qarr.set(1, q);
        assertEquals( 3, qarr.size() );
        assertEquals("Z", qarr.get(1).getName()); //no change

        String expected = "UNKNOWN[]";
        assertEquals( expected, qarr.get(1).getModelpath());

    }

    @Test
    public void testRebaseContent()
    {
        if (verbose){ System.out.println("Test rebaseContent");}
        MPArrayList<Quantity> qarr = new MPArrayList<Quantity>();
        Quantity q = new Quantity("Z","z",null,null);
        
        String mp = "QARR";
        String expected = "UNKNOWN[]";

        // Add quantity to array
        qarr.add(q);
        assertEquals(expected, q.getModelpath());
        
        // Set model path for array
        qarr.setModelpath(mp);
        assertEquals(expected, q.getModelpath()); //unchanged

        // Rebase the array contents        
        qarr.rebaseContent();
        
        expected = "QARR[]";
        assertEquals(expected, q.getModelpath()); //unchanged
        
    }
}