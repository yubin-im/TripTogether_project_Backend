package com.hanaro.triptogether.exchangeRate.batch;

import com.hanaro.triptogether.exchangeRate.dto.ExchangeDto;
import com.hanaro.triptogether.exchangeRate.service.ExchangeService;
import com.hanaro.triptogether.exchangeRate.utils.ExchangeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ExchangeBatch {

    private final ExchangeUtils exchangeUtils;
    private final ExchangeService exchangeService;

    @Bean
    public Job exchangeJob(JobRepository jobRepository,Step step) {
        return new JobBuilder("exchangeJob",jobRepository)
                .start(step)
                .build();
    }

    @Bean
    @JobScope
    public Step step(JobRepository jobRepository, Tasklet tasklet, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step",jobRepository)
                .tasklet(tasklet,platformTransactionManager).build();
    }


    @Bean
    public Tasklet tasklet() {
        return (((contribution, chunkContext) -> {

            List<ExchangeDto> exchangeDtoList = exchangeUtils.getExchangeDataAsDtoList();

            for(ExchangeDto exchangeDto: exchangeDtoList){
                System.out.println("통화코드 :" + exchangeDto.getCur_unit());
                System.out.println("환율 : "+ exchangeDto.getDeal_bas_r());
                exchangeService.saveExchangeRate(exchangeDto.getCur_unit(),exchangeDto.getDeal_bas_r());

            }

            exchangeService.checkNotifyAlarms();
            return RepeatStatus.FINISHED;
        }));
    }


}
