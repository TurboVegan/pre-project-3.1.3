package ru.alishev.springcourse.FirstSecurityApp.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.FirstSecurityApp.models.User;
import ru.alishev.springcourse.FirstSecurityApp.services.UsersDetailsService;
import ru.alishev.springcourse.FirstSecurityApp.services.UsersService;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UsersController {

	private final UsersService usersService;

	@Autowired
	public UsersController(UsersService usersService, UsersDetailsService usersDetailsService) {
		this.usersService = usersService;
	}

	@GetMapping()
	public String index(Principal principal, ModelMap model) {
		User user = usersService.findByUsername(principal.getName());

		model.addAttribute("user", user);
		return "user/index";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		usersService.delete(id);
		return "redirect:/auth/login";
	}
}