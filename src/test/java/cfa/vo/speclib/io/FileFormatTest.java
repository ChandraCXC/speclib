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
public class FileFormatTest {
     
    public FileFormatTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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
     * Test of values method, of class FileFormat.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        FileFormat[] result = FileFormat.values();
        
        assertEquals(FileFormat.VOT, result[0]);
        assertEquals(FileFormat.FITS, result[1]);
    }

    /**
     * Test of valueOf method, of class FileFormat.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "";
        FileFormat result;
        FileFormat expResult;
        Boolean ok = false;
        
        try{
          result = FileFormat.valueOf(name);
          assertNull( result );
        }catch( IllegalArgumentException ex){ ok = true;}
        assertTrue( ok );

        name = "FITS";
        expResult = FileFormat.FITS;
        result = FileFormat.valueOf(name);
        assertEquals(expResult, result);
        
        name = "VOT";
        expResult = FileFormat.VOT;
        result = FileFormat.valueOf(name);
        assertEquals(expResult, result);

    }

     /**
     * Test of toString method, of class FileFormat.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        String expResult;
        String result;
        FileFormat instance;
        
        instance = FileFormat.FITS;
        expResult = "FITS";
        result = instance.toString();
        assertEquals( expResult, result );
        
        instance = FileFormat.VOT;
        expResult = "VOTable";
        result = instance.toString();
        assertEquals(expResult, result);

    }

    /**
     * Test of exten method, of class FileFormat.
     */
    @Test
    public void testExten() {
        System.out.println("exten");
        FileFormat instance = FileFormat.VOT;
        String expResult = "vot";
        String result = instance.exten();
        assertEquals(expResult, result);
        
        instance = FileFormat.FITS;
        expResult = "fits";
        result = instance.exten();
        assertEquals(expResult, result);
        
    }
}