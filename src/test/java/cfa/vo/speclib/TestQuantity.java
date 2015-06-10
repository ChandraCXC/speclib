package cfa.vo.speclib;

/**
 * TestQuantity:
 *   JUnit test for Quantity class.
 */
/**
 * @author mdittmar
 */

import java.net.MalformedURLException;
import java.net.URL;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.BeforeClass;


public class TestQuantity
{
    static boolean verbose;
    
    public TestQuantity(){}
    
    @BeforeClass
    public static void setUpClass() 
    {
        verbose = false;
    }

    @Test
    public void testCopy()
    {
        Quantity a = new Quantity();
        Quantity b = new Quantity();
        Integer value = 1701;

        a.setModelpath("SPEC_TARGET_NAME");
        a.setID("001");
        a.setName("target");
        a.setDescription("Target name.");
        a.setUnit("");
        a.setUCD("meta.id;src");
        a.setUtype("spec:Target.Name");
        a.setValue( value );

        // Copy content
        b.fill(a);
        
        // Verify content is transferred.
        assertEquals("001", b.getID());
        assertEquals("SPEC_TARGET_NAME", b.getModelpath());
        assertEquals("target", b.getName());
        assertEquals("Target name.", b.getDescription());
        assertEquals("", b.getUnit());
        assertEquals("meta.id;src", b.getUCD());
        assertEquals("spec:Target.Name", b.getUtype());
        assertEquals( value, b.getValue());
        
        // Check we did value copy right.. should NOT be same object
        value = 9999;
        assertEquals( "1701", b.getValue().toString());
        assertEquals( "1701", a.getValue().toString());
        b.setValue(value);
        assertEquals( "9999", b.getValue().toString());
        assertEquals( "1701", a.getValue().toString());
        
    }
    
    @Test
    public void testBasic1() throws Exception 
    {
        Quantity q = new Quantity();

        // nothing should be set 
        assertEquals( false , q.hasModelpath());
        assertEquals( false , q.hasID());
        assertEquals( false , q.hasName());
        assertEquals( false , q.hasDescription());
        assertEquals( false , q.hasUnit());
        assertEquals( false , q.hasUCD());
        assertEquals( false , q.hasUtype());
        assertEquals( false , q.hasValue());
        
        q.setModelpath("SPEC_TARGET_NAME");
        q.setID("001");
        q.setName("target");
        q.setDescription("Target name.");
        q.setUnit("");
        q.setUCD("meta.id;src");
        q.setUtype("spec:Target.Name");
        q.setValue("NGC-1701");

        // everything should be set
        assertEquals( true , q.hasModelpath());
        assertEquals( true , q.hasID());
        assertEquals( true , q.hasName());
        assertEquals( true , q.hasDescription());
        assertEquals( true , q.hasUnit());
        assertEquals( true , q.hasUCD());
        assertEquals( true , q.hasUtype());
        assertEquals( true , q.hasValue());

        // check values
        assertEquals("SPEC_TARGET_NAME", q.getModelpath());
        assertEquals("001", q.getID());
        assertEquals("target", q.getName());
        assertEquals("Target name.", q.getDescription());
        assertEquals("", q.getUnit());
        assertEquals("meta.id;src", q.getUCD());
        assertEquals("spec:Target.Name", q.getUtype());
        assertEquals("NGC-1701", q.getValue());
    }
   
    @Test
    public void testBasic2() throws Exception 
    {
        Quantity q = new Quantity("NGC-1701");

        // only value should be set 
        assertEquals( false , q.hasModelpath());
        assertEquals( false , q.hasID());
        assertEquals( false , q.hasName());
        assertEquals( false , q.hasDescription());
        assertEquals( false , q.hasUnit());
        assertEquals( false , q.hasUCD());
        assertEquals( false , q.hasUtype());
        assertEquals( true  , q.hasValue());
        
        q.setModelpath("SPEC_TARGET_NAME");
        q.setID("001");
        q.setName("target");
        q.setDescription("Target name.");
        q.setUnit("");
        q.setUCD("meta.id;src");
        q.setUtype("spec:Target.Name");

        // everything should be set
        assertEquals( true , q.hasModelpath());
        assertEquals( true , q.hasID());
        assertEquals( true , q.hasName());
        assertEquals( true , q.hasDescription());
        assertEquals( true , q.hasUnit());
        assertEquals( true , q.hasUCD());
        assertEquals( true , q.hasUtype());
        assertEquals( true , q.hasValue());

        // check values
        assertEquals("SPEC_TARGET_NAME", q.getModelpath());
        assertEquals("001", q.getID());
        assertEquals("target", q.getName());
        assertEquals("Target name.", q.getDescription());
        assertEquals("", q.getUnit());
        assertEquals("meta.id;src", q.getUCD());
        assertEquals("spec:Target.Name", q.getUtype());
        assertEquals("NGC-1701", q.getValue());

    }
    @Test
    public void testBasic3() throws Exception 
    {
        Quantity q = new Quantity("target", "NGC-1701", "", "meta.id;src"  );

        // check attributes set 
        assertEquals( false , q.hasModelpath());
        assertEquals( false , q.hasID());
        assertEquals( true  , q.hasName());
        assertEquals( false , q.hasDescription());
        assertEquals( true  , q.hasUnit());
        assertEquals( true  , q.hasUCD());
        assertEquals( false , q.hasUtype());
        assertEquals( true  , q.hasValue());
        
        q.setModelpath("SPEC_TARGET_NAME");
        q.setID("001");
        q.setDescription("Target name.");
        q.setUtype("spec:Target.Name");

        // everything should be set
        assertEquals( true , q.hasModelpath());
        assertEquals( true , q.hasID());
        assertEquals( true , q.hasName());
        assertEquals( true , q.hasDescription());
        assertEquals( true , q.hasUnit());
        assertEquals( true , q.hasUCD());
        assertEquals( true , q.hasUtype());
        assertEquals( true , q.hasValue());

        // check values
        assertEquals("SPEC_TARGET_NAME", q.getModelpath());
        assertEquals("001", q.getID());
        assertEquals("target", q.getName());
        assertEquals("Target name.", q.getDescription());
        assertEquals("", q.getUnit());
        assertEquals("meta.id;src", q.getUCD());
        assertEquals("spec:Target.Name", q.getUtype());
        assertEquals("NGC-1701", q.getValue());

    }
    
