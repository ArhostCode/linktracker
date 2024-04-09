package edu.java.bot.util.retry;

import edu.java.bot.configuration.RetryConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

@UtilityClass
public class RetryFactory {

    private static final Map<String, Function<RetryConfig.RetryElement, Retry>> RETRY_BUILDERS = new HashMap<>();

    static {
        RETRY_BUILDERS.put(
            "fixed",
            retryElement -> RetryBackoffSpec.fixedDelay(retryElement.maxAttempts(), retryElement.minDelay())
                .filter(buildErrorFilter(retryElement.codes()))
        );
        RETRY_BUILDERS.put(
            "exponential",
            retryElement -> RetryBackoffSpec.backoff(retryElement.maxAttempts(), retryElement.minDelay())
                .maxBackoff(retryElement.maxDelay()).filter(buildErrorFilter(retryElement.codes()))
        );
        RETRY_BUILDERS.put(
            "linear",
            retryElement -> LinearRetryBackoffSpec.linear(retryElement.maxAttempts(), retryElement.minDelay())
                .factor(retryElement.factor()).filter(buildErrorFilter(retryElement.codes()))
        );
    }

    public static ExchangeFilterFunction createFilter(RetryConfig config, String target) {
        return (response, next) -> next.exchange(response)
            .flatMap(clientResponse -> {
                if (clientResponse.statusCode().isError()
                    && config.targets().get(target).codes().contains(clientResponse.statusCode().value())) {
                    return clientResponse.createError();
                } else {
                    return Mono.just(clientResponse);
                }
            }).retryWhen(createRetry(config, target));
    }

    public static Retry createRetry(RetryConfig config, String target) {
        RetryConfig.RetryElement element = config.targets().get(target);
        return RETRY_BUILDERS.get(element.type()).apply(element);
    }

    private static Predicate<Throwable> buildErrorFilter(List<Integer> retryCodes) {
        return retrySignal -> {
            if (retrySignal instanceof WebClientResponseException e) {
                return retryCodes.contains(e.getStatusCode().value());
            }
            return true;
        };
    }

}
