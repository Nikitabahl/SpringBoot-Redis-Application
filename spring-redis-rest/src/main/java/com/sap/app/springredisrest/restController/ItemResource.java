package com.sap.app.springredisrest.restController;

import com.sap.app.springredisrest.model.Item;
import com.sap.app.springredisrest.service.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@RestController
@RequestMapping("/items")
public class ItemResource {

    @Autowired
    private ItemRepository itemRepository;

    private Item item;

    public ItemResource(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    private String getTimeNow(){
        LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        ldt = ldt.minusSeconds(2);
        return ldt.toString();
    }

    public List<Map.Entry<String, Item>> descEntries(Map<String, Item> map){

        List<Map.Entry<String, Item>> entryList = new ArrayList<Map.Entry<String, Item>>(map.entrySet());

        Collections.sort(
                entryList, new Comparator<Map.Entry<String, Item>>() {
                    @Override
                    public int compare(Map.Entry<String, Item> item1,
                                       Map.Entry<String, Item> item2) {
                        return item2.getValue().getTime()
                                .compareTo(item1.getValue().getTime());
                    }
                }
        );

        return entryList;
    }

    public List<Map.Entry<String, Item>> getLast100(List<Map.Entry<String, Item>> map){

        if(map.size() <= 100)
            return map;
        else{
            return map.subList(0,100);
        }
    }

    public List<Map.Entry<String, Item>> getLast2Sec(List<Map.Entry<String, Item>> map){

        int entries = 0;
        String timeMinus2 = getTimeNow();

        for(Map.Entry<String, Item> i : map){
            if(i.getValue().getTime().compareTo(timeMinus2) >= 0){
                entries++;
            }
            else{
                break;
            }
        }
        return map.subList(0,entries);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Map.Entry<String, Item>> getComparingValues() {

        List<Map.Entry<String, Item>> map = descEntries(itemRepository.findAll());
        List<Map.Entry<String, Item>> last100 = getLast100(map);
        List<Map.Entry<String, Item>> last2Sec = getLast2Sec(map);

        if(last100.size() >= last2Sec.size())
            return last100;
        else
            return last2Sec;
    }

    @RequestMapping(value="/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Item> handleAllItemRequest() {
        return itemRepository.findAll();
    }

    @RequestMapping(value="/{id}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Item getById(@PathVariable("id") final String id){
        return itemRepository.findById(id);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> addItems(@RequestBody Map<String, Object>[] items) throws Exception{
        int i;
        for(i =0; i <items.length; i++){
            Map<String, String> map = (Map<String, String>) items[i].get("item");
            String time = map.get("time");
            String id = map.get("id");
            item = new Item(id, time);
            itemRepository.save(item);
            System.out.println("item saved with id: "+ item.getId());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
