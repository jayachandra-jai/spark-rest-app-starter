package com.jai.services;

import com.jai.model.modules.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author jai
 * created on 24/09/23
 */
public class ItemService {
    public static List<Item> items=new ArrayList<>();
    public static String addItem(Item item){
        if(items.size()>0)
            item.setId(items.get(items.size()-1).getId()+1);
        items.add(item);
        return "success";
    }
    public static Item getItem(int id){
        Optional<Item> item= items.stream().filter(x -> x.getId() == id).findFirst();
        if(item.isPresent())
            return item.get();
        return null;
    }
    public static boolean checkItem(int id){
        if(getItem(id)!=null){
            items.stream().filter(x -> x.getId() == id).findFirst().ifPresent(obj -> obj.setChecked(!obj.isChecked()));
            return true;
        }
        return false;
    }
    public static Item deleteItem(int id){
        Optional<Item> item= items.stream().filter(x -> x.getId() == id).findFirst();
        if(item.isPresent()){
            items.remove(item.get());
            return item.get();
        }
        return null;
    }
    public static List<Item> getAllItems(){
        return items;
    }
}
