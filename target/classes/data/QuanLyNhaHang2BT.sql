
USE master;
GO

CREATE DATABASE QLNhaHang2BTCHECK;
GO

USE QLNhaHang2BTCHECK;
GO

------------------------------------------------------------
-- BẢNG: BanAn
------------------------------------------------------------
CREATE TABLE BanAn (
    maBan NVARCHAR(50) PRIMARY KEY,
    loai NVARCHAR(50),
    trangThai NVARCHAR(20),
    viTri NVARCHAR(100)
);
GO

------------------------------------------------------------
-- BẢNG: NhanVien
------------------------------------------------------------
CREATE TABLE NhanVien (
    maNhanVien NVARCHAR(100) PRIMARY KEY,
    tenNhanVien NVARCHAR(100),
    chucVu NVARCHAR(50),
    CCCD NVARCHAR(20),
    soDienThoai NVARCHAR(15),
    ngaySinh DATE,
    ngayVaoLam DATE,
    ngayThoiViec DATE
);
GO

------------------------------------------------------------
-- BẢNG: KhachHang
------------------------------------------------------------
CREATE TABLE KhachHang (
    maKhachHang NVARCHAR(100) PRIMARY KEY,
    tenKhachHang NVARCHAR(100),
    soDienThoai NVARCHAR(15),
    diemTichLuy DECIMAL(10,2) DEFAULT (0)
);
GO

------------------------------------------------------------
-- BẢNG: MonAn
------------------------------------------------------------
CREATE TABLE MonAn (
    maMonAn NVARCHAR(100) PRIMARY KEY,
    tenMonAn NVARCHAR(100),
    loaiMon NVARCHAR(50),
    giaTien DECIMAL(12,2),
    moTa NVARCHAR(MAX), 
	img NVARCHAR(50)
);
GO

------------------------------------------------------------
-- BẢNG: KhuyenMai
------------------------------------------------------------
CREATE TABLE KhuyenMai (
    maKhuyenMai NVARCHAR(100) PRIMARY KEY,
    tenKhuyenMai NVARCHAR(100),
    ngayBatDau DATE,
    ngayKetThuc DATE,
    dieuKienApDung DECIMAL(12,2),
    giaTriToiDa DECIMAL(12,2),
    giamGiaPhanTram BIT,
    giaTriGiam DECIMAL(12,2)
);
GO

------------------------------------------------------------
-- BẢNG: HoaDon
------------------------------------------------------------
CREATE TABLE HoaDon (
    maHoaDon NVARCHAR(100) PRIMARY KEY,
    ngayTao DATETIME,
    trangThai NVARCHAR(50),
    phuongThuc NVARCHAR(50),
    ghiChu NVARCHAR(MAX),
    maNhanVien NVARCHAR(100),
    maKhachHang NVARCHAR(100),
    FOREIGN KEY (maNhanVien) REFERENCES dbo.NhanVien(maNhanVien),
    FOREIGN KEY (maKhachHang) REFERENCES dbo.KhachHang(maKhachHang)
);
GO

------------------------------------------------------------
-- BẢNG: ChiTietHoaDon
------------------------------------------------------------
CREATE TABLE ChiTietHoaDon (
    maHoaDon NVARCHAR(100) NOT NULL,
    maMonAn NVARCHAR(100) NOT NULL,
    soLuong INT,
    PRIMARY KEY (maHoaDon, maMonAn),
    FOREIGN KEY (maHoaDon) REFERENCES dbo.HoaDon(maHoaDon),
    FOREIGN KEY (maMonAn) REFERENCES dbo.MonAn(maMonAn)
);
GO

------------------------------------------------------------
-- BẢNG: ChiTietKMHD
------------------------------------------------------------
CREATE TABLE ChiTietKMHD (
    maHoaDon NVARCHAR(100) NOT NULL,
    maKhuyenMai NVARCHAR(100) NOT NULL,
    soTienGiam DECIMAL(12,2),
    PRIMARY KEY (maHoaDon, maKhuyenMai),
    FOREIGN KEY (maHoaDon) REFERENCES dbo.HoaDon(maHoaDon),
    FOREIGN KEY (maKhuyenMai) REFERENCES dbo.KhuyenMai(maKhuyenMai)
);
GO

