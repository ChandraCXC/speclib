package cfa.vo.vomodel;

import java.net.URL;

/**
 * AbstractURLModelBuilder: 
 *    Class of ModelBuilder whose content is obtained by resolving a URL.
 * 
 * Created by olaurino on 6/4/15.
 */
public abstract class AbstractURLModelBuilder extends AbstractModelBuilder {
    protected URL url;   // URL of Model content storage

    public AbstractURLModelBuilder(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

}
