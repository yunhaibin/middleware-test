package com.volcengine;

import com.volcengine.model.tls.*;
import com.volcengine.model.tls.exception.LogException;
import com.volcengine.model.tls.request.*;
import com.volcengine.model.tls.response.*;
import com.volcengine.service.tls.TLSLogClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.volcengine.model.tls.Const.LZ4;

@SpringBootTest
class ApplicationTests {

    static String endPoint = "https://tls-cn-shanghai.volces.com";
    static String accessKey = "AKLTM2ViOTM3NzhmYzZhNDk1NGEwYjgyYTQ4ZmVmOWM0N2Q";
    static String secretKey = "TnpSa1lUZGhNV1JqTURZd05HTXpNVGszTlRVM1l6TXpNak0wTmpKallqRQ==";
    static String region = "cn-shanghai";
    static String token = "";
    protected static final ClientConfig clientConfig = new ClientConfig(endPoint, region, accessKey,
            secretKey, token);
    protected static TLSLogClient client;

    static {
        try {
            client = ClientBuilder.newClient(clientConfig);
        } catch (LogException e) {
            e.printStackTrace();
        }
    }

    @Test
    void contextLoads() {
    }

    @Test
    void testPutLogs() throws LogException {
        String topicId = "7396f0cc-5f3f-4cde-94ea-b6b7dded8ddc";

        SimpleDateFormat sdf = new SimpleDateFormat(Const.DATE_FORMAT);
        Date date = new Date();
        long currentTimeMillis = date.getTime();
        String formatDate = sdf.format(date);
        int limit = 10;
        for (int index = 0; index < limit; index++) {
            // put logs
            List<LogItem> logs = new ArrayList<>();
            currentTimeMillis = System.currentTimeMillis();
            LogItem item = new LogItem(currentTimeMillis);
//            item.addContent("index", "" + index);
//            item.addContent("test-key", "test-value");
            item.addContent("log-content","[20230825 17:39:42.456] [INFO ] (server.go:146) serverType:1, serverId:1002 sid:9007199254740995 addr:192.168.12.6:49300 Close");
            logs.add(item);
            PutLogsRequestV2 request = new PutLogsRequestV2(logs, topicId, null, LZ4, "test-path", "test-file");
            PutLogsResponse putLogsResponse = client.putLogsV2(request);
            System.out.println("put logs success,response:" + putLogsResponse);

        }
    }

//    static String endPoint = System.getenv("endPoint");
//    static String accessKey = System.getenv("ak");
//    static String secretKey = System.getenv("sk");
//    static String region = System.getenv("region");
//    static String token = System.getenv("token");
//    //配置Endpoint、Region、AccessKeyID、AccessKeySecret和securityToken。其中，securityToken为通过IAM的STS机制获取的临时Token，使用临时Token时也应传入临时的AK、SK；不使用临时Token时securityToken传空即可。
//    protected static final ClientConfig clientConfig = new ClientConfig(endPoint, region, accessKey,
//            secretKey, token);
//    protected static TLSLogClient client;
//
//    static {
//        try {
//            client  = ClientBuilder.newClient(clientConfig);
//        } catch (LogException e) {
//            e.printStackTrace();
//        }
//    }
//    public static void main(String[] args) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DATE_FORMAT);
//        String prefix = "test";
//        String separator = "-";
//        String date = dateFormat.format(new Date());
//        try {
//            //创建日志项目
//            long currentTimeMillis = System.currentTimeMillis();
//            String projectName = prefix + separator + date + separator + currentTimeMillis;
//            String region = "your-region";
//            String description = "test project";
//            CreateProjectRequest project = new CreateProjectRequest(projectName, region, description);
//            CreateProjectResponse createProjectResponse = client.createProject(project);
//            System.out.println("create project success,response:" + createProjectResponse);
//            //创建日志主题
//            String topicName = prefix + separator + date + separator + currentTimeMillis;
//            CreateTopicRequest createTopicRequest = new CreateTopicRequest();
//            createTopicRequest.setTopicName(topicName);
//            createTopicRequest.setProjectId(createProjectResponse.getProjectId());
//            createTopicRequest.setTtl(500);
//            CreateTopicResponse createTopicResponse = client.createTopic(createTopicRequest);
//            System.out.println("create topic success,response:" + createTopicResponse);
//
//            //创建日志主题索引
//            CreateIndexRequest createIndexRequest = new CreateIndexRequest(createTopicResponse.getTopicId(),
//                    new FullTextInfo(false, ",-;", false), null);
//            CreateIndexResponse createIndexResponse = client.createIndex(createIndexRequest);
//            System.out.println("create index success,response:" + createIndexResponse);
//
//            //写入日志
//            PutLogRequest.LogContent logContent = PutLogRequest.LogContent.newBuilder().setKey("test-key-" +
//                    currentTimeMillis).setValue("test-value").build();
//            PutLogRequest.Log log = PutLogRequest.Log.newBuilder().setTime(currentTimeMillis).
//                    addContents(logContent).build();
//            PutLogRequest.LogGroup logGroup = PutLogRequest.LogGroup.newBuilder().
//                    setSource("test-source-" + currentTimeMillis).setFileName("test5.txt").addLogs(log).build();
//            PutLogRequest.LogGroupList logGroupList = PutLogRequest.LogGroupList.newBuilder().
//                    addLogGroups(logGroup).build();
//            String topicId = createTopicResponse.getTopicId();
//            PutLogsRequest putLogsRequest = new PutLogsRequest(logGroupList, topicId);
//            putLogsRequest.setCompressType(Const.LZ4);
//            PutLogsResponse putLogsResponse = client.putLogs(putLogsRequest);
//            System.out.println("put logs success,response:" + putLogsResponse);
//            DescribeCursorRequest describeCursorRequest =
//                    new DescribeCursorRequest(topicId, 0, "1656604800");
//            DescribeCursorResponse describeCursorResponse = client.describeCursor(describeCursorRequest);
//            System.out.println("describe cursor success,response:" + describeCursorResponse);
//
//            ConsumeLogsRequest consumeLogsRequest = new ConsumeLogsRequest();
//            consumeLogsRequest.setTopicId(topicId);
//            consumeLogsRequest.setShardId(0);
//            consumeLogsRequest.setLogGroupCount(1);
//            consumeLogsRequest.setCursor(describeCursorResponse.getCursor());
//            ConsumeLogsResponse consumeLogsResponse = client.consumeLogs(consumeLogsRequest);
//            System.out.println(String.format("consume logs success requestId:%s,cursor:%s,cursorCnt:%d.",
//                    consumeLogsResponse.getRequestId(), consumeLogsResponse.getXTlsCursor(),
//                    consumeLogsResponse.getXTlsCount()));
//            consumeLogsRequest.setCompression(Const.LZ4);
//            consumeLogsResponse = client.consumeLogs(consumeLogsRequest);
//            System.out.println(String.format("consume logs success requestId:%s,cursor:%s,cursorCnt:%d.",
//                    consumeLogsResponse.getRequestId(), consumeLogsResponse.getXTlsCursor(),
//                    consumeLogsResponse.getXTlsCount()));
//        } catch (LogException e) {
//            e.printStackTrace();
//        }
//    }

}