------------------------------------------------------------
-- BẢNG: ChiTietKMMonAn
------------------------------------------------------------
CREATE TABLE ChiTietKMMonAn (
    maMonAn NVARCHAR(100) NOT NULL,
    maKhuyenMai NVARCHAR(100) NOT NULL,
    giaSauKhuyenMai DECIMAL(12,2),
    PRIMARY KEY (maMonAn, maKhuyenMai),
    FOREIGN KEY (maMonAn) REFERENCES dbo.MonAn(maMonAn),
    FOREIGN KEY (maKhuyenMai) REFERENCES dbo.KhuyenMai(maKhuyenMai)
);
GO

------------------------------------------------------------
-- BẢNG: PhieuDatBan
------------------------------------------------------------
CREATE TABLE PhieuDatBan (
    maPhieu NVARCHAR(100) PRIMARY KEY,
    thoiGianBatDau DATETIME,
    trangThai NVARCHAR(50),
    soNguoi INT,
    ghiChu NVARCHAR(MAX),
    maKhachHang NVARCHAR(100),
    maBan NVARCHAR(50),
    maNhanVien NVARCHAR(100),
    maHoaDon NVARCHAR(100),
    FOREIGN KEY (maKhachHang) REFERENCES dbo.KhachHang(maKhachHang),
    FOREIGN KEY (maBan) REFERENCES dbo.BanAn(maBan),
    FOREIGN KEY (maNhanVien) REFERENCES dbo.NhanVien(maNhanVien),
    FOREIGN KEY (maHoaDon) REFERENCES dbo.HoaDon(maHoaDon)
);
GO

------------------------------------------------------------
-- BẢNG: TaiKhoan
------------------------------------------------------------
CREATE TABLE TaiKhoan (
    maTaiKhoan NVARCHAR(100) PRIMARY KEY,
    taiKhoan NVARCHAR(50) UNIQUE,
    matKhau NVARCHAR(255),
    taiKhoanQuanLi BIT,
    trangThaiHoatDong BIT,
    maNhanVien NVARCHAR(100),
    FOREIGN KEY (maNhanVien) REFERENCES dbo.NhanVien(maNhanVien)
);
GO

INSERT INTO BanAn (maBan, loai, trangThai, viTri) VALUES
('BA001', N'Thường', N'Trống', N'Tầng 1 - Góc A1'),
('BA002', N'Thường', N'Trống', N'Tầng 1 - Góc A2'),
('BA003', N'Thường', N'Đang dùng', N'Tầng 1 - Góc A3'),
('BA004', N'VIP', N'Trống', N'Tầng 2 - Phòng VIP 1'),
('BA005', N'VIP', N'Đang đặt', N'Tầng 2 - Phòng VIP 2'),
('BA006', N'Thường', N'Trống', N'Tầng 1 - Góc B1'),
('BA007', N'Thường', N'Trống', N'Tầng 1 - Góc B2'),
('BA008', N'VIP', N'Trống', N'Tầng 2 - Phòng VIP 3'),
('BA009', N'Thường', N'Đang dùng', N'Tầng 1 - Góc C1'),
('BA010', N'Thường', N'Trống', N'Tầng 1 - Góc C2');
GO


