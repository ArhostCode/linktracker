package edu.java.provider.api;

import edu.java.provider.InformationProviders;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;

public interface InformationProvider {

    boolean isSupported(URL url);

    String getType();

    LinkInformation fetchInformation(URL url);

    @Autowired
    default void registerSelf(InformationProviders providers) {
        providers.registerProvider(getType(), this);
    }
}
