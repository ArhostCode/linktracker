package edu.java.provider.api;

import java.net.URL;

public abstract class InformationProvider {

    private InformationProvider next;

    protected abstract boolean isSupported(URL url);

    protected abstract LinkInformation fetchInformation(URL url);

    public void linkNext(InformationProvider next) {
        this.next = next;
    }

    public LinkInformation getLinkInformation(URL url) {
        if (isSupported(url)) {
            return fetchInformation(url);
        }
        return next == null ? null : next.getLinkInformation(url);
    }
}