INSERT INTO NhanVien (maNhanVien, tenNhanVien, chucVu, CCCD, soDienThoai, ngaySinh, ngayVaoLam, ngayThoiViec) VALUES
('NV001', N'Nguyễn Văn An', N'Quản lý', '012345678901', '0901000001', '1990-05-10', '2020-01-05', NULL),
('NV002', N'Lê Thị Bình', N'Nhân viên phục vụ', '012345678902', '0901000002', '1995-02-20', '2021-03-15', NULL),
('NV003', N'Trần Quốc Cường', N'Nhân viên phục vụ', '012345678903', '0901000003', '1996-11-12', '2021-07-20', NULL),
('NV004', N'Phạm Thị Dung', N'Thu ngân', '012345678904', '0901000004', '1998-09-30', '2022-05-01', NULL),
('NV005', N'Hoàng Minh Đức', N'Đầu bếp', '012345678905', '0901000005', '1988-01-14', '2019-10-10', NULL),
('NV006', N'Vũ Thị Hà', N'Phụ bếp', '012345678906', '0901000006', '1999-04-18', '2022-09-01', NULL),
('NV007', N'Nguyễn Đức Long', N'Phục vụ', '012345678907', '0901000007', '1997-03-25', '2021-08-15', NULL),
('NV008', N'Trần Thu Lan', N'Lễ tân', '012345678908', '0901000008', '1994-12-12', '2020-12-01', NULL),
('NV009', N'Đinh Văn Minh', N'Phục vụ', '012345678909', '0901000009', '1998-07-07', '2021-10-10', NULL),
('NV010', N'Bùi Thị Hạnh', N'Thu ngân', '012345678910', '0901000010', '1993-06-22', '2020-04-04', NULL);
GO

INSERT INTO KhachHang (maKhachHang, tenKhachHang, soDienThoai, diemTichLuy) VALUES
('KH001', N'Nguyễn Thị Mai', '0912000001', 150),
('KH002', N'Trần Văn Nam', '0912000002', 200),
('KH003', N'Lê Thị Hoa', '0912000003', 300),
('KH004', N'Phạm Quốc Bảo', '0912000004', 120),
('KH005', N'Đỗ Thị Thảo', '0912000005', 400),
('KH006', N'Nguyễn Văn Bình', '0912000006', 180),
('KH007', N'Hoàng Anh Tuấn', '0912000007', 350),
('KH008', N'Đinh Thị Hằng', '0912000008', 260),
('KH009', N'Phan Văn Dũng', '0912000009', 90),
('KH010', N'Trần Thị Ngọc', '0912000010', 500);
GO

INSERT INTO MonAn (maMonAn, tenMonAn, loaiMon, giaTien, moTa, img) VALUES
--  Món ăn kèm
('MA001', N'Bánh mì bơ tỏi', N'Món ăn kèm', 45000, N'Món món ăn kèm ngon miệng, trình bày đẹp mắt', '/img/mon/ankem/banhmibotoi.jpg'),
('MA002', N'Bắp nướng', N'Món ăn kèm', 50000, N'Món món ăn kèm ngon miệng, trình bày đẹp mắt', '/img/mon/ankem/bapnuong.jpg'),
('MA003', N'Salad caesar', N'Món ăn kèm', 55000, N'Món món ăn kèm ngon miệng, trình bày đẹp mắt', '/img/mon/ankem/caesar.jpg'),
('MA004', N'Cơm trắng', N'Món ăn kèm', 60000, N'Món món ăn kèm ngon miệng, trình bày đẹp mắt', '/img/mon/ankem/com.jpg'),
('MA005', N'Khoai tây chiên', N'Món ăn kèm', 65000, N'Món món ăn kèm ngon miệng, trình bày đẹp mắt', '/img/mon/ankem/khoaitaychien.jpg'),
('MA006', N'Khoai tây nghiền', N'Món ăn kèm', 45000, N'Món món ăn kèm ngon miệng, trình bày đẹp mắt', '/img/mon/ankem/khoaitaynghien.jpg'),
('MA007', N'Khoai tây nướng', N'Món ăn kèm', 50000, N'Món món ăn kèm ngon miệng, trình bày đẹp mắt', '/img/mon/ankem/khoaitaynuong.jpg'),
('MA008', N'Nấm xào', N'Món ăn kèm', 55000, N'Món món ăn kèm ngon miệng, trình bày đẹp mắt', '/img/mon/ankem/namxao.jpg'),
('MA009', N'Salad bắp cải', N'Món ăn kèm', 60000, N'Món món ăn kèm ngon miệng, trình bày đẹp mắt', '/img/mon/ankem/saladbapcai.jpg'),

