package client;

/**
 * Listener for server messages so the GUI can update on the EDT.
 * ClientConnection invokes these methods on the Swing EDT when a response is received.
 */
public interface ServerMessageListener {

    /**
     * Called when the server sends an ERROR response.
     *
     * @param message The full error message (e.g. "ERROR_CODE description")
     */
    void onError(String message);

    /**
     * Called when the server sends an OK response with optional remainder.
     *
     * @param remainder The part after "OK " (e.g. "x y colour msg;..." for GET, "x y;x y;..." for GET PINS)
     */
    void onOkResponse(String remainder);
}
