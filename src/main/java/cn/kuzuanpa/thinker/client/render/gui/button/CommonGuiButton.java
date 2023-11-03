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

import static cn.kuzuanpa.thinker.Thinker.MOD_ID;

public class CommonGuiButton extends GuiButton {
    ResourceLocation textures=new ResourceLocation(MOD_ID,"textures/gui/think/base.png");

    public CommonGuiButton(int id, int xPos, int yPos, int width, int height, String displayText) {
        super(id, xPos, yPos,width,height,displayText);
    }
    public ArrayList<IAnime> animeList=new ArrayList<>();
    public void addAnime(IAnime anime){
        animeList.add(anime);
    }
    public void doAnime(long initTime,long currentTime){};
}
