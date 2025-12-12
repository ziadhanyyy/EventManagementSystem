package Model;

public class Registration {
    private String registrationId;
    private String attendeeId;
    private String sessionId;

    public Registration(String registrationId, String attendeeId, String sessionId) {
        this.registrationId = registrationId;
        this.attendeeId = attendeeId;
        this.sessionId = sessionId;
    }

    public String getRegistrationId() {
        return this.registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getAttendeeId() {
        return this.attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return   registrationId  + " " + attendeeId + " " + sessionId ;
    }
}
