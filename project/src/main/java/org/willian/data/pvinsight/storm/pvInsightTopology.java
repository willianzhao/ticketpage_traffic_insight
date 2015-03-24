package org.willian.data.pvinsight.storm;

import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.willian.data.pvinsight.storm.spout.RequestHandleSpout;
import org.willian.data.pvinsight.storm.spout.RequestScheme;
import storm.kafka.BrokerHosts;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

import java.io.IOException;

/**
 * Created by weilzhao on 3/15/15.
 */
public class PVInsightTopology extends BaseAnalysisTopology {

    private static final Logger logger = LogManager.getLogger(PVInsightTopology.class);

    public PVInsightTopology(String configurationFileLocation) throws IOException {
        super(configurationFileLocation);
    }


    private SpoutConfig configureKafkaSpout() {
        BrokerHosts hosts = new ZkHosts(topologyProperties.getProperty("kafka.zookeeper.host"));
        String topic = topologyProperties.getProperty("kafka.input.topic");
        String zkRoot = topologyProperties.getProperty("kafka.input.zkRoot");
        String consumerGroupId = "RequestInboundSpout";
        SpoutConfig spoutConfig = new SpoutConfig(hosts, topic, zkRoot, consumerGroupId);

        spoutConfig.scheme = new SchemeAsMultiScheme(new RequestScheme());

        logger.debug("Initialize the kafka spout configure");
        return spoutConfig;
    }

    public void setupRequestHandleSpout(TopologyBuilder builder) {

        RequestHandleSpout rhSpout = new RequestHandleSpout(configureKafkaSpout());
        String rhSpoutID = topologyProperties.getProperty("storm.RQHANDLE_SPOUT_ID");
        int tasks = Integer.parseInt(topologyProperties.getProperty("storm.RQHANDLE_SPOUT_TASKS"));
        builder.setSpout(rhSpoutID, rhSpout, tasks);
        logger.info("Set the incoming request handle spout on Kafka");
    }

    public static void main(String[] args) {

    }
}
