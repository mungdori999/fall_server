package com.mungdori.fallserver.adapter.webapi.dto;


import com.mungdori.fallserver.domain.admin.Admin;

public record AdminRegisterResponse(String name) {

    public static AdminRegisterResponse of(Admin admin) {
        return new AdminRegisterResponse(admin.getName());
    }
}

