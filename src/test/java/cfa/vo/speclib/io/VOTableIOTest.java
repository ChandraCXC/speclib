/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.io;

import cfa.vo.speclib.*;
import cfa.vo.speclib.doc.ModelObjectFactory;
import cfa.vo.vomodel.DefaultModelBuilder;
import cfa.vo.vomodel.Model;
import cfa.vo.vomodel.ModelMetadata;
import org.junit.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 *
 * @author mdittmar
 */
public class VOTableIOTest {
    
    static boolean verbose;

    public VOTableIOTest() {
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

    @Test
    public void testRead_DACHS1() throws IOException {
        if (verbose){ System.out.println("Test read(URL)"); }
        URL file = this.getClass().getResource("/test_data/baseline/DACHS1.vot.xml");
        VOTableIO instance = new VOTableIO();
        SpectralDataset expResult = null;
        SpectralDataset result = null;
        boolean ok;
           Model model = new DefaultModelBuilder("Spectrum-2.0")
                   .withModelMetadata(new ModelMetadata() {
                       @Override
                       public String getTitle() {
                           return null;
                       }

                       @Override
                       public String getModelName() {
                           return null;
                       }

                       @Override
                       public String getPrefix() {
                           return "spec2";
                       }

                       @Override
                       public URL getReferenceURL() {
                           return null;
                       }
                   })
                   .build();
           result = instance.read(file, model);
           assertEquals("bet Ori", result.getTarget().getName().getValue());


    }
    
    /**
     * Test of read method, of class VOTableIO.
     */
    @Test
    public void testRead_URL() {
        if (verbose){ System.out.println("Test read(URL)"); }
        URL file = this.getClass().getResource("/test_data/baseline/spectrum_2p0.vot");
        VOTableIO instance = new VOTableIO();
        SpectralDataset expResult = null;
        SpectralDataset result = null;
        boolean ok;
        try {
          result = instance.read(file);
        } catch (IOException ex){
            fail("Error reading input file. "+file);
        }
        
        // Verify by checking selected values.
        this.verify_read_results( result );
        
    }

    /**
     * Test of read method, of class VOTableIO.
     */
    @Test
    public void testRead_URL_WithModel() {
        if (verbose){ System.out.println("Test read(URL, Model)"); }
        URL file = this.getClass().getResource("/test_data/baseline/spectrum_2p0.vot");
        VOTableIO instance = new VOTableIO();
        SpectralDataset expResult = null;
        SpectralDataset result = null;
        boolean ok;
        try {
          Model model = new DefaultModelBuilder("SPECTRUM-2.0").build();
          result = instance.read(file, model);
        } catch (IOException ex){
            fail("Error reading input file. "+file);
        }
        
        // Verify by checking selected values.
        this.verify_read_results(result);
    }
    
    /**
     * Test of read method, of class VOTableIO.
     */
    @Test
    public void testRead_InputStream() {
        if (verbose){ System.out.println("Test read(InputStream)");}
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
        catch (IOException iox ){ ok = false; }
        assertTrue( ok );
    }

    /**
     * Test of write method, of class VOTableIO.
     */
    @Test
    public void testWrite_URL_SpectralDataset() {
        if (verbose){ System.out.println("Test write(URL)");}

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
            instance.write( outfile, doc );
        } catch (IOException ex) {
            fail("Error writing to output file.");
        }

        // Validate results against baseline.
        tmpstr = outroot.getFile()+"/baseline/spectrum_2p0.vot";
        String savfile = tmpstr.replaceFirst(".*:", "");
        tmpstr = outfile.getFile();
        String runfile = tmpstr.replaceFirst(".*:", "");
        
        this.compare_files( savfile, runfile );
        
    }

