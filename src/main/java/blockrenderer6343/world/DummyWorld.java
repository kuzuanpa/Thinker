package blockrenderer6343.world;

import javax.annotation.Nonnull;

import blockrenderer6343.api.utils.world.DummyChunkProvider;
import blockrenderer6343.api.utils.world.DummySaveHandler;
import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.client.handler.dummyWorldHandler;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.tick.IDummyWorldTileTickingAnime;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.dummyWorldTile;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.ForgeModContainer;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class DummyWorld extends World {

    private static final WorldSettings DEFAULT_SETTINGS = new WorldSettings(
            1L,
            WorldSettings.GameType.SURVIVAL,
            true,
            false,
            WorldType.DEFAULT);

    public static final DummyWorld INSTANCE = new DummyWorld();
    public boolean lock=false;
    public long timer =0;
    public DummyWorld() {
        super(new DummySaveHandler(), "DummyWorld", DEFAULT_SETTINGS, new WorldProviderSurface(), new Profiler());
        // Guarantee the dimension ID was not reset by the provider
        this.provider.setDimension(Integer.MAX_VALUE);
        int providerDim = this.provider.dimensionId;
        this.provider.worldObj = this;
        this.provider.setDimension(providerDim);
        this.chunkProvider = this.createChunkProvider();
        this.calculateInitialSkylight();
        this.calculateInitialWeatherBody();
        Thinker.dummyWorldTickThread.setTrackedDummyWorld(this);
    }
    @Override
    public void updateEntities() {
        if(!lock)System.out.println("World missing lock when ticking!");
        ArrayList<TileEntity> invalidTileEntities = new ArrayList<>();

        for (Object o : this.loadedTileEntityList) {
            TileEntity tileentity = (TileEntity) o;

            if (!tileentity.isInvalid() && tileentity.hasWorldObj() && this.blockExists(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord)) {
                try {
                    AtomicBoolean skipThisTick = new AtomicBoolean(false);
                    dummyWorldHandler.dummyWorldObjects.stream().filter(obj -> obj instanceof dummyWorldTile && ((dummyWorldTile) obj).tile.equals(tileentity)).findAny().ifPresent(idummyWorldThinkerObject -> skipThisTick.set(idummyWorldThinkerObject.getWorldAnimeList().stream().filter(anime -> anime instanceof IDummyWorldTileTickingAnime).anyMatch(anime -> ((IDummyWorldTileTickingAnime) anime).beforeTick(timer, tileentity))));
                    if (skipThisTick.get()) return;
                    tileentity.updateEntity();
                    dummyWorldHandler.dummyWorldObjects.stream().filter(obj -> obj instanceof dummyWorldTile && ((dummyWorldTile) obj).tile.equals(tileentity)).findAny().ifPresent(idummyWorldThinkerObject -> idummyWorldThinkerObject.getWorldAnimeList().stream().filter(anime -> anime instanceof IDummyWorldTileTickingAnime).forEach(anime -> ((IDummyWorldTileTickingAnime) anime).afterTick(timer, tileentity)));
                } catch (Throwable throwable) {
                    if (ForgeModContainer.removeErroringTileEntities) {
                        tileentity.invalidate();
                        setBlockToAir(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
                    }
                }
            }

            if (tileentity.isInvalid()) {
                invalidTileEntities.add(tileentity);
            }
        }
        invalidTileEntities.forEach(tileentity -> {
            if (this.chunkExists(tileentity.xCoord >> 4, tileentity.zCoord >> 4))
            {
                Chunk chunk = this.getChunkFromChunkCoords(tileentity.xCoord >> 4, tileentity.zCoord >> 4);

                if (chunk != null)
                {
                    chunk.removeInvalidTileEntity(tileentity.xCoord & 15, tileentity.yCoord, tileentity.zCoord & 15);
                }
            }
        });
        this.loadedTileEntityList.removeAll(invalidTileEntities);

    }
    public void updateEntitiesForNEI() {
        lock=true;
        super.updateEntities();
        lock=false;
    }

    @Override
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {}

    @Override
    protected int func_152379_p() {
        return 0;
    }

    @Override
    public Entity getEntityByID(int p_73045_1_) {
        return null;
    }

    @Nonnull
    @Override
    protected IChunkProvider createChunkProvider() {
        return new DummyChunkProvider(this);
    }

    @Override
    protected boolean chunkExists(int x, int z) {
        return chunkProvider.chunkExists(x, z);
    }

    @Override
    public boolean updateLightByType(EnumSkyBlock p_147463_1_, int p_147463_2_, int p_147463_3_, int p_147463_4_) {
        return true;
    }
}

