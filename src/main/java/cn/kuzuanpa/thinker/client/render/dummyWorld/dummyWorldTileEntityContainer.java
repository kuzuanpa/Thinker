package cn.kuzuanpa.thinker.client.render.dummyWorld;

import cn.kuzuanpa.thinker.api.IAnimatableThinkerObject;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldAnimes;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;
import cn.kuzuanpa.thinker.client.render.gui.button.ThinkerButton;
import cn.kuzuanpa.thinker.util.Nbt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getInt;

public class dummyWorldTileEntityContainer implements IAnimatableThinkerObject {
    public final ArrayList<IDummyWorldAnimes> WorldAnimeList = new ArrayList<>();
    public TileEntity tile;
    public dummyWorldTileEntityContainer(TileEntity tile, ArrayList<IDummyWorldAnimes> animes){
        this.tile=tile;
        WorldAnimeList.addAll(animes);
    }
    public dummyWorldTileEntityContainer(TileEntity tile, IDummyWorldAnimes... animes){
        this.tile=tile;
        Collections.addAll(WorldAnimeList,animes);
    }
    public dummyWorldTileEntityContainer(TileEntity tile){
        this(tile,new IDummyWorldAnimes[0]);
    }

    public static dummyWorldTileEntityContainer create(Map<String,Object> values){
        NBTTagCompound tileEntityNBT= (NBTTagCompound) Nbt.stringToNBT((String) values.get("tileEntityNBT"));
        tileEntityNBT.setInteger("x",getInt(values.get("posX")));
        tileEntityNBT.setInteger("y",getInt(values.get("posY")));
        tileEntityNBT.setInteger("z",getInt(values.get("posZ")));
        TileEntity tile = TileEntity.createAndLoadEntity(tileEntityNBT);
        return new dummyWorldTileEntityContainer(tile);
    }
    public static boolean doesMapHaveValidContents(Map<String,Object> values) {
        return (values.containsKey("tileEntityNBT"));
    }

    public ArrayList<IGuiAnime> getGuiAnimeList() {return null;}

    public ArrayList<IDummyWorldAnimes> getWorldAnimeList() {return WorldAnimeList;};
}
