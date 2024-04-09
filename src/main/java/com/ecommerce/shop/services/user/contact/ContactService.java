package com.ecommerce.shop.services.user.contact;

import com.ecommerce.shop.payload.ContactDto;

public interface ContactService {
    ContactDto addMessage(ContactDto contactDto);
}
