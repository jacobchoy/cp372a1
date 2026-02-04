package client;

// listener for server messages so the GUI can update on the EDT
public interface ServerMessageListener {

    // called when the server sends an ERROR response
    void onError(String message);

    // called when the server sends an OK response with optional remainder
    void onOkResponse(String remainder);
}
