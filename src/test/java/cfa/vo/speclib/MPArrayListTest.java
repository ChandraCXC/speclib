/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib;

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
    
    public MPArrayListTest() {
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
     * Test of getModelpath method, of class MPArrayList.
     */
    @Test
    public void testGetModelpath() {
        System.out.println("getModelpath");
        MPArrayList<Double> instance = new MPArrayList<Double>();
        String expResult;
        String result = instance.getModelpath();
        assertNull(result);
        
        expResult = "This_That";
        instance.setModelpath("This_That");
        result = instance.getModelpath();
        assertEquals(expResult, result);
    }

    /**
     * Test of setModelPath method, of class MPArrayList.
     */
    @Test
    public void testSetModelpath() {
        System.out.println("setModelPath");
        String mp = "Some_Path";
        MPArrayList instance = new MPArrayList();
        instance.setModelpath(mp);

        String result = instance.getModelpath();
        assertEquals(mp, result);
    }
    
    /**
     * 
     */
    
}