/*
 * This class was created by <kuzuanpa>. It is distributed as
 * part of the kTFRUAddon Mod. Get the Source Code in github:
 * https://github.com/kuzuanpa/kTFRUAddon
 *
 * kTFRUAddon is Open Source and distributed under the
 * LGPLv3 License: https://www.gnu.org/licenses/lgpl-3.0.txt
 *
 */

package cn.kuzuanpa.thinker.client.render.gui.button;

import cn.kuzuanpa.thinker.client.render.gui.anime.IAnime;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;

public class CommonGuiButton extends GuiButton {
    ResourceLocation baseTexture=new ResourceLocation(MOD_ID,"textures/gui/think/base.png");
    public long initTime=0;
    public CommonGuiButton(int id, int xPos, int yPos, int width, int height, String displayText) {
        super(id, xPos, yPos,width,height,displayText);
    }
    public ArrayList<IAnime> animeList=new ArrayList<>();
    public CommonGuiButton addAnime(IAnime anime){
        animeList.add(anime);
        return this;
    }
    public void update(int xPos,int yPos,int width,int height){
        this.xPosition=xPos;
        this.yPosition=yPos;
        this.width=width;
        this.height=height;
    }
    public CommonGuiButton addToList(List<CommonGuiButton> list){
        list.add(this);
        return this;
    }
    public void doAnime(long initTime){this.initTime=initTime;}
}
