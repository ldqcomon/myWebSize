package com.it.sf.controller;

import com.it.sf.common.FileUtils;
import com.it.sf.common.JsonData;
import com.it.sf.model.MusicChannelVo;
import com.it.sf.model.MusicSongVo;
import com.it.sf.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


/**
 * @Auther: ldq
 * @Date: 2020/9/21
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    MusicService musicService;

    // 上传图片
    @RequestMapping("/song")
    public JsonData uploadImages(@RequestParam(value = "file",required = false)MultipartFile file,
                                 @RequestParam(value = "fileType",required = false)String fileType) {
        String fileName = FileUtils.upload(fileType, file);
        return JsonData.success(fileName);
    }

}
