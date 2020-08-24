package cn.lianrf.utils.db;

import com.mongodb.MongoClient;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version: v1.0
 * @date: 2020/8/24
 * @author: lianrf
 */
public class MongoIndexUtil {
    /**
     * 拷贝mongo的索引
     */
    public static void copyIndex() {
        MongoDatabase regulation_bak = getDb("30.0.0.241", 27017, "regulation_bak");
        MongoDatabase regulation_v2 = getDb("30.0.0.241", 27017,"regulation_v2");

        MongoIterable<String> strings = regulation_bak.listCollectionNames();
        for (String string : strings) {
            ListIndexesIterable<Map> maps = regulation_bak.getCollection(string).listIndexes(Map.class);
            ArrayList<Object> objects = new ArrayList<>();
            for (Map map : maps) {
                objects.add(map);
            }
            Document document = buildIndex(objects);
            if(document!=null){
                regulation_v2.runCommand(document);
            }
        }

        for (MongoClient client : clients) {
            client.close();
        }
    }

    private static List<MongoClient> clients=new ArrayList<>();


    private static MongoDatabase getDb(String ip, int port, String db) {
        MongoClient source = new MongoClient(ip, port);
        clients.add(source);
        return source.getDatabase(db);
    }

    public static Document buildIndex(List list) {
        Document json = new Document();
        String collectionName = null;
        ArrayList<Object> indexes = new ArrayList<>();
        for (Object o : list) {
            Map object = (Map) o;
            HashMap<String,Object> itm = new HashMap<>();
            if (!"_id_".equals(object.get("name"))) {
                itm.put("key", object.get("key"));
                itm.put("name", object.get("name"));
                indexes.add(itm);
            }
            if(collectionName==null){
                Object ns = object.get("ns");
                if(ns==null){
                    continue;
                }
                String[] split = ns.toString().split("\\.");
                collectionName=split[split.length-1];
            }
        }
        json.put("createIndexes", collectionName);
        json.put("indexes", indexes);
        if(indexes.size()>0){
            return json;
        }
        return null;
        //return "db.runCommand("+JSONObject.toJSONString(json)+")";
    }
}
