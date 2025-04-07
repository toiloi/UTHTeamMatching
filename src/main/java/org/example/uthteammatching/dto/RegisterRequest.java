package org.example.uthteammatching.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String ho;
    private String ten;
    private String gioiTinh;
    private String chuyenNganh;
    private String sdt;
}
