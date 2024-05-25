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

package cn.kuzuanpa.thinker.mixinHandler;

public class mixinRenderBlockHandler {
    public static int color=0xbbffffff;
    public static boolean isColorOverwrite=true;
    public static void setColor(int color){
        mixinRenderBlockHandler.color=color;
    }
    public static void setColor(int color,boolean overwrite){
        mixinRenderBlockHandler.color=color;
        isColorOverwrite=overwrite;
    }
}