--  Món khai vị
('MA010', N'Bánh mì bruschetta', N'Món khai vị', 65000, N'Món món khai vị ngon miệng, trình bày đẹp mắt', '/img/mon/khaivi/banhmibruschetta.jpg'),
('MA011', N'Cánh gà chiên nước mắm', N'Món khai vị', 45000, N'Món món khai vị ngon miệng, trình bày đẹp mắt', '/img/mon/khaivi/canhgachiennuocmam.jpg'),
('MA012', N'Chả giò', N'Món khai vị', 50000, N'Món món khai vị ngon miệng, trình bày đẹp mắt', '/img/mon/khaivi/chagio.jpg'),
('MA013', N'Há cảo chiên', N'Món khai vị', 55000, N'Món món khai vị ngon miệng, trình bày đẹp mắt', '/img/mon/khaivi/hacaochien.jpg'),
('MA014', N'Nêm rán', N'Món khai vị', 60000, N'Món món khai vị ngon miệng, trình bày đẹp mắt', '/img/mon/khaivi/nemran.jpg'),
('MA015', N'Phô mai viên', N'Món khai vị', 65000, N'Món món khai vị ngon miệng, trình bày đẹp mắt', '/img/mon/khaivi/phomaivien.jpg'),
('MA016', N'Salad', N'Món khai vị', 45000, N'Món món khai vị ngon miệng, trình bày đẹp mắt', '/img/mon/khaivi/salad.jpg'),
('MA017', N'Samosa', N'Món khai vị', 50000, N'Món món khai vị ngon miệng, trình bày đẹp mắt', '/img/mon/khaivi/samosa.jpg'),
('MA018', N'Tart khai vị', N'Món khai vị', 55000, N'Món món khai vị ngon miệng, trình bày đẹp mắt', '/img/mon/khaivi/tart.jpg'),
('MA019', N'Tôm cocktail', N'Món khai vị', 60000, N'Món món khai vị ngon miệng, trình bày đẹp mắt', '/img/mon/khaivi/tomcotail.jpg'),

--  Món chính
('MA020', N'Bò bít tết', N'Món chính', 65000, N'Món món chính ngon miệng, trình bày đẹp mắt', '/img/mon/monchinh/bobittet.jpg'),
('MA021', N'Cá hồi nướng', N'Món chính', 45000, N'Món món chính ngon miệng, trình bày đẹp mắt', '/img/mon/monchinh/cahoinuong.jpg'),
('MA022', N'Cà ri gà', N'Món chính', 50000, N'Món món chính ngon miệng, trình bày đẹp mắt', '/img/mon/monchinh/cariga.jpg'),
('MA023', N'Cơm chiên trứng', N'Món chính', 55000, N'Món món chính ngon miệng, trình bày đẹp mắt', '/img/mon/monchinh/comchientrung.jpg'),
('MA024', N'Cơm gà', N'Món chính', 60000, N'Món món chính ngon miệng, trình bày đẹp mắt', '/img/mon/monchinh/comga.jpg'),
('MA025', N'Cơm sườn', N'Món chính', 65000, N'Món món chính ngon miệng, trình bày đẹp mắt', '/img/mon/monchinh/comsuon.jpg'),
('MA026', N'Cừu nướng', N'Món chính', 45000, N'Món món chính ngon miệng, trình bày đẹp mắt', '/img/mon/monchinh/cuunuong.jpg'),
('MA027', N'Gà nướng', N'Món chính', 50000, N'Món món chính ngon miệng, trình bày đẹp mắt', '/img/mon/monchinh/ganuong.jpg'),
('MA028', N'Gỏi đậu hũ', N'Món chính', 55000, N'Món món chính ngon miệng, trình bày đẹp mắt', '/img/mon/monchinh/goidauhu.jpg'),
('MA029', N'Mì ý', N'Món chính', 60000, N'Món món chính ngon miệng, trình bày đẹp mắt', '/img/mon/monchinh/myy.jpg'),
('MA030', N'Phở', N'Món chính', 65000, N'Món món chính ngon miệng, trình bày đẹp mắt', '/img/mon/monchinh/pho.jpg'),
('MA031', N'Spaghetti', N'Món chính', 45000, N'Món món chính ngon miệng, trình bày đẹp mắt', '/img/mon/monchinh/spaghetti.jpg'),
('MA032', N'Sườn nướng', N'Món chính', 50000, N'Món món chính ngon miệng, trình bày đẹp mắt', '/img/mon/monchinh/suonnuong.jpg'),
('MA033', N'Teriyaki', N'Món chính', 55000, N'Món món chính ngon miệng, trình bày đẹp mắt', '/img/mon/monchinh/teriyaki.jpg'),

