package org.willian.data.pvinsight.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;

import java.util.Map;

/**
 * Created by weilzhao on 3/15/15.
 */
public class RequestHandleSpout extends BaseRichSpout {
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {

    }

    @Override
    public void close() {

    }

    @Override
    public void nextTuple() {

    }

    @Override
    public void ack(Object o) {

    }

    @Override
    public void fail(Object o) {

    }
}
