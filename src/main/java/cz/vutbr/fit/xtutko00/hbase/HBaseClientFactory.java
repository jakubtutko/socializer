package cz.vutbr.fit.xtutko00.hbase;

import cz.vutbr.fit.xtutko00.hbase.halyard.HalyardHBaseClient;
import cz.vutbr.fit.xtutko00.hbase.hgraphdb.HGraphDbHBaseClient;

import java.io.Serializable;

/**
 * Creates HBase clients.
 *
 * @author xtutko00
 */
public class HBaseClientFactory implements Serializable {

    private EClient client;
    private HBaseClientConfig config;

    public HBaseClientFactory(EClient client, HBaseClientConfig config) {
        this.client = client;
        this.config = config;
    }

    public HBaseClientFactory(HBaseClientConfig config) {
        this.config = config;
    }

    public HBaseClient createClient() {
        switch (client) {
            case HALYARD:
                return new HalyardHBaseClient(config);
            case HGRAPHDB:
                return new HGraphDbHBaseClient(config);
            default:
                return null;
        }
    }

    public HBaseClient createClient(EClient client) {
        this.client = client;
        return createClient();
    }

    public enum EClient {
        HALYARD,
        HGRAPHDB,
    }
}
