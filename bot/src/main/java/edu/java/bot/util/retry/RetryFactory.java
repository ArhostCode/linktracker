package edu.java.bot.util.retry;

import edu.java.bot.configuration.RetryConfig;
import java.util.List;
import java.util.function.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

@UtilityClass
public class RetryFactory {

    public static ExchangeFilterFunction createFilter(Retry retry) {
        return (response, next) -> next.exchange(response)
            .flatMap(clientResponse -> {
                if (clientResponse.statusCode().isError()) {
                    return clientResponse.createError();
                } else {
                    return Mono.just(clientResponse);
                }
            }).retryWhen(retry);
    }

    public static Retry createRetry(RetryConfig config, String target) {
        return config.targets().stream().filter(element -> element.target().equals(target)).findFirst()
            .map(RetryFactory::createRetry).orElseThrow(() -> new IllegalStateException("Unknown target " + target));
    }

    private static Retry createRetry(RetryConfig.RetryElement retryElement) {
        return switch (retryElement.type()) {
            case "fixed" -> RetryBackoffSpec.fixedDelay(retryElement.maxAttempts(), retryElement.minDelay())
                .filter(buildErrorFilter(retryElement.codes()));
            case "exponential" -> RetryBackoffSpec.backoff(retryElement.maxAttempts(), retryElement.minDelay())
                .filter(buildErrorFilter(retryElement.codes())).maxBackoff(retryElement.maxDelay());
            case "linear" -> LinearRetryBackoffSpec.linear(retryElement.maxAttempts(), retryElement.minDelay())
                .filter(buildErrorFilter(retryElement.codes())).factor(retryElement.factor());
            default -> throw new IllegalStateException("Unknown retry type " + retryElement.type());
        };
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
