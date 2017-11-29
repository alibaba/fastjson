package oracle.sql;

import java.sql.Timestamp;
import java.util.Date;

public class TIMESTAMP {

    private Date date;

    public TIMESTAMP(Timestamp date){
        this.date = date;
    }
    
    public Date toJdbc() {
        return date;
    }
}
