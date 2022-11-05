package handlers;

import com.sun.net.httpserver.HttpHandler;

import database_access.DatabaseException;
import requests.RegisterRequest;
import services.RegisterService;
import utils.Deserializer;

/** Responsible for handling requests to the Fill API */
public class RegisterHandler extends Handler implements HttpHandler {

    @Override
    boolean authTokenRequired() {
        return false;
    }

    @Override
    boolean requestMethodOK() {
        return requestMethodIsPost(exchange);
    }

    @Override
    Object getResultBodyObject(String requestBody) {
        Deserializer d = new Deserializer();
        RegisterRequest req = (RegisterRequest)d.deserialize(requestBody, RegisterRequest.class);
        try {
            return new RegisterService().register(req);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}