package cfa.vo.vomodel;

import java.net.URL;
import java.util.List;

/**
 * Created by olaurino on 6/4/15.
 */
public interface ModelData {
    /**
     * Returns the title string associated with this model
     *
     * @return Data Model title.
     */
    String getTitle();

    /**
     * Returns the defined name string for the model, typically in format
     * "[name]-[version].[subversion]"
     *
     * @return Data Model name.
     */
    String getName();

    /**
     * Returns the defined prefix string for the model.  The prefix string is
     * used as part of the Utype to indicate which model the element is from.
     *
     * @return Model prefix.
     */
    String getPrefix();

    /**
     * Returns the {@link java.net.URL} pointer to the location containing reference
     * information about the model.  This may be the location of the XML Schema
     * or other resource.
     *
     * @return URL pointer to reference information.
     */
    URL getReferenceURL();

    /**
     * Return the list of entries for the Model definition.
     *
     * @return a {@link java.util.List} of {@link cfa.vo.vomodel.Entry}
     */
    List<Entry> getEntries();
}
