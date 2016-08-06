package com.zhangzhihao.SpringMVCSeedProject.Controller;

import com.zhangzhihao.SpringMVCSeedProject.Service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import static com.zhangzhihao.SpringMVCSeedProject.Utils.LogUtils.LogToDB;

@Controller
@RequestMapping("/Log")
public class LogController {
    @Autowired
    private LogService logService;

    /**
     * 日志统计界面
     *
     * @return 日志统计界面
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String logPage() {
        return "Log/Log";
    }

    /**
     * 获得日志的错误信息，日志条数
     * @return json数据
     */
    @RequestMapping("/getLogInfo")
    @ResponseBody
    public Map<String,Long> getLogInfo(){
        Map<String,Long> map=new HashMap<>();
        Long LogUtilsCount = logService.getExceptionCountByCallerFilename("LogUtils.java");//Controller出了异常
        Long LogAspectCount = logService.getExceptionCountByCallerFilename("LogAspect.java");//自定义类异常
        long totalCount = logService.getExceptionCount();
        Long otherCount = totalCount - LogAspectCount - LogUtilsCount;
        map.put("totalCount", totalCount);
        map.put("LogUtilsCount", LogUtilsCount);
        map.put("LogAspectCount", LogAspectCount);
        map.put("otherCount", otherCount);
        return map;
    }

    /**
     * 日志分页查询
     *
     * @param pageNumber 页码
     * @param pageSize   每页大小
     * @return json数据
     */
    @RequestMapping(value = "/getLogByPage", method = RequestMethod.GET)
    @ResponseBody
    public Object getLogByPage(@RequestParam int pageNumber,
                                  @RequestParam int pageSize) {
        try {
            return logService.getListByPage(pageNumber, pageSize);
        } catch (Exception e) {
            LogToDB(e);
            return "";
        }
    }

}
