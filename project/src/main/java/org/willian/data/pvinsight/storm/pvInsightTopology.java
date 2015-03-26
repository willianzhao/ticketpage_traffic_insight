package org.willian.data.pvinsight.storm;

import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.willian.data.pvinsight.storm.bolt.BotsDetectBolt;
import org.willian.data.pvinsight.storm.bolt.GeographyBolt;
import org.willian.data.pvinsight.storm.bolt.PVCountBolt;
import org.willian.data.pvinsight.storm.bolt.ResultPackBolt;
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

    String rhSpoutID;
    String pvCuntBoltID;
    String botsDetectBoltID;
    String geographyBoltID;
    String resultPackBoltID;
    String kafkaNotifyBoltID;
    String hdfsArchiveBoltID;

    public PVInsightTopology(String configurationFileLocation) throws IOException {
        super(configurationFileLocation);
        rhSpoutID = topologyProperties.getProperty("storm.RQHANDLE_SPOUT_ID");
        pvCuntBoltID = topologyProperties.getProperty("storm.PVCOUNT_BOLT_ID");
        botsDetectBoltID = topologyProperties.getProperty("storm.BOTSDETECT_BOLT_ID");
        geographyBoltID = topologyProperties.getProperty("storm.GEOGRAPHY_BOLT_ID");
        resultPackBoltID = topologyProperties.getProperty("storm.RESULTPACK_BOLT_ID");
        kafkaNotifyBoltID = topologyProperties.getProperty("storm.KAFKANOTIFY_BOLT_ID");
        hdfsArchiveBoltID = topologyProperties.getProperty("storm.HDFSARCHIVE_BOLT_ID");
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

        int tasks = Integer.parseInt(topologyProperties.getProperty("storm.RQHANDLE_SPOUT_TASKS"));
        builder.setSpout(rhSpoutID, rhSpout, tasks);
        logger.info("Set the incoming request handle spout on Kafka");
    }

    public void setupPVCountBolt(TopologyBuilder builder) {

        PVCountBolt pvBolt = new PVCountBolt();
        int tasks = Integer.parseInt(topologyProperties.getProperty("storm.PVCOUNT_BOLT_TASKS"));
        builder.setBolt(pvCuntBoltID, pvBolt, tasks).shuffleGrouping(rhSpoutID);
        logger.info("Set the PVCount bolt by receiving messages from Kafka spout");
    }

    public void setupBotsDetectBolt(TopologyBuilder builder) {
        BotsDetectBolt detectBolt = new BotsDetectBolt();
        int tasks = Integer.parseInt(topologyProperties.getProperty("storm.BOTSDETECT_BOLT_TASKS"));
        builder.setBolt(botsDetectBoltID, detectBolt, tasks).shuffleGrouping(pvCuntBoltID);
        logger.info("Set the bots detect bolt by receiving messages from pv count bolt");
    }

    public void setupGeographyBolt(TopologyBuilder builder) {
        GeographyBolt geographyBolt = new GeographyBolt();
        int tasks = Integer.parseInt(topologyProperties.getProperty("storm.GEOGRAPHY_BOLT_TASKS"));
        builder.setBolt(geographyBoltID, geographyBolt, tasks).shuffleGrouping(botsDetectBoltID);
        logger.info("Set the geography bolt by receiving messages from bot detect bolt");
    }

    public void setupResultPackBolt(TopologyBuilder builder) {
        ResultPackBolt resultPackBolt = new ResultPackBolt();
        int tasks = Integer.parseInt(topologyProperties.getProperty("storm.RESULTPACK_BOLT_TASKS"));
        builder.setBolt(resultPackBoltID, resultPackBolt, tasks).shuffleGrouping(geographyBoltID);
        logger.info("Set the result pack bolt after the geography bolt");
    }

    public void setupKafkaNotifyBolt(TopologyBuilder builder) {
        //TODO
    }

    public void setupHDFSArchiveBolt(TopologyBuilder builder) {

        //TODO
    }

    public TopologyBuilder prepareTopologyBuilder() {
        TopologyBuilder builder = new TopologyBuilder();
        setupKafkaNotifyBolt(builder);
        setupPVCountBolt(builder);
        setupBotsDetectBolt(builder);
        setupGeographyBolt(builder);
        setupResultPackBolt(builder);
        setupKafkaNotifyBolt(builder);
        setupHDFSArchiveBolt(builder);
        return builder;
    }

    public static void main(String[] args) {

    }
}