--  Nước sốt
('MA034', N'Nước tương', N'Nước sốt', 60000, N'Món nước sốt ngon miệng, trình bày đẹp mắt', '/img/mon/nuocsot/nuoctuong.jpg'),
('MA035', N'Sốt BBQ', N'Nước sốt', 65000, N'Món nước sốt ngon miệng, trình bày đẹp mắt', '/img/mon/nuocsot/sotbbq.jpg'),
('MA036', N'Sốt bơ tỏi', N'Nước sốt', 45000, N'Món nước sốt ngon miệng, trình bày đẹp mắt', '/img/mon/nuocsot/sotbotoi.jpg'),
('MA037', N'Sốt cà chua', N'Nước sốt', 50000, N'Món nước sốt ngon miệng, trình bày đẹp mắt', '/img/mon/nuocsot/sotcachua.jpg'),
('MA038', N'Sốt pesto', N'Nước sốt', 55000, N'Món nước sốt ngon miệng, trình bày đẹp mắt', '/img/mon/nuocsot/sotpesto.jpg'),
('MA039', N'Sốt phô mai', N'Nước sốt', 60000, N'Món nước sốt ngon miệng, trình bày đẹp mắt', '/img/mon/nuocsot/sotphomai.jpg'),

--  Đồ uống
('MA040', N'Bia', N'Đồ uống', 65000, N'Món đồ uống ngon miệng, trình bày đẹp mắt', '/img/mon/nuocuong/bia.jpg'),
('MA041', N'Cà phê sữa', N'Đồ uống', 45000, N'Món đồ uống ngon miệng, trình bày đẹp mắt', '/img/mon/nuocuong/caphesua.jpg'),
('MA042', N'Cocktail', N'Đồ uống', 50000, N'Món đồ uống ngon miệng, trình bày đẹp mắt', '/img/mon/nuocuong/coctail.jpg'),
('MA043', N'Cocktail trái cây', N'Đồ uống', 55000, N'Món đồ uống ngon miệng, trình bày đẹp mắt', '/img/mon/nuocuong/coctailtraicay.jpg'),
('MA044', N'Nước chanh', N'Đồ uống', 60000, N'Món đồ uống ngon miệng, trình bày đẹp mắt', '/img/mon/nuocuong/nuocchanh.jpg'),
('MA045', N'Nước chanh tươi', N'Đồ uống', 65000, N'Món đồ uống ngon miệng, trình bày đẹp mắt', '/img/mon/nuocuong/nuocchanhtuoi.jpg'),
('MA046', N'Nước kiwi', N'Đồ uống', 45000, N'Món đồ uống ngon miệng, trình bày đẹp mắt', '/img/mon/nuocuong/nuockiwi.jpg'),
('MA047', N'Nước lọc', N'Đồ uống', 50000, N'Món đồ uống ngon miệng, trình bày đẹp mắt', '/img/mon/nuocuong/nuocloc.jpg'),
('MA048', N'Trà sữa', N'Đồ uống', 55000, N'Món đồ uống ngon miệng, trình bày đẹp mắt', '/img/mon/nuocuong/trasua.jpg'),
('MA049', N'Volka', N'Đồ uống', 60000, N'Món đồ uống ngon miệng, trình bày đẹp mắt', '/img/mon/nuocuong/volka.jpg'),

