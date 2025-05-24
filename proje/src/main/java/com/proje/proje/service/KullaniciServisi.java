package com.proje.proje.service;

import org.springframework.stereotype.Service;

import com.proje.proje.model.*;

import java.util.*;

//kullanıcı işlemlerini yöneten katman
@Service
public class KullaniciServisi {
    private final ArrayList<Kullanici> kullanicilar=new ArrayList<>(); // Kullanıcıları  Tutan yapı
    
    // Yeni kullanıcı kaydı oluşturur Mevcut bilgiler buraya geldi
    public boolean kayitOl(String kullaniciAdi, String sifre) {
    	boolean durum=true;
    	//Şimdi buraya gerekn bilgiler geldi
    	//Mevcut Listimdeki tüm kullanicilar teker teker dolaşıldı eğer bu isimde bir
    	//kullaniciAdi varsa sorun var demek false deyip geri dönecek
    	for(Kullanici kullanici:kullanicilar) {
    		if (kullanici.getKullaniciAdi().equals(kullaniciAdi)) {
				durum=false;
			}
    	}
    	//üstten true geldimi ekleyip return durum dönderir
    	if (durum) kullanicilar.add(new Kullanici(kullaniciAdi, sifre));
//        if (kullanicilar.containsKey(kullaniciAdi)) 
//        	return false; // Aynı kullanıcı adı varsa hata
//        kullanicilar.put(kullaniciAdi, new Kullanici(kullaniciAdi, sifre)); // Kullanıcı eklenir
//        return true;
    	return durum;
    }

    // Kullanıcı adı ve şifreyle doğrulama yapar
    public boolean dogrula(String kullaniciAdi, String sifre) {
    	boolean durum=false;
    	for(Kullanici kullanici:kullanicilar) {
    		if (kullanici.getKullaniciAdi().equals(kullaniciAdi)||kullanici.getSifre().equals(sifre)) durum=true; }
				
    	return durum;
    	
//        Kullanici kullanici = kullanicilar.get(kullaniciAdi); // Kullanıcıyı bul
//        return kullanici != null && kullanici.getSifre().equals(sifre); // Şifre kontrolü
    }
}