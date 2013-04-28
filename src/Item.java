import java.util.HashMap;


public class Item {
    private int index;
    private String name;
    
    private static Item[] allItems;
    private static HashMap<String,Item> allItemsHashMap;
    
    public Item(int n_index, String n_name) {
        index = n_index;
        name = n_name;
    }
    
    static {
        allItems = new Item[Constants.itemNames.length];
        allItemsHashMap = new HashMap<String,Item>();
        
        Item item;
        for(int i = 0; i < allItems.length; i++) {
            String name = Constants.itemNames[i];
            item = new Item(i, name);
            allItems[i] = item;
            if(!name.isEmpty()) {
                allItemsHashMap.put(Constants.hashName(name), item);
            }
        }
    }
    
    public String toString() {
        return name;
    }
    
    public static Item getItem(int i) {
        if(i < 0 || i >= allItems.length)
            return null;
        return allItems[i];
    }
    
    public static Item getItemByName(String name) {
        name = Constants.hashName(name);
        if(!allItemsHashMap.containsKey(name))
            return null;
        return allItemsHashMap.get(name);
    }
    
    public String getName() {
        return name;
    }
}
