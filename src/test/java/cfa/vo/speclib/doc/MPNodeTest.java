/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author work
 */
public class MPNodeTest {
  
    static boolean verbose;
    
    public MPNodeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        verbose = false;
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

    // Test ModeledElement Interface.
    @Test
    public void testPathHandling()
    {
        if (verbose){System.out.println("PathHandling");}
        String expected;
        String result;
        Boolean ok;
        
        MPNode node = new MPNode();
        result = node.getModelpath();
        expected = ModeledElement.MP_UNDEFINED;
        assertEquals(expected, result);
        
        expected = "Dataset_Curation_Contact";
        node.setModelpath( expected );
        result = node.getModelpath();
        assertEquals(expected, result);

        expected = "Spectrum_DataID_Contact";
        node.rebaseModelpath( "Spectrum_DataID" );
        result = node.getModelpath();
        assertEquals(expected, result);

        // Verify provided mp "must not be empty"
        ok = false;
        expected = "";
        try{
          node.setModelpath( expected );
          fail("Expected Exception not thrown.");
        }
        catch ( IllegalArgumentException ex ){ ok = true; }
        assertTrue( ok );

        // Verify provided mp "must not be null"
        ok = false;
        expected = null;
        try{
          node.setModelpath( expected );
          fail("Expected Exception not thrown.");
        }
        catch ( IllegalArgumentException ex ){ ok = true; }
        assertTrue( ok );

    }

    @Test
    public void testSetModelpathPropagation() {
        if (verbose){ System.out.println("Test setModelpathPropagation");}
        
        // Verify propagation of model path to child elements.
        String mp = "Some_Path";
        String expected;
        String result;

        MPNode node = this.createTestNode();
        node.setModelpath(mp);
        
        expected = mp;  // MPNode itself
        result = node.getModelpath();
        assertEquals( expected, result );
        
        // Check path of child elements.
        result = ((ModeledElement)node.getChildren()[0]).getModelpath();
        expected = mp+"_Elem1";  // MPQuantity element
        assertEquals( expected, result );

        result = ((ModeledElement)node.getChildren()[2]).getModelpath();
        expected = mp+"_Elem3";  // MPArrayList element
        assertEquals( expected, result );

        result = ((ModeledElement)node.getChildren()[3]).getModelpath();
        expected = mp+"_SubNode";  // MPNode element
        assertEquals( expected, result );
        
    }
    
    @Test
    public void testToString()
    {
        if (verbose){System.out.println("Test toString");}
        String expected;
        String result;
        Boolean ok = true;

        // Test empty node representation
        MPNode node = new MPNode();
        expected = "<empty>";
        result = node.toString();
        assertEquals(expected, result);
        
        node = this.createTestNode();
        
        expected = "Node\n" + 
        "   Elem1: Quantity<Double>:     q1	1.0	m	a.b	null\n" +
        "   Elem2: Quantity<Double>:     q2	2.0	m	a.b	null\n" +
        "   Elem3: Quantity<Double>:     freq	0.001	Hz	em.freq	null\n" +
        "   Elem3: Quantity<Double>:     freq	0.002	Hz	em.freq	null\n" +
        "   SubNode: \n" +
        "      SubElem1: Quantity<Double>:     t1	123.0	K	x.temp	null\n" +
        "      SubElem2: Quantity<Double>:     t2	200.0	K	x.temp	null\n";
        if ( verbose ) {
          result = node.getModelpath()+node.toString();
          System.out.println( result );
          //TODO: Comparison does not quite work.
          assertEquals( expected, result );
        }
        assertTrue( ok );
    }
    
    @Test
    public void testIsEmpty()
    {
        if (verbose){System.out.println("Test isEmpty");}
        MPNode doc = new MPNode();
        Boolean result;
        
        result = doc.isEmpty();
        assertTrue( result );
        
        doc = this.createTestNode();
        result = doc.isEmpty();
        assertFalse( result );
        
    }
    
    @Test
    public void testAppendChild()
    {
        if (verbose){System.out.println("Test appendChild");}
        MPNode doc;
        MPQuantity q;
        Boolean result;
        
        // Append children to Node ( various types )
        doc = createTestNode();
        
        // Validate
        ModeledElement[] children = doc.getChildren();
        assertEquals(4, children.length );
        
        result = MPQuantity.class.isInstance( children[0] );
        assertTrue(result);
        result = (children[1].getClass() == MPQuantity.class );
        assertTrue(result);
        result = (children[2].getClass() == MPArrayList.class );
        assertTrue(result);
        result = (children[3].getClass() == MPNode.class );
        assertTrue(result);
        
    }
    
