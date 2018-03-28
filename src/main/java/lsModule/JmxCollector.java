package lsModule;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class JmxCollector {
    String[] hosts;
    //= {"192.168.56.100", "192.168.56.101", "192.168.56.102"};
//    String props = "kafka.server:type=BrokerTopicMetrics,name=Bytes*,topic=";

    JmxCollector(String[] hosts){
        this.hosts=hosts;
    }

    //collect JMX values by topic
    public long collectJmx(String topic) throws Exception {
        long val = 0;
        long total = 0;
        String props = "kafka.server:type=BrokerTopicMetrics,name=Bytes*,topic=";
        for (String host : this.hosts) {
            String connectTo = "service:jmx:rmi:///jndi/rmi://" + host + ":9999/jmxrmi";
            String query = props + topic;
            val = getJmx(connectTo, query);
            total += val;
        }
        return total;
    }

    //method Overloading
    public long collectJmx(Map map) throws Exception {
        long val = 0;
        long total = 0;
        String props = "kafka.server:type=BrokerTopicMetrics,name=Bytes*,~~"; //connection offset
        Set<String> keySet = map.keySet();
        //한번에 받기
        //첫번째 런(전체) - 두번째 런 (오프셋)
        /*
        for (String host : this.hosts) {
            for(String key:keySet) {
                String connectTo = "service:jmx:rmi:///jndi/rmi://" + host + ":9999/jmxrmi";
                String query = props + key;
                val = run(connectTo, query);
                total += val;
            }
        }
        */
        return total;
    }

    private long getJmx(String connectTo, String pattern) throws Exception {
        String url = connectTo;
        JMXConnector connector;
        try {
            connector = JMXConnectorFactory.connect(new JMXServiceURL(url));
        } catch (NullPointerException e) {
            return error("Sorry, can't connect to: " + url);
        }

        MBeanServerConnection connection = connector.getMBeanServerConnection();
        String objectPattern = pattern != null ? pattern : "*:*";
        Set<ObjectName> objectNames = new TreeSet<ObjectName>(connection.queryNames(new ObjectName(objectPattern), null)); // metrics저장

        long val = 0;
        String tmp = "";
        int count = 0;

        for (ObjectName objectName : objectNames) {
            MBeanInfo mbeanInfo = connection.getMBeanInfo(objectName);// MBeans 값 get
            MBeanAttributeInfo[] attribute = mbeanInfo.getAttributes(); //불러온 MBeans의 attribute값 출력
            Object object = connection.getAttribute(objectName, attribute[0].getName());
            tmp = object.toString();
            if (count < 1) {
                count++;
                val = Long.parseLong(tmp);
            } else {
                val -= Long.parseLong(tmp);
                break;
            }
        }
        return val;
    }

    private int error(Object out) {
        System.out.println(out);
        return -1;
    }
}
