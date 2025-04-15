package org.example.uthteammatching.controllers;


import org.example.uthteammatching.models.*;
import org.example.uthteammatching.repositories.*;
import org.example.uthteammatching.services.ArticleService;
import org.example.uthteammatching.services.ListFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

@Controller
public class homeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListFriendRepository listFriendRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ThanhvienProjectRepository thanhvienProjectRepository;

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ListFriendService listFriendService;

    // Phương thức chung để lấy currentUser và thêm vào model
    private UthUser addCurrentUserToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = authentication.getName();
            Optional<UthUser> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                UthUser currentUser = userOptional.get();
                model.addAttribute("currentUser", currentUser);
                return currentUser;
            }
        }
        return null; // Trả về null nếu không tìm thấy user
    }


    // Phương thức chung để lấy danh sách bạn bè và thêm vào model
    private void addFriendUsersToModel(Model model, UthUser currentUser) {
        if (currentUser != null) {
            List<ListFriend> friends = listFriendRepository.findByUserId1OrUserId2WithFetch(currentUser);
            List<UthUser> friendUsers = new ArrayList<>();
            for (ListFriend friend : friends) {
                if (friend.getUserId1().equals(currentUser)) {
                    friendUsers.add(friend.getUserId2());
                } else {
                    friendUsers.add(friend.getUserId1());
                }
            }
            model.addAttribute("friendUsers", friendUsers);
        }
    }


    @GetMapping("/")
    public String home(Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser); // Thêm danh sách bạn bè
        if (currentUser != null) {
            List<BaiViet> baiViets = articleService.getAllArticles();
            model.addAttribute("baiViets", baiViets);

            Map<Long, String> baiVietRoleMap = new HashMap<>();
            for (BaiViet bv : baiViets) {
                Project project = bv.getProjectMaSo();
                if (project != null && project.getThanhVienProjects() != null) {
                    for (ThanhvienProject tv : project.getThanhVienProjects()) {
                        if (tv.getUserMaSo() != null && tv.getUserMaSo().getMaSo().equals(currentUser.getMaSo())) {
                            baiVietRoleMap.put(bv.getId(), tv.getVaiTro());
                            break;
                        }
                    }
                }
            }
            model.addAttribute("baiVietRoleMap", baiVietRoleMap);
        }
        return "home"; // Trả về home.html
    }


    @PostMapping("/project/join")
    public String joinProject(@RequestParam("projectId") Long projectId, Model model, RedirectAttributes redirectAttributes) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project không tồn tại"));

        ThanhvienProject tvp = new ThanhvienProject();
        ThanhvienProjectId id = new ThanhvienProjectId();
        id.setUserMaSo(currentUser.getMaSo());
        id.setProjectMaSo(project.getMaProject());
        tvp.setId(id);
        tvp.setUserMaSo(currentUser);
        tvp.setProjectMaSo(project);
        tvp.setVaiTro("PENDING");
        thanhvienProjectRepository.save(tvp);


        return "redirect:/";
    }


    @GetMapping("/project")
    public String project(Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser); // Thêm danh sách bạn bè
        List<Project> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);
        long totalProjects = projects.size();
        long doingProjects = projects.stream()
                .filter(p -> "Đang thực hiện".equalsIgnoreCase(p.getTrangThai()))
                .count();
        long doneProjects = projects.stream()
                .filter(p -> "Đã hoàn thành".equalsIgnoreCase(p.getTrangThai()))
                .count();

        model.addAttribute("totalProjects", totalProjects);
        model.addAttribute("doingProjects", doingProjects);
        model.addAttribute("doneProjects", doneProjects);

        return "project";
    }


    @PostMapping("/project")
    public String createProject(@RequestParam("nameProject") String tenProject,
                                @RequestParam("moTa") String moTa,
                                @RequestParam("trangThai") String trangThai,
                                @RequestParam("ngayBatDau") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayBatDau,
                                @RequestParam("ngayKetThuc") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayKetThuc, Model model) {


        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser);

        Project project = new Project();
        project.setTenProject(tenProject);
        project.setMoTa(moTa);
        project.setTrangThai(trangThai);
        project.setNgayBatDau(ngayBatDau);
        project.setNgayKetThuc(ngayKetThuc);

        ThanhvienProject tv = new ThanhvienProject();
        ThanhvienProjectId tvId = new ThanhvienProjectId(currentUser.getMaSo(), project.getMaProject());
        tv.setId(tvId);
        tv.setProjectMaSo(project);
        tv.setUserMaSo(currentUser);
        tv.setVaiTro("Trưởng nhóm");
        project.getThanhVienProjects().add(tv);

        projectRepository.save(project);

        return "redirect:/project";
    }


    @GetMapping("/project/search")
    public String searchProject(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser); // Thêm danh sách bạn bè
        List<Project> results = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            results = projectRepository.findByTenProjectContainingIgnoreCase(keyword);
            model.addAttribute("keyword", keyword);
        }

        model.addAttribute("results", results);
        return "project";
    }


    @GetMapping("/user-detail/{id}")
    public String userDetail(@PathVariable("id") Long id, Model model) {
        Optional<UthUser> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            UthUser sinhVien = userOptional.get();
            model.addAttribute("student", sinhVien);
            UthUser currentUser = addCurrentUserToModel(model);
            addFriendUsersToModel(model, currentUser);
            return "user-detail";
        } else {
            model.addAttribute("errorMessage", "User not found");
            return "404";
        }
    }


    @PostMapping("/user-detail/{id}")
    public String updateUser(
            @PathVariable("id") Long id,
            @RequestParam("ho") String ho,
            @RequestParam("ten") String ten,
            @RequestParam("email") String email,
            @RequestParam("sdt") String sdt,
            @RequestParam("gioiTinh") String gioiTinh,
            @RequestParam("chuyenNganh") String chuyenNganh,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
            Model model) {
        Optional<UthUser> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            UthUser user = userOptional.get();
            model.addAttribute("student", user);

            // Cập nhật thông tin người dùng
            user.setHo(ho);
            user.setTen(ten);
            user.setEmail(email);
            user.setSdt(sdt);
            user.setGioiTinh(gioiTinh);
            user.setChuyenNganh(chuyenNganh);

            // Xử lý upload ảnh đại diện
            if (avatarFile != null && !avatarFile.isEmpty()) {
                try {
                    // Kiểm tra định dạng file
                    String contentType = avatarFile.getContentType();
                    if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                        model.addAttribute("errorMessage", "Chỉ hỗ trợ file JPG hoặc PNG");
                        return "user-detail";
                    }

                    // Kiểm tra kích thước file (giới hạn 5MB)
                    if (avatarFile.getSize() > 5 * 1024 * 1024) {
                        model.addAttribute("errorMessage", "File quá lớn, tối đa 5MB");
                        return "user-detail";
                    }

                    // Xóa ảnh cũ nếu có
                    if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                        try {
                            String oldAvatarPath = "src/main/resources/static" + user.getAvatar();
                            Files.deleteIfExists(Paths.get(oldAvatarPath));
                        } catch (IOException e) {
                            e.printStackTrace(); // Ghi log lỗi
                        }
                    }

                    // Lưu ảnh mới
                    String uploadDir = "src/main/resources/static/img/avatars/";
                    String fileName = System.currentTimeMillis() + "_" + avatarFile.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir, fileName);
                    Files.createDirectories(filePath.getParent());
                    Files.write(filePath, avatarFile.getBytes());

                    // Cập nhật đường dẫn ảnh trong database
                    user.setAvatar("/img/avatars/" + fileName);
                } catch (IOException e) {
                    model.addAttribute("errorMessage", "Lỗi khi upload ảnh: " + e.getMessage());
                    return "user-detail";
                }
            }

            userRepository.save(user); // Lưu vào database
            return "redirect:/user-detail/" + id; // Quay lại trang chi tiết
        } else {
            model.addAttribute("errorMessage", "User not found");
            return "404";
        }
    }


    @GetMapping("/notification")
    public String notification(Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser);
        List<Project> joinRequests = new ArrayList<>();
        for (ThanhvienProject tv : currentUser.getThanhVienProjects()) {
            if ("LEADER".equals(tv.getVaiTro())) {
                Project project = tv.getProjectMaSo();
                boolean hasPendingMember = project.getThanhVienProjects().stream()
                        .anyMatch(member -> "PENDING".equals(member.getVaiTro()));
                if (hasPendingMember) {
                    joinRequests.add(project);
                }
            }
        }
        model.addAttribute("joinRequests", joinRequests);
        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(currentUser);
        model.addAttribute("notifications", notifications);

        return "notification";
    }


    @PostMapping("/project/approve")
    public String approveRequest(@RequestParam("userIdRequest") Long userId,
                                 @RequestParam("projectIdRequest") Long projectId, Model model) {

        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser); // Thêm danh sách bạn bè

        ThanhvienProjectId tvId = new ThanhvienProjectId();
        tvId.setUserMaSo(userId);
        tvId.setProjectMaSo(projectId);
        UthUser user = userRepository.findById(userId).orElse(null);
        try {
            ThanhvienProject request = thanhvienProjectRepository.findByUserMaSo_MaSoAndProjectMaSo_MaProject(userId, projectId);
            request.setVaiTro("Thành viên");
            thanhvienProjectRepository.save(request);
            notificationRepository.save(new Notification("Bạn đã duyệt "+user.getHo()+' '+user.getTen()+" vào dự án "+projectRepository.findByMaProject(projectId).getTenProject(), currentUser, null, NotificationType.NOTIFICATION));
            notificationRepository.save(new Notification(
                    "Bạn đã được duyệt tham gia dự án " + projectRepository.findByMaProject(projectId).getTenProject(), user, currentUser, NotificationType.NOTIFICATION));

        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/notification";
    }


    @PostMapping("/project/reject")
    public String rejectRequest(@RequestParam("userIdRequest") Long userId,
                                @RequestParam("projectIdRequest") Long projectId, Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser); // Thêm danh sách bạn bè

        UthUser user = userRepository.findById(userId).orElse(null);
        try {
            ThanhvienProject tv = thanhvienProjectRepository
                    .findByUserMaSo_MaSoAndProjectMaSo_MaProject(userId, projectId);

            thanhvienProjectRepository.delete(tv);

            notificationRepository.save(new Notification("Bạn từ chối cho "+user.getHo()+' '+user.getTen()+" tham vào dự án "+projectRepository.findByMaProject(projectId).getTenProject(), currentUser, null, NotificationType.NOTIFICATION));

            notificationRepository.save(new Notification("Bạn đã bị từ chối tham vào dự án "+projectRepository.findByMaProject(projectId).getTenProject(), currentUser, null, NotificationType.NOTIFICATION));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/notification";
    }


    @GetMapping("/ketban")
    public String ketBan(Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser);
        List<Notification> friendRequestNotifications = notificationRepository
                .findByUserAndTypeOrderByCreatedAtDesc(currentUser, NotificationType.FRIEND_REQUEST);

        model.addAttribute("friendRequestNotifications", friendRequestNotifications);
        return "ketban";
    }

    @PostMapping("/ketban")
    public String ketBan1(Model model) {
        return "ketban";
    }

    @GetMapping("/ketban/search")
    public String searchUserByPhone(@RequestParam("phone") String phone, Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser); // Thêm danh sách bạn bè
        UthUser foundUser = userRepository.findBySdt(phone);

        if (foundUser != null && !foundUser.getMaSo().equals(currentUser.getMaSo())) {
            model.addAttribute("searchResult", foundUser);
            boolean isFriend = listFriendRepository.existsByUserId1AndUserId2(currentUser, foundUser) ||
                    listFriendRepository.existsByUserId1AndUserId2(foundUser, currentUser);
            model.addAttribute("isFriend", isFriend);
        } else {
            model.addAttribute("notFound", "Không tìm thấy người dùng phù hợp.");
        }

        return "ketban";
    }

    @PostMapping("/ketban/send-request")
    public String sendFriendRequest(@RequestParam("friendId") Long friendId, Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser);

        UthUser targetUser = userRepository.findById(friendId).orElse(null);

        if (targetUser != null) {
            notificationRepository.save(new Notification(currentUser.getHo()+' '+currentUser.getTen()+" đã gửi lời mời kết bạn!", targetUser, currentUser, NotificationType.FRIEND_REQUEST));
        }

        return "redirect:/ketban";
    }

    @PostMapping("/ketban/accept-friend")
    public String acceptFriend(@RequestParam("notificationId") Long notificationId, Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser);

        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        UthUser targetUser = notification.getUserFrom();

        if (targetUser != null) {
            assert currentUser != null;
            listFriendService.createListFriend(targetUser, currentUser);
            notificationRepository.delete(notification);
            notificationRepository.save(new Notification("Bạn đã chấp nhận lời mời kết bạn của "+targetUser.getHo()+' '+targetUser.getTen(), currentUser, null, NotificationType.NOTIFICATION));
            notificationRepository.save(new Notification(currentUser.getHo()+' '+currentUser.getTen()+" đã chấp nhận lời mời kết bạn!", targetUser, null, NotificationType.NOTIFICATION));

        }
        return "redirect:/ketban";
    }

    @PostMapping("/ketban/reject-friend")
    public String rejectFriend(@RequestParam("notificationId") Long notificationId, Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser);

        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        UthUser targetUser = notification.getUserFrom();

        if (targetUser != null) {
            assert currentUser != null;
            listFriendService.createListFriend(targetUser, currentUser);
            notificationRepository.delete(notification);
        }
        return "redirect:/ketban";
    }



}
