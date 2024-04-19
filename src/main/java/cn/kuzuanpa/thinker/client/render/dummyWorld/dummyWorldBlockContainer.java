package cn.kuzuanpa.thinker.client.render.dummyWorld;

import cn.kuzuanpa.thinker.api.IAnimatableThinkerObject;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldAnimes;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;
import cn.kuzuanpa.thinker.client.render.gui.button.ThinkerButton;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static cn.kuzuanpa.thinker.Thinker.getBoolean;
import static cn.kuzuanpa.thinker.Thinker.getInt;
import static cn.kuzuanpa.thinker.client.json.jsonReader.getItemStack;

public class dummyWorldBlockContainer implements IAnimatableThinkerObject {
    public final ArrayList<IDummyWorldAnimes> WorldAnimeList = new ArrayList<>();
    public Block block;
    public ItemStack itemStack;
    public boolean renderAllFaces;
    public int meta;
    public static dummyWorldBlockContainer create(Map<String,Object> values){
        boolean renderAllFaces = false;
        if(values.containsKey("renderAllFaces"))renderAllFaces = getBoolean(values.get("renderAllFaces"));

        if(values.containsKey("fromItem"))return new dummyWorldBlockContainer(getItemStack((String)values.get("fromItem"))).setRenderAllFace(renderAllFaces);
        if(values.containsKey("block")&&values.containsKey("meta"))return new dummyWorldBlockContainer(Block.getBlockFromName((String) values.get("block")),getInt(values.get("meta"))).setRenderAllFace(renderAllFaces);
        return null;
    }
    public static boolean doesMapHaveValidContents(Map<String,Object> values) {
        return (values.containsKey("block") && values.containsKey("meta") )|| values.containsKey("fromItem");
    }
    public dummyWorldBlockContainer setRenderAllFace(boolean renderAllFace){this.renderAllFaces=renderAllFace;return this;}
    public dummyWorldBlockContainer(Block block, ArrayList<IDummyWorldAnimes> animes){
        this(block,0,animes);
    }

    public dummyWorldBlockContainer(Block block, IDummyWorldAnimes... animes){
        this(block,0,animes);
    }
    public dummyWorldBlockContainer(Block block) {
        this( block,0);
    }
    public dummyWorldBlockContainer(ItemStack itemStack, IDummyWorldAnimes... animes){
        this.itemStack =itemStack;
        Collections.addAll(WorldAnimeList,animes);
    }
    public dummyWorldBlockContainer(ItemStack itemStack, ArrayList<IDummyWorldAnimes> animes){
        this.itemStack =itemStack;
        WorldAnimeList.addAll(animes);
    }
    public dummyWorldBlockContainer(Block block, int meta, IDummyWorldAnimes... animes){
        this.block=block;
        this.meta=meta;
        Collections.addAll(WorldAnimeList,animes);
    }
    public dummyWorldBlockContainer(Block block, int meta, ArrayList<IDummyWorldAnimes> animes){
        this.block=block;
        this.meta=meta;
        WorldAnimeList.addAll(animes);
    }

    public ArrayList<IGuiAnime> getGuiAnimeList() {return null;}

    public ArrayList<IDummyWorldAnimes> getWorldAnimeList() {return WorldAnimeList;};
}
