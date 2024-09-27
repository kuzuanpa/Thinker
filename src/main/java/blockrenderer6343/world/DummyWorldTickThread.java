package blockrenderer6343.world;

import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.client.handler.configHandler;
import cn.kuzuanpa.thinker.client.handler.dummyWorldHandler;

public class DummyWorldTickThread extends Thread{
    @Override
    public synchronized void start() {
        this.setDaemon(true);
        super.start();
    }
    public DummyWorldTickThread() {
        super("Dummy World Tick Thread");
    }
    private DummyWorld trackedDummyWorld;
    public final int intervalBetweenTicks=49;
    public int lastTickTime=0;
    @Override
    public void run() {
        while (true) {
            if(trackedDummyWorld!=null&&!trackedDummyWorld.lock&&(int)Math.abs((System.currentTimeMillis()%100000)-lastTickTime) > intervalBetweenTicks){
                lastTickTime = (int)(System.currentTimeMillis()%100000);

                try {
                    trackedDummyWorld.updateEntities();
                }catch (Exception e){Thinker.err("Exception occurred when ticking Dummy world, Previous user:"+ dummyWorldHandler.ObjectListUser);e.printStackTrace();}

                if(configHandler.recordDummyWorldTickTooLong.get()&&System.currentTimeMillis()%100000-lastTickTime>2*intervalBetweenTicks)System.out.println("A Tick of DummyWorld takes too long: "+(System.currentTimeMillis()%100000-lastTickTime)+"ms");
            }else {
                try {
                    sleep(0,100000);
                } catch (InterruptedException ignored) {}
            }
        }
    }
    public void setTrackedDummyWorld(DummyWorld world){
        trackedDummyWorld=world;
        this.interrupt();
    }
}
