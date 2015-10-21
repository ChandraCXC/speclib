/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

import cfa.vo.speclib.Quantity;
import cfa.vo.speclib.spectral.v1.SpectralDataset;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test code for validating I/O for Spectrum1.1 model
 * 
 * @author mdittmar
 */
public class TestSpectrum1 {
    
    static boolean verbose;
    
    public TestSpectrum1() {
    }
    
    @BeforeClass
    public static void setUpClass() {
      verbose = true;
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
     * Test read and write Spectrum-1.1 instance
     */
    @Test
    public void testReadWrite_VOT() {
        if (verbose){ System.out.println("Test read-write Spectrum-1.1"); }
        
        VOTableIO instance = new VOTableIO();
        SpectralDataset result = null;
        URL infile = this.getClass().getResource("/test_data/baseline/spectrum_1p1.vot");
        URL outroot = this.getClass().getResource("/test_data");
        URL outfile = null;

        // Generate output file name.
        try {
            String tmpstr = outroot.toString() + "/out/spectrum_1p1.vot";
            outfile = new URL( tmpstr );
        } catch (MalformedURLException ex) {
            fail("Output file location not found.");
        }
        
        
        try {
          result = (SpectralDataset)instance.read(infile);
        } catch (IOException ex){
            fail("Error reading input file. "+infile);
        }
        
        // Verify by checking selected values.
        this.verify_read_results( result );

        // Write instance out to VOTable
        try {
            instance.write( outfile, result );
        } catch (IOException ex) {
            fail("Error writing to output file.");
        }
    
    }
    
    
    private void verify_read_results( SpectralDataset ds ){
        Quantity item;
    
        // Must have actually gotten something back
        assertNotNull(ds);
        
        // Check Dataset attributes
        assertEquals( "Spectrum-1.0", ds.getDataModel().getValue() );
        assertEquals( "Spectrum", ds.getType().getValue() );
        assertEquals( "ms", ds.getTimeSI().getValue() );
        assertEquals( "time;arith.zp", ds.getTimeSI().getUCD() );
        assertEquals( "Angstrom", ds.getSpectralSI().getValue() );
        assertEquals( "ergs/m-2s-1Mev-1", ds.getFluxSI().getValue() );
        
        
    }
}