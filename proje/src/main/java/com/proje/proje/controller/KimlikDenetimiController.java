package com.proje.proje.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.proje.proje.service.KullaniciServisi;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/kimlik")
public class KimlikDenetimiController {
    @Autowired
    //contexte bulunan service ait referans alıındı
    private KullaniciServisi kullaniciServisi;

    @PostMapping("/kayit")
    public Map<String, String> kayitOl(@RequestParam String kullaniciAdi, @RequestParam String sifre) {
    	//html tarafından kullaniciAdi ve sifre değerleri döndürülecek
    	//yanitin HashMap olma sebebi return yapınca html ile kontrol sağlanacak 
        Map<String, String> yanit = new HashMap<>();
        //kullanıcı adı boş ise uyarı verir .trim() demek stringin kenarlardaki boşlukları kaldır demek ve bunun boş olup
        //olmadığını kontrol et
        if (kullaniciAdi == null || kullaniciAdi.trim().isEmpty()) {
            yanit.put("mesaj", "Kullanıcı adı boş olamaz!");
            //yanit ekle bunu return edecek sonra
            
            return yanit;
        }
        //aynı işlemi sifre içinde yapıyor
        if (sifre == null || sifre.trim().isEmpty()) {
            yanit.put("mesaj", "Şifre boş olamaz!");
            return yanit;
        }
        //gelen değerlerde sorun yoksa servis sınıfına gidip kayıt işlemi yapıyor
        if (kullaniciServisi.kayitOl(kullaniciAdi, sifre)) {
            yanit.put("mesaj", "Kayıt başarılı!"); 
            //buradan true geldi kayıt başarılı diyecek
        } else {
            yanit.put("mesaj", "Kullanıcı adı zaten alınmış!");
   
        }
        return yanit;
    }

    @PostMapping("/giris")
    //local host ile bu istek atıldı
    public Map<String, String> girisYap(@RequestParam String kullaniciAdi, @RequestParam String sifre, HttpSession oturum) {
        //@RequestParam ile kullaniciAdi ve sifre değerleri alınıyor
    	//yanit return yapılacak şeydir
    	Map<String, String> yanit = new HashMap<>();
        if (kullaniciAdi == null || kullaniciAdi.trim().isEmpty()) {
        	//kullaaniciAdi boş veya str etrafında boşluklar kaldırıldığı zaman boş ise bu kısım çalışır
            yanit.put("mesaj", "Kullanıcı adı boş olamaz!");
            return yanit;
        }
        if (sifre == null || sifre.trim().isEmpty()) {
            yanit.put("mesaj", "Şifre boş olamaz!");
            //şifrenin boş olma durumu
            return yanit;
        }
        if (kullaniciServisi.dogrula(kullaniciAdi, sifre)) {
            oturum.setAttribute("kullaniciAdi", kullaniciAdi);
            //servis sınıfında bu bilgiler doğrulanır eğer true değeri gelirse sorun yok giriş yapabilir
            //giriş yaptığı anda kullanıcıya benzersiz bir session oluşturulur bunu sayesinde kullanıcı her işelm yapmak için oturum açmaya gerek kalmaz
            yanit.put("mesaj", "Giris başarılı!");
        } else {
            yanit.put("mesaj", "Kullanıcı adı veya şifre hatalı!");
        }
        return yanit;
    }

    @PostMapping("/cikis")
    public Map<String, String> cikisYap(HttpSession oturum) {
        oturum.invalidate();
        //çıkış yapınce mevcut session değeri kapatılır
        Map<String, String> yanit = new HashMap<>();
        yanit.put("mesaj", "Çıkış yapıldı!");
        return yanit;
    }

}