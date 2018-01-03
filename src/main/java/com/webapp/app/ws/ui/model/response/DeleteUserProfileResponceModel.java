package com.webapp.app.ws.ui.model.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeleteUserProfileResponceModel {

    private RequestOperation requestOperation;
    private ResponseStatus responceStatus;

    public RequestOperation getRequestOperation() {
        return requestOperation;
    }

    public void setRequestOperation(RequestOperation requestOperation) {
        this.requestOperation = requestOperation;
    }

    public ResponseStatus getResponceStatus() {
        return responceStatus;
    }

    public void setResponceStatus(ResponseStatus responceStatus) {
        this.responceStatus = responceStatus;
    }
}
