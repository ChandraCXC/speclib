/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

import cfa.vo.speclib.Quantity;
import cfa.vo.speclib.doc.MPArrayList;
import cfa.vo.speclib.doc.MPNode;
import cfa.vo.vomodel.DefaultModelBuilder;
import cfa.vo.vomodel.Model;
import org.junit.*;
import org.xml.sax.SAXException;
import uk.ac.starlink.votable.VOElement;
import uk.ac.starlink.votable.VOElementFactory;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
        MPNode doc = null;
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
        MPNode expResult = null;
        MPNode result = instance.convert(top);

        // Top level.. the TABLE;
        assertEquals("SpectralDataset", result.getModelpath());

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
        MPNode expResult = null;
        Model model = null;
        try{
          model = new DefaultModelBuilder("SPECTRUM-2.0").build();
        }catch (IOException ex){
          fail();
        }
        
        // Convert document according to model.
        MPNode result = instance.convert(top, model);

        // Top level.. the TABLE;
        assertEquals("SpectralDataset", result.getModelpath());

        if ( verbose )
        {
          System.out.println("Content:");
          System.out.println(result.toString());
        }
        
        // Verify some elements
        this.validate_results( result );
        
    }

    private void validate_results( MPNode result )
    {
        MPNode tmp;

        // URL element
        tmp = (MPNode)result.getChildByMP("SpectralDataset_DataModel");
        Quantity q = (Quantity)tmp.getChildByMP("SpectralDataset_DataModel_URL");
        assertEquals("http://www.ivoa.net/sample/spectral",((URL)q.getValue()).toString());

        String mp;
        // Double element
        tmp = (MPNode)result.getChildByMP("SpectralDataset_Target");
        q = (Quantity)tmp.getChildByMP("SpectralDataset_Target_Redshift");
        assertEquals((Double)0.15833, q.getValue());
        
        // String element
        tmp = (MPNode)result.getChildByMP("SpectralDataset_Curation");
        q = (Quantity)tmp.getChildByMP("SpectralDataset_Curation_Publisher");
        assertEquals("Chandra X-ray Center",q.getValue());
        
        // Element through array
        tmp = (MPNode)result.getChildByMP("SpectralDataset_ObsConfig");
        MPArrayList arr = (MPArrayList)tmp.getChildByMP("SpectralDataset_ObsConfig_ObservingElements");
        tmp = (MPNode)arr.get(0);
        q = (Quantity)tmp.getChildByMP("SpectralDataset_ObsConfig_ObservingElements[].Facility_Name");
        assertEquals("CHANDRA", q.getValue());
        
        // Array Element
        tmp = (MPNode)result.getChildByMP("SpectralDataset_DataID");
        arr = (MPArrayList)tmp.getChildByMP("SpectralDataset_DataID_Collections");
        q = (Quantity)arr.get(2);
        assertEquals("Third Cambridge Catalogue of Radio Sources", q.getValue());
        
        // Data Element
        tmp = (MPNode)((MPArrayList)result.getChildByMP("SpectralDataset_Data")).get(1);
        tmp = (MPNode)tmp.getChildByMP("SpectralDataset_Data[].SPPoint_SpectralAxis");
        tmp = (MPNode)tmp.getChildByMP("SpectralDataset_Data[].SPPoint_SpectralAxis_Accuracy");
        q = (Quantity)tmp.getChildByMP("SpectralDataset_Data[].SPPoint_SpectralAxis_Accuracy_StatError");
        assertEquals( 3.0e10, q.getValue());

    }

}