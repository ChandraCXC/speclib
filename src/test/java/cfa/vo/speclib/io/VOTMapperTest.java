/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

import cfa.vo.speclib.Quantity;
import cfa.vo.speclib.doc.MPArrayList;
import cfa.vo.speclib.doc.ModelDocument;
import cfa.vo.vomodel.Model;
import cfa.vo.vomodel.ModelFactory;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import uk.ac.starlink.votable.VOElement;
import uk.ac.starlink.votable.VOElementFactory;

/**
 *
 * @author mdittmar
 */
public class VOTMapperTest {

    private static boolean verbose;
    private static VOElement top;
    
    public VOTMapperTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        verbose = false;
        
        Quantity q = new Quantity();
        URL infile = q.getClass().getResource("/test_data/baseline/spectrum_2p0.vot");
        //Load VOTable file.
        try {
            top = new VOElementFactory().makeVOElement( infile );
        } catch (SAXException ex) {
            Logger.getLogger(VOTMapperTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VOTMapperTest.class.getName()).log(Level.SEVERE, null, ex);
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
     * Test of convert method, of class VOTMapper.
     */
    @Test
    public void testConvert_ModelDocument() {
        if (verbose){System.out.println("convert_ModelDoc");}
        ModelDocument doc = null;
        VOTMapper instance = new VOTMapper();
        VOElement expResult = null;
        VOElement result = instance.convert(doc);
        assertEquals(expResult, result);
        //fail("The test case is a prototype.");
    }

    /**
     * Test of convert method, of class VOTMapper.
     */
    @Test
    public void testConvert_VOElement() {
        if (verbose){System.out.println("convert_VOT");}
        VOTMapper instance = new VOTMapper();
        ModelDocument expResult = null;
        ModelDocument result = instance.convert(top);

        // Top level, only 1 element.. the TABLE;
        assertEquals(1, result.getKeys().size());

        if ( verbose )
        {
          System.out.println("Content:");
          System.out.println(result.toString());
        }
        
        // Verify some elements
        this.validate_results( result );
        
    }

    /**
     * Test of convert method, of class VOTMapper.
     */
    @Test
    public void testConvert_VOElement_WithModel() {
        if (verbose){System.out.println("convert_VOT_WithModel");}
        VOTMapper instance = new VOTMapper();
        ModelDocument expResult = null;
        Model model = null;
        try{
          model = new ModelFactory().newInstance("SPECTRUM-2.0");
        }catch (IOException ex){
          fail();
        }
        
        // Convert document according to model.
        ModelDocument result = instance.convert(top, model);

        // Top level, only 1 element.. the TABLE;
        assertEquals(1, result.getKeys().size());

        if ( verbose )
        {
          System.out.println("Content:");
          System.out.println(result.toString());
        }
        
        // Verify some elements
        this.validate_results( result );
        
    }

    private void validate_results( ModelDocument result )
    {

        // URL element
        ModelDocument tmp = (ModelDocument)result.get("SpectralDataset");
        tmp = (ModelDocument)tmp.get("SpectralDataset_DataModel");
        Quantity q = (Quantity)tmp.get("SpectralDataset_DataModel_URL");
        assertEquals("http://www.ivoa.net/sample/spectral",((URL)q.getValue()).toString());

        String mp;
        // Double element
        tmp = (ModelDocument)result.get("SpectralDataset");
        tmp = (ModelDocument)tmp.get("SpectralDataset_Target");
        q = (Quantity)tmp.get("SpectralDataset_Target_Redshift");
        assertEquals((Double)0.15833, q.getValue());
        
        // String element
        tmp = (ModelDocument)result.get("SpectralDataset");
        tmp = (ModelDocument)tmp.get("SpectralDataset_Curation");
        q = (Quantity)tmp.get("SpectralDataset_Curation_Publisher");
        assertEquals("Chandra X-ray Center",q.getValue());
        
        // Element through array
        tmp = (ModelDocument)result.get("SpectralDataset");
        tmp = (ModelDocument)tmp.get("SpectralDataset_ObsConfig");
        MPArrayList arr = (MPArrayList)tmp.get("SpectralDataset_ObsConfig_ObservingElements");
        tmp = (ModelDocument)arr.get(0);
        q = (Quantity)tmp.get("SpectralDataset_ObsConfig_ObservingElements[].Facility_Name");
        assertEquals("CHANDRA", q.getValue());
        
        // Array Element
        tmp = (ModelDocument)result.get("SpectralDataset");
        tmp = (ModelDocument)tmp.get("SpectralDataset_DataID");
        arr = (MPArrayList)tmp.get("SpectralDataset_DataID_Collections");
        q = (Quantity)arr.get(2);
        assertEquals("Third Cambridge Catalogue of Radio Sources", q.getValue());
        
        // Data Element
        tmp = (ModelDocument)result.get("SpectralDataset");
        tmp = (ModelDocument)((MPArrayList)tmp.get("SpectralDataset_Data")).get(1);
        q = (Quantity)tmp.get("SpectralDataset_Data[].SPPoint_SpectralAxis_Accuracy_StatError");
        assertEquals( 3.0e10, q.getValue());

    }

}