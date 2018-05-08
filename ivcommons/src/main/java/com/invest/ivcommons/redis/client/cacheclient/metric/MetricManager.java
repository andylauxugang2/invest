package com.invest.ivcommons.redis.client.cacheclient.metric;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Created by admin on 2017/5/17.
 *
 */
public class MetricManager {
    protected static Logger logger = LoggerFactory.getLogger(MetricManager.class);

    private static Gson gson=new Gson();

    private static AtomicReference<MetricInfo> metricInfoAtomic = new AtomicReference<>(new MetricInfo());
   private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    static {
        executorService.submit(()->{

            while (true) {
                try {
                    MetricInfo oldInfo = metricInfoAtomic.getAndSet(new MetricInfo());
                    MetricDao dao=new MetricDao();
                    dao.setSuccess(oldInfo.getSuccess().get());
                    dao.setError(oldInfo.getError().get());

                    List<SlowCommand> slowCommands= oldInfo.getSlowCommand().values().stream()
                            .sorted((p1,p2)->Long.compare(p2.getTime(),p1.getTime()))
                          .limit(20).collect(Collectors.toList());
                    dao.setSlowCommandList(slowCommands);

                    List<MetricExp> expList= oldInfo.getExpMap().values().stream()
                            .sorted((p1,p2)->Long.compare(p2.getCount().get(),p1.getCount().get()))
                            .limit(20).collect(Collectors.toList());
                    dao.setMetricExpList(expList);

                    Map<String,Integer> maxKey=oldInfo.getKeyCount().entrySet().stream()
                            .sorted((p1,p2)->Integer.compare(p2.getValue(),p1.getValue()))
                            .limit(20)
                            .collect(Collectors.toMap(p->p.getKey(),p->p.getValue()));
                    dao.setMaxKeys(maxKey);

                    String jsonString = gson.toJson(dao);
                    // logger.metric(jsonString);
                    Thread.sleep(5000);
                } catch (Throwable t) {
                    logger.error(t.getMessage(),t);
                }
            }
        });
    }

    public static void addslowCommand(String commandName, String key,long time) {
        metricInfoAtomic.get().addSlowCommand(commandName, key,time);
    }

    public static void addException(String commandName, String key, Throwable throwable)
    {
        metricInfoAtomic.get().addException(commandName,key,throwable);
    }

    public static void addSuccess()
    {
        metricInfoAtomic.get().addSuccess();
    }

    public static void addError()
    {
        metricInfoAtomic.get().addError();
    }


    public static void addKey(String key) {
        metricInfoAtomic.get().addkey(key);
    }
}
