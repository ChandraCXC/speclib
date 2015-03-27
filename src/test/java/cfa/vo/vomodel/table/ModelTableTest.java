/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.vomodel.table;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author mdittmar
 */
public class ModelTableTest {
    
    static ModelTable model;
    static boolean verbose;

    public ModelTableTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        verbose = true;
        try{
            model = new ModelTable();
            model.read( ModelTable.class.getResource("/spectrum_2p0.txt") );
        }
        catch ( IOException e ){
            System.out.println( e.getMessage());
            fail();
        }
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
    public void testEmptyModel() 
    {
        String tmpstr;
        Boolean ok = false;
        
        if (verbose){ System.out.println("Test Empty Model Handling.");}
        
        if (verbose){ System.out.println("  + Create empty model table.");}
        ModelTable instance = new ModelTable();

        if (verbose){ System.out.println("  + Access attributes.");}
        try {
            tmpstr = instance.getModelName();
        }
        catch ( IllegalStateException ex )
        {
            ok = true;
        }
        if ( !ok ){ fail("getModelName"); }
        ok = false;

        try {
            tmpstr = instance.getPrefix();
        }
        catch ( IllegalStateException ex )
        {
            ok = true;
        }
        if ( !ok ){ fail("getPrefix"); }
        ok = false;

        try {
            tmpstr = instance.getReferenceURL().toString();
        }
        catch ( IllegalStateException ex )
        {
            ok = true;
        }
        if ( !ok ){ fail("getReferenceURL"); }

        if (verbose){ System.out.println("  + Access record.");}
        try {
            tmpstr = instance.getUCD("some.utype.string");
        }
        catch ( IllegalStateException ex )
        {
            ok = true;
        }
        if ( !ok ){ fail("getUCD"); }

        assert(ok);
    }

    /**
     * Test of read method, of class ModelTable.
     */
    @Test
    public void testRead() {
        if (verbose){ System.out.println("Test read"); }
        Boolean caught;
        String filename = "/spectrum_2p0.txt";
        String badfile = "/badname.txt";

        if (verbose){ System.out.println("  + Create Empty ModelTable.");}
        ModelTable instance = new ModelTable();

        if (verbose){ System.out.println("  + Read Bad Filename.");}
        try{
            caught = false;
            instance.read(ModelTable.class.getResource(badfile));
        }
        catch (FileNotFoundException ex){caught=true;}
        catch (IOException ex){caught=false;} //not right exception
        assertTrue(caught);

        if (verbose){ System.out.println("  + Read Model File.");}
        try{
            caught = false;
            instance.read(ModelTable.class.getResource(filename));
        }
        catch (FileNotFoundException ex){caught=true;}
        catch (IOException ex){caught=true;}
        assertFalse(caught);

        if (verbose){ System.out.println("  + Access Model info.");}
        String name = instance.getModelName();
        assertTrue( name.equals("Spectrum-2.0"));
    }

