package edu.java.bot.configuration;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import edu.java.bot.util.retry.LinearRetryBackoffSpec;
import edu.java.bot.util.retry.RetryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.RetryBackoffSpec;

@Configuration
public class RetryConfiguration {

    @Bean
    @ConditionalOnProperty(name = "retry.type", havingValue = "fixed")
    public RetryPolicy fixedRetry(
        @Value("${retry.max-attempts}") int maxAttempts,
        @Value("${retry.min-delay}") Duration minDelay,
        @Value("${retry.codes}") List<Integer> retryCodes
    ) {
        return new RetryPolicy(
            RetryBackoffSpec.fixedDelay(maxAttempts, minDelay)
                .filter(buildErrorFilter(retryCodes))
        );
    }

    @Bean
    @ConditionalOnProperty(name = "retry.type", havingValue = "exponential")
    public RetryPolicy exponentialRetry(
        @Value("${retry.max-attempts:5}") int maxAttempts,
        @Value("${retry.min-delay:1s}") Duration minDelay,
        @Value("${retry.max-delay:10s}") Duration maxDelay,
        @Value("${retry.codes}") List<Integer> retryCodes
    ) {
        return new RetryPolicy(
            RetryBackoffSpec.backoff(maxAttempts, minDelay)
                .maxBackoff(maxDelay)
                .filter(buildErrorFilter(retryCodes))
        );
    }

    @Bean
    @ConditionalOnProperty(name = "retry.type", havingValue = "linear")
    public RetryPolicy linearRetry(
        @Value("${retry.max-attempts:5}") int maxAttempts,
        @Value("${retry.min-delay:1s}") Duration minDelay,
        @Value("${retry.factor:1.0}") double factor,
        @Value("${retry.codes}") List<Integer> retryCodes
    ) {
        return new RetryPolicy(
            LinearRetryBackoffSpec.linear(maxAttempts, minDelay)
                .factor(factor)
                .filter(buildErrorFilter(retryCodes))
        );
    }

    private Predicate<Throwable> buildErrorFilter(List<Integer> retryCodes) {
        return retrySignal -> {
            if (retrySignal instanceof WebClientResponseException e) {
                return retryCodes.contains(e.getStatusCode().value());
            }
            return true;
        };
    }
}
