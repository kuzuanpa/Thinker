package cn.kuzuanpa.thinker.util;
import net.minecraft.nbt.*;
public class Nbt {
    public static NBTBase stringToNBT(String str){
        try
        {
            return JsonToNBT.func_150315_a(str);
        }
        catch (NBTException e)
        {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Could not parse String to NBT");
    }

}
