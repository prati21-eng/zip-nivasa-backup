package com.zipnivasa.backend.modules.mess.service;

import com.zipnivasa.backend.modules.mess.dto.MessRequest;
import com.zipnivasa.backend.modules.mess.model.Mess;
import com.zipnivasa.backend.modules.mess.repository.MessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessService {

    private final MessRepository messRepository;

    public Mess addMess(MessRequest req) {
        Mess mess = Mess.builder()
                .messOwnerId(req.getMessOwnerId())
                .title(req.getTitle())
                .description(req.getDescription())
                .location(req.getLocation())
                .price(req.getPrice())
                .type(req.getType())
                .capacity(req.getCapacity())
                .menu(req.getMenu())
                .contact(req.getContact())
                .images(req.getImages())
                .build();

        return messRepository.save(mess);
    }

    public List<Mess> getAll() {
        return messRepository.findAll();
    }

    public Mess getById(String id) {
        return messRepository.findById(id).orElse(null);
    }

    public List<Mess> getByOwner(String ownerId) {
        return messRepository.findByMessOwnerId(ownerId);
    }

    public Mess update(String id, MessRequest req) {
        return messRepository.findById(id).map(mess -> {
            mess.setTitle(req.getTitle());
            mess.setDescription(req.getDescription());
            mess.setLocation(req.getLocation());
            mess.setPrice(req.getPrice());
            mess.setType(req.getType());
            mess.setCapacity(req.getCapacity());
            mess.setMenu(req.getMenu());
            mess.setContact(req.getContact());
            return messRepository.save(mess);
        }).orElse(null);
    }

    public void delete(String id) {
        messRepository.deleteById(id);
    }

    public Mess publishSpecial(String ownerId, Mess.SpecialToday special) {
        Mess mess = messRepository.findByMessOwnerId(ownerId).stream().findFirst().orElse(null);
        if (mess == null) return null;

        mess.setSpecialToday(special);
        return messRepository.save(mess);
    }

    public Mess addRating(String messId, Mess.Rating rating) {
        return messRepository.findById(messId).map(mess -> {
            mess.getRatings().add(rating);
            return messRepository.save(mess);
        }).orElse(null);
    }
}
