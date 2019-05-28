package com.workorder.ticket.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workorder.ticket.service.TemplateService;

/**
 * 模板管理
 * 
 * @author wzdong
 * @Date 2019年4月3日
 * @version 1.0
 */
@RestController
public class TemplateController {

	@Resource
	private TemplateService templateService;

	@PostMapping("/template/deploy")
	public String deploy(@RequestParam("key") String templatekey,
			@RequestParam(value = "key", required = false) String name,
			@RequestParam(value = "key", required = false) String description) {
		String key = templateService.deploy(templatekey, name, description);
		key = key.replaceAll("\"", "");
		return key;
	}

	@PostMapping("/template/reDeploy")
	public String reDeploy(@RequestParam("key") String templatekey) {
		String key = templateService.deploy(templatekey, null, null);
		key = key.replaceAll("\"", "");
		return key;
	}
}
