package com.proje.proje.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

// Dosya işlemlerini yöneten controller
@RestController
@RequestMapping("/dosyalar")
public class DosyaController {
    private final String YUKLEME_KLASORU = "uploads";
    //DOSYALARIN YÜKLENECEĞİ KLASORUN ADI(PROJEDE OTOMATİKMEN uploads klasörü oluşturluacak)

    // Kullanıcının giriş yapıp yapmadığını kontrol eden yardımcı metot
    private boolean kullaniciGirisliMi(HttpSession oturum) {
        return oturum.getAttribute("kullaniciAdi") != null;
        //kullaniciAdi ne ait session bilgisi bos ise false
    }

    // Dosya yükleme endpoint'i
    @PostMapping("/yukle")
    public ResponseEntity<Map<String, String>> dosyaYukle(@RequestParam("dosya") MultipartFile dosya, HttpSession oturum) throws IOException {
        //MultipartFile yüklenen dosyayı alır
    	Map<String, String> yanit = new HashMap<>();

        // Kullanıcı girişli değilse yetkisiz hata döndür
        if (!kullaniciGirisliMi(oturum)) {
            yanit.put("mesaj", "Önce giriş yapınız.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(yanit);
        }
        // Dosya boşsa hata döndür
        if (dosya.isEmpty()) {
            yanit.put("mesaj", "Dosya boş!");
            return ResponseEntity.badRequest().body(yanit);
        }
        // Sadece PDF, PNG, JPG/JPEG dosya uzantılarını kabul et
        String uzanti = StringUtils.getFilenameExtension(dosya.getOriginalFilename());
        //dosyanı uzantısı alınır
        //ve kontrol ediliir jpg pdf png büyük küçük harf duyarlılığı yok
        if (uzanti == null || !(uzanti.equalsIgnoreCase("pdf") || uzanti.equalsIgnoreCase("png") || uzanti.equalsIgnoreCase("jpg") || uzanti.equalsIgnoreCase("jpeg"))) {
            yanit.put("mesaj", "Yalnızca PDF, PNG veya JPG dosyaları yükleyebilirsiniz.");
            return ResponseEntity.badRequest().body(yanit);
        }
        // uploads klasörü yoksa oluştur
        File klasor = new File(YUKLEME_KLASORU); //klasörün yolu
        if (!klasor.exists()) klasor.mkdirs(); //varsa bişey yapma yoksa oluştur

        // Dosya adını benzersiz yapmak için UUID ekle
        String benzersizDosyaAdi = UUID.randomUUID() + "_" + dosya.getOriginalFilename();

        // Dosyanın kaydedileceği yolu oluştur
        Path yol = Paths.get(YUKLEME_KLASORU, benzersizDosyaAdi);

        // Dosyayı belirtilen konuma kopyala
        Files.copy(dosya.getInputStream(), yol, StandardCopyOption.REPLACE_EXISTING);

        yanit.put("mesaj", "Dosya yüklendi!");
        return ResponseEntity.ok(yanit);
    }

    // Yüklenen dosyaları listeleyen endpoint
    @GetMapping("/liste")
    public ResponseEntity<?> dosyaListele(HttpSession oturum) {
        // Kullanıcı girişli değilse hata döndür
        if (!kullaniciGirisliMi(oturum)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Önce giriş yapınız.");
        }
        // uploads klasörü yoksa oluştur
        File klasor = new File(YUKLEME_KLASORU);
        if (!klasor.exists()) klasor.mkdirs();

        // uploads klasöründeki tüm dosyaları al
        File[] dosyalar = klasor.listFiles();
        List<String> dosyaAdlari = new ArrayList<>();
        if (dosyalar != null) {
            for (File dosya : dosyalar) dosyaAdlari.add(dosya.getName());
        }
        return ResponseEntity.ok(dosyaAdlari);
    }

    // Dosya silme endpoint'i
    @DeleteMapping("/sil/{dosyaAdi}")
    public ResponseEntity<Map<String, String>> dosyaSil(@PathVariable String dosyaAdi, HttpSession oturum) {
        Map<String, String> yanit = new HashMap<>();
        // Kullanıcı girişli değilse hata döndür
        if (!kullaniciGirisliMi(oturum)) {
            yanit.put("mesaj", "Önce giriş yapınız.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(yanit);
        }
        // Silinecek dosyanın yolunu belirle
        File dosya = new File(YUKLEME_KLASORU, dosyaAdi);
        // Dosya varsa sil, yoksa hata döndür
        if (dosya.exists() && dosya.delete()) {
            yanit.put("mesaj", "Dosya silindi!");
            return ResponseEntity.ok(yanit);
        } else {
            yanit.put("mesaj", "Dosya bulunamadı!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(yanit);
        }
    }
}