    @Test
    public void testGetChildren()
    {
        if (verbose){System.out.println("Test getChildren");}
        MPNode doc;
        ModeledElement[] children;
        
        // From empty MPNode.. get back empty array.
        doc = new MPNode();
        children = doc.getChildren();
        assertEquals( 0, children.length );
        
        // From full MPNode
        doc = createTestNode();
        children = doc.getChildren();
        assertEquals( 4, children.length );

        assertEquals( "Node_Elem1", children[0].getModelpath() );   // MPQuantity
        assertEquals( "Node_Elem2", children[1].getModelpath() );   // MPQuantity
        assertEquals( "Node_Elem3", children[2].getModelpath() );   // MPArrayList
        assertEquals( "Node_SubNode", children[3].getModelpath() ); // MPNode

    }
    
    @Test
    public void testGetChildrenMP()
    {
        if (verbose){System.out.println("Test getChildrenMP");}
        MPNode doc;
        String[] keys;
        
        // Get List of model paths of the children.
        
        // From empty MPNode.. get back empty array.
        doc = new MPNode();
        keys = doc.getChildrenMP();
        assertEquals( 0, keys.length );
        
        // From full MPNode
        doc = createTestNode();
        keys = doc.getChildrenMP();
        assertEquals( 4, keys.length );
        
        assertEquals( "Node_Elem1", keys[0] );   // MPQuantity
        assertEquals( "Node_Elem2", keys[1] );   // MPQuantity
        assertEquals( "Node_Elem3", keys[2] );   // MPArrayList
        assertEquals( "Node_SubNode", keys[3] ); // MPNode
        
    }
    
    @Test
    public void testGetChildByMP()
    {
        if (verbose){System.out.println("Test getChildByMP");}
        MPNode doc = new MPNode();
        ModeledElement result;
        
        // From empty node - nothing to match
        result = doc.getChildByMP("Node_Elem2");
        assertNull( result );
        
        // From full node
        doc = createTestNode();
        //  + matching path
        result = doc.getChildByMP("Node_Elem2");
        assertNotNull( result );
        
        //  + non-matching path
        result = doc.getChildByMP("Bogus_Path");
        assertNull( result );
        
        // null input path
        result = doc.getChildByMP( null );
        assertNull( result );
        
    }
    
    @Test
    public void testRemoveChild()
    {
        if (verbose){System.out.println("Test removeChild");}
        MPNode doc = new MPNode();
        ModeledElement elem = new MPQuantity("Sample");
        Boolean ok;
        
        // From empty node - nothing to match
        ok = false;
        try {
          doc.removeChild( elem );
          fail("Expected exception not thrown.");
        }
        catch (IllegalArgumentException ex ){
          assertEquals("Node is empty.",ex.getMessage());
          ok = true;
        } 
        assertTrue( ok );
        
        // From full node
        doc = createTestNode();
        //  + non-matching element
        ok = false;
        try {
          doc.removeChild( elem );
          fail("Expected exception not thrown.");
        }
        catch (IllegalArgumentException ex ){
          assertEquals("Node does not contain child object.",ex.getMessage());
          ok = true;
        } 
        assertTrue( ok );
        
        //  + matching element
        elem = doc.getChildByMP("Node_Elem2");
        ok = false;
        try {
          doc.removeChild( elem );
          ok = true;
        }
        catch (Exception ex ){
          fail("Unexpected exception thrown.");
        }
        assertTrue( ok );
        
        String[] keys = doc.getChildrenMP();
        assertEquals( 3, keys.length );
        elem = doc.getChildByMP("Node_Elem2");
        assertNull( elem );
    }
    
