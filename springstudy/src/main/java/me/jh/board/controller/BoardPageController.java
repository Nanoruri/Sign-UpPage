package me.jh.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board/page")
public class BoardPageController {

	@GetMapping("/{path}")
	public String board(@PathVariable String path) {
		return "board/" + path;
	}
}
