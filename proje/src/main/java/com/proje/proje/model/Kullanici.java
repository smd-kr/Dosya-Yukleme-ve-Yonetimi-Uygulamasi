package com.proje.proje.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
//Bu üstteki anatosyonlar bize kkolaylık sağlar
// Kullanıcı bilgilerini tutan sınıf
public class Kullanici {
    private String kullaniciAdi; // Kullanıcı adı
    private String sifre;        // Kullanıcının şifresi

  
}