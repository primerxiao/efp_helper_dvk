package com.efp.plugins.general.regex.service;

import com.efp.plugins.general.regex.bean.AsyncResult;
import com.efp.plugins.general.regex.bean.Handler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleDataService {
	JsonArray data = new JsonArray();
	public static RuleDataService getInstance(Project project) {
		return ServiceManager.getService(project, RuleDataService.class);
	}

	public RuleDataService() {
		this.initData();
	}

	public void filterRule(String keyword, Handler<AsyncResult> handler) {
		if (StringUtils.isEmpty(keyword)) {
			handler.handle(new AsyncResult(true, data));
			return;
		}
		JsonArray result = new JsonArray();
		Pattern pattern = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
		for (int i = 0; i < this.data.size(); i++) {
			JsonObject rule = this.data.get(i).getAsJsonObject();
			String title = rule.get("title").getAsString();
			if (pattern.matcher(title).find()) {
				result.add(rule);
			}
		}
		handler.handle(new AsyncResult(true, result));
	}

	private void initData() {
		try {
			InputStream resourceAsStream = RuleDataService.class.getResourceAsStream("/js/rule.js");
			InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream, "utf-8");
			String content = FileUtil.loadTextAndClose(inputStreamReader);
			if (StringUtils.isNotEmpty(content)) {
				Pattern compile = Pattern.compile("\\{\r\n([\\d\\D]*?)\r\n *?}");
				Matcher matcher = compile.matcher(content);
				Pattern contentPattern = Pattern.compile("title: \'([\\d\\D]*?)\',[\\d\\D]*?rule: ([\\d\\D]*?),\r\n[\\d\\D]*?examples: \\[([\\d\\D]*?)]");
				while (matcher.find()) {
					String group = matcher.group(1);
					Matcher matcher1 = contentPattern.matcher(group);
					if (matcher1.find()) {
						String title = matcher1.group(1);
						String rule = matcher1.group(2);
						String examples = matcher1.group(3);
						JsonObject ruleObj = new JsonObject();
						ruleObj.addProperty("title", title);
						ruleObj.addProperty("rule", rule);
						ruleObj.addProperty("examples", examples);
						this.data.add(ruleObj);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
