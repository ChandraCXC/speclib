package cfa.vo.vomodel;

import java.net.URL;

/**
 * Created by olaurino on 6/4/15.
 */
public abstract class AbstractURLModelBuilder extends AbstractModelBuilder {
    protected URL url;

    public AbstractURLModelBuilder(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

}