--  Tráng miệng
('MA050', N'Brownie', N'Tráng miệng', 65000, N'Món tráng miệng ngon miệng, trình bày đẹp mắt', '/img/mon/trangmieng/brownie.jpg');
GO



INSERT INTO KhuyenMai (maKhuyenMai, tenKhuyenMai, ngayBatDau, ngayKetThuc, dieuKienApDung, giaTriToiDa, giamGiaPhanTram, giaTriGiam) VALUES
('KM001', N'Giảm 10% hóa đơn trên 200k', '2025-10-01', '2025-10-31', 200000, 100000, 1, 10),
('KM002', N'Giảm 20k cho khách mới', '2025-09-01', '2025-12-31', 0, 20000, 0, 20000),
('KM003', N'Combo món chính + nước', '2025-10-15', '2025-11-15', 100000, 30000, 0, 30000),
('KM004', N'Giảm 15% sinh nhật', '2025-01-01', '2025-12-31', 0, 150000, 1, 15),
('KM005', N'Giảm 5% toàn menu', '2025-10-20', '2025-10-31', 0, 50000, 1, 5),
('KM006', N'Giảm 50k cho đơn trên 500k', '2025-10-01', '2025-12-31', 500000, 50000, 0, 50000),
('KM007', N'Giảm 30% món tráng miệng', '2025-10-10', '2025-11-10', 0, 40000, 1, 30),
('KM008', N'Giảm 10k đồ uống', '2025-10-01', '2025-12-31', 0, 10000, 0, 10000),
('KM009', N'Lễ hội ẩm thực', '2025-09-15', '2025-11-15', 0, 80000, 1, 10),
('KM010', N'Giảm 20% cho bàn VIP', '2025-10-05', '2025-11-05', 0, 200000, 1, 20);
GO


INSERT INTO HoaDon (maHoaDon, ngayTao, trangThai, phuongThuc, ghiChu, maNhanVien, maKhachHang) VALUES
('HD001', '2025-10-20 18:30', N'Đã thanh toán', N'Tiền mặt', N'Không ghi chú', 'NV004', 'KH001'),
('HD002', '2025-10-20 19:00', N'Đã thanh toán', N'Chuyển khoản', N'Có khuyến mãi', 'NV010', 'KH002'),
('HD003', '2025-10-21 12:15', N'Chưa thanh toán', N'Tiền mặt', N'Khách đặt trước', 'NV004', 'KH003'),
('HD004', '2025-10-21 19:40', N'Đã thanh toán', N'Tiền mặt', N'Khách quen', 'NV010', 'KH004'),
('HD005', '2025-10-22 20:00', N'Đã thanh toán', N'Tiền mặt', N'Bàn VIP', 'NV004', 'KH005'),
('HD006', '2025-10-22 18:00', N'Hủy', N'Chuyển khoản', N'Khách không đến', 'NV004', 'KH006'),
('HD007', '2025-10-23 11:30', N'Chưa thanh toán', N'Tiền mặt', N'Tạo mới', 'NV010', 'KH007'),
('HD008', '2025-10-23 13:45', N'Đã thanh toán', N'Tiền mặt', N'Đặt tại quán', 'NV004', 'KH008'),
('HD009', '2025-10-23 18:00', N'Đã thanh toán', N'Tiền mặt', N'Khách quen', 'NV010', 'KH009'),
('HD010', '2025-10-23 19:15', N'Đã thanh toán', N'Chuyển khoản', N'Có khuyến mãi', 'NV004', 'KH010');
GO


INSERT INTO ChiTietHoaDon (maHoaDon, maMonAn, soLuong) VALUES
('HD001', 'MA001', 2),
('HD001', 'MA008', 2),
('HD002', 'MA002', 1),
('HD002', 'MA010', 2),
('HD003', 'MA006', 1),
('HD004', 'MA003', 2),
('HD005', 'MA004', 3),
('HD006', 'MA005', 2),
('HD007', 'MA007', 1),
('HD008', 'MA009', 2);
GO


