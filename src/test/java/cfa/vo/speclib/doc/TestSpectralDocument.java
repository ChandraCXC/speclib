/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import cfa.vo.speclib.Quantity;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author work
 */
public class TestSpectralDocument {
  
    static boolean verbose;
    
    public TestSpectralDocument() {
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
    
    /**
     * Test basic functionality
     */
    @Test
    public void testBasic()
    {
        Quantity q = new Quantity("target", "NGC-1701", "", "meta.id;src"  );
        q.setModelpath("SPEC_TARGET_NAME");
                
        ModelDocument doc = new ModelDocument();
        if (verbose){
            System.out.println("New Document:");
            System.out.println( doc.toSimpleString() );
        }
        
        doc.put("SPEC_DATASET_TYPE", "Spectrum" );
        doc.put("SPEC_TARGET_NAME", q);
        
        if (verbose)
        {
          System.out.println("Loaded Document:");
          System.out.println( doc.toSimpleString() );
        }

        Set keys = doc.getKeys();
        assertEquals( keys.size(), 2);
        
        doc.remove( "SPEC_TARGET_NAME");
        keys = doc.getKeys();
        assertEquals( keys.size(), 1);
        
        
    }
    
}