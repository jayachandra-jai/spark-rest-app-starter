package com.jai.model.modules;

import lombok.Data;

/**
 * @author jai
 * created on 24/09/23
 */
@Data
public class Item {
    int id;
    String title;
    String info;
    public Item(int id,String title,String info){
        this.id=id;
        this.title=title;
        this.info=info;
    }
}
