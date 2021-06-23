
package Model;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    String consultant, custName, type;
    int aptId, custId;
    ZonedDateTime start, end;
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

    public Appointment(int aptId, int custId, String consultant, String custName, ZonedDateTime start, ZonedDateTime end, String type) {
        this.consultant = consultant;
        this.custName = custName;
        this.aptId = aptId;
        this.custId = custId;
        this.start = start;
        this.end = end.plusSeconds(1); // a second was taken away from end time when placed into DB for overlap appointment check. It is now being added back.
        this.type = type;
    }

    public String getConsultant() {
        return consultant;
    }

    public String getCustName() {
        return custName;
    }

    public int getAptId() {
        return aptId;
    }

    public int getCustId() {
        return custId;
    }
    
    public String getDate(){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M-d-yyyy");
        return start.format(dateFormatter); 
    }

    public String getStart() {
        return start.format(timeFormatter);
    }
    
    public String getEnd() {
        return end.format(timeFormatter);
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Appointment:" + "Consultant is" + consultant + ", Customer is" + custName + ", aptId=" + aptId + ", custId=" + custId + ", Start date and time is " + start + ", End date and time is " + end;
    }
}
