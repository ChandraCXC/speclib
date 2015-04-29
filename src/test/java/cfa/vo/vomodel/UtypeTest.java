/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.vomodel;

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
public class UtypeTest {
    
    public UtypeTest() {
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
     * Test Constructors.
     */
    @Test
    public void testConstructors() {
        System.out.println("Constructor");
        Utype instance = null;
        boolean caught;
        
        caught = false;
        try {
          instance = new Utype( null, "Target", "spec");
        }
        catch (IllegalArgumentException ex){
            caught = true;
        }
        assertTrue(caught);
        
        caught = false;
        try {
          instance = new Utype( "Dataset_Target", null, "spec");
        }
        catch (IllegalArgumentException ex){
            caught = true;
        }
        assertTrue(caught);
    
        caught = false;
        try {
          instance = new Utype( "Dataset_Target", "Target", null);
        }
        catch (IllegalArgumentException ex){
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try {
          instance = new Utype( "", "Target", "spec");
        }
        catch (IllegalArgumentException ex){
            caught = true;
        }
        assertTrue(caught);
        
        caught = false;
        try {
          instance = new Utype( "Dataset_Target", " ", "spec");
        }
        catch (IllegalArgumentException ex){
            caught = true;
        }
        assertTrue(caught);
    
        caught = false;
        try {
          instance = new Utype( "Dataset_Target", "Target", "  ");
        }
        catch (IllegalArgumentException ex){
            caught = true;
        }
        assertTrue(caught);

        caught = false;
        try {
          instance = new Utype( "Dataset_Target", "Target", "spec" );
        }
        catch (IllegalArgumentException ex){
            caught = true;
        }
        assertFalse(caught);

    }

    /**
     * Test of getModelPath method, of class Utype.
     */
    @Test
    public void testGetModelPath() {
        System.out.println("getModelPath");
        Utype instance = new Utype( "CoordSys_TimeFrame_TimeScale", "CoordSys.TimeFrame.TimeScale", "stc" );
        String expResult = "CoordSys_TimeFrame_TimeScale";
        String result = instance.getModelPath();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTag method, of class Utype.
     */
    @Test
    public void testGetTag() {
        System.out.println("getTag");
        Utype instance = new Utype( "CoordSys_TimeFrame_TimeScale", "CoordSys.TimeFrame.TimeScale", "stc" );
        String expResult = "stc:CoordSys.TimeFrame.TimeScale";
        String result = instance.getTag();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTag method, of class Utype.
     */
    @Test
    public void testGetTagNoPrefix() {
        System.out.println("getTagNoPrefix");
        Utype instance = new Utype( "CoordSys_TimeFrame_TimeScale", "CoordSys.TimeFrame.TimeScale", "stc" );
        String expResult = "CoordSys.TimeFrame.TimeScale";
        String result = instance.getTagNoPrefix();
        assertEquals(expResult, result);
    }

    /**
     * Test of matchModelPath method, of class Utype.
     */
    @Test
    public void testMatchModelPath() {
        System.out.println("matchModelPath");
        String modelpath;
        Utype instance = new Utype( "CoordSys_TimeFrame_TimeScale", "CoordSys.TimeFrame.TimeScale", "stc" );

        // Regular match
        modelpath = "CoordSys_TimeFrame_TimeScale";
        boolean expResult = true;
        boolean result = instance.matchModelPath(modelpath);
        assertEquals(expResult, result);

        // Regular mismatch
        modelpath = "CoordSys_TimeFrame_TimeZero";
        expResult = false;
        result = instance.matchModelPath(modelpath);
        assertEquals(expResult, result);
        
        // Case sensitivity match
        modelpath = "coordsys_timeframe_timescale";
        expResult = true;
        result = instance.matchModelPath(modelpath);
        assertEquals(expResult, result);

        // Empty string
        modelpath = "";
        expResult = false;
        result = instance.matchModelPath(modelpath);
        assertEquals(expResult, result);

        // NULL argument
        modelpath = null;
        expResult = false;
        result = instance.matchModelPath(modelpath);
        assertEquals(expResult, result);

    }

    /**
     * Test of matchTag method, of class Utype.
     */
    @Test
    public void testMatchTag() {
        System.out.println("matchTag");
        String label;
        Utype instance = new Utype( "CoordSys_TimeFrame_TimeScale", "CoordSys.TimeFrame.TimeScale", "stc" );

        // Basic match.
        label = "CoordSys.TimeFrame.TimeScale";
        boolean expResult = true;
        boolean result = instance.matchTag(label);
        assertEquals(expResult, result);

        // Basic match.. case sentitivity
        label = "CoOrDSyS.TiMeFrAmE.TiMEScAle";
        expResult = true;
        result = instance.matchTag(label);
        assertEquals(expResult, result);

        // Basic mis-match.
        label = "CoordSys.TimeFrame.TimeZero";
        expResult = false;
        result = instance.matchTag(label);
        assertEquals(expResult, result);

        // Input includes prefix.
        label = "stc:CoordSys.TimeFrame.TimeScale";
        expResult = true;
        result = instance.matchTag(label);
        assertEquals(expResult, result);

        // Input includes prefix.. case sensitivity.
        label = "StC:CoOrDSyS.TiMeFrAmE.TiMEScAle";
        expResult = true;
        result = instance.matchTag(label);
        assertEquals(expResult, result);

        // Input includes non-matching prefix.
        label = "spec:CoordSys.TimeFrame.TimeScale";
        expResult = false;
        result = instance.matchTag(label);
        assertEquals(expResult, result);

        // Input includes non-matching tag.
        label = "stc:CoordSys.TimeFrame.TimeZero";
        expResult = false;
        result = instance.matchTag(label);
        assertEquals(expResult, result);

        // null input.
        label = null;
        expResult = false;
        result = instance.matchTag(label);
        assertEquals(expResult, result);

    }

    /**
     * Test of equals method, of class Utype.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Utype a = new Utype( "CoordSys_TimeFrame_TimeScale", "CoordSys.TimeFrame.TimeScale", "stc" );
        Utype b = new Utype( "CoordSys_TimeFrame_TimeScale", "CoOrDSyS.TiMeFrAmE.TiMEScAle", "StC" );
        Utype c = new Utype( "CoordSys_TimeFrame_TimeScale", "CoordSys.TimeFrame.TimeScale", "spec" );
        Utype d = new Utype( "Dataset_Target", "Target", "spec" );

        boolean expResult = true;
        boolean result = a.equals(b);
        assertEquals(expResult, result);

        // test against itself.
        expResult = true;
        result = a.equals(a);
        assertEquals(expResult, result);

        // test against equivalent content except case.
        expResult = true;
        result = a.equals(b);
        assertEquals(expResult, result);

        // prefix not equivalent
        expResult = false;
        result = a.equals(c);
        assertEquals(expResult, result);

        // path and tag not equivalent
        expResult = false;
        result = a.equals(c);
        assertEquals(expResult, result);

        // Send in different type of object.
        String tmpstr = "blah";
        result = a.equals( tmpstr );
        assertFalse( result );
    
    }

}