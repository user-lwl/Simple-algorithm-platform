package com.lwl.controller;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwl.pojo.Algo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("algo")
public class AlgoController {
//    private static final String BASE_DIR = "D:\\webstorm_project\\";
    @Value("${base-dir}")
    private String baseDir;

//    @GetMapping("baseDir")
//    public String baseDir(){
//        return baseDir;
//    }

    @GetMapping("list")
    public String list() throws Exception{
        //System.out.println(1 / 0);
        //假设从数据库加载算法数据

        List<Algo> algos = new ArrayList<>();
        algos.add(new Algo("语音识别", "能将语音转成对应的文字", "voice.html"));
        algos.add(new Algo("人脸识别", "能准确识别出人脸信息", "face.html"));
        algos.add(new Algo("车牌识别", "能准确识别出黄牌、蓝牌、绿牌等", "carPlateNo.html"));
        algos.add(new Algo("动物识别", "能准确识别出动物的品种", "animal.html"));

        // 将数据库中的数据，转为JSON字符串
        return new ObjectMapper().writeValueAsString(algos);

    }
    @PostMapping("animal")
    public String animal(MultipartFile image) throws Exception{
        // 获取文件扩展名
        String ext = FilenameUtils.getExtension(image.getOriginalFilename());
        // 随机文件夹名
        String dirName = UUID.randomUUID().toString();
        // 图片要写入的文件路径
        String filepath = baseDir + "images" + File.separator + dirName + "." + ext;
        File imgFile = new File(filepath);
        // 生成目录
        imgFile.getParentFile().mkdirs();
        // 将客户端上传的图片数据，写入到服务器的硬盘上
        image.transferTo(new File(filepath));

//        System.out.println(new Date() + ">>>>>>>>开始执行python");

        // 存放算法参数和结果的文件夹
//        String dir = baseDir + "test\\" + dirName;
        String dir = baseDir + dirName;

        // 写入参数到params.json
        String paramsFilepath = dir + File.separator + "params.json";
        Map<String, String> params = new HashMap<>();
        params.put("image", filepath);
        String paramsString = new ObjectMapper().writeValueAsString(params);
        FileUtils.writeStringToFile(new File(paramsFilepath), paramsString, StandardCharsets.UTF_8);

        // 调用python代码
//        String py = "D:\\py_project\\ai_box_algo\\animal.py";
        String py = baseDir + "ai_box_algo" + File.separator + "animal.py";
        String cmd =String.format("python %s -path %s", py, dir);

        System.out.println("[cmd]" + cmd);

        // 通过命令行调用python代码
        Process process = Runtime.getRuntime().exec(cmd);
        // 获取python的输出（print）
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"))){
            String line;
            while ((line = reader.readLine()) != null){
                System.out.println("[python]" + line);
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"))){
            String line;
            while ((line = reader.readLine()) != null){
                System.err.println("[python]" + line);
            }
        }

        // 等待python代码执行完毕
        process.waitFor();

//        System.out.println(new Date() + ">>>>>>>>python执行完毕");

        // 读取python算法的执行结果
        String resultsFilepath = dir + File.separator + "results.json";

        return FileUtils.readFileToString(new File(resultsFilepath), StandardCharsets.UTF_8);
    }
}
