/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

import cfa.vo.speclib.Quantity;
import cfa.vo.speclib.SPPoint;
import cfa.vo.speclib.SpectralDataset;
import cfa.vo.speclib.doc.SpectralFactory;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
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
public class VOTableIOTest {
    
    public VOTableIOTest() {
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
     * Test of read method, of class VOTableIO.
     */
    @Test
    public void testRead_URL() {
        System.out.println("read");
        URL file = null;
        VOTableIO instance = new VOTableIO();
        SpectralDataset expResult = null;
        SpectralDataset result;
        boolean ok;
        
        ok = false;
        try 
        {
           result = instance.read(file);
           assertEquals(expResult, result);
        }
        catch (UnsupportedOperationException ex) { ok = true; }
        assertTrue( ok );
    }

    /**
     * Test of read method, of class VOTableIO.
     */
    @Test
    public void testRead_InputStream() {
        System.out.println("read");
        InputStream is = null;
        VOTableIO instance = new VOTableIO();
        SpectralDataset expResult = null;
        SpectralDataset result;
        boolean ok;
        
        ok = false;
        try 
        {
           result = instance.read(is);
           assertEquals(expResult, result);
        }
        catch (UnsupportedOperationException ex) { ok = true; }
        assertTrue( ok );
    }

    /**
     * Test of write method, of class VOTableIO.
     */
    @Test
    public void testWrite_URL_SpectralDataset() {
        System.out.println("write");

        VOTableIO instance = new VOTableIO();
        URL outroot = this.getClass().getResource("/test_data");
        URL outfile = null;

        // Generate output file name.
        String tmpstr = outroot.toString() + "/out/spectrum.vot";
        try {
            outfile = new URL( tmpstr );
        } catch (MalformedURLException ex) {
            fail("Output file location not found.");
        }

        // Create test Spectrum instance.
        SpectralDataset doc = make_test_spectrum();

        // Write as VOTable.
        try {
            instance.write( outfile, doc);
        } catch (IOException ex) {
            fail("Error writing to output file.");
        }
        
        // TODO Validate results against baseline.
        // See: http://stackoverflow.com/questions/466841/comparing-text-files-w-junit
    }

    /**
     * Test of write method, of class VOTableIO.
     */
    @Test
    public void testWrite_OutputStream_SpectralDataset() {
        System.out.println("write");
        OutputStream os = null;
        
        // Generate output file name.
        URL outroot = this.getClass().getResource("/test_data");
        String outfile = outroot.getFile() + "/out/spectrum.vot";
        try {
            os = new FileOutputStream( outfile );
        } catch (FileNotFoundException ex) {
            fail("Error generating output file stream.");
        }
        assertNotNull(os);
        
        // Create test Spectrum instance
        SpectralDataset doc = make_test_spectrum();
        VOTableIO instance = new VOTableIO();

        // Write as VOTable.
        try {
            instance.write(os, doc);
            os.close();        
        } catch (IOException ex){
            System.out.println("MCD TEMP: Write Error - " + ex.toString());
            fail("Error writing to output file.");            
        }
    }
    
    
    private SpectralDataset make_test_spectrum(){
        SpectralFactory factory;
        SpectralDataset ds;

        factory = new SpectralFactory();
        ds = (SpectralDataset)factory.newInstance( SpectralDataset.class );
 
        // Top level metadata
        ds.setSpectralSI("L");
        ds.setTimeSI("T");
        ds.setFluxSI("1.E-23 MT-2");
        ds.setDataProductType("Spectrum");
        ds.setDataProductSubtype( new Quantity("dpSubType","L2 Spectrum",null,"meta.id"));
 
        // DataModel specification.
        ds.getDataModel().setName("Spectrum-2.0");
        ds.getDataModel().setPrefix("spec");
        //ds.getDataModel().setURL(null); //TODO
        
        // Derived metadata
        ds.getDerived().setSNR(Double.valueOf("1.3"));
 
        
        // Data Points
        Double[] flux = new Double[]{ 8.32826233e+14,
                                      8.32190479e+14,
                                      8.31555695e+14,
                                      8.30921878e+14,
                                      8.30289027e+14
                                     };
        Double[] freq = new Double[]{ 3.72981229e-30,
                                      2.58023996e-30,
                                      3.49485448e-30,
                                      3.53532448e-30,
                                      3.53108340e-30
                                     };
        
        List<SPPoint> data = ds.getData();
        for ( int ii=0; ii<3; ii++)
        {
            //TODO - Should not have to go back to the factory to generate 
            //       new instances of List entries.. 
            SPPoint point = (SPPoint)factory.newInstance( SPPoint.class );
            point.getFluxAxis().setValue( flux[ii] );
            point.getFluxAxis().getAccuracy().setStatError( 3.0e+10 );
            point.getSpectralAxis().setValue( freq[ii] );
            point.getSpectralAxis().getAccuracy().setStatError( 1.0e-35 );
            point.getBackgroundModel().setValue( Double.NaN );   
            
            data.add(point);
        }
        
        return ds;
    }
}