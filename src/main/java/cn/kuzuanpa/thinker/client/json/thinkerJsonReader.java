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
package cn.kuzuanpa.thinker.client.json;

import cn.kuzuanpa.thinker.Thinker;
import cn.kuzuanpa.thinker.client.objects.IThinkerObject;
import cn.kuzuanpa.thinker.client.handler.profileHandler;
import cn.kuzuanpa.thinker.client.objects.IThinkerAnime;
import cn.kuzuanpa.thinker.client.objects.dummyWorld.*;
import cn.kuzuanpa.thinker.client.objects.gui.ThinkerButtonBase;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class thinkerJsonReader {
    public static ArrayList<IThinkerObjectsAdaptor> objectsAdaptors=new ArrayList<>();
    public static ArrayList<IThinkerAnimeAdaptor> animesAdaptors=new ArrayList<>();

    public static void readAllProfiles(String path) throws IOException {
        ArrayList<profileHandler.thinkingProfile> profileList = new ArrayList<>();
        Files.list(Paths.get(path)).forEach(file -> {
            if(file.toString().equalsIgnoreCase("ideas/resources")||file.toString().equalsIgnoreCase("ideas/ignore"))return;
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
        profileHandler.buildDirTree();

    }
    public static profileHandler.thinkingProfile readProfiles(JsonReader json,String profileName)throws IOException,IllegalArgumentException {
        String id="";
        String bindItemID="";
        String[] dir = null;
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
                }else if (jsonName.equalsIgnoreCase("bindItemID")) {
                    bindItemID = json.nextString();
                }else if (jsonName.equalsIgnoreCase("dir")) {
                    String str =  json.nextString();
                    if(!str.equals(""))dir =str.split("/");
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
            ArrayList<IdummyWorldThinkerObject> objs=new ArrayList<>();
            List<ThinkerButtonBase> buttons=new ArrayList<>();
            ThinkerObjects.forEach(obj->{
                if(obj instanceof IdummyWorldThinkerObject) objs.add(((IdummyWorldThinkerObject)obj));
            });
            if(id.equals("")||ThinkerObjects.isEmpty()){logError(json,profileName,"Invaild Profile");return null;}
            profileHandler.thinkingProfile returnProfile = objs.isEmpty() ? new profileHandler.thinkingProfile(id,icon,iconR,iconG,iconB,iconA,buttons) : new profileHandler.thinkingProfile(id,icon,iconR,iconG,iconB,iconA,objs,buttons);
            returnProfile.setBindItemId(bindItemID);
            if(dir != null&&dir.length>0)returnProfile.setDir(dir);
            return returnProfile;
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
    public static IThinkerObject readThinkerObject(JsonReader json, String fileName)throws JsonParseException,IOException,IllegalArgumentException{
        HashMap<String,Object> values=new HashMap<>();
        ArrayList<IThinkerAnime> animes=new ArrayList<>();
        json.beginObject();
        while (json.hasNext())
        {
            String jsonName = json.nextName();
            if(jsonName.equalsIgnoreCase("comment")){
                json.skipValue();
            } else if(jsonName.equalsIgnoreCase("animes")){
                animes=readAllAnime(json, fileName);
            } else {
                JsonToken token = json.peek();
                if(token.equals(JsonToken.BOOLEAN))values.put(jsonName,Boolean.toString(json.nextBoolean()));
                else if(token.equals(JsonToken.BEGIN_OBJECT))values.put(jsonName,readCompounds(json,fileName));
                else values.put(jsonName,json.nextString());
            }
        }
        if(!values.containsKey("type"))requestedErr.add("Missing Required Element: type");
        else for (IThinkerObjectsAdaptor objectsAdaptor : objectsAdaptors) {
            if(objectsAdaptor.isMapHaveValidContents(values)){
                json.endObject();
                return objectsAdaptor.create(values);
            }
        }
        if(!requestedErr.isEmpty())requestedErr.forEach(err->logError(json,fileName,err));   //If error occurred when creating object:
        else logError(json,fileName,"Unknown object type");//If no match type:
        requestedErr.clear();
        json.endObject();
        return null;
    }

    public static Map<String ,Object> readCompounds(JsonReader json, String fileName) throws JsonParseException,IOException,IllegalArgumentException {
        json.beginObject();
        Map<String, Object> values = new HashMap<>();
        String jsonName;
        while (json.hasNext()) {
            jsonName = json.nextName();
            if (jsonName.equals("comment")) {
                json.skipValue();
            }else {
                JsonToken token = json.peek();
                if(token.equals(JsonToken.BOOLEAN))values.put(jsonName,Boolean.toString(json.nextBoolean()));
                else if(token.equals(JsonToken.BEGIN_OBJECT))values.put(jsonName,readCompounds(json,fileName));
                else values.put(jsonName,json.nextString());
            }
        }
        json.endObject();
        return values;
    }
    public static ArrayList<IThinkerAnime> readAllAnime(JsonReader json, String fileName)throws JsonParseException,IOException,IllegalArgumentException {
        ArrayList<IThinkerAnime> objects=new ArrayList<>();
        json.beginArray();
        while (json.hasNext()) {
            objects.add(readAnime(json,fileName));
        }
        json.endArray();
        return objects;
    }
    public static IThinkerAnime readAnime(JsonReader json, String fileName)throws JsonParseException,IOException,IllegalArgumentException{
        HashMap<String,Object> values=new HashMap<>();
        json.beginObject();
        while (json.hasNext())
        {
            String jsonName = json.nextName();
            if(jsonName.equalsIgnoreCase("comment")){
                json.skipValue();
            } else {
                JsonToken token = json.peek();
                if(token.equals(JsonToken.BOOLEAN))values.put(jsonName,Boolean.toString(json.nextBoolean()));
                else if(token.equals(JsonToken.BEGIN_OBJECT))values.put(jsonName,readCompounds(json,fileName));
                else values.put(jsonName,json.nextString());
            }
        }
        if(!values.containsKey("type"))requestedErr.add("Missing Required Element: type");
        else for (IThinkerAnimeAdaptor animeAdaptor : animesAdaptors) {
            if(animeAdaptor.isMapHaveValidContents(values)){
                json.endObject();
                return animeAdaptor.create(values);
            }
        }
        if(!requestedErr.isEmpty())requestedErr.forEach(err->logError(json,fileName,err));   //If error occurred when creating object:
        else logError(json,fileName,"Unknown anime type");//If no match type:
        requestedErr.clear();
        json.endObject();
        return null;
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
    public static ItemStack getItemStack(String itemStackString){
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
                Thinker.err("Invaild item name: " + itemName);
                return null;
            }
            return new ItemStack(item,1,itemDamage);
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
    public final static ArrayList<String> requestedErr = new ArrayList<>();
    public static void requestLogError(String error){
        requestedErr.add(error);
    }
    public static void logError(JsonReader jsonReader, String fileName,String error){
        /*FMLLog.log(Level.ERROR,*/System.out.println("Error: "+error+"\nIn file: "+fileName+jsonReader.toString().replaceAll("JsonReader",""));
    }
    public static void logMissing(JsonReader jsonReader, String fileName, String additionalText){
        /*FMLLog.log(Level.ERROR,*/System.out.println("Error: Missing Required Element: "+additionalText+"\nIn file: "+fileName+jsonReader.toString().replaceAll("JsonReader",""));
    }
}
