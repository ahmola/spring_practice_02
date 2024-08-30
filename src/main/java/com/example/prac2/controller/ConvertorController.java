package com.example.prac2.controller;

import com.example.prac2.service.ConvertorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ConvertorController {

    private final ConvertorService convertorService;

    @Value("${path.download_path}")
    private String download_path;

    @Value("${path.ytdlp_path}")
    private String ytdlp_path;

    @Value("${path.ffmpeg_path}")
    private String ffmpeg_path;

    @PostMapping("/download")
    public ResponseEntity<String> downloadVideo(@RequestParam("url") String videoUrl){
        try{
            String ytDlpPath = ytdlp_path;
            String ffmpegPath = ffmpeg_path;

            String webmPath = download_path;
            String mp3Path = download_path;

            webmPath = convertorService.webmDownloader(ytDlpPath, webmPath, videoUrl);

            convertorService.mp3Convertor(ffmpegPath, webmPath, mp3Path);

            convertorService.webmFileDeleter(webmPath);

            return new ResponseEntity<>("Video has been successfully download" +
                    " and converted to MP3. Check your downloads folder", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error occurs during download or conversion : " + e.getMessage());
        }
    }
}
