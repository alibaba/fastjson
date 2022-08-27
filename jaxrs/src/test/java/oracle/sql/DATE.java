package oracle.sql;

import java.sql.Date;
import java.sql.Timestamp;

public class DATE {

    private Date date;

    public DATE(Timestamp date){
        long time = date.getTime();
        time = (time  / 1000) * 1000;
        this.date = new Date(time);
    }
    
    public Date toJdbc() {
        return date;
    }
}
