package blockrenderer6343.world;

import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.client.configHandler;
import net.minecraft.world.World;

import java.util.Random;

public class DummyWorldTickThread extends Thread{
    @Override
    public synchronized void start() {
        this.setDaemon(true);
        super.start();
    }
    public DummyWorldTickThread() {
        super("Dummy World Tick Thread");
    }
    public DummyWorld trackedDummyWorld;
    public final int intervalBetweenTicks=49;
    public int lastTickTime=0;
    @Override
    public void run() {
        while (true) {
            if(trackedDummyWorld!=null&&(int)Math.abs((System.currentTimeMillis()%100000)-lastTickTime) > intervalBetweenTicks){
                lastTickTime = (int)(System.currentTimeMillis()%100000);
                trackedDummyWorld.lock=true;
                trackedDummyWorld.updateEntities();
                trackedDummyWorld.lock=false;
                if(configHandler.recordDummyWorldTickTooLong.get()&&System.currentTimeMillis()%100000-lastTickTime>intervalBetweenTicks)System.out.println("A Tick of DummyWorld takes too long: "+(System.currentTimeMillis()%100000-lastTickTime)+"ms");
            }else {
                try {
                    sleep(0,100000);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
