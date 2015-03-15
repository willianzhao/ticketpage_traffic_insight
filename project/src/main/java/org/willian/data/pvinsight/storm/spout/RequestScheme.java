package org.willian.data.pvinsight.storm.spout;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.willian.data.pvinsight.util.ProjectUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by weilzhao on 3/15/15.
 */
public class RequestScheme implements Scheme {

    public static final String FIELD_REQUEST_ID = "requestId";
    public static final String FIELD_USER_ID = "userID";
    public static final String FIELD_TICKET_ID = "ticketID";
    /*
    The date format is yyyy-mm-dd
     */
    public static final String FIELD_START_DATE = "startDate";
    public static final String FIELD_END_DATE = "endDate";

    private static final Logger logger = LogManager.getLogger();

    private static final long serialVersionUID = -2990121166902741545L;

    @Override
    public List<Object> deserialize(byte[] bytes) {
        String message = Bytes.toStringBinary(bytes);
        String[] pieces = message.split("\\|");
        String requestID = ProjectUtils.cleanupStr(pieces[0]);
        String userID = ProjectUtils.cleanupStr(pieces[1]);
        String ticketID = ProjectUtils.cleanupStr(pieces[2]);
        Date startDate;
        Date endDate;

        try {
            startDate = ProjectUtils.convertToDate(pieces[3]);
            endDate = ProjectUtils.convertToDate(pieces[4]);
        } catch (ParseException e) {
            logger.error("Terminate the message deserialize", e);
            throw new RuntimeException(e);
        }
        return new Values(requestID, userID, ticketID, startDate, endDate);

    }

    @Override
    public Fields getOutputFields() {
        return new Fields(FIELD_REQUEST_ID, FIELD_USER_ID, FIELD_TICKET_ID, FIELD_START_DATE, FIELD_END_DATE);
    }

}
