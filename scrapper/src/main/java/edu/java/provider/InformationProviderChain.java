package edu.java.provider;

import edu.java.provider.api.InformationProvider;
import edu.java.provider.api.LinkInformation;
import java.net.URL;

public class InformationProviderChain {

    private final InformationProvider first;

    public InformationProviderChain(InformationProvider first) {
        this.first = first;
    }

    public LinkInformation getLinkInformation(URL url) {
        return first.getLinkInformation(url);
    }

    public void linkNext(InformationProvider next) {
        first.linkNext(next);
    }

    public static InformationProviderChain link(InformationProvider... providers) {
        if (providers.length == 0) {
            return null;
        }
        InformationProviderChain chain = new InformationProviderChain(providers[0]);
        for (int i = 1; i < providers.length; i++) {
            chain.linkNext(providers[i]);
        }
        return chain;
    }

}