    /**
     * Test of write method, of class VOTableIO.
     */
    @Test
    public void testWrite_OutputStream_SpectralDataset() {
        if (verbose){ System.out.println("Test write(OutputStream)");}
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
    
    /**
     * Test read/write of VOTable is lossless.
     */
    @Test
    public void testRoundRobin() {
        if (verbose){ System.out.println("Test RoundRobin"); }
        URL infile = this.getClass().getResource("/test_data/baseline/spectrum_2p0.vot");
        URL outroot = this.getClass().getResource("/test_data");
        URL outfile = null;

        // Generate output file name.
        try {
            String tmpstr = outroot.toString() + "/out/spectrum_rr.vot";
            outfile = new URL( tmpstr );
        } catch (MalformedURLException ex) {
            fail("Output file location not found.");
        }
        
        // Read baseline file.
        VOTableIO instance = new VOTableIO();
        SpectralDataset result = null;
        try {
          result = instance.read(infile);
        } catch (IOException ex ){ fail("Error reading baseline file. "+infile);}
        
        // Pull most troublesome element from the dataset
        boolean ok = result.getData().get(0).getFluxAxis().getCorrections(0).isSetApplied();
        assertTrue( ok );
        
        // Write as VOTable.
        try {
            instance.write( outfile, result );
        } catch (IOException ex) {
            fail("Error writing to output file.");
        }
        
        // Compare against baseline.
        String savfile = infile.getFile().replaceFirst(".*:", "");
        String runfile = outfile.getFile().replaceFirst(".*:", "");
        
        this.compare_files( savfile, runfile );
        
    }
    private void verify_read_results( SpectralDataset result )
    {
        Quantity q;
        
        // URL element
        q = result.getDataModel().getURL();
        assertEquals("http://www.ivoa.net/sample/spectral",((URL)q.getValue()).toString());

        // Double element
        q = result.getTarget().getRedshift();
        assertEquals((Double)0.15833, q.getValue());
        
        // String element
        q = result.getCuration().getPublisher();
        assertEquals("Chandra X-ray Center",q.getValue());
        
        // Element through array
        q = result.getObsConfig().getObservingElements().get(0).getName();
        assertEquals("CHANDRA", q.getValue());
        
        // Array Element
        q = result.getDataID().getCollections().get(2);
        assertEquals("Third Cambridge Catalogue of Radio Sources", q.getValue());
        
        // Data Element
        q = result.getData().get(1).getSpectralAxis().getAccuracy().getStatError();
        assertEquals(3.0e10, q.getValue());

    }

    private void compare_files( String savfile, String runfile )
    {
        int ndiffs = 0;
        try {
            List<String> savdata = Files.readAllLines(Paths.get(savfile),Charset.defaultCharset());
            List<String> outdata = Files.readAllLines(Paths.get(runfile),Charset.defaultCharset());

//            assertEquals( "File sizes differ!", savdata.size(), outdata.size());
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
            Logger.getLogger(VOTableIOTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private SpectralDataset make_test_spectrum(){
        ModelObjectFactory factory;
        SpectralDataset ds;
        Quantity q;

        factory = new ModelObjectFactory();
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
          ds.getDataID().setCreatorDID(new URI("xxx-xxxx")); // RANDOM
          ds.getDataID().setObservationID("12345");
          ds.getDataID().setDate("2001-12-08T03:11:11");
          ds.getDataID().setVersion("001");
          ds.getDataID().setCreationType("Archival");
          //  NOTE: Alternatively, for List type.. you can get the List 
          //        and add the Quantities, see Contributors below
          List<Quantity> items = new ArrayList<Quantity>();
          items.add( new Quantity("Collection1","Chandra",null,null));
          items.add( new Quantity("Collection2","X-Ray",null,null));
          items.add( new Quantity("Collection3","Third Cambridge Catalogue of Radio Sources",null,null));
          ds.getDataID().setCollections(items);
          ds.getDataID().setLogo(new URL("http://www.cfa.harvard.edu/common/images/left/cfa-logo.gif"));

          items = ds.getDataID().getContributors();
          items.add( new Quantity("Contributor1", "This research has made use of software provided by the Chandra X-ray Center (CXC) in the application packages CIAO, ChIPS, and Sherpa.",null,null));

          // Curation metadata
          ds.getCuration().setPublisher("Chandra X-ray Center");
          ds.getCuration().setPublisherID(new URI("ivo://cfa.harvard.edu"));
          ds.getCuration().setPublisherDID(new URI("ivo://cfa.harvard.edu/UNKNOWN"));
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
          
          // CoordSys metadata
          List<CoordSys> systems = ds.getCoordSys();
          CoordSys system = (CoordSys)factory.newInstance( CoordSys.class );
          systems.add(system);

          List<CoordFrame> cframes = system.getCoordFrames();
          CoordFrame cframe = (CoordFrame)factory.newInstance( CoordFrame.class );
          cframe.setID("CoordFrame_1");
          cframe.setName("Sample");
          cframe.setUCD("xxx.xxx");
          cframe.setReferencePosition("UNKNOWN");
          cframes.add(cframe);
          
          List<FluxFrame> fframes = system.getFluxFrames();
          FluxFrame fluxframe = (FluxFrame)factory.newInstance( FluxFrame.class );
          fluxframe.setID("FluxFrame_1");
          fluxframe.setName("B");
          fluxframe.setUCD("phot.mag");
          fluxframe.setReferencePosition("UNKNOWN");
          fluxframe.setRefID(new URI("ivo://some.resource.edu/B_Filter/phot_cal"));
          fframes.add(fluxframe);
          
          system.getSpaceFrame().setID("SpaceFrame_ICRS");
          system.getSpaceFrame().setName("ICRS");
          system.getSpaceFrame().setUCD("pos.eq");
          system.getSpaceFrame().setReferencePosition("UNKNOWN");
          system.getSpaceFrame().setEquinox(2000.0);
          
          system.getSpectralFrame().setID("SpecFrame_HZ");
          system.getSpectralFrame().setName("FREQ");
          system.getSpectralFrame().setUCD("em.freq");
          system.getSpectralFrame().setReferencePosition("TOPOCENTER");
          system.getSpectralFrame().setRedshift(0.0);
          
          system.getTimeFrame().setID("TimeFrame_TT");
          system.getTimeFrame().setName("TT");
          system.getTimeFrame().setUCD("time");
          system.getTimeFrame().setReferencePosition("TOPOCENTER");
          system.getTimeFrame().setZero(0.0);
          
          system.getRedshiftFrame().setID("RSFrame_1");
          system.getRedshiftFrame().setName("PRS");
          system.getRedshiftFrame().setUCD("src.redshift.phot");
          system.getRedshiftFrame().setReferencePosition("UNKNOWN");
          system.getRedshiftFrame().setDopplerDefinition("optical");

          // Characterisation metadata
          List<CharacterizationAxis> axes = ds.getCharacterization().getCharacterizationAxes();
          FluxCharAxis fluxchar = (FluxCharAxis)factory.newInstance( FluxCharAxis.class ); 
          TimeCharAxis timechar = (TimeCharAxis)factory.newInstance( TimeCharAxis.class ); 
          SpatialCharAxis spacechar = (SpatialCharAxis)factory.newInstance( SpatialCharAxis.class ); 
          SpectralCharAxis specchar = (SpectralCharAxis)factory.newInstance( SpectralCharAxis.class ); 
          axes.add(fluxchar);
          axes.add(timechar);
          axes.add(spacechar);
          axes.add(specchar);
          ds.getCharacterization().setCharacterizationAxes(axes);
          
          // Flux Characterization Axis
          fluxchar.setName("flux");
          fluxchar.setUCD("phot.flux.density;em.freq");
          fluxchar.setUnit("W.m**(-2).Hz**(-1)");
          fluxchar.setCalibrationStatus("CALIBRATED");
          fluxchar.getAccuracy().setStatError(new Quantity("flux_err", 1.0e-35, "W.m**(-2).Hz**(-1)", "stat.error;phot.flux.density;em.freq"));
          fluxchar.getAccuracy().setSysError(new Quantity("flux_syserr", -1.0e-40, "W.m**(-2).Hz**(-1)", "stat.error.sys;phot.flux.density;em.freq"));
          List<QualityCode> quals = fluxchar.getQualityDefs();
          QualityCode qcode = (QualityCode)factory.newInstance( QualityCode.class );
          qcode.setCodeNum(0);
          qcode.setDefinition("Good");
          quals.add(qcode);
          qcode = (QualityCode)factory.newInstance( QualityCode.class );
          qcode.setCodeNum(1);
          qcode.setDefinition("Bad");
          quals.add(qcode);
          
          // Time Characterization Axis
          timechar.setName("time");
          timechar.setUCD("time");
          timechar.setUnit("d");
          timechar.setCalibrationStatus("CALIBRATED");
          timechar.getAccuracy().setBinSize(new Quantity("time_binsiz", 2.89e-6, "d", "time.interval"));
          timechar.getAccuracy().setStatError(new Quantity("time_err", 2.89e-6, "d", "stat.error;time"));
          timechar.getAccuracy().setSysError(new Quantity("time_syserr", 1.16e-6, "d", "stat.error.sys;time"));

          pos = new Double[]{ 62514.3 };
          timechar.getCoverage().getLocation().setValue( pos );
          timechar.getCoverage().getBounds().setExtent(0.416);
          timechar.getCoverage().getBounds().setStart(62514.2792);
          timechar.getCoverage().getBounds().setStop(62514.3208);
          timechar.getCoverage().getSupport().setExtent(0.387);
          timechar.getResolution().setRefVal(2.89e-6);
          timechar.getSamplingPrecision().setSampleExtent(2.89e-6);
          timechar.getSamplingPrecision().getSamplingPrecisionRefVal().setFillFactor(0.93);
          quals = timechar.getQualityDefs();
          qcode = (QualityCode)factory.newInstance( QualityCode.class );
          qcode.setCodeNum(128);
          qcode.setDefinition("Bad");
          quals.add(qcode);
          
          
          // Spatial Characterization Axis
          spacechar.setName("sky");
          spacechar.setUCD("pos.eq");
          spacechar.setUnit("deg");
          spacechar.setCalibrationStatus("CALIBRATED");
          spacechar.getAccuracy().setStatError(new Quantity("sky_err", 2.77e-4, "deg", "stat.error;pos.eq"));
          spacechar.getAccuracy().setSysError(new Quantity("sky_syserr", 1.385e-5, "deg", "stat.error.sys;pos.eq"));
          pos = new Double[]{ 187.2767, 2.0519};
          spacechar.getCoverage().getLocation().setValue(new Quantity(null, pos, "deg","pos.eq"));
          spacechar.getCoverage().getBounds().setExtent(3.0);
          spacechar.getCoverage().getSupport().setExtent(10.0);
          spacechar.getCoverage().getSupport().setArea("circle 187.2767 2.0519 10.0");
          spacechar.getResolution().setRefVal(new Quantity(null, 1.0, "arcsec", "pos.angResolution"));
          spacechar.getSamplingPrecision().setSampleExtent(6.94e-6);
          spacechar.getSamplingPrecision().getSamplingPrecisionRefVal().setFillFactor(new Quantity(null, 1.0, null, "stat.filling;pos.eq"));
          quals = spacechar.getQualityDefs();
          qcode = (QualityCode)factory.newInstance( QualityCode.class );
          qcode.setCodeNum(255);
          qcode.setDefinition("Bad");
          quals.add(qcode);

          // Spectral Characterization Axis
          specchar.setName("freq");
          specchar.setUCD("em.freq");
          specchar.setUnit("Hz");
          specchar.setCalibrationStatus("CALIBRATED");
          specchar.getAccuracy().setBinSize(new Quantity("freq_binsiz", 6.3575e+11, "Hz", "em.freq;spect.binSize"));
          specchar.getAccuracy().setStatError(new Quantity("freq_err", 3.0e+10, "Hz", "stat.error;em.freq"));
          specchar.getAccuracy().setSysError(new Quantity("freq_syserr", -1.0e+3, "Hz", "stat.error.sys;em.freq"));

          pos = new Double[]{ 8.3e-14 };
          specchar.getCoverage().getLocation().setValue(new Quantity(null, pos, "Hz","em.freq;instr.bandpass"));
          specchar.getCoverage().getBounds().setExtent(5e-14);
          specchar.getCoverage().getBounds().setStart(new Quantity(null, 3.3e-14, "Hz", "em.freq;stat.min"));
          specchar.getCoverage().getBounds().setStop(new Quantity(null, 1.33e-13, "Hz", "em.freq;stat.max"));
          specchar.getCoverage().getSupport().setExtent(10e-14);
          specchar.getResolution().setRefVal(new Quantity(null,6.3575e+11,"Hz","spect.resolution;em.freq") );
          specchar.getResolution().getResolPower().setRefVal(Double.NaN);
          specchar.getSamplingPrecision().setSampleExtent(new Quantity(null,6.3575e+11,"Hz","em.freq;spect.binSize"));
          specchar.getSamplingPrecision().getSamplingPrecisionRefVal().setFillFactor(new Quantity(null,1.0,null,"stat.filling;em.freq"));
          quals = specchar.getQualityDefs();
          qcode = (QualityCode)factory.newInstance( QualityCode.class );
          qcode.setCodeNum(255);
          qcode.setDefinition("Bad");
          quals.add(qcode);

          
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
        Double[] bkg  = new Double[]{ 3.72981229e-30/2.0,
                                      2.58023996e-30/2.0,
                                      3.49485448e-30/2.0,
                                      3.53532448e-30/2.0,
                                      3.53108340e-30/2.0
                                     }; 
        List<SPPoint> data = ds.getData();
        for ( int ii=0; ii<3; ii++) {

          Quantity c10 = new Quantity("freq",freq[ii],"Hz","em.freq");
          Quantity c11 = new Quantity("freq_err",3.0e+10,"Hz","stat.error;em.freq");
          Quantity c12 = new Quantity("freq_syserr",-1.0e+3,"Hz","stat.error.sys;em.freq");
          Quantity c13 = new Quantity("freq_res",10.0,"Hz","spect.resolution;em.freq");
          Quantity c14 = new Quantity("freq_binsiz",6.3575e+11,"Hz","em.freq;spect.binSize");

          Quantity c20 = new Quantity("flux",flux[ii],"W.m**(-2).Hz**(-1)","phot.flux.density;em.freq");
          Quantity c21 = new Quantity("flux_err",1.0e-35,"W.m**(-2).Hz**(-1)","stat.error;phot.flux.density;em.freq");
          Quantity c22 = new Quantity("flux_syserr",-1.0e-40,"W.m**(-2).Hz**(-1)","stat.error.sys;phot.flux.density;em.freq");
          Quantity c23 = new Quantity("flux_qual",(Integer)0,null,"meta.code.qual;phot.flux.density;em.freq");

          Quantity c30 = new Quantity("bkg",bkg[ii],"W.m**(-2).Hz**(-1)","phot.flux.density;em.freq");
          Quantity c31 = new Quantity("bkg_errlo",1.5e-40,"W.m**(-2).Hz**(-1)","stat.error.sys;phot.flux.density;em.freq;stat.min");
          Quantity c32 = new Quantity("bkg_errhi",2.5e-40,"W.m**(-2).Hz**(-1)","stat.error.sys;phot.flux.density;em.freq;stat.max");
          Quantity c33 = new Quantity("bkg_qual",(Integer)1,null,"meta.code.qual;phot.flux.density;em.freq");

          //TODO - Would prefer to not go back to the factory to generate 
          //       new instances of List entries.. not sure how to tell
          //       it which flavor of content is wanted (ie subclass of List type)
          SPPoint point = (SPPoint)factory.newInstance( SPPoint.class );

          point.getSpectralAxis().setValue( c10 );
          point.getSpectralAxis().getAccuracy().setBinSize(c14);
          point.getSpectralAxis().getAccuracy().setStatError( c11 );
          point.getSpectralAxis().getAccuracy().setSysError( c12 );
          point.getSpectralAxis().getResolution().setRefVal( c13 );

          point.getFluxAxis().setValue( c20 );
          point.getFluxAxis().getAccuracy().setStatError( c21 );
          point.getFluxAxis().getAccuracy().setSysError( c22 );
          point.getFluxAxis().getAccuracy().setQualityStatus( c23 );
          List<Correction> corrs = new ArrayList<Correction>();
          ApFrac corr = (ApFrac)factory.newInstance( ApFrac.class );
          corr.setName("ApFrac");
          corr.setValue(0.75);
          corr.setApplied( Boolean.TRUE );
          corrs.add(corr);
          point.getFluxAxis().setCorrections(corrs);

          point.getBackgroundModel().setValue( c30 );   
          point.getBackgroundModel().getAccuracy().setStatErrLow( c31 );
          point.getBackgroundModel().getAccuracy().setStatErrHigh( c32 );
          point.getBackgroundModel().getAccuracy().setQualityStatus( c33 );
            
          data.add(point);
        }
        
        return ds;
    }
}