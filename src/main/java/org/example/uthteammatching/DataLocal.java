package org.example.uthteammatching;
import org.example.uthteammatching.models.ProjectType;
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
public class    DataLocal implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) {

        if(!userRepository.existsByUsername("admin123")) {

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

            KeyHolder gvRoleKey = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO role (ten) VALUES (?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "LECTURE");
                return ps;
            }, gvRoleKey);

            Long roleIdUser = userRoleKey.getKey().longValue();
            Long roleIdAdmin = adminRoleKey.getKey().longValue();
            Long roleIdGv = gvRoleKey.getKey().longValue();


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
            jdbcTemplate.update("INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)", userId, roleIdAdmin);


            // 1. Insert Users và lấy ID
            KeyHolder user1Key = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO uth_user (chuyen_nganh, email, enabled, gioi_tinh, ho, pass, sdt, ten, username, avatar) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "Công nghệ phần mềm");
                ps.setString(2, "bang@example.com");
                ps.setBoolean(3, true);
                ps.setString(4, "Nam");
                ps.setString(5, "Duy");
                ps.setString(6, passwordEncoder.encode("123")); // Mã hoá
                ps.setString(7, "0123429789");
                ps.setString(8, "Bằng");
                ps.setString(9, "bang123");
                ps.setString(10, "/img/avatars/1744609252130_user1.jpg");
                return ps;
            }, user1Key);
            Long userId1 = user1Key.getKey().longValue();
            jdbcTemplate.update("INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)", userId1, roleIdUser);



            KeyHolder user2Key = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO uth_user (chuyen_nganh, email, enabled, gioi_tinh, ho, pass, sdt, ten, username, avatar) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
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
                ps.setString(10, "/img/avatars/1745154277727_user2.jpg");
                return ps;
            }, user2Key);
            Long userId2 = user2Key.getKey().longValue();
            jdbcTemplate.update("INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)", userId2, roleIdUser);




            KeyHolder user3Key = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO uth_user (chuyen_nganh, email, enabled, gioi_tinh, ho, pass, sdt, ten, username, avatar) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
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
                ps.setString(10, "/img/avatars/1744618978327_user3.jpg");
                return ps;
            }, user3Key);
            Long userId3 = user3Key.getKey().longValue();
            jdbcTemplate.update("INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)", userId3, roleIdUser);




            KeyHolder user4Key = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO uth_user (chuyen_nganh, email, enabled, gioi_tinh, ho, pass, sdt, ten, username, avatar) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
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
                ps.setString(10, "/img/avatars/1744608670490_user4.jpg");
                return ps;
            }, user4Key);
            Long userId4 = user4Key.getKey().longValue();
            jdbcTemplate.update("INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)", userId4, roleIdUser);



            KeyHolder gvKey = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO uth_user (chuyen_nganh, email, enabled, gioi_tinh, ho, pass, sdt, ten, username) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "Lập trình java");
                ps.setString(2, "chien@example.com");
                ps.setBoolean(3, true);
                ps.setString(4, "Nam");
                ps.setString(5, "Văn");
                ps.setString(6, passwordEncoder.encode("123")); // Mã hoá
                ps.setString(7, "0937294789");
                ps.setString(8, "Chiến");
                ps.setString(9, "chien123");
                return ps;
            }, gvKey);
            Long gvId = gvKey.getKey().longValue();
            jdbcTemplate.update("INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)", gvId, roleIdGv);


            jdbcTemplate.update("INSERT INTO list_friend(user_id_1, user_id_2) values (?, ?)", userId1, userId2);
            jdbcTemplate.update("INSERT INTO list_friend(user_id_1, user_id_2) values (?, ?)", userId1, userId3);
            jdbcTemplate.update("INSERT INTO list_friend(user_id_1, user_id_2) values (?, ?)", userId1, userId4);
            jdbcTemplate.update("INSERT INTO list_friend(user_id_1, user_id_2) values (?, ?)", userId2, userId3);
            jdbcTemplate.update("INSERT INTO list_friend(user_id_1, user_id_2) values (?, ?)", userId2, userId4);
            jdbcTemplate.update("INSERT INTO list_friend(user_id_1, user_id_2) values (?, ?)", userId3, userId4);
            jdbcTemplate.update("INSERT INTO list_friend(user_id_1, user_id_2) values (?, ?)", gvId, userId1);
            jdbcTemplate.update("INSERT INTO list_friend(user_id_1, user_id_2) values (?, ?)", gvId, userId2);
            jdbcTemplate.update("INSERT INTO list_friend(user_id_1, user_id_2) values (?, ?)", gvId, userId3);
            jdbcTemplate.update("INSERT INTO list_friend(user_id_1, user_id_2) values (?, ?)", gvId, userId4);


            // 2. Insert Projects và lấy ID
            KeyHolder project1Key = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO project (mo_ta, ngay_bat_dau, ngay_ket_thuc, ngay_tao, ten_project, trang_thai, loai, ma_giang_vien) " +
                                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "Dự án UTH Team Matching làm một trang web với công nghệ java spring boot giúp tạo một môi trường để sinh viên trao đổi, tìm kiếm thành viên cho các dự án");
                ps.setDate(2, Date.valueOf("2025-03-01"));
                ps.setDate(3, Date.valueOf("2025-05-01"));
                ps.setString(4, "Dự án UTH Team Matching");
                ps.setString(5, "Đang thực hiện");
                ps.setString(6, String.valueOf(ProjectType.HOC_THUAT));
                ps.setLong(7, gvId);
                return ps;
            }, project1Key);
            Long projectId1 = project1Key.getKey().longValue();


            KeyHolder project2Key = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO project (mo_ta, ngay_bat_dau, ngay_ket_thuc, ngay_tao, ten_project, trang_thai, loai) " +
                                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "Xây dựng một trang web giúp khách hàng đặt lịch để kiểm tra sức khoẻ, chăm sóc cho thú cưng");
                ps.setDate(2, Date.valueOf("2025-04-9"));
                ps.setDate(3, Date.valueOf("2025-4-28"));
                ps.setString(4, "Dự án chăm sóc thú cưng");
                ps.setString(5, "Đang thực hiện");
                ps.setString(6, String.valueOf(ProjectType.BEN_NGOAI));
                return ps;
            }, project2Key);
            Long projectId2 = project2Key.getKey().longValue();


            KeyHolder project3Key = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO project (mo_ta, ngay_bat_dau, ngay_ket_thuc, ngay_tao, ten_project, trang_thai, loai) " +
                                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, "Xây dựng hệ thống đặt phòng trực tuyến để khách hàng có thể checkin và thanh toán trực tiếp, giúp tiết kiệm thời gian đặt phòng khi du lịch");
                ps.setDate(2, Date.valueOf("2025-04-01"));
                ps.setDate(3, Date.valueOf("2025-4-25"));
                ps.setString(4, "Dự án đặt phòng khách sạn");
                ps.setString(5, "Hoàn thành");
                ps.setString(6, String.valueOf(ProjectType.BEN_NGOAI));
                return ps;
            }, project3Key);
            Long projectId3 = project3Key.getKey().longValue();

            jdbcTemplate.update("INSERT INTO chat_groups(group_id, group_name) VALUES (?, ?)",
                    project1Key.getKey().longValue(), "Dự án UTH Team Matching");

            jdbcTemplate.update("INSERT INTO chat_groups(group_id, group_name) VALUES (?, ?)",
                    project2Key.getKey().longValue(), "Dự án chăm sóc thú cưng");

            jdbcTemplate.update("INSERT INTO chat_groups(group_id, group_name) VALUES (?, ?)",
                    project3Key.getKey().longValue(), "Dự án đặt phòng khách sạn");


            // 3. Insert BaiViet dùng ID thật
            jdbcTemplate.update("INSERT INTO bai_viet (noi_dung, ngay_dang, user_ma_so, project_ma_so) VALUES (?, CURRENT_TIMESTAMP, ?, ?)",
                    "Nội dung bài viết 1: Chia sẻ dự án UTH Team Matching của nhóm", userId1, projectId1);


            jdbcTemplate.update("INSERT INTO bai_viet (noi_dung, ngay_dang, user_ma_so, project_ma_so) VALUES (?, CURRENT_TIMESTAMP, ?, ?)",
                    "Nội dung bài viết 2: Tìm người làm phụ dự án chăm sóc thú cưng", userId2, projectId2);


            jdbcTemplate.update("INSERT INTO bai_viet (noi_dung, ngay_dang, user_ma_so, project_ma_so) VALUES (?, CURRENT_TIMESTAMP, ?, ?)",
                    "Nội dung bài viết 3: Tìm kiếm thành viên tham gia làm dự án đặt phòng khách sạn", userId3, projectId3);


            jdbcTemplate.update(
                    "INSERT INTO thanhvien_project (user_ma_so, project_ma_so, vai_tro) VALUES (?, ?, ?)",
                    userId1, projectId1, "Trưởng nhóm");
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
                    gvId, projectId1, "Giảng viên hướng dẫn");

            jdbcTemplate.update(
                    "INSERT INTO thanhvien_project (user_ma_so, project_ma_so, vai_tro) VALUES (?, ?, ?)",
                    userId2, projectId2, "Trưởng nhóm");

            jdbcTemplate.update(
                    "INSERT INTO thanhvien_project (user_ma_so, project_ma_so, vai_tro) VALUES (?, ?, ?)",
                    userId3, projectId3, "Trưởng nhóm");


        }
    }
}