INSERT INTO ChiTietKMHD (maHoaDon, maKhuyenMai, soTienGiam) VALUES
('HD001', 'KM001', 20000),
('HD002', 'KM004', 30000),
('HD005', 'KM010', 40000),
('HD008', 'KM005', 15000),
('HD010', 'KM009', 25000);
GO


INSERT INTO ChiTietKMMonAn (maMonAn, maKhuyenMai, giaSauKhuyenMai) VALUES
('MA010', 'KM007', 17500),
('MA008', 'KM008', 25000),
('MA006', 'KM009', 162000),
('MA002', 'KM001', 76500),
('MA001', 'KM005', 42750),
('MA005', 'KM009', 36000),
('MA009', 'KM008', 30000),
('MA004', 'KM005', 52250),
('MA007', 'KM010', 44000),
('MA003', 'KM005', 61750);
GO


INSERT INTO PhieuDatBan (maPhieu, thoiGianBatDau, trangThai, soNguoi, ghiChu, maKhachHang, maBan, maNhanVien, maHoaDon) VALUES
('PDB-20251023-001', '2025-10-23 17:30', N'Đã đặt', 4, N'Khách đặt trước', 'KH001', 'BA005', 'NV002', 'HD003'),
('PDB-20251023-002', '2025-10-23 18:00', N'Đã dùng', 2, N'Dùng ngay', 'KH004', 'BA003', 'NV007', 'HD004'),
('PDB-20251023-003', '2025-10-23 19:00', N'Đang dùng', 6, N'Dùng ngay', 'KH005', 'BA004', 'NV002', 'HD005'),
('PDB-20251023-004', '2025-10-23 12:00', N'Đã đặt', 3, N'Dùng ngay', 'KH006', 'BA002', 'NV008', NULL),
('PDB-20251023-005', '2025-10-23 13:30', N'Đã dùng', 5, N'Dùng ngay', 'KH007', 'BA009', 'NV009', 'HD008'),
('PDB-20251023-006', '2025-10-23 11:00', N'Hủy', 4, N'Dùng ngay', 'KH009', 'BA001', 'NV007', 'HD006'),
('PDB-20251023-007', '2025-10-23 14:00', N'Đã đặt', 2, N'Khách đặt trước', 'KH010', 'BA008', 'NV008', NULL),
('PDB-20251023-008', '2025-10-23 19:30', N'Đang dùng', 3, N'Khách đặt trước', 'KH002', 'BA009', 'NV002', 'HD009'),
('PDB-20251023-009', '2025-10-23 15:00', N'Đã đặt', 2, N'Khách đặt trước', 'KH003', 'BA006', 'NV009', NULL),
('PDB-20251023-010', '2025-10-23 20:00', N'Đang dùng', 6, N'Khách đặt trước', 'KH008', 'BA010', 'NV002', 'HD010');
GO



INSERT INTO TaiKhoan (maTaiKhoan, taiKhoan, matKhau, taiKhoanQuanLi, trangThaiHoatDong, maNhanVien) VALUES
('TK001', 'admin', '123456', 1, 1, 'NV001'),
('TK002', 'binhlt', '123456', 0, 1, 'NV002'),
('TK003', 'cuongtq', '123456', 0, 1, 'NV003'),
('TK004', 'dungpt', '123456', 0, 1, 'NV004'),
('TK005', 'duchm', '123456', 0, 1, 'NV005'),
('TK006', 'havu', '123456', 0, 1, 'NV006'),
('TK007', 'longnd', '123456', 0, 1, 'NV007'),
('TK008', 'lantr', '123456', 0, 1, 'NV008'),
('TK009', 'minhdv', '123456', 0, 1, 'NV009'),
('TK010', 'hanhb', '123456', 0, 1, 'NV010');
GO

