package cn.ywrby.controller;

import cn.ywrby.domain.UEditorFile;
import cn.ywrby.utils.UEditorUpload;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditor.ActionEnter;
import com.fasterxml.jackson.annotation.JsonView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UeditorController {

    @Autowired
    private UEditorUpload uEditorUpload;

    //配置ueditor后端上传接口的验证
    @RequestMapping("/config")
    public String config(HttpServletRequest request, HttpServletResponse response, String action, MultipartFile upfile) throws IOException {
        if (action.equals("config")) {
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html");
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();

            PrintWriter printWriter = response.getWriter();
            printWriter.write(new ActionEnter(request, path).exec());
            printWriter.flush();
            printWriter.close();
        } else if (action.equals("uploadimage")) {
            UEditorFile editorFile = uEditorUpload.uploadImage(upfile);
            JSONObject jsonObject = new JSONObject((Map<String, Object>) editorFile);
            return jsonObject.toString();
        }
        return null;
    }
}


