package org.willian.data.pvinsight.util;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import org.apache.hadoop.security.authorize.AuthorizationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by weilzhao on 3/15/15.
 */
public final class StormRunner {
    private final static Logger logger = LogManager.getLogger(StormRunner.class);
    private static final int MILLIS_IN_SEC = 1000;

    private StormRunner() {
    }

    public static void runTopologyLocally(StormTopology topology, String topologyName, Config conf, int runtimeInSeconds)
            throws InterruptedException {
        logger.entry();
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(topologyName, conf, topology);
        logger.debug("Submit the topology to local cluster");
        Thread.sleep((long) runtimeInSeconds * MILLIS_IN_SEC);
        cluster.killTopology(topologyName);
        logger.debug("Terminate the topology {} after {} seconds", topologyName, runtimeInSeconds);
        cluster.shutdown();
        logger.exit();
    }

    public static void runTopologyRemotely(StormTopology topology, String topologyName, Config conf)
            throws AlreadyAliveException, InvalidTopologyException, AuthorizationException {
        logger.entry();
        StormSubmitter.submitTopology(topologyName, conf, topology);
        logger.info("Submit the topology {} to remote server", topologyName);
    }
}