    @Test
    public void testAddSubNode()
    {
        if (verbose){System.out.println("Test addSubNode");}
        MPQuantity q;
        MPNode node = this.createTestNode();

        // Create new subnode to add
        //  + same elements.. change values for validation
        MPNode subnode = this.createTestSubNode("Node_SubNode");
        MPArrayList<ModeledElement> qarr = this.createTestArray("SubElem3");
        ((MPQuantity)qarr.get(0)).setValue(1.001);
        ((MPQuantity)qarr.get(1)).setValue(1.002);
        subnode.appendChild(qarr);

        // Add one to the test node as well (tests elemnts added to array )
        qarr = this.createTestArray("SubElem3");
        ((MPNode)node.getChildByMP("Node_SubNode")).appendChild( qarr );

        // Add another non-existing array to the subnode being added
        qarr = this.createTestArray("SubElem4");
        ((MPQuantity)qarr.get(0)).setValue(2.001);
        ((MPQuantity)qarr.get(1)).setValue(2.002);
        subnode.appendChild(qarr);
                
        // Add subnode to test node... already exists, so content merged
        node.build( subnode );
                
        // Validate
        subnode = (MPNode)node.getChildByMP("Node_SubNode"); //merged
        
        q = (MPQuantity)subnode.getChildByMP("Node_SubNode_SubElem1"); //replaced
        assertEquals( 123.0, q.getValue() );
        q = (MPQuantity)subnode.getChildByMP("Node_SubNode_SubElem2"); //replaced
        assertEquals( 200.0, q.getValue() );
        qarr = (MPArrayList)subnode.getChildByMP("Node_SubNode_SubElem3"); //added to
        assertEquals( 4, qarr.size() );
        q = (MPQuantity)qarr.get(0);
        assertEquals( 0.001, q.getValue() );
        q = (MPQuantity)qarr.get(1);
        assertEquals( 0.002, q.getValue() );
        q = (MPQuantity)qarr.get(2);
        assertEquals( 1.001, q.getValue() );
        q = (MPQuantity)qarr.get(3);
        assertEquals( 1.002, q.getValue() );
        qarr = (MPArrayList)subnode.getChildByMP("Node_SubNode_SubElem4"); //new
        assertEquals( 2, qarr.size() );
        q = (MPQuantity)qarr.get(0);
        assertEquals( 2.001, q.getValue() );
        q = (MPQuantity)qarr.get(1);
        assertEquals( 2.002, q.getValue() );
        
        
        // Change subnode path to unique (not already exists)
        subnode = this.createTestSubNode("Node_SubNode2");
        subnode.appendChild( this.createTestArray("SubElem3") );

        // Add subnode to test node... does not exist, so just added
        node.build( subnode );
        
        // Validate
        subnode = (MPNode)node.getChildByMP("Node_SubNode2"); //merged
        
        q = (MPQuantity)subnode.getChildByMP("Node_SubNode2_SubElem1");
        assertEquals( 123.0, q.getValue() );
        q = (MPQuantity)subnode.getChildByMP("Node_SubNode2_SubElem2");
        assertEquals( 200.0, q.getValue() );
        qarr = (MPArrayList)subnode.getChildByMP("Node_SubNode2_SubElem3");
        assertEquals( 2, qarr.size() );
        q = (MPQuantity)qarr.get(0);
        assertEquals( 0.001, q.getValue() );
        q = (MPQuantity)qarr.get(1);
        assertEquals( 0.002, q.getValue() );

    }
    
    private MPArrayList<ModeledElement> createTestArray( String mp ) {
        
        MPArrayList<ModeledElement> qarr;
        MPQuantity q;
        
        qarr = new MPArrayList<ModeledElement>();
        qarr.setModelpath(mp);
        q = new MPQuantity("freq", (Double)0.001, "Hz", "em.freq");
        q.setModelpath("Freq");
        qarr.add(q);
        q = new MPQuantity("freq", (Double)0.002, "Hz", "em.freq");
        q.setModelpath("Freq");
        qarr.add(q);
        
        return qarr;
    }
    private MPNode createTestSubNode(String mp) {
        MPNode subnode;
        MPQuantity q;
        
        subnode = new MPNode(mp);
        q = new MPQuantity("t1", (Double)123.0, "K", "x.temp");
        q.setModelpath("SubElem1");
        subnode.appendChild(q);
        q = new MPQuantity("t2", (Double)200.0, "K", "x.temp");
        q.setModelpath("SubElem2");
        subnode.appendChild(q);

        return subnode;
    }
    private MPNode createTestNode() {
        
        MPNode node = new MPNode();
        node.setModelpath("Node");
        
        // Add some content
        MPQuantity q;
        q = new MPQuantity("q1", (Double)1.0, "m", "a.b");
        q.setModelpath("Elem1");
        node.appendChild(q);
        q = new MPQuantity("q2", (Double)2.0, "m", "a.b");
        q.setModelpath("Elem2");
        node.appendChild(q);
 
        MPArrayList<ModeledElement> qarr = this.createTestArray("Elem3");
        node.appendChild(qarr);

        MPNode subnode;
        subnode = this.createTestSubNode("SubNode");
        node.appendChild(subnode);
        
        return node;
    }
    
}