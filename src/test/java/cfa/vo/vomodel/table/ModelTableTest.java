/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.vomodel.table;

import cfa.vo.vomodel.Utype;
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
        verbose = false;
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
            tmpstr = instance.getUCD( 7 );
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
    public void testWrite() {
        if (verbose){ System.out.println("Test write"); }
        URL infile;
        String outfile;
        Boolean caught;

        // Load model spec.
        ModelTable instance = new ModelTable();
        infile = ModelTable.class.getResource("/spectrum_2p0.txt");
        caught = false;
        try {
            instance.read( infile );
        } 
        catch (IOException ex) { caught=true; }
        assertFalse(caught);

        // Null filename test.
        caught = false;
        try{
            instance.write(null);
        }
        catch (FileNotFoundException ex){caught=true;}
        catch (IOException ex){caught=false;} //not right exception
        assertTrue(caught);

        // Generate output file name.
        URL outroot = this.getClass().getResource("/test_data");
        outfile = outroot.toString() + "/out/spectrum_2p0.txt";        

        caught = false;
        try{
            instance.write( outfile );
        }
        catch (FileNotFoundException ex){caught=true;}
        catch (IOException ex){caught=true;}
        assertFalse(caught);
        
        //TODO - add verification of infile vs outfile.
    }

    /**
     * Test of getTitle method, of class ModelTable.
     */
    @Test
    public void testGetTitle() {
        if (verbose){ System.out.println("Test getTitle"); }
        String expResult = "Spectrum Version 2.0";
        String result = model.getTitle();
        assertEquals(expResult, result);
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
     * Test of getRecordIndex method, of class ModelTable.
     */
    @Test
    public void testGetRecordIndex() {
        if (verbose){ System.out.println("Test getRecordIndex"); }
        Utype utype = new Utype("SpectralDataset_Target_Name","Target.Name","spec");
        Integer expResult = 283;
        Integer result = model.getRecordIndex(utype);
        assertEquals(expResult, result);
        
        utype = new Utype("SpectralDataset_Path_DNE","none","spec");
        boolean caught=false;
        try{ result = model.getRecordIndex(utype);}
        catch ( IllegalArgumentException ex){ caught = true;}
        assertTrue(caught);
    }

        /**
     * Test of getRecordIndexByPath method, of class ModelTable.
     */
    @Test
    public void testGetRecordIndexByPath() {
        if (verbose){ System.out.println("Test getRecordIndexByPath"); }
        String mp = "SpectralDataset_Target_Name";
        Integer expResult = 283;
        Integer result = model.getRecordIndexByPath( mp );
        assertEquals(expResult, result);
       
        mp = "SpectralDataset_Char";
        expResult = 0;
        result = model.getRecordIndexByPath( mp );
        assertEquals(expResult, result);

        mp = "SpectralDataset_Element_DNE";
        expResult = -1;
        result = model.getRecordIndexByPath( mp );
        assertEquals(expResult, result );
        
        // null test (no match)
        mp = null;
        expResult = -1;
        result = model.getRecordIndexByPath( mp );
        assertEquals(expResult, result );
        
        // empty test (no match)
        mp = "  ";
        expResult = -1;
        result = model.getRecordIndexByPath( mp );
        assertEquals(expResult, result );
    }

    /**
     * Test of getRecordIndexByTag method, of class ModelTable.
     */
    @Test
    public void testGetRecordIndexByTag() {
        if (verbose){ System.out.println("Test getRecordIndexByTag"); }
        Utype utype = new Utype("SpectralDataset_Target_Name","Target.Name","spec");
        Integer expResult = 283;
        Integer result;
        boolean caught = false;
        
        try{ result = model.getRecordIndexByTag(utype.getTag());}
        catch ( UnsupportedOperationException ex){ caught = true;}
        assertTrue(caught);
    }
    
    /**
     * Test of getUtype method, of class ModelTable.
     */
    @Test
    public void testGetUtype() {
        if (verbose){ System.out.println("Test getUtype"); }
        Integer utypenum = 283;
        Utype utype = new Utype("SpectralDataset_Target_Name","Target.Name","spec");
        String expResult = "Target.Name";
        Utype result = model.getUtype(utypenum);
        assertTrue( ( result.getTag().indexOf(expResult) >= 0) );
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
        Utype utype = new Utype("SpectralDataset_Char_TimeAxis_Coverage_Location_Value","Char.TimeAxis.Coverage.Location.Value","spec");
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
        Utype utype = new Utype("SpectralDataset_Char_TimeAxis_Coverage_Location_Value","Char.TimeAxis.Coverage.Location.Value","spec");
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
        Utype utype = new Utype("SpectralDataset_Char_TimeAxis_Coverage_Location_Value","Char.TimeAxis.Coverage.Location.Value","spec");
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
        Utype utype = new Utype("SpectralDataset_Char_TimeAxis_CalibrationStatus","Char.TimeAxis.CalibrationStatus","spec");
        String expResult = "CALIBRATED";
        String result = model.getDefault(utype);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDescription method, of class ModelTable.
     */
    @Test
    public void testGetDescription_Integer() {
        if (verbose){ System.out.println("Test getDescription Int"); }
        Integer utypenum = 97;
        String expResult = "Type of coord calibration";
        String result = model.getDescription(utypenum);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDescription method, of class ModelTable.
     */
    @Test
    public void testGetDescription_String() {
        if (verbose){ System.out.println("Test getDescription String"); }
        Utype utype = new Utype("SpectralDataset_Char_TimeAxis_CalibrationStatus","Char.TimeAxis.CalibrationStatus","spec");
        String expResult = "Type of coord calibration";
        String result = model.getDescription(utype);
        assertEquals(expResult, result);
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
        Utype utype = new Utype("SpectralDataset_Char_TimeAxis_Coverage_Location_Value","Char.TimeAxis.Coverage.Location.Value","spec");
        Boolean expResult = true;
        Boolean result = model.isMandatory(utype);
        assertEquals(expResult, result);

        utype = new Utype("SpectralDataset_Char_TimeAxis_CalibrationStatus","Char.TimeAxis.CalibrationStatus","spec");
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
        Utype utype = new Utype("SpectralDataset_Char_TimeAxis_CalibrationStatus","Char.TimeAxis.CalibrationStatus","spec");
        Boolean expResult = true;
        Boolean result = model.isValidUtype(utype);
        assertEquals(expResult, result);

        utype = new Utype("One_Two_Three","utype label","spec");
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
        Utype expResult = new Utype("SpectralDataset_Char_TimeAxis_CalibrationStatus","Char.TimeAxis.CalibrationStatus","spec");
        int expSize = 288;
        
        List result = model.getUtypes();
        assertEquals(expResult, result.get(97));
        assertEquals(expSize, result.size());
    }    
}