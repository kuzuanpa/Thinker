package cn.kuzuanpa.thinker.client.render.dummyWorld.anime;

import cn.kuzuanpa.thinker.Thinker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class SetTileNBT implements IDummyWorldTilePropertiesAnime {
    public NBTTagCompound tag=new NBTTagCompound();
    public SetTileNBT(NBTTagCompound tag){
        this.tag=tag;
    }
    public void doAnime(TileEntity tileEntity){
        tileEntity.readFromNBT( tag);
    }

    @Override
    public String jsonName() {
        return "Prop.SetTileNBT";
    }
}
