package org.example.uthteammatching;
import org.example.uthteammatching.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;


// Nếu lỗi  Query did not return a unique result: thì drop table và chạy lại

@Component
public class DataLocal implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) {

        if(!userRepository.existsByUsername("bang123")) {

            KeyHolder userKey = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO uth_user (chuyen_nganh, email, enabled, gioi_tinh, ho, pass, sdt, ten, username) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "Công nghệ phần mềm");
                ps.setString(2, "admin@example.com");
                ps.setBoolean(3, true);
                ps.setString(4, "Nam");
                ps.setString(5, "Admin");
                ps.setString(6, passwordEncoder.encode("123")); // Mã hoá
                ps.setString(7, "0123456433");
                ps.setString(8, "Admin");
                ps.setString(9, "admin123");
                return ps;
            }, userKey);
            Long userId = userKey.getKey().longValue();

            KeyHolder adminRoleKey = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO role (ten) VALUES (?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "ADMIN");
                return ps;
            }, adminRoleKey);

            KeyHolder userRoleKey = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO role (ten) VALUES (?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "USER");
                return ps;
            }, userRoleKey);

            Long roleIdUser = userRoleKey.getKey().longValue();
            Long roleIdAdmin = adminRoleKey.getKey().longValue();
            jdbcTemplate.update("INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)", userId, roleIdAdmin);


            // 1. Insert Users và lấy ID
            KeyHolder user1Key = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO uth_user (chuyen_nganh, email, enabled, gioi_tinh, ho, pass, sdt, ten, username) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "Công nghệ phần mềm");
                ps.setString(2, "bang@example.com");
                ps.setBoolean(3, true);
                ps.setString(4, "Nam");
                ps.setString(5, "Duy");
                ps.setString(6, passwordEncoder.encode("123")); // Mã hoá
                ps.setString(7, "0123456789");
                ps.setString(8, "Bằng");
                ps.setString(9, "bang123");
                return ps;
            }, user1Key);
            Long userId1 = user1Key.getKey().longValue();
            jdbcTemplate.update("INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)", userId1, roleIdUser);



            KeyHolder user2Key = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO uth_user (chuyen_nganh, email, enabled, gioi_tinh, ho, pass, sdt, ten, username) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "An toàn thông tin");
                ps.setString(2, "phi@example.com");
                ps.setBoolean(3, true);
                ps.setString(4, "Nam");
                ps.setString(5, "Hoàng");
                ps.setString(6, passwordEncoder.encode("123")); // Mã hoá
                ps.setString(7, "0585456789");
                ps.setString(8, "Phi");
                ps.setString(9, "phi123");
                return ps;
            }, user2Key);
            Long userId2 = user2Key.getKey().longValue();
            jdbcTemplate.update("INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)", userId2, roleIdUser);




            KeyHolder user3Key = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO uth_user (chuyen_nganh, email, enabled, gioi_tinh, ho, pass, sdt, ten, username) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "Hệ thống thông tin");
                ps.setString(2, "toai@example.com");
                ps.setBoolean(3, true);
                ps.setString(4, "Nam");
                ps.setString(5, "Anh");
                ps.setString(6, passwordEncoder.encode("123")); // Mã hoá
                ps.setString(7, "0181536789");
                ps.setString(8, "Toại");
                ps.setString(9, "toai123");
                return ps;
            }, user3Key);
            Long userId3 = user3Key.getKey().longValue();
            jdbcTemplate.update("INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)", userId3, roleIdUser);



            KeyHolder user4Key = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO uth_user (chuyen_nganh, email, enabled, gioi_tinh, ho, pass, sdt, ten, username) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "Công nghệ phần mềm");
                ps.setString(2, "thuc@example.com");
                ps.setBoolean(3, true);
                ps.setString(4, "Nam");
                ps.setString(5, "Tài");
                ps.setString(6, passwordEncoder.encode("123")); // Mã hoá
                ps.setString(7, "0465856789");
                ps.setString(8, "Thức");
                ps.setString(9, "thuc123");
                return ps;
            }, user4Key);
            Long userId4 = user4Key.getKey().longValue();
            jdbcTemplate.update("INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)", userId4, roleIdUser);



            KeyHolder user5Key = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO uth_user (chuyen_nganh, email, enabled, gioi_tinh, ho, pass, sdt, ten, username) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "Lập trình web");
                ps.setString(2, "tho@example.com");
                ps.setBoolean(3, true);
                ps.setString(4, "Nam");
                ps.setString(5, "Bão");
                ps.setString(6, passwordEncoder.encode("123")); // Mã hoá
                ps.setString(7, "0937256789");
                ps.setString(8, "Thọ");
                ps.setString(9, "tho123");
                return ps;
            }, user5Key);
            Long userId5 = user5Key.getKey().longValue();
            jdbcTemplate.update("INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)", userId5, roleIdUser);



            // 2. Insert Projects và lấy ID
            KeyHolder project1Key = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO project (mo_ta, ngay_bat_dau, ngay_ket_thuc, ngay_tao, ten_project, trang_thai) " +
                                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "Xây dựng hệ thống quản lý sinh viên");
                ps.setDate(2, Date.valueOf("2025-04-01"));
                ps.setDate(3, Date.valueOf("2025-5-01"));
                ps.setString(4, "Dự án quản lý sinh viên");
                ps.setString(5, "Đang thực hiện");
                return ps;
            }, project1Key);
            Long projectId1 = project1Key.getKey().longValue();


            KeyHolder project2Key = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO project (mo_ta, ngay_bat_dau, ngay_ket_thuc, ngay_tao, ten_project, trang_thai) " +
                                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "Hệ thống thu phí");
                ps.setDate(2, Date.valueOf("2025-04-9"));
                ps.setDate(3, Date.valueOf("2025-4-28"));
                ps.setString(4, "Dự án thu phí tự động");
                ps.setString(5, "Đang thực hiện");
                return ps;
            }, project2Key);
            Long projectId2 = project2Key.getKey().longValue();


            KeyHolder project3Key = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO project (mo_ta, ngay_bat_dau, ngay_ket_thuc, ngay_tao, ten_project, trang_thai) " +
                                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "Xây dựng hệ thống chăm sóc khách hàng");
                ps.setDate(2, Date.valueOf("2025-04-01"));
                ps.setDate(3, Date.valueOf("2025-4-25"));
                ps.setString(4, "Dự án chăm sóc khách hàng");
                ps.setString(5, "Đã hoàn thành");
                return ps;
            }, project3Key);
            Long projectId3 = project3Key.getKey().longValue();


            // 3. Insert BaiViet dùng ID thật
            jdbcTemplate.update("INSERT INTO bai_viet (noi_dung, ngay_dang, user_ma_so, project_ma_so) VALUES (?, CURRENT_TIMESTAMP, ?, ?)",
                    "Nội dung bài viết 1: Cập nhật tiến độ dự án quản lý sinh viên", userId1, projectId1);


            jdbcTemplate.update("INSERT INTO bai_viet (noi_dung, ngay_dang, user_ma_so, project_ma_so) VALUES (?, CURRENT_TIMESTAMP, ?, ?)",
                    "Nội dung bài viết 2: Tìm người làm phụ dựa án thu phí tự động", userId2, projectId2);


            jdbcTemplate.update("INSERT INTO bai_viet (noi_dung, ngay_dang, user_ma_so, project_ma_so) VALUES (?, CURRENT_TIMESTAMP, ?, ?)",
                    "Nội dung bài viết 3: Cập nhật tiến độ dự chăm sóc khách hàng", userId3, projectId3);


            jdbcTemplate.update(
                    "INSERT INTO thanhvien_project (user_ma_so, project_ma_so, vai_tro) VALUES (?, ?, ?)",
                    userId1, projectId1, "LEADER");
            jdbcTemplate.update(
                    "INSERT INTO thanhvien_project (user_ma_so, project_ma_so, vai_tro) VALUES (?, ?, ?)",
                    userId2, projectId1, "Thành viên");
            jdbcTemplate.update(
                    "INSERT INTO thanhvien_project (user_ma_so, project_ma_so, vai_tro) VALUES (?, ?, ?)",
                    userId3, projectId1, "Thành viên");
            jdbcTemplate.update(
                    "INSERT INTO thanhvien_project (user_ma_so, project_ma_so, vai_tro) VALUES (?, ?, ?)",
                    userId4, projectId1, "Thành viên");
            jdbcTemplate.update(
                    "INSERT INTO thanhvien_project (user_ma_so, project_ma_so, vai_tro) VALUES (?, ?, ?)",
                    userId5, projectId1, "Thành viên");

            jdbcTemplate.update(
                    "INSERT INTO thanhvien_project (user_ma_so, project_ma_so, vai_tro) VALUES (?, ?, ?)",
                    userId2, projectId2, "LEADER");

            jdbcTemplate.update(
                    "INSERT INTO thanhvien_project (user_ma_so, project_ma_so, vai_tro) VALUES (?, ?, ?)",
                    userId3, projectId3, "LEADER");


        }
    }
}