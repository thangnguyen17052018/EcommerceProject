package com.nminhthang.admin.user;

import com.nminhthang.common.entity.Role;
import com.nminhthang.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listFirstPage(Model model){
        return listByPage(model, 1);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(Model model, @PathVariable(name = "pageNum") int pageNum){


        Page<User> userPage = userService.listByPage(pageNum);
        List<User> listUsers = userPage.getContent();

        long startCount = (pageNum - 1) * userService.USERS_PER_PAGE + 1;
        long endCount = startCount + userService.USERS_PER_PAGE - 1;

        if (endCount > userPage.getTotalElements())
            endCount = userPage.getTotalElements();

        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", userPage.getTotalPages());


        return "users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model){
        List<Role> listRoles = userService.listRoles();
        User user = new User();
        user.setEnabled(true);

        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Create New User");

        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes){
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "The user was created successfully");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes){
        try {
            User user = userService.get(id);
            List<Role> listRoles = userService.listRoles();

            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
            model.addAttribute("listRoles", listRoles);

            return "user_form";
        } catch (UserNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
            return "redirect:/users";
        }

    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes){
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message", "User by ID = " + id + " has been deleted");
        } catch (UserNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
        }
        return "redirect:/users";

    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable(name = "id") Integer id,
                             @PathVariable(name = "status") boolean enabled,
                             Model model,
                             RedirectAttributes redirectAttributes){
        userService.updateUserEnabledStatus(id, enabled);
        String status = (enabled == true) ? "enabled" : "disabled";
        redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been " + status);
        return "redirect:/users";
    }


}
