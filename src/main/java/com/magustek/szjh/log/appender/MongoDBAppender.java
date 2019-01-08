package com.magustek.szjh.log.appender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.magustek.szjh.log.entity.LogEntity;
import com.magustek.szjh.utils.ClassUtils;
import com.magustek.szjh.utils.ContextUtils;
import com.magustek.szjh.utils.SpringUtils;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * log appender，用于向MongoDB记录日志
 *
 * 由于日志功能初始化时，Spring容器尚未完成初始化，该类无法使用任何Spring容器提供的功能，
 * 无法便捷的使用【配置文件】或【数据库】进行配置，该问题待解决。
 * */
@Component
public class MongoDBAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

//    @Value("${logger.mongodb.host}")
//    private String host = "10.154.70.121";
//    @Value("${logger.mongodb.port}")
//    private int port = 27017;
//    @Value("${logger.mongodb.database}")
//    private String database = "szjh";

    private MongoTemplate mongoTemplate;

    @Override
    protected void append(ILoggingEvent eventObject) {
        MongoTemplate mongoTemplate = SpringUtils.getBean(MongoTemplate.class);
        if (mongoTemplate != null) {
            LogEntity doc = new LogEntity();
            String time = ClassUtils.dfFullTime.format(new Date(eventObject.getTimeStamp()));

            doc.setTime(time);
            doc.setLevel(eventObject.getLevel().toString());
            doc.setLogger(eventObject.getLoggerName());
            doc.setThread(eventObject.getThreadName());
            doc.setMessage(eventObject.getFormattedMessage());

            mongoTemplate.insert(doc, "logEntity");
        }
    }

    /**
     * 由于MongoDB在创建连接的时候，会向MongoDB写日志，造成死循环，在初始化日志Appender的时候，手动创建连接。
     * */
//    @Override
//    public void start(){
//        super.started = true;
//
//        MongoClientOptions settings = MongoClientOptions.builder()
//                .codecRegistry(MongoClient.getDefaultCodecRegistry())
//                .build();
//        this.mongoTemplate = new MongoTemplate(new MongoClient(new ServerAddress(host, port), settings), database);
//
//        LogEntity doc = new LogEntity();
//        String time = ClassUtils.dfFullTime.format(Calendar.getInstance().getTime());
//        doc.setTime(time);
//        doc.setLevel("INFO");
//        doc.setLogger(this.getClass().toString());
//        doc.setThread(Thread.currentThread().getName());
//        doc.setMessage("【初始化】 MongoDBAppender ！");
//
//        mongoTemplate.insert(doc, "logEntity");
//    }
}
