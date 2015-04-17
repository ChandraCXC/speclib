/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

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
public class IOFactoryTest {

    static boolean verbose;

    public IOFactoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() 
    {
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
     * Test of newInstance method, of class IOFactory.
     */
    @Test
    public void testNewInstance() 
    {
        if ( verbose ) {System.out.println("Test newInstance");}
        
        FileFormat format;
        IFileIO expResult = null;
        IFileIO result;
        boolean ok;

        /* create factory */
        IOFactory instance = new IOFactory();

        /* create VOTable IO provider */
        try
        {
          format = FileFormat.VOT;
          result = instance.newInstance(format);
          assertNotNull(result);
          assertEquals(result.getClass().getSimpleName(), "VOTableIO");
          ok = true;
        }
        catch ( Exception ex ) { ok = false; }
        assertTrue( ok );

        /* create FITS IO provider */
        ok = false;
        try
        {
          format = FileFormat.FITS;
          result = instance.newInstance(format);
          assertEquals(expResult, result);
        }
        catch (UnsupportedOperationException ex) { ok = true; }
        assertTrue( ok );

    }
}