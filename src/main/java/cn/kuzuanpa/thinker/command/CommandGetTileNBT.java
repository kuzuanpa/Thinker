/*
 * This class was created by <kuzuanpa>. It is a part of Thinker.
 * Get the Source Code in github:
 * https://github.com/kuzuanpa/Thinker
 *
 * Thinker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * Thinker is Open Source and distributed under the
 * LGPLv3 License: https://www.gnu.org/licenses/lgpl-3.0.txt
 *
 */
package cn.kuzuanpa.thinker.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.List;


public class CommandGetTileNBT extends CommandBase {
    @Override
    public String getCommandName() {
        return "getTileNBT";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "Usage: <x> <y> <z> [world]";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return true;
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
        if (p_71515_2_.length< 3)throw new WrongUsageException("Usage: <x> <y> <z> [world]");
        TileEntity tile;
        if(p_71515_2_.length> 3){
            if(DimensionManager.getWorld(parseInt(p_71515_1_,p_71515_2_[3]))==null)throw new WrongUsageException("World ID: "+p_71515_2_[3]+" is invalid!");
            tile = DimensionManager.getWorld(parseInt(p_71515_1_,p_71515_2_[3])).getTileEntity(parseInt(p_71515_1_,p_71515_2_[0]),parseInt(p_71515_1_,p_71515_2_[1]),parseInt(p_71515_1_,p_71515_2_[2]));
        }
        else tile = p_71515_1_.getEntityWorld().getTileEntity(parseInt(p_71515_1_,p_71515_2_[0]),parseInt(p_71515_1_,p_71515_2_[1]),parseInt(p_71515_1_,p_71515_2_[2]));
        if(tile == null) throw new WrongUsageException("TileEntity at: "+p_71515_2_[0]+" "+p_71515_2_[1]+" "+p_71515_2_[2]+" is invalid!");
        NBTTagCompound nbt = new NBTTagCompound();
        tile.writeToNBT(nbt);
        p_71515_1_.addChatMessage(new ChatComponentText(nbt.toString()));
    }
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] string){
        ArrayList<String> list = new ArrayList<>();
           if(string[0].equals("")){list.add(String.valueOf(sender.getPlayerCoordinates().posX));return list;}
           else if(string.length ==2 && string[1].equals("")) {list.add(String.valueOf(sender.getPlayerCoordinates().posY - 1));return list;}
           else if(string.length ==3 && string[2].equals("")) {list.add(String.valueOf(sender.getPlayerCoordinates().posZ));return list;}
           else if(string.length ==4 && string[3].equals("")) list.add(String.valueOf(sender.getEntityWorld().provider.dimensionId));
        return list;
    }
}
