package com.twnch.eachbatch.service.myTest;

import com.google.gson.Gson;
import com.twnch.eachbatch.dao.FlACHPTabRepository;
import com.twnch.eachbatch.dao.model.FlACHPTab;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/")
public class MyRestDao {

    @Autowired
    private FlACHPTabRepository flACHPTabRepository;

    // create 未完成

    @GetMapping("/api/rest")
    public ResponseEntity<List<FlACHPTab>> read() {
        List<FlACHPTab> allData = flACHPTabRepository.findAll();
        return ResponseEntity.ok(allData);
    }

    @PutMapping("/api/rest")
    public ResponseEntity<String> update(@RequestBody Map<String, String> jsonFile) {
        try {
        String updateBatchSeq = jsonFile.get("updateBatchSeq");
        String modifyBatchSeq = jsonFile.get("modifyBatchSeq");
        List<FlACHPTab> allData = flACHPTabRepository.findByBatchSeq(updateBatchSeq);
        for (FlACHPTab flACHPTab : allData) {
            flACHPTab.setBatchSeq(modifyBatchSeq);
        }
        flACHPTabRepository.saveAll(allData);
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("updateBatchSeq", updateBatchSeq);
        returnMap.put("modifyBatchSeq", modifyBatchSeq);
        Gson gson = new Gson();
        return ResponseEntity.ok(gson.toJson(returnMap));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("处理请求时发生错误: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/rest/{restBatchSeq}")
    public ResponseEntity<String> delete(@PathVariable String restBatchSeq){
        List<FlACHPTab> allData = flACHPTabRepository.findByBatchSeq(restBatchSeq);
        flACHPTabRepository.deleteAll(allData);
        Gson gson = new Gson();
        return ResponseEntity.ok(gson.toJson(allData));
    }
}

