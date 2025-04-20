package org.example.uthteammatching.controllers;


import org.example.uthteammatching.models.*;
import org.example.uthteammatching.repositories.*;
import org.example.uthteammatching.services.ArticleService;
import org.example.uthteammatching.services.ListFriendService;
import org.example.uthteammatching.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ChatGroupRepository chatGroupRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private LecturerRequestRepository lecturerRequestRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    // Phương thức chung để lấy currentUser và thêm vào model
    private UthUser addCurrentUserToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UthUser currentUser = userRepository.findByUsername(username).orElse(null);
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            return currentUser;
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
            
            // Add friend request notifications
            List<Notification> friendRequestNotifications = notificationRepository
                .findByUserAndTypeOrderByCreatedAtDesc(currentUser, NotificationType.FRIEND_REQUEST);
            model.addAttribute("friendRequestNotifications", friendRequestNotifications);
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

            List<Project> projects = projectRepository.findByThanhVienProjects_UserMaSo(currentUser);
            model.addAttribute("projects", projects);
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
        addFriendUsersToModel(model, currentUser);
        List<Project> projects = projectRepository.findByThanhVienProjects_UserMaSo(currentUser);
        Iterator<Project> iterator = projects.iterator();
        while (iterator.hasNext()) {
            Project project = iterator.next();
            for (ThanhvienProject tv : project.getThanhVienProjects()) {
                if (tv.getUserMaSo().equals(currentUser) && "PENDING".equals(tv.getVaiTro())) {
                    iterator.remove();
                    break;
                }
            }
        }
        model.addAttribute("projects", projects);
        long totalProjects = projects.size();
        long doingProjects = projects.stream()
                .filter(p -> "Đang thực hiện".equalsIgnoreCase(p.getTrangThai()))
                .count();
        long doneProjects = projects.stream()
                .filter(p -> "Đã hoàn thành".equalsIgnoreCase(p.getTrangThai()))
                .count();

        List<UthUser> giangViens = userRepository.findByRoleName("LECTURE");
        model.addAttribute("giangViens", giangViens);

        model.addAttribute("totalProjects", totalProjects);
        model.addAttribute("doingProjects", doingProjects);
        model.addAttribute("doneProjects", doneProjects);

        return "project";
    }

    @PostMapping("/project")
    public String createProject(
            @RequestParam("nameProject") String tenProject,
            @RequestParam("moTa") String moTa,
            @RequestParam("ngayBatDau") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayBatDau,
            @RequestParam("ngayKetThuc") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayKetThuc,
            @RequestParam("loai") String loai,
            @RequestParam(value = "maGiangVien", required = false) Long maGiangVien,
            @RequestParam(value = "taiLieuFiles", required = false) List<MultipartFile> taiLieuFiles,
            Model model) {

        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser);

        // Log để kiểm tra
        System.out.println("taiLieuFiles: " + (taiLieuFiles == null ? "null" : taiLieuFiles.size()));

        Project project = new Project();
        project.setTenProject(tenProject);
        project.setMoTa(moTa);
        project.setNgayBatDau(ngayBatDau);
        project.setNgayKetThuc(ngayKetThuc);
        ProjectType projectType = ProjectType.valueOf(loai);
        project.setLoai(projectType);

        // Gán trạng thái dựa trên loại dự án
        if (projectType == ProjectType.HOC_THUAT) {
            project.setTrangThai("Đang chờ duyệt");
        } else {
            project.setTrangThai("Đang thực hiện");
        }

        // Xử lý maGiangVien
        UthUser giangVien = null;
        if (projectType == ProjectType.HOC_THUAT) {
            if (maGiangVien == null) {
                model.addAttribute("error", "Dự án học thuật yêu cầu chọn giảng viên.");
                model.addAttribute("project", project);
                model.addAttribute("giangViens", userRepository.findByRoleName("LECTURE"));
                return "project";
            }
            giangVien = userRepository.findById(maGiangVien)
                    .orElseThrow(() -> new IllegalArgumentException("Giảng viên không tồn tại"));
            boolean isGiangVien = giangVien.getUserRoles().stream()
                    .anyMatch(userRole -> "LECTURE".equals(userRole.getRole().getTen()));
            if (!isGiangVien) {
                model.addAttribute("error", "Người dùng không phải là giảng viên.");
                model.addAttribute("project", project);
                model.addAttribute("giangViens", userRepository.findByRoleName("LECTURE"));
                return "project";
            }
            project.setMaGiangVien(giangVien.getMaSo());
        } else {
            project.setMaGiangVien(null);
        }

        // Thêm trưởng nhóm
        ThanhvienProject tvTruongNhom = new ThanhvienProject();
        ThanhvienProjectId tvId = new ThanhvienProjectId(currentUser.getMaSo(), null);
        tvTruongNhom.setId(tvId);
        tvTruongNhom.setUserMaSo(currentUser);
        tvTruongNhom.setVaiTro("Trưởng nhóm");
        project.addThanhVien(tvTruongNhom);

        // Thêm giảng viên vào danh sách thành viên nếu là dự án học thuật
        ThanhvienProject tvGiangVien = null;
        if (projectType == ProjectType.HOC_THUAT && giangVien != null) {
            tvGiangVien = new ThanhvienProject();
            ThanhvienProjectId tvGiangVienId = new ThanhvienProjectId(giangVien.getMaSo(), null);
            tvGiangVien.setId(tvGiangVienId);
            tvGiangVien.setUserMaSo(giangVien);
            tvGiangVien.setVaiTro("Giảng viên hướng dẫn");
            project.addThanhVien(tvGiangVien);
        }

        // Lưu project trước để sinh maProject
        projectRepository.save(project);

        // Cập nhật maProject cho các thành viên
        tvTruongNhom.getId().setMaProject(project.getMaProject());
        tvTruongNhom.setProjectMaSo(project);
        if (tvGiangVien != null) {
            tvGiangVien.getId().setMaProject(project.getMaProject());
            tvGiangVien.setProjectMaSo(project);
        }

        // Xử lý tài liệu
        if (taiLieuFiles != null && !taiLieuFiles.isEmpty()) {
            for (MultipartFile file : taiLieuFiles) {
                if (!file.isEmpty()) {
                    try {
                        String duongDan = fileStorageService.saveFile(file);
                        String fileName = file.getOriginalFilename();
                        String loaiTaiLieu = fileStorageService.determineFileType(fileName);

                        TaiLieu taiLieu = new TaiLieu(fileName, duongDan, loaiTaiLieu, project);
                        project.addTaiLieu(taiLieu);
                    } catch (IllegalArgumentException e) {
                        model.addAttribute("error", e.getMessage());
                        model.addAttribute("project", project);
                        model.addAttribute("giangViens", userRepository.findByRoleName("LECTURE"));
                        return "project";
                    } catch (IOException e) {
                        model.addAttribute("error", "Lỗi khi tải tệp: " + file.getOriginalFilename());
                        model.addAttribute("project", project);
                        model.addAttribute("giangViens", userRepository.findByRoleName("LECTURE"));
                        return "project";
                    }
                }
            }
        } else {
            System.out.println("No files uploaded");
        }

        // Lưu lại project sau khi thêm tài liệu
        projectRepository.save(project);

        ChatGroup chatGroup = new ChatGroup();
        chatGroup.setGroupName(tenProject);
        chatGroup.setGroupId(project.getMaProject());
        chatGroup.addMember(currentUser);
        if (projectType == ProjectType.HOC_THUAT && giangVien != null) {
            chatGroup.addMember(giangVien); // Thêm giảng viên vào nhóm chat
        }
        chatGroupRepository.save(chatGroup);

        model.addAttribute("success", "Dự án đã được tạo thành công!");
        return "redirect:/project";
    }

    @PostMapping("/project/{id}/approve")
    @Transactional
    public String approveProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        System.out.println("Approving project with ID: " + id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dự án không tồn tại"));
        System.out.println("Project found: maProject=" + project.getMaProject() + ", trangThai=" + project.getTrangThai());

        UthUser currentUser = userRepository.findById(1L).orElse(null); // Thay bằng logic thực tế
        System.out.println("CurrentUser: " + (currentUser != null ? currentUser.getMaSo() : "null"));
        System.out.println("Project maGiangVien: " + project.getMaGiangVien());

        // Tạm thời bỏ kiểm tra quyền để kiểm tra chức năng
        /*
        if (currentUser == null || !project.getMaGiangVien().equals(currentUser.getMaSo())) {
            redirectAttributes.addFlashAttribute("error", "Bạn không có quyền duyệt dự án này.");
            return "redirect:/project";
        }
        */

        project.setTrangThai("Đang thực hiện");
        projectRepository.save(project);
        redirectAttributes.addFlashAttribute("success", "Dự án đã được duyệt thành công!");
        return "redirect:/project";
    }

    @PostMapping("/project/{id}/reject")
    @Transactional
    public String rejectProject(@PathVariable Long id,
                                @RequestParam(value = "rejectionReason", required = false) String rejectionReason,
                                RedirectAttributes redirectAttributes) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dự án không tồn tại"));

        UthUser currentUser = userRepository.findById(1L).orElse(null); // Thay bằng logic lấy currentUser thực tế

        // Xóa dự án (cascade sẽ xóa các bản ghi liên quan trong thanhvien_project, tai_lieu, chat_groups)
        projectRepository.delete(project);
        redirectAttributes.addFlashAttribute("success", "Dự án đã bị từ chối và xóa thành công!");
        return "redirect:/project";
    }

    @GetMapping("/search")
    public String searchProject(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser); // Thêm danh sách bạn bè

        List<BaiViet> results = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            results = articleService.searchArticlesByProjectName(keyword); // Tìm kiếm bài viết theo tên dự án
            model.addAttribute("keyword", keyword);
        }

        model.addAttribute("results", results); // Truyền danh sách bài viết tìm được
        model.addAttribute("baiViets", articleService.getAllArticles()); // Giữ danh sách bài viết cho news feed

        // Thêm vai trò người dùng trong dự án
        Map<Long, String> baiVietRoleMap = new HashMap<>();
        if (currentUser != null) {
            for (BaiViet bv : results) {
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
            for (BaiViet bv : articleService.getAllArticles()) {
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
        }
        model.addAttribute("baiVietRoleMap", baiVietRoleMap);

        return "home";
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



    @PostMapping("/ketban/send-request")
    @ResponseBody
    public Map<String, Object> sendFriendRequest(@RequestParam("friendId") Long friendId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UthUser currentUser = userRepository.findByUsername(username).orElse(null);
            UthUser targetUser = userRepository.findById(friendId).orElse(null);

            if (currentUser != null && targetUser != null) {
                // Kiểm tra xem đã có lời mời kết bạn chưa
                List<Notification> existingRequests = notificationRepository
                    .findByUserAndTypeOrderByCreatedAtDesc(targetUser, NotificationType.FRIEND_REQUEST);
                boolean hasExistingRequest = existingRequests.stream()
                    .anyMatch(n -> n.getUserFrom() != null && n.getUserFrom().getMaSo().equals(currentUser.getMaSo()));

                if (hasExistingRequest) {
                    response.put("success", false);
                    response.put("error", "Bạn đã gửi lời mời kết bạn cho người này rồi");
                    return response;
                }

                // Kiểm tra xem đã là bạn bè chưa
                boolean isFriend = listFriendRepository.existsByUserId1AndUserId2(currentUser, targetUser) ||
                                 listFriendRepository.existsByUserId1AndUserId2(targetUser, currentUser);
                
                if (isFriend) {
                    response.put("success", false);
                    response.put("error", "Các bạn đã là bạn bè rồi");
                    return response;
                }

                notificationRepository.save(new Notification(
                    currentUser.getHo() + ' ' + currentUser.getTen() + " đã gửi lời mời kết bạn!", 
                    targetUser, currentUser, NotificationType.FRIEND_REQUEST
                ));
                response.put("success", true);
            } else {
                response.put("success", false);
                response.put("error", "Không tìm thấy người dùng");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "Đã xảy ra lỗi khi gửi lời mời kết bạn");
        }
        return response;
    }

    @PostMapping("/ketban/accept-friend")
    @ResponseBody
    public Map<String, Object> acceptFriend(@RequestParam("notificationId") Long notificationId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UthUser currentUser = userRepository.findByUsername(username).orElse(null);
            
            if (currentUser == null) {
                response.put("success", false);
                response.put("error", "Không tìm thấy người dùng hiện tại");
                return response;
            }

            Notification notification = notificationRepository.findById(notificationId).orElse(null);
            if (notification == null) {
                response.put("success", false);
                response.put("error", "Không tìm thấy lời mời kết bạn");
                return response;
            }

            UthUser targetUser = notification.getUserFrom();
            if (targetUser == null) {
                response.put("success", false);
                response.put("error", "Không tìm thấy người gửi lời mời");
                return response;
            }

            // Kiểm tra xem đã là bạn bè chưa
            boolean alreadyFriends = listFriendRepository.existsByUserId1AndUserId2(currentUser, targetUser) ||
                                   listFriendRepository.existsByUserId1AndUserId2(targetUser, currentUser);
            
            if (!alreadyFriends) {
                // Tạo mối quan hệ bạn bè mới
                listFriendService.createListFriend(targetUser, currentUser);
                
                // Xóa thông báo lời mời kết bạn
                notificationRepository.delete(notification);
                
                // Tạo thông báo cho cả hai người
                notificationRepository.save(new Notification(
                    "Bạn đã chấp nhận lời mời kết bạn của " + targetUser.getHo() + ' ' + targetUser.getTen(),
                    currentUser, null, NotificationType.NOTIFICATION
                ));
                notificationRepository.save(new Notification(
                    currentUser.getHo() + ' ' + currentUser.getTen() + " đã chấp nhận lời mời kết bạn!",
                    targetUser, null, NotificationType.NOTIFICATION
                ));
                
                response.put("success", true);
                response.put("message", "Đã chấp nhận lời mời kết bạn thành công");
            } else {
                response.put("success", false);
                response.put("error", "Các bạn đã là bạn bè rồi");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "Đã xảy ra lỗi khi chấp nhận lời mời kết bạn");
        }
        
        return response;
    }

    @PostMapping("/ketban/reject-friend")
    public String rejectFriend(@RequestParam("notificationId") Long notificationId, Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser);

        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            UthUser targetUser = notification.getUserFrom();
            if (targetUser != null) {
                // Chỉ xóa thông báo, không tạo kết bạn
                notificationRepository.delete(notification);
                // Thêm thông báo cho cả hai người dùng
                notificationRepository.save(new Notification(
                    "Bạn đã từ chối lời mời kết bạn của " + targetUser.getHo() + ' ' + targetUser.getTen(),
                    currentUser, null, NotificationType.NOTIFICATION
                ));
                notificationRepository.save(new Notification(
                    currentUser.getHo() + ' ' + currentUser.getTen() + " đã từ chối lời mời kết bạn của bạn",
                    targetUser, null, NotificationType.NOTIFICATION
                ));
            }
        }
        return "redirect:/ketban";
    }

    @GetMapping("/ketban/search-ajax")
    @ResponseBody
    public Map<String, Object> searchUserByPhoneAjax(@RequestParam("phone") String searchTerm) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Lấy current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UthUser currentUser = userRepository.findByUsername(username).orElse(null);
            
            if (currentUser == null) {
                response.put("success", false);
                response.put("error", "User not found");
                return response;
            }
            
            // Tìm user theo số điện thoại
            UthUser foundUser = userRepository.findFirstBySdt(searchTerm).orElse(null);
            
            if (foundUser == null) {
                // Nếu không tìm thấy theo số điện thoại, thử tìm theo username
                foundUser = userRepository.findByUsername(searchTerm).orElse(null);
            }
            
            if (foundUser != null && !foundUser.getMaSo().equals(currentUser.getMaSo())) {
                boolean isFriend = listFriendRepository.existsByUserId1AndUserId2(currentUser, foundUser) ||
                        listFriendRepository.existsByUserId1AndUserId2(foundUser, currentUser);
                
                List<Notification> notifications = notificationRepository.findByUserAndTypeOrderByCreatedAtDesc(foundUser, NotificationType.FRIEND_REQUEST);
                boolean hasSentRequest = notifications.stream()
                        .anyMatch(n -> n.getUserFrom() != null && n.getUserFrom().getMaSo().equals(currentUser.getMaSo()));
                
                response.put("user", foundUser);
                response.put("isFriend", isFriend);
                response.put("hasSentRequest", hasSentRequest);
                response.put("success", true);
            } else {
                response.put("user", null);
                response.put("success", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "Đã xảy ra lỗi khi tìm kiếm: " + e.getMessage());
        }
        
        return response;
    }

    @PostMapping("/user/change-password")
    public String changePassword(
            @RequestParam("maSo") Long maSo,
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<UthUser> userOptional = userRepository.findById(maSo);
        if (!userOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Người dùng không tồn tại");
            redirectAttributes.addFlashAttribute("data-error-message", "Người dùng không tồn tại");
            return "redirect:/user-detail/" + maSo;
        }

        UthUser user = userOptional.get();

        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(currentPassword, user.getPass())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu hiện tại không đúng");
            redirectAttributes.addFlashAttribute("data-error-message", "Mật khẩu hiện tại không đúng");
            return "redirect:/user-detail/" + maSo;
        }

        // Kiểm tra mật khẩu mới và xác nhận mật khẩu
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu mới và xác nhận mật khẩu không khớp");
            redirectAttributes.addFlashAttribute("data-error-message", "Mật khẩu mới và xác nhận mật khẩu không khớp");
            return "redirect:/user-detail/" + maSo;
        }

        // Kiểm tra mật khẩu mới không được giống mật khẩu cũ
        if (passwordEncoder.matches(newPassword, user.getPass())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu mới không được giống mật khẩu hiện tại");
            redirectAttributes.addFlashAttribute("data-error-message", "Mật khẩu mới không được giống mật khẩu hiện tại");
            return "redirect:/user-detail/" + maSo;
        }

        // Kiểm tra độ dài mật khẩu mới
        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu mới phải có ít nhất 6 ký tự");
            redirectAttributes.addFlashAttribute("data-error-message", "Mật khẩu mới phải có ít nhất 6 ký tự");
            return "redirect:/user-detail/" + maSo;
        }

        // Kiểm tra độ phức tạp của mật khẩu mới
        if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mật khẩu mới phải có ít nhất 6 ký tự, bao gồm chữ hoa, chữ thường và số");
            redirectAttributes.addFlashAttribute("data-error-message", "Mật khẩu mới phải có ít nhất 6 ký tự, bao gồm chữ hoa, chữ thường và số");
            return "redirect:/user-detail/" + maSo;
        }

        try {
            // Cập nhật mật khẩu mới
            user.setPass(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            // Thêm thông báo thành công
            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
            redirectAttributes.addFlashAttribute("data-success-message", "Đổi mật khẩu thành công!");

            // Đóng modal sau khi đổi mật khẩu thành công
            redirectAttributes.addFlashAttribute("closeModal", true);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi đổi mật khẩu. Vui lòng thử lại sau.");
            redirectAttributes.addFlashAttribute("data-error-message", "Có lỗi xảy ra khi đổi mật khẩu. Vui lòng thử lại sau.");
        }

        return "redirect:/user-detail/" + maSo;
    }

    @PostMapping("/user/request-lecturer")
    public String submitLecturerRequest(
            @RequestParam("maSo") Long maSo,
            @RequestParam("expertise") String expertise,
            @RequestParam("experience") String experience,
            @RequestParam("reason") String reason,
            Model model,
            RedirectAttributes redirectAttributes) {

        UthUser user = userRepository.findById(maSo)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Kiểm tra xem user đã có đơn xin đang chờ duyệt chưa
        if (lecturerRequestRepository.existsByUserAndStatus(user, "PENDING")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn đã có đơn xin làm giảng viên đang chờ duyệt");
            return "redirect:/user-detail/" + maSo;
        }

        // Kiểm tra xem user đã là giảng viên chưa
        if (user.getUserRoles().stream().anyMatch(ur -> ur.getRole().getTen().equals("LECTURE"))) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn đã là giảng viên");
            return "redirect:/user-detail/" + maSo;
        }

        LecturerRequest request = new LecturerRequest();
        request.setUser(user);
        request.setExpertise(expertise);
        request.setExperience(experience);
        request.setReason(reason);

        lecturerRequestRepository.save(request);
        redirectAttributes.addFlashAttribute("successMessage", "Đơn xin làm giảng viên đã được gửi thành công!");
        return "redirect:/user-detail/" + maSo;
    }

    @GetMapping("/admin/lecturer-requests")
    public String viewLecturerRequests(Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        if (currentUser == null || !currentUser.getUserRoles().stream()
                .anyMatch(ur -> ur.getRole().getTen().equals("ADMIN"))) {
            return "redirect:/";
        }

        List<LecturerRequest> pendingRequests = lecturerRequestRepository.findByStatusOrderByCreatedAtDesc("PENDING");
        model.addAttribute("pendingRequests", pendingRequests);
        return "admin/lecturer-requests";
    }

    @PostMapping("/admin/lecturer-requests/{id}/approve")
    public String approveLecturerRequest(
            @PathVariable("id") Long requestId,
            Model model,
            RedirectAttributes redirectAttributes) {

        UthUser currentUser = addCurrentUserToModel(model);
        if (currentUser == null || !currentUser.getUserRoles().stream()
                .anyMatch(ur -> ur.getRole().getTen().equals("ADMIN"))) {
            return "redirect:/";
        }

        LecturerRequest request = lecturerRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        UthUser user = request.getUser();

        // Kiểm tra xem user đã có vai trò LECTURE chưa
        boolean isAlreadyLecturer = user.getUserRoles().stream()
                .anyMatch(ur -> ur.getRole().getTen().equals("LECTURE"));
        if (isAlreadyLecturer) {
            redirectAttributes.addFlashAttribute("errorMessage", "Người dùng đã là giảng viên.");
            return "redirect:/admin/lecturer-requests";
        }

        // Tìm hoặc tạo vai trò LECTURE
        Role lecturerRole = roleRepository.findByTen("LECTURE")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setTen("LECTURE");
                    return roleRepository.save(newRole);
                });

        // Thêm vai trò LECTURE
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(lecturerRole);
        userRoleRepository.save(userRole);

        // Cập nhật trạng thái đơn
        request.setStatus("APPROVED");
        request.setProcessedAt(LocalDateTime.now());
        request.setProcessedBy(currentUser);
        lecturerRequestRepository.save(request);

        // Tạo thông báo
        notificationRepository.save(new Notification(
                "Đơn xin làm giảng viên của bạn đã được phê duyệt",
                user,
                currentUser,
                NotificationType.NOTIFICATION
        ));

        redirectAttributes.addFlashAttribute("successMessage", "Đã phê duyệt đơn xin làm giảng viên");
        return "redirect:/admin/lecturer-requests";
    }

    @PostMapping("/admin/lecturer-requests/{id}/reject")
    public String rejectLecturerRequest(
            @PathVariable("id") Long requestId,
            @RequestParam(value = "rejectionReason", required = false) String rejectionReason,
            Model model,
            RedirectAttributes redirectAttributes) {

        UthUser currentUser = addCurrentUserToModel(model);
        if (currentUser == null || !currentUser.getUserRoles().stream()
                .anyMatch(ur -> ur.getRole().getTen().equals("ADMIN"))) {
            return "redirect:/";
        }

        LecturerRequest request = lecturerRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        // Cập nhật trạng thái đơn
        request.setStatus("REJECTED");
        request.setProcessedAt(LocalDateTime.now());
        request.setProcessedBy(currentUser);
        lecturerRequestRepository.save(request);

        // Tạo thông báo
        String message = "Đơn xin làm giảng viên của bạn đã bị từ chối";
        if (rejectionReason != null && !rejectionReason.trim().isEmpty()) {
            message += ". Lý do: " + rejectionReason;
        }

        notificationRepository.save(new Notification(
                message,
                request.getUser(),
                currentUser,
                NotificationType.NOTIFICATION
        ));

        redirectAttributes.addFlashAttribute("successMessage", "Đã từ chối đơn xin làm giảng viên");
        return "redirect:/admin/lecturer-requests";
    }



    @PostMapping("/chat/invite")
    @ResponseBody
    public Map<String, Object> inviteToGroupChat(
            @RequestParam("userId") Long userId,
            @RequestParam("groupId") Long groupId) {
        Map<String, Object> response = new HashMap<>();
        try {
            UthUser currentUser = addCurrentUserToModel(new ConcurrentModel());
            UthUser targetUser = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            ChatGroup chatGroup = chatGroupRepository.findById(groupId)
                    .orElseThrow(() -> new IllegalArgumentException("Chat group not found"));

            // Kiểm tra xem user đã trong group chưa
            if (chatGroup.getMembers().contains(targetUser)) {
                response.put("success", false);
                response.put("error", "Người dùng đã là thành viên của nhóm chat");
                return response;
            }

            // Kiểm tra xem đã gửi lời mời chưa
            List<Notification> existingInvites = notificationRepository
                    .findByUserAndTypeOrderByCreatedAtDesc(targetUser, NotificationType.CHAT_INVITE);
            boolean hasExistingInvite = existingInvites.stream()
                    .anyMatch(n -> n.getUserFrom() != null && n.getUserFrom().getMaSo().equals(currentUser.getMaSo())
                            && n.getGroupId() != null && n.getGroupId().equals(groupId));

            if (hasExistingInvite) {
                response.put("success", false);
                response.put("error", "Bạn đã gửi lời mời cho người này vào nhóm chat này rồi");
                return response;
            }

            // Tạo thông báo lời mời
            Notification notification = new Notification(
                    currentUser.getHo() + " " + currentUser.getTen() + " đã mời bạn tham gia nhóm chat của dự án " + chatGroup.getGroupName(),
                    targetUser, currentUser, NotificationType.CHAT_INVITE);
            notification.setGroupId(groupId);
            notificationRepository.save(notification);

            response.put("success", true);
            response.put("message", "Đã gửi lời mời thành công");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "Đã xảy ra lỗi khi gửi lời mời: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/chat/accept-invite")
    @ResponseBody
    public Map<String, Object> acceptChatInvite(@RequestParam("notificationId") Long notificationId) {
        Map<String, Object> response = new HashMap<>();
        try {
            UthUser currentUser = addCurrentUserToModel(new ConcurrentModel());
            Notification notification = notificationRepository.findById(notificationId)
                    .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

            if (!notification.getUser().equals(currentUser)) {
                response.put("success", false);
                response.put("error", "Bạn không có quyền chấp nhận lời mời này");
                return response;
            }

            if (!notification.getType().equals(NotificationType.CHAT_INVITE)) {
                response.put("success", false);
                response.put("error", "Đây không phải lời mời tham gia nhóm chat");
                return response;
            }

            Long groupId = notification.getGroupId();
            ChatGroup chatGroup = chatGroupRepository.findById(groupId)
                    .orElseThrow(() -> new IllegalArgumentException("Chat group not found"));

            // Kiểm tra xem user đã trong group chưa
            if (chatGroup.getMembers().contains(currentUser)) {
                response.put("success", false);
                response.put("error", "Bạn đã là thành viên của nhóm chat này");
                return response;
            }

            // Thêm user vào group chat
            chatGroup.addMember(currentUser);
            chatGroupRepository.save(chatGroup);

            // Xóa thông báo
            notificationRepository.delete(notification);

            // Tạo thông báo cho cả hai
            notificationRepository.save(new Notification(
                    "Bạn đã chấp nhận lời mời tham gia nhóm chat " + chatGroup.getGroupName(),
                    currentUser, null, NotificationType.NOTIFICATION));
            notificationRepository.save(new Notification(
                    currentUser.getHo() + " " + currentUser.getTen() + " đã chấp nhận lời mời tham gia nhóm chat " + chatGroup.getGroupName(),
                    notification.getUserFrom(), null, NotificationType.NOTIFICATION));

            response.put("success", true);
            response.put("message", "Đã tham gia nhóm chat thành công");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "Đã xảy ra lỗi khi chấp nhận lời mời: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/chat/reject-invite")
    public String rejectChatInvite(@RequestParam("notificationId") Long notificationId, RedirectAttributes redirectAttributes) {
        try {
            UthUser currentUser = addCurrentUserToModel(new ConcurrentModel());
            Notification notification = notificationRepository.findById(notificationId)
                    .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

            if (!notification.getUser().equals(currentUser)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền từ chối lời mời này");
                return "redirect:/notification";
            }

            if (!notification.getType().equals(NotificationType.CHAT_INVITE)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Đây không phải lời mời tham gia nhóm chat");
                return "redirect:/notification";
            }

            // Xóa thông báo
            notificationRepository.delete(notification);

            // Tạo thông báo cho cả hai
            notificationRepository.save(new Notification(
                    "Bạn đã từ chối lời mời tham gia nhóm chat " + chatGroupRepository.findById(notification.getGroupId()).get().getGroupName(),
                    currentUser, null, NotificationType.NOTIFICATION));
            notificationRepository.save(new Notification(
                    currentUser.getHo() + " " + currentUser.getTen() + " đã từ chối lời mời tham gia nhóm chat",
                    notification.getUserFrom(), null, NotificationType.NOTIFICATION));

            redirectAttributes.addFlashAttribute("successMessage", "Đã từ chối lời mời thành công");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi từ chối lời mời: " + e.getMessage());
        }
        return "redirect:/notification";
    }

    @GetMapping("/api/users/search")
    @ResponseBody
    public List<UthUser> searchUsers(@RequestParam("term") String term) {
        if (term == null || term.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return userRepository.findByHoContainingIgnoreCaseOrTenContainingIgnoreCaseOrSdtContainingIgnoreCase(term.trim())
                .stream()
                .filter(user -> !user.getUserRoles().stream().anyMatch(ur -> ur.getRole().getTen().equals("ADMIN")))
                .collect(Collectors.toList());
    }

    @PostMapping("/api/projects/{id}/files/upload")
    @ResponseBody
    public Map<String, Object> uploadFiles(
            @PathVariable("id") Long projectId,
            @RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Tìm project theo projectId
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new IllegalArgumentException("Dự án không tồn tại"));

            // Xử lý file upload
            if (file != null && !file.isEmpty()) {
                // Lưu file vào hệ thống và lấy đường dẫn
                String duongDan = fileStorageService.saveFile(file);
                String fileName = file.getOriginalFilename();
                String loaiTaiLieu = fileStorageService.determineFileType(fileName);

                // Tạo đối tượng TaiLieu và liên kết với Project
                TaiLieu taiLieu = new TaiLieu(fileName, duongDan, loaiTaiLieu, project);
                project.addTaiLieu(taiLieu);

                // Lưu lại project
                projectRepository.save(project);

                response.put("success", true);
                response.put("message", "Tài liệu đã được tải lên thành công!");
                return response;
            } else {
                response.put("success", false);
                response.put("error", "Vui lòng chọn một file để tải lên!");
                return response;
            }
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return response;
        } catch (IOException e) {
            response.put("success", false);
            response.put("error", "Lỗi khi tải tệp: " + e.getMessage());
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Có lỗi xảy ra khi tải lên tài liệu: " + e.getMessage());
            return response;
        }
    }
}
