CREATE DATABASE QL_GAME
GO
USE QL_GAME
set dateformat dmy

CREATE TABLE MUSIC
(
	ID INT IDENTITY PRIMARY KEY,
    TENBAI VARCHAR(100),
    DUONGDAN VARCHAR(255)
)

create table NGUOIDUNG
(
	ID int identity(1,1) not null,
	TENDN varchar(25) unique,
	MATKHAUHASH char(255) not null,
	EMAIL varchar(255) unique,
	HOTEN nvarchar(255),
	NGAYSINH Date,
	GIOITINH nvarchar(5),
	DIEM int,
	constraint PK_NGUOIDUNG primary key (ID)  --băm sha256 
)

INSERT INTO NGUOIDUNG (TENDN, MATKHAUHASH, EMAIL, NGAYSINH, HOTEN, GIOITINH, DIEM)
VALUES	('Danh', CONVERT(VARCHAR(64), HASHBYTES('SHA2_256', '123'), 2),'danh@gmail.com','03/02/2003', N'Nguyễn Công Danh','Nam',0),
		('Phuong', CONVERT(VARCHAR(64), HASHBYTES('SHA2_256', '456'), 2),'phuong@gmail.com','2003-07-10', N'Nguyễn Thị Kim Phượng',N'Nữ',0),
		('Khoa', CONVERT(VARCHAR(64), HASHBYTES('SHA2_256', '789'), 2),'khoa@gmail.com','2003-09-12', N'Lư Trần Đăng Khoa','Nam',0),
		('Khang', CONVERT(VARCHAR(64), HASHBYTES('SHA2_256', '852'), 2),'khang@gmail.com','2003-10-05', N'Huỳnh Lê Bảo Khang','Nam',0),
		('Tra', CONVERT(VARCHAR(64), HASHBYTES('SHA2_256', '963'), 2),'tra@gmail.com','2003-12-27', N'Trà',N'Nữ',0)

INSERT INTO MUSIC VALUES
('Ethereal Vistas', 'D:\.OneDrive - Ho Chi Minh city University of Food Industry\Desktop\HK6\CNJAVA\DOAN\GAMEOANQUAN\MUSIC\ethereal-vistas-191254.wav'),
('Good Night', 'D:\.OneDrive - Ho Chi Minh city University of Food Industry\Desktop\HK6\CNJAVA\DOAN\GAMEOANQUAN\MUSIC\good-night-160166.wav'),
('Once In Paris', 'D:\.OneDrive - Ho Chi Minh city University of Food Industry\Desktop\HK6\CNJAVA\DOAN\GAMEOANQUAN\MUSIC\once-in-paris-168895.wav')

set dateformat dmy
select * from MUSIC

select * from NGUOIDUNG
delete from NGUOIDUNG