    @Test
    public void testValueAccess() throws Exception 
    {
        Double  dval = new Double(3.14159);
        Integer ival = new Integer(100);
        String  sval = "NGC-1701";
        
        Double[]  darr = new Double[]{1.0, 2.0};
        Integer[] iarr = new Integer[]{1,3,5,7,9};

        URL url;
        try {
            url = new URL("http://www.ivoa.net/xml");
        } catch (MalformedURLException ex) {
            fail( ex.getMessage());
            return;
        }
        
        Quantity a = new Quantity( dval );
        Quantity b = new Quantity( ival );
        Quantity c = new Quantity( sval );
        Quantity d = new Quantity( darr );
        Quantity e = new Quantity( iarr );
        Quantity<URL> u = new Quantity("url", url ,"","meta.ref.url");
        
        assertEquals(a.getValue().getClass(), Double.class );
        assertEquals(b.getValue().getClass(), Integer.class );
        assertEquals(c.getValue().getClass(), String.class );

        assertEquals(d.getValue().getClass(), Double[].class );
        assertEquals(e.getValue().getClass(), Integer[].class );

        assertEquals(u.getValue().getClass(), URL.class );

        if (verbose)
        {
          System.out.println("TestQuantity:Values" );
          System.out.println( a.toString() );
          System.out.println( b.toString() );
          System.out.println( c.toString() );
          System.out.println( d.toString() );
          System.out.println( e.toString() );
          System.out.println( u.toString() );
        }
    }
    
    @Test
    public void testTypeSafety() throws Exception 
    {
        Double  dval = new Double(3.14159);
        Integer ival = new Integer(100);
        String  sval = "NGC-1701";
        
        Double[]  darr = new Double[]{1.0, 2.0};
        int count = 0;
        
        if ( verbose ){System.out.println("Test Type Safety");}

        if ( verbose ){System.out.println("  + create Quantities with values");}
        Quantity a = new Quantity( dval );
        Quantity b = new Quantity( ival );
        Quantity c = new Quantity( sval );
        Quantity e = new Quantity();

        if ( verbose ){ System.out.println("  + attempt to set different type");}
        try{
            if ( verbose ){ System.out.println("     - Integer on to Double ");}
            a.setValue(ival);
        }catch ( IllegalArgumentException ex){ count++;}
        
        try{
            if ( verbose ){ System.out.println("     - Double on to String ");}
            c.setValue(dval);
        }catch ( IllegalArgumentException ex){ count++;}

        e.setValue( dval );
        try{
          if ( verbose ){ System.out.println("     - Double[] on to Double ");}
            e.setValue(darr);
        }catch ( IllegalArgumentException ex){ count++;}

        if ( verbose ){ System.out.println("  + allows set by same primitive type");}
        try{
            if ( verbose ){ System.out.println("     - int on to Integer ");}
            b.setValue(count);
        }catch ( IllegalArgumentException ex){ count++;}

        try{
            if ( verbose ){ System.out.println("     - double on to Double ");}
            double k = 6.279;
            a.setValue(k);
        }catch ( IllegalArgumentException ex){ count++;}

        if ( verbose ){ System.out.println("  + Number of Exceptions caught = "+count);}

        // Should have had 3 errors.
        assertEquals( count, 3 );

        // Data types must not have been changed.
        assertEquals(a.getValue().getClass(), Double.class );
        assertEquals(b.getValue().getClass(), Integer.class );
        assertEquals(c.getValue().getClass(), String.class );
    }
   
    @Test
    public void testSimpleString() throws Exception 
    {
        Quantity a = new Quantity("target", "NGC-1701", "", "meta.id;src"  );
        a.setUtype("spec:Target.Name");
        Quantity b = new Quantity("syserr", new Double(400.0), "deg", "stat.error.sys"  );
        b.setUtype("spec:Char.SpacialAxis.Accuracy.SysError");
        Quantity c = new Quantity("quality", new Integer(3), "", "meta.qual.code"  );
        c.setUtype("spec:Data.FluxAxis.Accuracy.QualityStatus");
        Quantity d = new Quantity("darr", new Double[]{1.0, 2.0}, "mm", "something"  );
        d.setUtype("unk:Custom.Darr");

        if (verbose){
            System.out.println(a.toSimpleString());
            System.out.println(b.toSimpleString());
            System.out.println(c.toSimpleString());
            System.out.println(d.toSimpleString());
        }
    }
}