package edu.java.provider.api;

import java.net.URL;

public interface InformationProvider {

    boolean isSupported(URL url);

    LinkInformation fetchInformation(URL url);
}
