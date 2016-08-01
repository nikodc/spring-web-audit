package io.github.nikodc.springwebaudit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
public class ItemController {

    @AuditableMethod
    @RequestMapping(value = "/items", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Item>> list(
            @RequestParam(value = "count", required = false) Integer count) {

        if (count == null) {
            count = 10;
        }

        List<Item> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(new Item()
                    .withRandomId()
                    .withRandomContent());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @AuditableMethod
    @RequestMapping(value = "/items", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Item> add(
            @RequestBody Item item) {

        return new ResponseEntity<Item>(item, HttpStatus.OK);
    }

    public static class Item {

        private long id;
        private String content;

        public Item withRandomId() {
            this.id = new Random().nextInt();
            return this;
        }

        public Item withRandomContent() {
            this.content = UUID.randomUUID().toString();
            return this;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
