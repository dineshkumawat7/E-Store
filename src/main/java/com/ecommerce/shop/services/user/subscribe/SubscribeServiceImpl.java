package com.ecommerce.shop.services.user.subscribe;

import com.ecommerce.shop.entity.Subscribe;
import com.ecommerce.shop.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService{

    @Autowired
    private SubscribeRepository subscribeRepository;
    @Override
    public Subscribe subscribeNewsLetter(Subscribe subscribe) {
        this.subscribeRepository.save(subscribe);
        return  subscribe;
    }
}
