package cn.kuzuanpa.thinker.client.json;

import blockrenderer6343.api.utils.BlockPosition;
import cn.kuzuanpa.thinker.client.profileHandler;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldAnimes;
import cn.kuzuanpa.thinker.client.render.dummyWorld.anime.IDummyWorldGraphicAnime;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldBlock;
import cn.kuzuanpa.thinker.client.render.dummyWorld.dummyWorldTileEntity;
import cn.kuzuanpa.thinker.client.render.gui.anime.IGuiAnime;
import cn.kuzuanpa.thinker.client.render.gui.button.ThinkerButton;
import cn.kuzuanpa.thinker.client.render.gui.button.custom.customImage;
import cn.kuzuanpa.thinker.client.render.gui.button.custom.customText;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class jsonReader {
    public static ArrayList<profileHandler.thinkingProfile> profileList=new ArrayList<>();
    public static void readAllProfiles(String path) throws IOException {
        ArrayList<profileHandler.thinkingProfile> profileList = new ArrayList<>();
        Files.list(Paths.get(path)).forEach(file -> {
            try (JsonReader json = new JsonReader(new InputStreamReader(Files.newInputStream(file), StandardCharsets.UTF_8))) {
                if(Files.size(file)> 67108864 /*64MiB*/) throw new IOException("Too large file");
                json.setLenient(true);
                profileHandler.thinkingProfile obj = readProfiles(json, String.valueOf(file));
                if(obj != null)profileList.add(obj);
            } catch (Exception e) {
                System.out.println("Exception in "+file+": "+e.getMessage());
            }
        });
        //profileHandler.clearAllProfile();
        profileHandler.addProfiles(profileList);

    }
    public static profileHandler.thinkingProfile readProfiles(JsonReader json,String profileName)throws IOException,IllegalArgumentException {
            String id="";
            IIcon icon=null;
            float iconR=1.0F;
            float iconG=1.0F;
            float iconB=1.0F;
            float iconA=1.0F;
            List<Object> ThinkerObjects = new ArrayList<>();
            json.beginObject();
            while (json.hasNext())
            {
                String jsonName = json.nextName();
                if (jsonName.equalsIgnoreCase("id")) {
                    id =  json.nextString();
                }else if (jsonName.equalsIgnoreCase("icon")) {
                    icon=getIcon(json.nextString(),json,profileName);
                } else if (jsonName.equalsIgnoreCase("iconR")) {
                    iconR = (float) json.nextDouble();
                } else if (jsonName.equalsIgnoreCase("iconG")) {
                    iconG = (float) json.nextDouble();
                } else if (jsonName.equalsIgnoreCase("iconB")) {
                    iconB = (float) json.nextDouble();
                }else if (jsonName.equalsIgnoreCase("iconA")) {
                    iconA = (float) json.nextDouble();
                } else if (jsonName.equalsIgnoreCase("Objects")) {
                    ThinkerObjects=readAllThinkerObjects(json,profileName);
                } else if(jsonName.equalsIgnoreCase("comment")){
                    json.skipValue();
                } else {
                    logError(json,profileName,"unknown Element:"+jsonName);
                    json.skipValue();
                }
            }
            json.endObject();
            HashMap<BlockPosition,dummyWorldBlock> blocks=new HashMap<>();
            HashMap<BlockPosition,dummyWorldTileEntity> tiles=new HashMap<>();
            List<ThinkerButton> buttons=new ArrayList<>();
            ThinkerObjects.forEach(obj->{
                if(obj instanceof dummyBlockWithCoord) blocks.put(((dummyBlockWithCoord) obj).pos,((dummyBlockWithCoord) obj).block);
                if(obj instanceof dummyTileWithCoord)tiles.put(((dummyTileWithCoord) obj).pos,((dummyTileWithCoord) obj).tile);
                if(obj instanceof ThinkerButton)buttons.add((ThinkerButton) obj);
            });
            if(id.equals("")||ThinkerObjects.isEmpty()){logError(json,profileName,"Invaild Profile");return null;}
            if(!blocks.isEmpty()||!tiles.isEmpty())return new profileHandler.thinkingProfile(id,icon,iconR,iconG,iconB,iconA,blocks,tiles,buttons);
            else return new profileHandler.thinkingProfile(id,icon,iconR,iconG,iconB,iconA,buttons);
    }
    public static IIcon getIcon(String iconString, JsonReader json, String fileName){
        try {
            String itemName="minecraft:stone";
            int itemDamage=0;
            String[] str=iconString.trim().split(":");
            if(str.length==1)itemName="minecraft:"+str[0];
            if(str.length==2){
                try{
                    itemDamage=Integer.parseInt(str[1]);
                    itemName="minecraft:"+str[0];
                }catch (NumberFormatException e){
                    itemName=str[0]+":"+str[1];
                }
            }
            if(str.length==3){
                itemDamage=Integer.parseInt(str[2]);
                itemName=str[0]+":"+str[1];
            }
            Item item=(Item)Item.itemRegistry.getObject(itemName);
            if(item==null){
                logError(json,fileName,"Invaild item name: "+itemName);
                return null;
            }
            return item.getIconFromDamage(itemDamage);
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
    public static ItemStack getItemStack(String itemStackString, JsonReader json, String fileName){
        try {
            String itemName="minecraft:stone";
            int itemDamage=0;
            String[] str=itemStackString.trim().split(":");
            if(str.length==1)itemName="minecraft:"+str[0];
            if(str.length==2){
                try{
                    itemDamage=Integer.parseInt(str[1]);
                    itemName="minecraft:"+str[0];
                }catch (NumberFormatException e){
                    itemName=str[0]+":"+str[1];
                }
            }
            if(str.length==3){
                itemDamage=Integer.parseInt(str[2]);
                itemName=str[0]+":"+str[1];
            }
            Item item=(Item)Item.itemRegistry.getObject(itemName);
            if(item==null){
                logError(json,fileName,"Invaild item name: "+itemName);
                return null;
            }
            return new ItemStack(item,1,itemDamage);
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
    public static ArrayList<Object> readAllThinkerObjects(JsonReader json,String fileName)throws JsonParseException,IOException,IllegalArgumentException {
        ArrayList<Object> objects=new ArrayList<>();
        json.beginArray();
        while (json.hasNext()) {
            Object obj = readThinkerObject(json,fileName);
            if(obj==null)continue;
            objects.add(obj);
        }
        json.endArray();
        return objects;
    }
        public static Object readThinkerObject(JsonReader json, String fileName)throws JsonParseException,IOException,IllegalArgumentException{
            String name = null;
            String type = null;
            String path = null;
            String text = null;
            String tileEntityNBTString = null;
            int posX=-2147483647;
            int posY=-2147483647;
            int posZ=-2147483647;
            int width=0;
            int height=0;
            int color=0;
            int meta=0;
            boolean renderAllFaces=false;
            ItemStack blockFromItemStack = null;
            ArrayList<Object> unsortedAnimes=new ArrayList<>();
            ArrayList<IGuiAnime> guiAnimes=new ArrayList<>();
            ArrayList<IDummyWorldAnimes> dummyWorldAnimes=new ArrayList<>();
            json.beginObject();
            while (json.hasNext())
            {
                String jsonName = json.nextName();
                if (jsonName.equalsIgnoreCase("name")) {
                    name = json.nextString();
                } else if (jsonName.equalsIgnoreCase("type")) {
                    type = json.nextString();
                } else if (jsonName.equalsIgnoreCase("posX")) {
                    posX = json.nextInt();
                } else if (jsonName.equalsIgnoreCase("posY")) {
                    posY = json.nextInt();
                } else if (jsonName.equalsIgnoreCase("posZ")) {
                    posZ = json.nextInt();
                }else if (jsonName.equalsIgnoreCase("width")) {
                    width = json.nextInt();
                }else if (jsonName.equalsIgnoreCase("height")) {
                    height = json.nextInt();
                }else if (jsonName.equalsIgnoreCase("color")) {
                    color = json.nextInt();
                }else if (jsonName.equalsIgnoreCase("text")) {
                    text  = json.nextString();
                }else if (jsonName.equalsIgnoreCase("tileEntityNBT")) {
                    tileEntityNBTString = json.nextString();
                }else if (jsonName.equalsIgnoreCase("meta")) {
                    meta = json.nextInt();
                }else if (jsonName.equalsIgnoreCase("animes")) {
                    unsortedAnimes=readAllAnime(json,fileName);
                    unsortedAnimes.forEach(anime->{if(anime instanceof IGuiAnime)guiAnimes.add((IGuiAnime) anime);else dummyWorldAnimes.add((IDummyWorldAnimes) anime);});
                }else if (jsonName.equalsIgnoreCase("blockFromItem")) {
                    blockFromItemStack=getItemStack(json.nextString(),json,fileName);
                }else if (jsonName.equalsIgnoreCase("renderAllFaces")||
                          jsonName.equalsIgnoreCase("renderAllFace")) {
                    renderAllFaces = json.nextBoolean();
                }else if (jsonName.equalsIgnoreCase("path")) {
                    path = json.nextString();
                } else if(jsonName.equalsIgnoreCase("comment")){
                    json.skipValue();
                } else {
                    logError(json,fileName,"unknown Element:"+jsonName);
                    json.skipValue();
                }
            }
            if(type==null){json.endObject();logMissing(json,fileName,"Type");return null;}
            Object out=null;

            switch (type){
                case "image":
                case "Image": out= getImage(json,fileName,posX,posY,width,height,path,guiAnimes,dummyWorldAnimes);break;
                case "text":
                case "Text": out= getText(json,fileName,posX,posY,text,color,guiAnimes,dummyWorldAnimes);break;
                case "block":
                case "Block": out= blockFromItemStack==null?getBlock(json,fileName,posX,posY,posZ,name,meta,guiAnimes,dummyWorldAnimes,renderAllFaces):getBlockFromItem(json,fileName,posX,posY,posZ,blockFromItemStack,meta,guiAnimes,dummyWorldAnimes,renderAllFaces);break;
                case "tile":
                case "Tile":
                case "tileEntity":
                case "TileEntity": out= getTileEntity(json,fileName,posX,posY,posZ,tileEntityNBTString,guiAnimes,dummyWorldAnimes);break;
                default: logError(json,fileName,"Unknown Type:"+type);
            }
            json.endObject();
            return out;
        }


    public static ArrayList<Object> readAllAnime(JsonReader json, String fileName)throws JsonParseException,IOException,IllegalArgumentException {
        ArrayList<Object> objects=new ArrayList<>();
        json.beginArray();
        while (json.hasNext()) {
            objects.add(readAnime(json,fileName));
        }
        json.endArray();
        return objects;
    }
    public static Object readAnime(JsonReader json, String fileName)throws JsonParseException,IOException,IllegalArgumentException{
        String name = null;
        String type = null;
        String path = null;
        String text = null;
        String tileEntityNBTString = null;
        int posX=-2147483647;
        int posY=-2147483647;
        int posZ=-2147483647;
        int width=0;
        int height=0;
        int color=0;
        int meta=0;
        ArrayList<Object> unsortedAnimes=new ArrayList<>();
        json.beginObject();
        while (json.hasNext())
        {
            String jsonName = json.nextName();
            if (jsonName.equalsIgnoreCase("type")) {
                type = json.nextString();
            } else if (jsonName.equalsIgnoreCase("posX")) {
                posX = json.nextInt();
            } else if (jsonName.equalsIgnoreCase("posY")) {
                posY = json.nextInt();
            } else if (jsonName.equalsIgnoreCase("posZ")) {
                posZ = json.nextInt();
            }else if (jsonName.equalsIgnoreCase("width")) {
                width = json.nextInt();
            }else if (jsonName.equalsIgnoreCase("height")) {
                height = json.nextInt();
            }else if (jsonName.equalsIgnoreCase("color")) {
                color = json.nextInt();
            }else if (jsonName.equalsIgnoreCase("text")) {
                text  = json.nextString();
            }else if (jsonName.equalsIgnoreCase("tileEntityNBT")) {
                tileEntityNBTString = json.nextString();
            }else if (jsonName.equalsIgnoreCase("meta")) {
                meta = json.nextInt();
            }else if (jsonName.equalsIgnoreCase("Animes")) {
                unsortedAnimes=readAllAnime(json,fileName);
            }else if (jsonName.equalsIgnoreCase("path")) {
                path = json.nextString();
            }else if (jsonName.equalsIgnoreCase("path")) {
                path = json.nextString();
            }else if (jsonName.equalsIgnoreCase("path")) {
                path = json.nextString();
            } else if(jsonName.equalsIgnoreCase("name")||jsonName.equalsIgnoreCase("comment")){
                json.skipValue();
            } else {
                logError(json,fileName,"unknown Element:"+jsonName);
                json.skipValue();
            }
        }
        if(type==null){json.endObject();logMissing(json,fileName,"Type");return null;}
        Object out=null;
        switch (type){
            default: logError(json,fileName,"Unknown Anime Type:"+type);
        }
        json.endObject();
        return out;
    }
    public static customImage getImage(JsonReader jsonReader, String fileName,int posX,int posY,int width,int height,String path,ArrayList<IGuiAnime> guiAnimes,ArrayList<IDummyWorldAnimes> blockAnimes) {
        if(!blockAnimes.isEmpty())blockAnimes.forEach(anime->logError(jsonReader,fileName,"Invaild anime type: "+anime.jsonName()+"for block"));

        if(path==null){logMissing(jsonReader,fileName,"path");return null;}
        if(posX==-2147483647){logMissing(jsonReader,fileName,"posX");return null;}
        if(posY==-2147483647){logMissing(jsonReader,fileName,"posY");return null;}
        if(width==0){logMissing(jsonReader,fileName,"Width");return null;}
        if(height==0){logMissing(jsonReader,fileName,"Height");return null;}
        return new customImage(10,path,posX,posY,width,height);
    }
    public static customText getText(JsonReader jsonReader, String fileName,int posX,int posY,String text,int color,ArrayList<IGuiAnime> guiAnimes,ArrayList<IDummyWorldAnimes> blockAnimes) {
        if(!blockAnimes.isEmpty())blockAnimes.forEach(anime->logError(jsonReader,fileName,"Invaild anime type: "+anime.jsonName()+"for block"));

        if(text==null){logMissing(jsonReader,fileName,"text");return null;}
        if(posX==-2147483647){logMissing(jsonReader,fileName,"posX");return null;}
        if(posY==-2147483647){logMissing(jsonReader,fileName,"posY");return null;}
        return new customText(10,text,posX,posY,color);
    }
    public static dummyBlockWithCoord getBlock(JsonReader jsonReader, String fileName,int posX,int posY,int posZ,String blockName,int meta,ArrayList<IGuiAnime> guiAnimes,ArrayList<IDummyWorldAnimes> blockAnimes, boolean renderAllFace) {
        if(!guiAnimes.isEmpty())guiAnimes.forEach(anime->logError(jsonReader,fileName,"Invaild anime type: "+anime.jsonName()+"for block"));
        if(blockName==null){logMissing(jsonReader,fileName,"Name");return null;}
        if(posX==-2147483647){logMissing(jsonReader,fileName,"posX");return null;}
        if(posY==-2147483647){logMissing(jsonReader,fileName,"posY");return null;}
        if(posZ==-2147483647){logMissing(jsonReader,fileName,"posZ");return null;}
        return new dummyBlockWithCoord(new BlockPosition(posX,posY,posZ),new dummyWorldBlock(Block.getBlockFromName(blockName),blockAnimes).setRenderAllFace(renderAllFace));
    }
    public static dummyBlockWithCoord getBlockFromItem(JsonReader jsonReader, String fileName, int posX, int posY, int posZ, ItemStack itemStack, int meta, ArrayList<IGuiAnime> guiAnimes, ArrayList<IDummyWorldAnimes> blockAnimes, boolean renderAllFace) {
        if(!guiAnimes.isEmpty())guiAnimes.forEach(anime->logError(jsonReader,fileName,"Invaild anime type: "+anime.jsonName()+"for block"));
        if(itemStack==null){logMissing(jsonReader,fileName,"fromItem");return null;}
        if(meta!=0)          {  logError(jsonReader,fileName,"Useless Element: meta in blockFromItem");return null;}
        if(posX==-2147483647){logMissing(jsonReader,fileName,"posX");return null;}
        if(posY==-2147483647){logMissing(jsonReader,fileName,"posY");return null;}
        if(posZ==-2147483647){logMissing(jsonReader,fileName,"posZ");return null;}
        return new dummyBlockWithCoord(new BlockPosition(posX,posY,posZ),new dummyWorldBlock(itemStack,blockAnimes).setRenderAllFace(renderAllFace));
    }

    public static dummyTileWithCoord getTileEntity(JsonReader jsonReader, String fileName,int posX,int posY,int posZ,String tileEntityNBTString,ArrayList<IGuiAnime> guiAnimes,ArrayList<IDummyWorldAnimes> blockAnimes) {
        if(!guiAnimes.isEmpty())guiAnimes.forEach(anime->logError(jsonReader,fileName,"Invaild anime type: "+anime.jsonName()+" for tileEntity"));
        NBTTagCompound tileEntityNBT= new NBTTagCompound();
        if(posX!=-2147483647)tileEntityNBT.setInteger("x",posX);
        else posX=tileEntityNBT.getInteger("x");
        if(posY!=-2147483647)tileEntityNBT.setInteger("y",posY);
        else posY=tileEntityNBT.getInteger("y");
        if(posZ!=-2147483647)tileEntityNBT.setInteger("z",posZ);
        else posZ=tileEntityNBT.getInteger("z");
        TileEntity tile = TileEntity.createAndLoadEntity(tileEntityNBT);
        if (tile==null){
            logError(jsonReader,fileName,"Invalid TileEntity");
            return null;
        }
        return new dummyTileWithCoord(new BlockPosition(posX,posY,posZ),new dummyWorldTileEntity(tile,blockAnimes));
    }


    public static void logError(JsonReader jsonReader, String fileName,String error){
        /*FMLLog.log(Level.ERROR,*/System.out.println("Error: "+error+"\nIn file: "+fileName+jsonReader.toString().replaceAll("JsonReader",""));
    }
    public static void logMissing(JsonReader jsonReader, String fileName, String additionalText){
        /*FMLLog.log(Level.ERROR,*/System.out.println("Error: Missing Required Element: "+additionalText+"\nIn file: "+fileName+jsonReader.toString().replaceAll("JsonReader",""));
    }
}
