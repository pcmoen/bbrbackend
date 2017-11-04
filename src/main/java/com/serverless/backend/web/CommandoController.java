package com.serverless.backend.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author pcmoen
 */
@Controller
public class CommandoController {
	private static final LinkedBlockingQueue<String> nextCommand = new LinkedBlockingQueue<>();

	@RequestMapping(value = "/commando", method = RequestMethod.GET)
	public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", nextCommand.peek());
		return "commando";
	}
	private String oppdater(final Model model, String commando) {
		try {
			nextCommand.put(commando);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		model.addAttribute("name", commando);
		return "commando";
	}

	@PostMapping(value = "/nextCommand", produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String nextCommand() {
		try {
			return nextCommand.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	@PostMapping("/advance")
	public String advance(Model model) { return oppdater(model, "advance"); }

	@PostMapping("/retreat")
	public String retreat(Model model) { return oppdater(model, "retreat"); }

	@PostMapping("/shoot")
	public String shoot(Model model) { return oppdater(model, "shoot"); }

	@PostMapping("/pass")
	public String pass(Model model) { return oppdater(model, "pass"); }

	@PostMapping("/rotate-left")
	public String rotateLeft(Model model) { return oppdater(model, "rotate-left"); }

	@PostMapping("/rotate-right")
	public String rotateRight(Model model) { return oppdater(model, "rotate-right"); }
}
