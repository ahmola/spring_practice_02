package com.example.prac2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
public class ConvertorService {

    public String webmDownloader(String ytDlpPath, String webmPath, String videoUrl){
        try{
            ProcessBuilder webmBuilder = new ProcessBuilder(ytDlpPath, "--extract-audio",
                    "--audio-format", "mp3", "-o", webmPath + "%(title)s.%(ext)s", videoUrl);
            webmBuilder.redirectErrorStream(true);
            Process webmProcess = webmBuilder.start();
            webmProcess.waitFor();

            return getLatestFile(webmPath, ".webm").toString();
        }catch (Exception e){
            throw new RuntimeException("Error occur while processing webmFile : " + e.getMessage());
        }
    }

    public void mp3Convertor(String ffmpegPath, String webmPath, String mp3Path){
        try{
            String fileName = webmPath.substring(webmPath.lastIndexOf('/')+1);
            int index = fileName.lastIndexOf('.');
            if(index > 0)
                fileName = fileName.substring(0, index);
            log.info(fileName);

            ProcessBuilder mp3Builder = new ProcessBuilder(ffmpegPath, "-i", webmPath,
                    "-q:a", "0", "-map", "a", mp3Path + fileName + ".mp3");
            mp3Builder.redirectErrorStream(true);
            Process mp3Process = mp3Builder.start();
            mp3Process.waitFor();
        }catch (Exception e){
            throw new RuntimeException("Error occurs during converting to mp3File : " + e.getMessage());
        }
    }

    public void webmFileDeleter(String webmPath){
        File webmFile = new File(webmPath);
        if(webmFile.exists()){
            try {
                webmFile.delete();
                log.info("Successfully delete webmFile");
            }catch (Exception e){
                throw new RuntimeException("File Deleting Process Failed. " + e.getMessage());
            }
        }else{
            throw new RuntimeException("There is no such File : " + webmPath);
        }
    }

    public File getLatestFile(String directoryPath, String filePrefix){
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(filePrefix));

        if(files == null || files.length == 0)
            return null;

        File latestFile = files[0];
        for(File file : files){
            if(file.lastModified() > latestFile.lastModified()){
                latestFile = file;
            }
        }

        return latestFile.getAbsoluteFile();
    }
}
