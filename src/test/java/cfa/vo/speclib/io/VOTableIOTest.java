/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

import cfa.vo.speclib.Bandpass;
import cfa.vo.speclib.DataSource;
import cfa.vo.speclib.Facility;
import cfa.vo.speclib.Instrument;
import cfa.vo.speclib.ObservingElement;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        Quantity q;

        factory = new SpectralFactory();
        ds = (SpectralDataset)factory.newInstance( SpectralDataset.class );
 
        try 
        {
          // Top level metadata.. bypass description.
          ds.setSpectralSI("L");
          ds.setTimeSI("T");
          ds.setFluxSI("1.E-23 MT-2");
          ds.setDataProductType("Spectrum");
          ds.setDataProductSubtype( new Quantity(null,"L2 Spectrum",null,"meta.id"));
         
          // DataModel specification... manually set description
          ds.getDataModel().setName("Spectrum-2.0");
          ds.getDataModel().getName().setDescription("Data model name and version");
          ds.getDataModel().setPrefix("spec");
          ds.getDataModel().getPrefix().setDescription("Data model prefix tag");
          ds.getDataModel().setURL( new URL("http://www.ivoa.net/sample/spectral"));
          ds.getDataModel().getURL().setDescription("Reference URL for model");
        
          // DataID metadata
          ds.getDataID().setTitle("Sample Spectrum Instance");
          ds.getDataID().setCreator("UNKNOWN");
          ds.getDataID().setDatasetID(new URI("ivo://ADS/Sa.CXO#obs/12345"));
          //ds.getDataID().setCreatorDID();
          ds.getDataID().setObservationID("12345");
          ds.getDataID().setDate("2001-12-08T03:11:11");
          ds.getDataID().setVersion("001");
          ds.getDataID().setCreationType("Archival");
          //  NOTE: Alternatively, for List type.. you can get the List and add
          //        the Quantities, but you also have to set the model path
          //        to them... see Contributor below
          List<Quantity> items = new ArrayList<Quantity>();  // TODO - need VOList type to propogate modelpath.
          items.add( new Quantity("Collection1","Chandra",null,null));
          items.add( new Quantity("Collection2","X-Ray",null,null));
          items.add( new Quantity("Collection3","Third Cambridge Catalogue of Radio Sources",null,null));
          ds.getDataID().setCollections(items);
          ds.getDataID().setLogo( new URL("http://www.cfa.harvard.edu/common/images/left/cfa-logo.gif"));
          items = ds.getDataID().getContributors();
          items.add( new Quantity("Contributor1", "This research has made use of software provided by the Chandra X-ray Center (CXC) in the application packages CIAO, ChIPS, and Sherpa.",null,null));
          items.get(0).setModelpath("SpectralDataset_DataID_Contributors");

          // Curation metadata
          ds.getCuration().setPublisher("Chandra X-ray Center");
          ds.getCuration().setPublisherID( new URI("ivo://cfa.harvard.edu"));
          ds.getCuration().setPublisherDID( new URI("ivo://cfa.harvard.edu/UNKNOWN"));
          ds.getCuration().setReleaseDate("2007-03-11T10:33:14");
          ds.getCuration().setVersion("002");
          ds.getCuration().setRights("public");
          items = new ArrayList<Quantity>();
          items.add( new Quantity(null,"UNKNOWN",null,null));
          ds.getCuration().setReferences(items);
          ds.getCuration().getContact().setName("CXC Help Desk");
          ds.getCuration().getContact().setEmail("cxchelp@head.cfa.harvard.edu");
          
          // Target metadata
          ds.getTarget().setName("3c273");
          ds.getTarget().setDescription("Optically Bright Quasar in Virgo");
          ds.getTarget().setRedshift(Double.valueOf("0.15833"));
          ds.getTarget().setTargetClass("Quasar");
          ds.getTarget().setSpectralClass("UNKNOWN");
          Double[] pos = new Double[]{ 187.2767, 2.0519};
          ds.getTarget().getPos().setValue( pos );
          ds.getTarget().setVarAmpl(Double.NaN);
          
          // ObsConfig metadata
          List<ObservingElement> elems = new ArrayList<ObservingElement>();
          elems.add( (ObservingElement)factory.newInstance( Facility.class ) );
          elems.add( (ObservingElement)factory.newInstance( Instrument.class ) );
          elems.add( (ObservingElement)factory.newInstance( Bandpass.class ) );
          elems.add( (ObservingElement)factory.newInstance( DataSource.class ) );
          elems.get(0).setName("CHANDRA");
          elems.get(1).setName("HRC");
          elems.get(2).setName("X-ray");
          elems.get(3).setName("pointed");
          ds.getObsConfig().setObservingElements(elems);

          // Proposal metadata
          ds.getProposal().setIdentifier("2001c2-p55422");

          // Derived metadata
          ds.getDerived().setSNR(Double.valueOf("1.3"));
          ds.getDerived().getRedshift().setValue(Double.valueOf("0.159"));
          ds.getDerived().getRedshift().setStatError(Double.valueOf("0.002"));
          ds.getDerived().getRedshift().setConfidence(Double.valueOf("0.999"));
          ds.getDerived().getVarAmpl().setValue(Double.valueOf("0.0001"));
 
        }
        catch (MalformedURLException ex) {
            Logger.getLogger(VOTableIOTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (URISyntaxException ex) {
            Logger.getLogger(VOTableIOTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Data Points
        Double[] freq = new Double[]{ 8.32826233e+14,
                                      8.32190479e+14,
                                      8.31555695e+14,
                                      8.30921878e+14,
                                      8.30289027e+14
                                     };
        Double[] flux = new Double[]{ 3.72981229e-30,
                                      2.58023996e-30,
                                      3.49485448e-30,
                                      3.53532448e-30,
                                      3.53108340e-30
                                     };
        
        List<SPPoint> data = ds.getData();
        Quantity c10 = new Quantity("freq",freq[0],"Hz","em.freq");
        Quantity c11 = new Quantity("freq_err",3.0e+10,"Hz","stat.error;em.freq");
        Quantity c12 = new Quantity("freq_syserr",-1.0e+3,"Hz","stat.error.sys;em.freq");
        Quantity c13 = new Quantity("freq_res",10.0,"Hz","spect.resolution;em.freq");
        Quantity c14 = new Quantity("freq_binsiz",6.3575e+11,"Hz","em.freq;spect.binSize");

        Quantity c20 = new Quantity("flux",flux[0],"W.m**(-2).Hz**(-1)","phot.flux.density;em.freq");
        Quantity c21 = new Quantity("flux_err",1.0e-35,"W.m**(-2).Hz**(-1)","stat.error;phot.flux.density;em.freq");
        Quantity c22 = new Quantity("flux_syserr",-1.0e-40,"W.m**(-2).Hz**(-1)","stat.error.sys;phot.flux.density;em.freq");
        Quantity c23 = new Quantity("flux_qual",(Integer)0,null,"meta.code.qual;phot.flux.density;em.freq");

        Quantity c30 = new Quantity("bkg",Double.NaN,"W.m**(-2).Hz**(-1)","phot.flux.density;em.freq");
        Quantity c31 = new Quantity("bkg_qual",(Integer)1,null,"meta.code.qual;phot.flux.density;em.freq");

        for ( int ii=0; ii<3; ii++)
        {
            //TODO - Should not have to go back to the factory to generate 
            //       new instances of List entries.. 
            SPPoint point = (SPPoint)factory.newInstance( SPPoint.class );

            c10.setValue(freq[ii]);
            point.getSpectralAxis().setValue( c10 );
            point.getSpectralAxis().getAccuracy().setBinSize(c14);
            point.getSpectralAxis().getAccuracy().setStatError( c11 );
            point.getSpectralAxis().getAccuracy().setSysError( c12 );
            point.getSpectralAxis().getResolution().setRefVal( c13 );

            c20.setValue(flux[ii]);
            point.getFluxAxis().setValue( c20 );
            point.getFluxAxis().getAccuracy().setStatError( c21 );
            point.getFluxAxis().getAccuracy().setSysError( c22 );
            point.getFluxAxis().getAccuracy().setQualityStatus( c23 );

            point.getBackgroundModel().setValue( c30 );   
            point.getBackgroundModel().getAccuracy().setQualityStatus( c31 );
            
            data.add(point);
        }
        
        return ds;
    }
}