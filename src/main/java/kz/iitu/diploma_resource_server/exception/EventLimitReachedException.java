package kz.iitu.diploma_resource_server.exception;

public class EventLimitReachedException extends RuntimeException {

    public EventLimitReachedException(String message) {
        super(message);
    }

}
