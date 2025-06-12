package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import com.example.entity.Account;
import com.example.repository.MessageRepository;
import com.example.entity.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepo;

    public Optional<Message> postMessage(Message msg) {
        if (msg.getMessageText() == null || msg.getMessageText().isBlank() || msg.getMessageText().length() > 255) {
            return Optional.empty();
        }
        return Optional.of(messageRepo.save(msg));
    }

    public List<Message> getAllMessages() {
        return messageRepo.findAll();
    }

    public Optional<Message> getMessageById(int id) {
        return messageRepo.findById(id);
    }

    public int updateMessageText(int id, String newText) {
        Optional<Message> optional = messageRepo.findById(id);
        if (optional.isPresent() && newText != null && !newText.isBlank() && newText.length() <= 255) {
            Message msg = optional.get();
            msg.setMessageText(newText);
            messageRepo.save(msg);
            return 1;
        }
        return 0;
    }

    public List<Message> getMessagesByAccount(Account acc) {
        return messageRepo.findByPostedBy(acc.getAccountId());
    }

    public List<Message> getMessagesByUserId(Integer accountId) {
        return messageRepo.findMessagesByUserId(accountId);
    }

    @Transactional
    public int deleteMessageById(Integer messageId) {
        if (!messageRepo.existsById(messageId)) {
            return 0; // no deletion
        }
        messageRepo.deleteById(messageId);
        return 1;
    }

}
