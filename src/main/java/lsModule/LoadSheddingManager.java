package lsModule;

import sun.nio.cs.ext.JIS_X_0201;
import sun.plugin2.message.Message;

import javax.management.JMX;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LoadSheddingManager {
    public Map<String, Long> jmxTopics = new HashMap<String, Long>();

    public int calculateThreshold(){
        int threshold=1000000;
        return threshold;
    }

    public static void main(String args[]) throws Exception {
        LoadSheddingManager lsm = new LoadSheddingManager();
        lsm.start();

    }

    public void start() throws Exception {
        //create MessageReceiver + run 생성 시 hashMap 함께 넘겨줘야함
        long threshold;
        long currentJmx;

        MessageReceiver messageReceiver = new MessageReceiver(jmxTopics, "localhost",5003);

        Set<String> keySet = jmxTopics.keySet();
        String[] hosts = {};

        JmxCollector collector = new JmxCollector(hosts);

        while(true) {
            currentJmx = collector.collectJmx(jmxTopics);
            threshold = calculateThreshold();
            //db switch도 확인
            if(currentJmx<threshold){
                //LoadShedding ON
                //DbAdapter.getInstance().setSwicthValue(jmxTopics,"true");
            }
            Thread.sleep(1000);

        }
    }
}
