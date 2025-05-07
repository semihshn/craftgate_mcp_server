# MCP Craftgate Projesi

## Proje Nedir?

Bu proje, **Craftgate** ödeme altyapısı ile entegre çalışan bir **MCP (Model Context Protocol) sunucusudur**. Yani, ödeme işlemlerini sorgulamak, raporlamak ve yönetmek için bir arayüz sağlar. Proje, Spring Boot ile yazılmıştır ve Craftgate API'sine bağlanarak ödeme verilerini işler.

Kısaca:
- Craftgate ile yapılan ödemeleri sorgulamak ve raporlamak için kullanılır.
- Bir sunucu olarak çalışır, dışarıdan gelen istekleri işler.
- MCP protokolü ile uyumludur.

## Proje Yapısı

- `src/main/java/`: Java kaynak kodları burada.
- `src/main/resources/application.properties`: Konfigürasyon dosyası (API anahtarları burada tutulur).
- `build.gradle`: Projenin bağımlılıkları ve derleme ayarları.
- `build/libs/`: Derlenen JAR dosyası burada oluşur.

## Gereksinimler

- Java 17 veya üzeri
- Gradle (veya projenin içindeki `gradlew` scripti)
- İnternet bağlantısı (bağımlılıkları indirmek için)

## Adım Adım Kurulum ve Çalıştırma

### 1. Kaynak Kodunu İndir

Projeyi bir klasöre klonla veya indir.

### 2. API Anahtarlarını Ayarla

`src/main/resources/application.properties` dosyasında aşağıdaki satırları göreceksin:

```
craftgate.api-key=dummy-api-key
craftgate.secret-key=dummy-secret-key
craftgate.base-url=https://sandbox-api.craftgate.io
```

Gerçek anahtarlarını kullanacaksan, `dummy-api-key` ve `dummy-secret-key` yerine kendi Craftgate API anahtarlarını yazmalısın. (Test için dummy değerlerle bırakabilirsin.)

### 3. JAR Dosyasını Oluştur

Terminali aç ve proje klasörüne gir. Ardından aşağıdaki komutu çalıştır:

```
./gradlew clean build
```

- Windows kullanıyorsan: `gradlew.bat clean build`
- Bu komut, projenin derlenmesini ve çalıştırılabilir bir JAR dosyası oluşturulmasını sağlar.

Oluşan JAR dosyasını şu klasörde bulacaksın:

```
build/libs/mcp_craftgate-0.0.1-SNAPSHOT.jar
```

### 4. JAR Dosyasını Çalıştır

Terminalde aşağıdaki komutu kullanabilirsin:

```
java -jar build/libs/mcp_craftgate-0.0.1-SNAPSHOT.jar
```

Bu komut, MCP Craftgate sunucusunu başlatır.

### 5. MCP Konfigürasyon Dosyası Nasıl Olmalı?

MCP ortamında bu JAR dosyasını kullanmak için, konfigürasyon dosyanı aşağıdaki gibi ayarlamalısın. Özellikle `command` ve `args` alanlarına dikkat et:

```json
"craftgate-mcp": {
  "command": "java",
  "args": [
    "-Dspring.ai.mcp.server.stdio=true",
    "-jar",
    "/tam/yol/build/libs/mcp_craftgate-0.0.1-SNAPSHOT.jar"
  ]
}
```

- `command`: Çalıştırılacak ana komut (burada `java`).
- `args`: Komuta gönderilecek argümanlar. 
  - `-Dspring.ai.mcp.server.stdio=true` parametresi MCP ile uyumlu çalışması için gereklidir.
  - `-jar` ve ardından JAR dosyasının tam yolu gelmelidir.
- `/tam/yol/` kısmını kendi bilgisayarındaki gerçek dizin ile değiştirmen gerekir.

> MCP ortamında bu konfigürasyon ile Craftgate aracı otomatik olarak başlatılır ve kullanılabilir.

### 6. Sunucunun Çalıştığını Kontrol Et

Sunucu başlatıldıktan sonra, loglarda hata yoksa ve port çakışması yaşanmıyorsa, MCP ortamında bu aracı kullanabilirsin.

---

## Sıkça Sorulan Sorular

**S: API anahtarlarımı nereden alacağım?**  
C: Craftgate panelinden veya yöneticinden alabilirsin.

**S: JAR dosyasını başka bir yere taşırsam ne olur?**  
C: MCP konfigürasyon dosyasındaki JAR yolunu yeni konuma göre güncellemelisin.

**S: Bağımlılıklar otomatik mi iniyor?**  
C: Evet, Gradle ilk derlemede tüm bağımlılıkları otomatik indirir.

---

## Ekstra Bilgiler

- Proje Spring Boot ile yazılmıştır, yani klasik bir Java uygulaması gibi çalışır.
- Testler için `./gradlew test` komutunu kullanabilirsin.
- Loglar, `model-context-protocol/craftgate/starter-stdio-server/target/mcp-craftgate-stdio-server.log` dosyasına yazılır.

---

Tüm adımları takip ederek projenin JAR dosyasını oluşturabilir ve MCP ortamında kullanabilirsin. Takıldığın bir yer olursa, adım adım tekrar kontrol edebilir veya yardım isteyebilirsin! 