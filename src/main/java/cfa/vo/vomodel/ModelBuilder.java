package cfa.vo.vomodel;

import java.net.URL;

/**
 * Created by olaurino on 6/3/15.
 */
public interface ModelBuilder {
    String getTitle();

    ModelBuilder withTitle(String title);

    String getName();

    ModelBuilder withName(String name);

    String getPrefix();

    ModelBuilder withPrefix(String prefix);

    URL getRefURL();

    ModelBuilder withRefURL(URL refURL);

    ModelBuilder withModelMetadata(ModelMetadata modelMetadata);

    public Model build() throws Exception;

}
