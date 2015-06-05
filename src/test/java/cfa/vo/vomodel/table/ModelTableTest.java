/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.vomodel.table;

import cfa.vo.vomodel.Utype;
import org.junit.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            model = new ModelTableBuilder(ModelTable.class.getResource("/spectrum_2p0.txt")).build();
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
        ModelTable instance = null;

        if (verbose){ System.out.println("  + Read Bad Filename.");}
        try{
            caught = false;
            instance = new ModelTableBuilder(ModelTable.class.getResource(badfile)).build();
        }
        catch (FileNotFoundException ex){caught=true;}
        catch (IOException ex){caught=false;} //not right exception
        assertTrue(caught);

        if (verbose){ System.out.println("  + Read Model File.");}
        try{
            caught = false;
            instance = new ModelTableBuilder(ModelTable.class.getResource(filename)).build();
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
        URL outfile = null;
        Boolean caught;
        ModelTable instance = null;

        // Generate file names.
        infile = ModelTable.class.getResource("/spectrum_2p0.txt");
        URL outroot = this.getClass().getResource("/test_data");
        try {
            outfile = new URL( outroot.toString() + "/out/spectrum_2p0.txt" );
        } catch (MalformedURLException ex) {
            fail("Output file location not found.");
        }

        // Load model spec.

        caught = false;
        try {
            instance = new ModelTableBuilder(infile).build();
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

        // Write to file.
        caught = false;
        try{
            instance.write( outfile );
        }
        catch (FileNotFoundException ex){caught=true;}
        catch (IOException ex){caught=true;}
        assertFalse(caught);
        
        // Validate results against baseline.
        String tmpstr = outroot.getFile()+"/baseline/spec2p0_model_table.txt";
        String savfile = tmpstr.replaceFirst(".*:", "");
        String runfile = outfile.getFile().replaceFirst(".*:", "");
        
        this.compare_files( savfile, runfile );
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
        Integer expResult = 289;
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
        Integer expResult = 289;
        Integer result = model.getRecordIndexByPath( mp );
        assertEquals(expResult, result);
       
        mp = "SpectralDataset_Characterization";
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

        String utype = "Target.Name";
        Integer expResult = 289;
        Integer result = model.getRecordIndexByTag( utype );
        assertEquals(expResult, result);
       
        utype = "spec:Target.Name";
        expResult = 289;
        result = model.getRecordIndexByTag( utype );
        assertEquals(expResult, result);

        utype = "Char";
        expResult = 0;
        result = model.getRecordIndexByTag( utype );
        assertEquals(expResult, result);

        utype = "SpectralDataset.Element.DNE";
        expResult = -1;
        result = model.getRecordIndexByTag( utype );
        assertEquals(expResult, result );
        
        // null test (no match)
        utype = null;
        expResult = -1;
        result = model.getRecordIndexByTag( utype );
        assertEquals(expResult, result );
        
        // empty test (no match)
        utype = "  ";
        expResult = -1;
        result = model.getRecordIndexByTag( utype );
        assertEquals(expResult, result );
    }
    
    /**
     * Test of getUtype method, of class ModelTable.
     */
    @Test
    public void testGetUtype() {
        if (verbose){ System.out.println("Test getUtype"); }
        Integer utypenum = 289;
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
        Utype utype = new Utype("SpectralDataset_Characterization_CharacterizationAxes[].TimeCharAxis_Coverage_Location_Value","Char.TimeAxis.Coverage.Location.Value","spec");
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
        Utype utype = new Utype("SpectralDataset_Characterization_CharacterizationAxes[].TimeCharAxis_Coverage_Location_Value","Char.TimeAxis.Coverage.Location.Value","spec");
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
        Utype utype = new Utype("SpectralDataset_Characterization_CharacterizationAxes[].TimeCharAxis_Coverage_Location_Value","Char.TimeAxis.Coverage.Location.Value","spec");
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
        Utype utype = new Utype("SpectralDataset_Characterization_CharacterizationAxes[].TimeCharAxis_CalibrationStatus","Char.TimeAxis.CalibrationStatus","spec");
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
        Utype utype = new Utype("SpectralDataset_Characterization_CharacterizationAxes[].TimeCharAxis_CalibrationStatus","Char.TimeAxis.CalibrationStatus","spec");
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
        Utype utype = new Utype("SpectralDataset_Characterization_CharacterizationAxes[].TimeCharAxis_Coverage_Location_Value","Char.TimeAxis.Coverage.Location.Value","spec");
        Boolean expResult = true;
        Boolean result = model.isMandatory(utype);
        assertEquals(expResult, result);

        utype = new Utype("SpectralDataset_Characterization_CharacterizationAxes[].TimeCharAxis_CalibrationStatus","Char.TimeAxis.CalibrationStatus","spec");
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
        Utype utype = new Utype("SpectralDataset_Characterization_CharacterizationAxes[].TimeCharAxis_CalibrationStatus","Char.TimeAxis.CalibrationStatus","spec");
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
        Utype expResult = new Utype("SpectralDataset_Characterization_CharacterizationAxes[].TimeCharAxis_CalibrationStatus","Char.TimeAxis.CalibrationStatus","spec");
        int expSize = 294;
        
        List result = model.getUtypes();
        assertEquals(expResult, result.get(97));
        assertEquals(expSize, result.size());
    }    
    
    private void compare_files( String savfile, String runfile )
    {
        int ndiffs = 0;
        try {
            List<String> savdata = Files.readAllLines(Paths.get(savfile),Charset.defaultCharset());
            List<String> outdata = Files.readAllLines(Paths.get(runfile),Charset.defaultCharset());

            int nrows = savdata.size();
            if ( savdata.size() != outdata.size())
            {
                System.out.println("File sizes differ "+savdata.size()+" vs "+outdata.size());
                ndiffs++;
                if ( outdata.size() > nrows )
                    nrows = outdata.size();
            }

            String sline;
            String oline;
            for ( int ii = 0; ii < nrows; ii++ )
            {
               if ( ii >= savdata.size() )
                   sline = "MISSING";
               else
               {
                 sline = savdata.get(ii).replaceAll("........-....-....-....-............", "ID");
                 savdata.set(ii, sline);
               }
               if ( ii >= outdata.size() )
                   oline = "MISSING";
               else
               {
                 oline = outdata.get(ii).replaceAll("........-....-....-....-............", "ID");
                 outdata.set(ii, oline);
               }

               if ( ! sline.equals( oline ))
               {
                   System.out.println("Line "+ii+" differs:");
                   System.out.println("    Expected: "+sline);
                   System.out.println("    Resulted: "+oline);
                   System.out.println("");
                   ndiffs++;
               }
            }
            assertEquals( "Files differ! ", 0 , ndiffs);

        } catch (IOException ex) {
            System.out.println("Test compare_files.. hit IO Exception = ");
            Logger.getLogger(ModelTableTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}