    /**
     * Test of write method, of class ModelTable.
     */
    @Test
    @Ignore
    public void testWrite() {
        if (verbose){ System.out.println("write"); }
        String filename;
        ModelTable instance = new ModelTable();

        filename = System.getProperty("java.io.tmpdir");
        System.out.println("MCD TEMP: System property TMPDIR == XX"+filename+"XX");

        instance.write(filename);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setIncludeModelPrefix method, of class ModelTable.
     */
    @Test
    public void testSetIncludeModelPrefix() {
        if (verbose){ System.out.println("Test setIncludeModelPrefix"); }
        String utype;
        
        if (verbose){ System.out.println("  + get utype with prefix"); }
        model.setIncludeModelPrefix(true);
        utype = model.getUtype( 1 );
        assert( utype.equals("spec:Char.FluxAxis"));
        
        if (verbose){ System.out.println("  + change flag"); }
        model.setIncludeModelPrefix(false);

        if (verbose){ System.out.println("  + get utype without prefix"); }
        utype = model.getUtype( 1 );
        assert( utype.equals("Char.FluxAxis"));
    }

    /**
     * Test of getModelName method, of class ModelTable.
     */
    @Test
    public void testGetModelName() {
        if (verbose){ System.out.println("Test getModelName"); }
        String expResult = "Spectrum-2.0";
        String result = model.getModelName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPrefix method, of class ModelTable.
     */
    @Test
    public void testGetPrefix() {
        if (verbose){ System.out.println("Test getPrefix"); }
        String expResult = "spec";
        String result = model.getPrefix();
        assertEquals(expResult, result);
    }

    /**
     * Test of getReferenceURL method, of class ModelTable.
     */
    @Test
    public void testGetReferenceURL() {
        if (verbose){ System.out.println("Test getReferenceURL"); }
        String expResult = "http://www.ivoa.net/xml/Spectrum/Spectrum-2.0.xsd";
        URL result = model.getReferenceURL();
        assertEquals(expResult, result.toString());
    }

    /**
     * Test of getUtypeNum method, of class ModelTable.
     */
    @Test
    public void testGetUtypeNum() {
        if (verbose){ System.out.println("Test getUtypeNum"); }
        String utype = "Target.Name";
        Integer expResult = 269;
        Integer result = model.getUtypeNum(utype);
        assertEquals(expResult, result);
    }

    /**
     * Test of getUtype method, of class ModelTable.
     */
    @Test
    public void testGetUtype() {
        if (verbose){ System.out.println("Test getUtype"); }
        Integer utypenum = 269;
        String expResult = "Target.Name";
        String result = model.getUtype(utypenum);
        assertTrue( ( result.indexOf(expResult) >= 0) );
    }

    /**
     * Test of getUnit method, of class ModelTable.
     */
    @Test
    public void testGetUnit_Integer() {
        if (verbose){ System.out.println("Test getUnit Int"); }
        Integer utypenum = 111;
        String expResult = "";
        String result = model.getUnit(utypenum);
        assertEquals(expResult, result);
    }

    /**
     * Test of getUnit method, of class ModelTable.
     */
    @Test
    public void testGetUnit_String() {
        if (verbose){ System.out.println("Test getUnit String"); }
        String utype = "Char.TimeAxis.Coverage.Location.Value";
        String expResult = "";
        String result = model.getUnit(utype);
        assertEquals(expResult, result);
    }

    /**
     * Test of getUCD method, of class ModelTable.
     */
    @Test
    public void testGetUCD_Integer() {
        if (verbose){ System.out.println("Test getUCD Int"); }
        Integer utypenum = 111;
        String expResult = "time.epoch;obs";
        String result = model.getUCD(utypenum);
        assertEquals(expResult, result);
    }

    /**
     * Test of getUCD method, of class ModelTable.
     */
    @Test
    public void testGetUCD_String() {
        if (verbose){ System.out.println("Test getUCD String"); }
        String utype = "Char.TimeAxis.Coverage.Location.Value";
        String expResult = "time.epoch;obs";
        String result = model.getUCD(utype);
    }

    /**
     * Test of getType method, of class ModelTable.
     */
    @Test
    public void testGetType_Integer() {
        if (verbose){ System.out.println("Test getType Int"); }
        Integer utypenum = 111;
        String expResult = "Double";
        String result = model.getType(utypenum);
        assertEquals(expResult, result);
    }

    /**
     * Test of getType method, of class ModelTable.
     */
    @Test
    public void testGetType_String() {
        if (verbose){ System.out.println("Test getType String"); }
        String utype = "Char.TimeAxis.Coverage.Location.Value";
        String expResult = "Double";
        String result = model.getType(utype);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDefault method, of class ModelTable.
     */
    @Test
    public void testGetDefault_Integer() {
        if (verbose){ System.out.println("Test getDefault Int"); }
        Integer utypenum = 97;
        String expResult = "CALIBRATED";
        String result = model.getDefault(utypenum);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDefault method, of class ModelTable.
     */
    @Test
    public void testGetDefault_String() {
        if (verbose){ System.out.println("Test getDefault String"); }
        String utype = "Char.TimeAxis.CalibStatus";
        String expResult = "CALIBRATED";
        String result = model.getDefault(utype);
    }

    /**
     * Test of isMandatory method, of class ModelTable.
     */
    @Test
    public void testIsMandatory_Integer() {
        if (verbose){ System.out.println("Test isMandatory Int"); }
        Integer utypenum = 111;
        Boolean expResult = true;
        Boolean result = model.isMandatory(utypenum);
        assertEquals(expResult, result);

        utypenum = 97;
        expResult = false;
        result = model.isMandatory(utypenum);
        assertEquals(expResult, result);
}

    /**
     * Test of isMandatory method, of class ModelTable.
     */
    @Test
    public void testIsMandatory_String() {
        if (verbose){ System.out.println("Test isMandatory String"); }
        String utype = "Char.TimeAxis.Coverage.Location.Value";
        Boolean expResult = true;
        Boolean result = model.isMandatory(utype);
        assertEquals(expResult, result);

        utype = "Char.TimeAxis.CalibStatus";
        expResult = false;
        result = model.isMandatory(utype);
        assertEquals(expResult, result);
}

    /**
     * Test of isValidUtype method, of class ModelTable.
     */
    @Test
    public void testIsValidUtype() {
        if (verbose){ System.out.println("Test isValidUtype"); }
        String utype = "spec:Char.TimeAxis.CalibStatus";
        Boolean expResult = true;
        model.setIncludeModelPrefix(true);
        Boolean result = model.isValidUtype(utype);
        assertEquals(expResult, result);

        model.setIncludeModelPrefix(false);
        expResult = true;
        result = model.isValidUtype(utype);
        assertEquals(expResult, result);

        utype = "One.Two.Three";
        expResult = false;
        result = model.isValidUtype(utype);
        assertEquals(expResult, result);
    }

    /**
     * Test of getUtypes method, of class ModelTable.
     */
    @Test
    public void testGetUtypes() {
        if (verbose){ System.out.println("Test getUtypes"); }
        String expResult = "Char.TimeAxis.CalibStatus";
        int expSize = 276;
        
        model.setIncludeModelPrefix( false );
        List result = model.getUtypes();
        assertEquals(expResult, result.get(97));
        assertEquals(expSize, result.size());
        
        expResult = "spec:Char.TimeAxis.CalibStatus";
        model.setIncludeModelPrefix( true );
        result = model.getUtypes();
        assertEquals(expResult, result.get(97));
        assertEquals(expSize, result.size());
        
        
    }
}