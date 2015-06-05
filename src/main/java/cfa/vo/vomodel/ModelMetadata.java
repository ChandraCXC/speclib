package cfa.vo.vomodel;

import java.net.URL;

/**
 * Created by olaurino on 6/4/15.
 */
public interface ModelMetadata {
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
    String getModelName();

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
}
