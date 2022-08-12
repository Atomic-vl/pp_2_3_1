package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.DAO.UserDao;
import web.model.User;

@Controller
public class testController {

    @Autowired
    private UserDao userDao;

    @GetMapping(value = "/")
    public String showAllUsers(Model model) {
        model.addAttribute("usersInController", userDao.showAllUsers());

        return "users-view";
    }

    @GetMapping("/{id}")
    public String findUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("userInController", userDao.findUser(id));
        return "show-view";
    }

    @GetMapping("/addUser")
    public String addUser(Model model) {
        model.addAttribute("addUserModel", new User());
        return "addUser-view";
    }

    @PostMapping("/")
    public String newUser(@ModelAttribute("xui") User user) {
        userDao.addUser(user);
        return "redirect:/";
    }

    @GetMapping("/{id}/update")
    public String updateUser1(@PathVariable("id") int id, Model model) {
        model.addAttribute("updateUser", userDao.findUser(id));
        return "updateUser-view";
    }

    @PatchMapping("/{id}")
    public String updateInfo(@ModelAttribute("updateInfo") User user, @PathVariable("id") int id) {
//        User updateUser = userDao.findUser(id);
        userDao.updateUser(user);
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userDao.deleteUser(id);
        return "redirect:/";
    }
}
