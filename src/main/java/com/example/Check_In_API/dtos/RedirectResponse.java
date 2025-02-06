package com.example.Check_In_API.dtos;

import com.example.Check_In_API.enums.CheckInRedirectEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class RedirectResponse implements Serializable {
    private Session session;
    private CheckInRedirectEnum goTo;

    public RedirectResponse(Session session, CheckInRedirectEnum goTo) {
        this.session = session;
        this.goTo = goTo;
    }
}

