package org.example.uthteammatching.controllers;

import org.example.uthteammatching.models.*;
import org.example.uthteammatching.repositories.*;
import org.example.uthteammatching.services.ArticleService;
import org.example.uthteammatching.services.ProjectService;
import org.example.uthteammatching.services.ThanhvienProjectService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class homeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Autowired
    private ListFriendRepository listFriendRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ThanhvienProjectService thanhvienProjectService;

    @Autowired
    private ProjectService projectService;


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
            Set<Long> projectUserJoinedIds = currentUser.getThanhVienProjects().stream()
                    .map(tp -> tp.getProjectMaSo().getMaProject())
                    .collect(Collectors.toSet());
            model.addAttribute("projectUserJoinedIds", projectUserJoinedIds);
            //List những project mà người dùng đã tham gia
        }
        return "home"; // Trả về home.html
    }

    @PostMapping("/project/join")
    public String joinProject(@RequestParam("projectId") Long projectId, Model model, RedirectAttributes redirectAttributes) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project không tồn tại"));

        thanhvienProjectService.addUserToProject(currentUser, project, "Thành viên");
        //Thông báo tham gia thành công

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


    @GetMapping("/user-detail/{id}")
    public String userDetail(@PathVariable("id") Long id, Model model) {
        Optional<SinhVien> sinhVienOptional = sinhVienRepository.findById(id);
        if (sinhVienOptional.isPresent()) {
            SinhVien sinhVien = sinhVienOptional.get();
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
    public String updateUser(@PathVariable("id") Long id, UthUser updatedUser, Model model) {
        Optional<UthUser> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            UthUser user = userOptional.get();
            model.addAttribute("student", user);
            user.setHo(updatedUser.getHo());
            user.setTen(updatedUser.getTen());

            user.setEmail(updatedUser.getEmail());
            user.setSdt(updatedUser.getSdt());
            user.setGioiTinh(updatedUser.getGioiTinh());
            user.setChuyenNganh(updatedUser.getChuyenNganh());
            user.setAvatar(updatedUser.getAvatar());

            userRepository.save(user); // Lưu vào database

            return "redirect:/user-detail/" + id; // Quay lại trang chi tiết
        } else {
            model.addAttribute("errorMessage", "User not found");
            return "404";
        }
    }



    // tìm kiếm dự án theo từ khoá nè
    @GetMapping("/home")
    public String searchProjects(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser); // Thêm danh sách bạn bè
        if (currentUser != null) {
            List<Project> projects;
            if (keyword == null || keyword.trim().isEmpty()) {
                projects = projectService.getAllProject();
                model.addAttribute("message", "Vui lòng nhập từ khóa tìm kiếm.");
            } else {
                projects = projectService.searchProjects(keyword);
                model.addAttribute("message", projects.isEmpty() ? "Không tìm thấy dự án." : null);
            }
            model.addAttribute("projects", projects);
            model.addAttribute("keyword", keyword);
        }


        return "home";
    }

    @GetMapping("/notification")
    public String notification(Model model) {
        UthUser currentUser = addCurrentUserToModel(model);
        addFriendUsersToModel(model, currentUser); // Thêm danh sách bạn bè

        return "notification";
    }




}

