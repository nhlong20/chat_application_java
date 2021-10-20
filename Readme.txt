MSSV: 18120449
Họ tên: Nguyễn Hoàng Long
Email: 18120449@student.hcmus.edu.vn

---------------------------------------------------------------
HƯỚNG DẪN CHẠY BIÊN DỊCH NGUỒN BẰNG COMMAND LINE INTERFACE (CLI)
---------------------------------------------------------------
Lưu ý: 
- mã lệnh nằm sau ký hiệu "/18120449>"
- "/18120449>" là đường dẫn thư mục hiện tại

1.1 [SERVER] Chuyển đến thư mục chứa file build
/18120449> cd source/Chat_App_Server/production/Chat_App_Server
/production/Chat_App_Server> java cvfm ChatAppServer.jar ./META-INF/MANIFEST.MF server messages com

1.2 [SERVER] Chạy mã nguồn vừa tạo
/production/Chat_App_Server> java -jar ChatAppServer.jar

2.1 [CLIENT] Chuyển đến thư mục chứa file build
/18120449> cd source/Chat_App_Client/production/Chat_App_Client
/production/Chat_App_Client> java cvfm ChatAppClient.jar ./META-INF/MANIFEST.MF client messages com resources

2.2 [CLIENT] Chạy mã nguồn vừa tạo
/production/Chat_App_Client> java -jar ChatAppClient.jar

---------------------------------------------------------
HƯỚNG DẪN CHẠY MÃ NGUỒN BẰNG COMMAND LINE INTERFACE (CLI)
---------------------------------------------------------
Lưu ý: 
- mã lệnh nằm sau ký hiệu "/18120449>"
- "/18120449>" là đường dẫn thư mục hiện tại

1. Mở folder chứa tập tin jar
/18120449> cd jar

2.1 Mở server
/18120449> cd Chat_App_Server
/18120449/Chat_App_Server> java -jar Chat_App_Server.jar

2.2. Chạy server
Ấn nút để chạy Server, port mặc định Chat Server là 3000 và File Transfer Server 3004

3.1 Mở client
/18120449/Chat_App_Server> cd ..
/18120449> cd Chat_App_Client
/18120449/Chat_App_Client> java -jar Chat_App_Client.jar

3.2 Bắt dầu sử dụng chương trình
