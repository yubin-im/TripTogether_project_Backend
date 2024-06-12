package com.hanaro.triptogether.exchangeRate.scheduler;

import com.hanaro.triptogether.exchangeRate.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ExchangeBatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job job;

    private final ExchangeService exchangeService;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void resetNotifiedFlags() {
        exchangeService.resetNotifiedFlags();
    }

//    @Scheduled(cron = "* * * * * *")
    @Transactional
    public void run() throws Exception {
        JobParameters parameters = new JobParametersBuilder()
                .addString("jobName","exchangeJob"+System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(job,parameters);
    }


}
