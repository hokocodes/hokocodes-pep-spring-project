package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        Optional<Account> result = accountService.register(account);

        if (result.isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400
        }

        Account registered = result.get();

        if (registered.getAccountId() != null && registered.getAccountId() == -1) {
            return ResponseEntity.status(409).build(); // conflict
        }

        return ResponseEntity.ok(registered); // 200
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account credentials) {
        Optional<Account> account = accountService.login(credentials.getUsername(), credentials.getPassword());
        return account.map(ResponseEntity::ok).orElse(ResponseEntity.status(401).build());
    
    }

    @PostMapping("/messages")
    public Object postMessage(@RequestBody Message msg) {
        Optional<Account> poster = accountService.findById(msg.getPostedBy());
        if (poster.isEmpty()) return ResponseEntity.badRequest().build();
        msg.setPostedBy(poster.get().getAccountId());
        Optional<Message> result = messageService.postMessage(msg);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{id}")
    public Message getMessageById(@PathVariable int id) {
        return messageService.getMessageById(id).orElse(null);
    }

    @PatchMapping("/messages/{id}")
    public Object updateMessage(@PathVariable int id, @RequestBody Message msg) {
        int updated = messageService.updateMessageText(id, msg.getMessageText());
        return updated == 1 ? 1 : ResponseEntity.badRequest().build();
    }

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByUserId(accountId);
        return ResponseEntity.ok(messages); // Always return 200, even if empty
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId) {
        try {
            int rowsDeleted = messageService.deleteMessageById(messageId);
            // Always return 200, with 0 or 1 depending on result
            if (rowsDeleted == 1) {
                return ResponseEntity.ok(rowsDeleted);
            } else {
                return ResponseEntity.ok().build();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
