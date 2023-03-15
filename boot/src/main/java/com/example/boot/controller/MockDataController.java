package com.example.boot.controller;

import com.example.boot.constant.CommonConstant;
import com.example.boot.dao.RecordMapper;
import com.example.boot.data.DataScrap;
import com.example.boot.data.HttpRequest;
import com.example.boot.entity.dto.Record;
import com.example.boot.entity.vo.DeviceResponse;
import com.example.boot.entity.vo.HistorysVo;
import com.example.boot.service.impl.DeviceServiceImpl;
import com.example.boot.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lishuai
 * @since 2023/3/11
 */
@RestController
@RequestMapping(value = "/api/data")
public class MockDataController {
    public static String token = "/KfKIFu+USTe4bk6MbApucs3+FWDIbDUKVCM43WXkvkXDsOKsBO5NjjphxiepCWc";
    public static String replaceUrl = "https://platform.hzcjkj.com";

    private static final Logger LOGGER = LoggerFactory.getLogger(MockDataController.class);
    @Autowired
    private DataScrap dataScrap;
    @Autowired
    private DeviceServiceImpl deviceService;

    @Autowired
    private RecordMapper recordMapper;

    @GetMapping()
    public String updateDataBase(String data) {
        return CommonConstant.SUCCESS;
    }


    @GetMapping("/addDeviceData")
    public String addDeviceData() {
//        String filePath = "./history_1.txt";
        String historyPath = "./history_2.txt";
        String devicePath = "./device.txt";
//        dataScrap.addHistoryDatabaseByFile(filePath);
        dataScrap.addDeviceDatabaseByFile(devicePath);
        return CommonConstant.SUCCESS;
    }

    @GetMapping("/addHistoryData")
    public String addHistoryData() {
//        String filePath = "./history_1.txt";
        String historyPath = "./history_1.txt";
//        String devicePath = "./device.txt";
        dataScrap.addHistoryDatabaseByFile(historyPath);
//        dataScrap.addDeviceDatabaseByFile(devicePath);
        return CommonConstant.SUCCESS;
    }


    @PostMapping("/updateHistoryData")
    public String updateHistoryData(int startTime, int endTime, boolean updateRepeat) {

        List<HistorysVo> historysVos = dataScrap.collectHistoryDataToObject(startTime, endTime);
        LOGGER.info("查询到的历史数据数量：{}", historysVos.size());
//        1671638400  1671724800
        int index = 0;
        for (HistorysVo historysVo : historysVos) {
            index++;
            dataScrap.dealHistoryVoAndInsertOrUpdate(index, historysVo, updateRepeat);
        }
        refreshCache();
        return CommonConstant.SUCCESS;
    }

    @GetMapping("/refreshCache")
    public void refreshCache() {

        deviceService.refreshCache();
    }

    @GetMapping("update-device")
    public void updateDevices() {
        String url = replaceUrl + "/api/devices?" + "size=" + 20 + "&current=" + 0 + "&deviceType=" + "JBZ";
        String s = HttpRequest.sendGet(url, "", token);
        DeviceResponse deviceResponse = JsonUtils.parseObject(s, DeviceResponse.class);

        recordMapper.delete(null);

        Record[] records = deviceResponse.getResult().getRecords();
        for (Record record : records) {
            recordMapper.insert(record);
        }
        recordMapper.selectList(null);
        LOGGER.info("刷新设备数据成功");
    }


    @GetMapping("/update-history-data")
    public void updateHistoryDataByFile() {
        // 污水泵项目

        //顺序号
        String order = "1";
        // 排桩号
        String pileDiscribe = "510";
        // 开始时间
        long beginTime = 123123;
        // 结束时间
        long endTime = 123123;
        // 施工总桩长  DEPTH
        double depth = 24.05;

        // 电流(A)  MAX_CURRENT  PILE_TOP_CURRENT

        // 第一次下钻速度
        // 第一次上升速度
        // 第二次下钻速度
        // 第二次上升速度
        // 压力   AVERAGE_PRESSURE
        // 流量
        // 单桩喷浆量  CUMULATIVE_PULP
        // 垂直度    Y_ANGEL
        // 水泥浆比重
        // 水泥用量  CUMULATIVE_ASH




        HistorysVo historysVo = new HistorysVo();


    }


//    @GetMapping("auto-update")
//    public void autoUpdatedata(){
//        //
//        long currentTime = CommonUtils.getCurrentTime();
//        long originTime =
//
//    }
}
