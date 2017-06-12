package de.fhdortmund.seelab.tadoac.Exception;

import java.io.IOException;

/**
 * @author Jonas Fleck
 */
public class TadoApiException extends IOException{
    public TadoApiException(String message) {
        super(message);
    }

}
