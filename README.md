# Project_Final_Fadjriaf
Final Project Fadjriaf VSGA DTS 2019 (Android Studio)

## Panduan Menggunakan
## Xampp
1. Download Xampp dan Install
2. Clone API Android MySQL
> git clone https://github.com/fadjriaf/Api-Android-MySQL
3. Pindahkan ke Folder htdocs dan buat folder namanya vsga
> C:\xampp\htdocs\vsga
4. Nyalakan Apache dan MySQL di Xampp lalu buka di browser
> localhost/phpmyadmin atau 127.0.0.1/phpmyadmin
5. Buat Database baru dengan nama `db_android`
6. Buka folder htdocs tadi dibuat lalu import database `db_android.sql`

## Android Studio
1. Download Android Studio dan Install
2. Buka Android Studio
3. Pilih Open Existing Project
4. Pilih Projectnya 
> Project_Final_Fadjriaf
5. Tunggu proses sync selesai
6. Buka `cmd` atau `command prompt` untuk user Windows
7. Ketikkan `ipconfig`
8. Salin Ipnya 
> IP Laptop yang terkoneksi ke Jaringan HP / Wifi
9. Pastekan IP Laptop yang tadi di salin ke dalam file
- MainActivity.java
  - searchEmployee
  - deleteEmployee
- MainAdd.java
  - apiAddEmployee
  - apiEditEmployee
> Contoh : http://iplaptopnya/foldernya/namafile.php
10. Jalankan atau Run Aplikasinya
