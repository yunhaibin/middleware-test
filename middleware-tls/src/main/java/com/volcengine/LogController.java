package com.volcengine;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
@Slf4j
public class LogController {
	@PostMapping("/log")
	public String log(@RequestBody JSONObject json){
		log.info(json.toJSONString());
		System.out.println(json.toJSONString());
		return json.toJSONString();
	}
}
