package lsModule;

public class DeletionThread implements Runnable {
    private String topic;

    public DeletionThread(String topic){
        this.topic=topic;
    }

    @Override
    public void run() {
        LoadSheddingManager.jmxTopics.remove(topic);
    }
}
