package blockrenderer6343.world;

import javax.annotation.Nonnull;

import blockrenderer6343.api.utils.world.DummyChunkProvider;
import blockrenderer6343.api.utils.world.DummySaveHandler;
import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.client.handler.dummyWorldHandler;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.IdummyWorldThinkerObject;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.anime.tick.IDummyWorldTickingAnime;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.dummyWorldTile;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.ForgeModContainer;

import java.util.ArrayList;

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

        for (IdummyWorldThinkerObject o : dummyWorldHandler.dummyWorldObjects) {

            if(o.getWorldAnimeList().stream().filter(anime -> anime instanceof IDummyWorldTickingAnime).anyMatch(anime -> ((IDummyWorldTickingAnime) anime).beforeTick(timer, o, this)))return;

            if(o instanceof dummyWorldTile) {
                TileEntity tileEntity = ((dummyWorldTile) o).tile;
                if (!tileEntity.isInvalid() && tileEntity.hasWorldObj() && this.blockExists(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord)) {
                    try {
                        tileEntity.updateEntity();
                        o.getWorldAnimeList().stream().filter(anime -> anime instanceof IDummyWorldTickingAnime).forEach(anime -> ((IDummyWorldTickingAnime) anime).afterTick(timer, o, this));
                    } catch (Throwable throwable) {
                        tileEntity.invalidate();
                        setBlockToAir(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
                    }
                }
                if (tileEntity.isInvalid()) invalidTileEntities.add(tileEntity);
            }
        }
        invalidTileEntities.forEach(tileentity -> {
            if (this.chunkExists(tileentity.xCoord >> 4, tileentity.zCoord >> 4))
            {
                Chunk chunk = this.getChunkFromChunkCoords(tileentity.xCoord >> 4, tileentity.zCoord >> 4);
                if (chunk != null) chunk.removeInvalidTileEntity(tileentity.xCoord & 15, tileentity.yCoord, tileentity.zCoord & 15);
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

