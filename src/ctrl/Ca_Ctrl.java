//package ctrl;
//
//import dao.Ca_DAO;
//import dao.NhanVien_DAO; // Cần thiết để khởi tạo Ca_DAO
//import entity.Ca;
//import entity.NhanVien;
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
//    // Khai báo các DAO (Dependency)
//    private Ca_DAO caDAO;
//    private NhanVien_DAO nvDAO; // Nếu cần các thao tác trực tiếp với NhanVien
//
//    /**
//     * Constructor sử dụng Dependency Injection để nhận các lớp DAO.
//     * Đây là cách khởi tạo đúng và linh hoạt nhất.
//     */
//    public Ca_Ctrl(NhanVien_DAO nvDAO) {
//        // Lưu NhanVien_DAO nếu cần dùng trực tiếp
//        this.nvDAO = nvDAO;
//
//        // Khởi tạo Ca_DAO bằng cách truyền (Inject) NhanVien_DAO
//        this.caDAO = new Ca_DAO(nvDAO);
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
//        NhanVien nhanVien = nvDAO.getNhanVienTheoMa(maNhanVien);
//
//        if (nhanVien == null) {
//            System.err.println("Lỗi: Không tìm thấy thông tin nhân viên.");
//            return null;
//        }
//
//        // 2. Gọi DAO để lưu bản ghi vào ca
//        Ca caMoi = caDAO.themCaVao(tongTienDauCa, nhanVien);
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
//        // Gọi DAO để tra cứu
//        return caDAO.getCaChuaKet(maNhanVien);
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
//        // TODO: (Thêm logic kiểm tra chênh lệch ở đây trước khi gọi DAO)
//
//        // 1. Gọi DAO để cập nhật thời gian và tiền cuối ca
//        return caDAO.ketCa(maCa, tongTienCuoiCa);
//    }
//}