package org.willian.data.pvinsight.storm.spout;

import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;

/**
 * Created by weilzhao on 3/15/15.
 */
public class RequestHandleSpout extends KafkaSpout {

    public RequestHandleSpout(SpoutConfig spoutConf) {
        super(spoutConf);
    }
}
