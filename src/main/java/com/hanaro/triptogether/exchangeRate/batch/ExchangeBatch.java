package com.hanaro.triptogether.exchangeRate.batch;

import com.hanaro.triptogether.exchangeRate.dto.ExchangeDto;
import com.hanaro.triptogether.exchangeRate.utils.ExchangeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
@RequiredArgsConstructor
public class ExchangeBatch {

    private final ExchangeUtils exchangeUtils;

    @Bean
    public Job exchangeJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("exchangeJob",jobRepository)
                .start(step)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, Tasklet tasklet, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("step",jobRepository)
                .tasklet(tasklet,platformTransactionManager).build();
    }

    @Bean
    public Tasklet tasklet() {
        return (((contribution, chunkContext) -> {
            List<ExchangeDto> exchangeDtoList = exchangeUtils.getExchangeDataAsDtoList();

            for(ExchangeDto exchangeDto: exchangeDtoList){
                System.out.println("통화 :" + exchangeDto.getCur_nm());
                System.out.println("환율 : "+exchangeDto.getDeal_bas_r());
            }
            return RepeatStatus.FINISHED;
        }));
    }


}
