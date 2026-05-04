//package client.ctrl;
//
//import client.service.CaClient;
//import client.service.NhanVienClient; // Cần thiết để khởi tạo CaClient
//import common.entity.Ca;
//import common.entity.NhanVien;
//import java.time.LocalDateTime;
//
///*
// *@description: Lớp Controller xử lý nghiệp vụ liên quan đến Ca làm việc
// *@author: Bao Nguyen
// *@Date: 10/31/2025
// *@version:      1.0
// */
//public class Ca_Ctrl {
//
//    // Khai báo các service (Dependency)
//    private CaClient caClient;
//    private NhanVienClient nhanVienClient; // Nếu cần các thao tác trực tiếp với NhanVien
//
//    /**
//     * Constructor sử dụng Dependency Injection để nhận các lớp service.
//     * Đây là cách khởi tạo đúng và linh hoạt nhất.
//     */
//    public Ca_Ctrl(NhanVienClient nhanVienClient) {
//        // Lưu NhanVienClient nếu cần dùng trực tiếp
//        this.nhanVienClient = nhanVienClient;
//
//        // Khởi tạo CaClient bằng cách truyền (Inject) NhanVienClient
//        this.caClient = new CaClient(nhanVienClient);
//    }
//
//    // -----------------------------------------------------------------------
//    //                   PHƯƠNG THỨC NGHIỆP VỤ 1: VÀO CA
//    // -----------------------------------------------------------------------
//
//    /**
//     * Xử lý nghiệp vụ khi nhân viên xác nhận vào ca.
//     * @param maNhanVien Mã nhân viên đăng nhập
//     * @param tongTienDauCa Số tiền mặt nhân viên nhập vào két
//     * @return Ca đối tượng Ca đã được lưu thành công, hoặc null nếu thất bại
//     */
//    public Ca xuLyVaoCa(String maNhanVien, double tongTienDauCa) {
//        // 1. Lấy thông tin NhanVien đầy đủ
//        NhanVien nhanVien = nhanVienClient.getNhanVienTheoMa(maNhanVien);
//
//        if (nhanVien == null) {
//            System.err.println("Lỗi: Không tìm thấy thông tin nhân viên.");
//            return null;
//        }
//
//        // 2. Gọi service để lưu bản ghi vào ca
//        Ca caMoi = caClient.themCaVao(tongTienDauCa, nhanVien);
//
//        return caMoi;
//    }
//
//    // -----------------------------------------------------------------------
//    //                 PHƯƠNG THỨC NGHIỆP VỤ 2: LẤY CA ĐANG MỞ
//    // -----------------------------------------------------------------------
//
//    /**
//     * Lấy ca làm việc hiện tại (chưa kết thúc) của một nhân viên.
//     * @param maNhanVien Mã nhân viên
//     * @return Ca đối tượng Ca đang mở, hoặc null.
//     */
//    public Ca layCaDangMo(String maNhanVien) {
//        // Gọi service để tra cứu
//        return caClient.getCaChuaKet(maNhanVien);
//    }
//
//    // -----------------------------------------------------------------------
//    //                 PHƯƠNG THỨC NGHIỆP VỤ 3: KẾT CA
//    // -----------------------------------------------------------------------
//
//    /**
//     * Xử lý nghiệp vụ kết thúc ca làm việc.
//     * @param maCa Mã ca cần kết thúc
//     * @param tongTienCuoiCa Số tiền mặt nhân viên bàn giao
//     * @return boolean true nếu kết thúc ca thành công
//     */
//    public boolean xuLyKetCa(String maCa, double tongTienCuoiCa) {
//        // TODO: (Thêm logic kiểm tra chênh lệch ở đây trước khi gọi service)
//
//        // 1. Gọi service để cập nhật thời gian và tiền cuối ca
//        return caClient.ketCa(maCa, tongTienCuoiCa);
//    }
//}