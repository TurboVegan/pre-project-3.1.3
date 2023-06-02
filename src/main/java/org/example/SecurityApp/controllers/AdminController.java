package org.example.SecurityApp.controllers;


import org.example.SecurityApp.models.Role;
import org.example.SecurityApp.models.User;
import org.example.SecurityApp.repositories.RolesRepository;
import org.example.SecurityApp.services.UsersService;
import org.example.SecurityApp.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserValidator userValidator;
    private final RolesRepository rolesRepository;
    private final UsersService usersService;

    @Autowired
    public AdminController(UserValidator userValidator,
                           RolesRepository rolesRepository, UsersService usersService) {
        this.userValidator = userValidator;
        this.rolesRepository = rolesRepository;
        this.usersService = usersService;
    }

    @GetMapping()
    public String index(ModelMap model) {
        model.addAttribute("users", usersService.findAll());
        return "admin/index";
    }

    @GetMapping("/user/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", usersService.findOne(id));
        return "admin/show";
    }

    @GetMapping("/new")
    public ModelAndView newUser() {
        User user = new User();
        ModelAndView mav = new ModelAndView("admin/new");
        mav.addObject("user", user);

        List<Role> roles = (List<Role>) rolesRepository.findAll();

        mav.addObject("allRoles", roles);

        return mav;
    }

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }
        usersService.register(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editUser(@PathVariable(name = "id") Integer id) {
        User user = usersService.findOne(id);
        ModelAndView mav = new ModelAndView("admin/edit");
        mav.addObject("user", user);

        List<Role> roles = (List<Role>) rolesRepository.findAll();

        mav.addObject("allRoles", roles);

        return mav;
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") int id) {
        usersService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/user/{id}")
    public String delete(@PathVariable("id") int id) {
        usersService.delete(id);
        return "redirect:/admin";
    }
}