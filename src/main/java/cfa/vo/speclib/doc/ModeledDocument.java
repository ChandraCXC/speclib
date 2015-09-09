/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cfa.vo.speclib.doc;

import java.util.List;

/**
 *
 * @author mdittmar
 */
public interface ModeledDocument {

    // ModeledDocument Interface Methods
    /**
     * Add Element to document node.
     *
     * @param elem
     *   Element to be added to node
     */
    void appendChild(ModeledElement elem);

    /**
     * Get Element from document node whose model path attribute matches the
     * specified value.
     * If no match is found, NULL is returned.
     *
     * @param mp
     *   Model path identifier of the requested object.
     *
     * @return
     *   Requested object, or NULL if no match found.
     */
    ModeledElement getChildByMP(String mp);

    /**
     * Returns array of all Elements in the document node.
     * 
     * @return 
     *   ModeledElement[] of node elements; if node is empty, returns zero length array.
     */
    ModeledElement[] getChildren();

    /**
     * Returns array of the model path of each Element in the document node.
     * 
     * @return 
     *   String[] of node element model paths; if node is empty, returns zero length array.
     */
    String[] getChildrenMP();

    /**
     * Returns true if document node has no child elements.
     *
     * @return
     *   true if node is empty; else false.
     */
    boolean isEmpty();

    /**
     * Remove specified object from the document node.
     * 
     * @param elem
     *   Child element to remove from node.
     * 
     * @exception
     *   IllegalArgumentException if the node is empty.
     *   IllegalArgumentException if the node does not contain the element
     * 
     */
    void removeChild(ModeledElement elem);
    
    /**
     * Incorporate the provided stack of ModeledElements into the document.
     *  
     *  Input List of elements is organized according to their model path
     *  settings, and organized into common subgroups.  Each resulting element 
     *  is folded into the document hierarchy using the addElement method.
     * 
     *  This method is useful for constructing an object hierarchy from a 
     *  flattened set of base elements and/or folding new elements into an
     *  existing hierarchy.
     * 
     * @param stack
     *     List<ModeledELement> - Set of elements to incorporate.
     */
    void build( List<ModeledElement> stack );
    
     /**
     *  Incorporates the provided element into the hierarchy of this document.
     *  If the element does not already exist in the node, it is simply added.
     *  If it does exist:
     *   - and is an array, the element contents are added to the existing list
     *   - and implements ModeledDocument (ie: is a complex heirarchy of ModeledElements)
     *     the content is merged with the existing content.
     *      + replacing individual elements
     *      + adding to list elements.
     *   - otherwise, the element replaces the existing one.
     * 
     * @param elem
     *     {@link ModeledElement } - element to be incorporated.
     */
    void build( ModeledElement elem );
    
}
