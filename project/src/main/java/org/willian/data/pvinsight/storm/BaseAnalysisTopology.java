package org.willian.data.pvinsight.storm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by weilzhao on 3/15/15.
 */
public class BaseAnalysisTopology {
    private static final Logger logger = LogManager.getLogger(BaseAnalysisTopology.class);

    protected Properties topologyProperties;

    public  BaseAnalysisTopology(String configurationFileLocation) throws IOException{

        topologyProperties= new Properties();
        try{
            topologyProperties.load(ClassLoader.getSystemResourceAsStream(configurationFileLocation));
        }catch (IOException e) {
            logger.error("{} can't be read.",configurationFileLocation,e);
            throw e;
        }
    }


}
