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

package cn.kuzuanpa.thinker.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Session;
import net.minecraft.world.World;

import java.util.Random;
import java.util.UUID;

public class DummyEntityPlayer extends AbstractClientPlayer {
    public DummyEntityPlayer(World p_i45074_1_) {
        super(p_i45074_1_, new GameProfile(new UUID(0,0),"ThinkerDummyPlayer"));
    }
    int posX=0,posY=0,posZ=0;
    float pitch=0,yaw=0;
    @Override
    public void addChatMessage(IChatComponent p_145747_1_) {
    }

    @Override
    public boolean canCommandSenderUseCommand(int p_70003_1_, String p_70003_2_) {
        return false;
    }

    @Override
    public ChunkCoordinates getPlayerCoordinates() {
        return new ChunkCoordinates(posX,posY,posZ);
    }

    public DummyEntityPlayer setPos(int x, int y, int z) {
        this.posX=x;
        this.posY=y;
        this.posZ=z;
        return this;
    }
    public DummyEntityPlayer setFacing(float pitch, float yaw) {
        this.rotationPitch=pitch;
        this.rotationYaw=yaw;
        this.rotationYawHead=yaw;
        return this;
    }
}
