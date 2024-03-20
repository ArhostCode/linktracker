package edu.java.util.retry;

import edu.java.util.retry.RetryPolicy;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

@UtilityClass
public class RetryFilterCreator {

    public static ExchangeFilterFunction create(RetryPolicy policy) {
        return (response, next) -> next.exchange(response)
            .flatMap(clientResponse -> {
                if (clientResponse.statusCode().isError()) {
                    return clientResponse.createError();
                } else {
                    return Mono.just(clientResponse);
                }
            }).retryWhen(policy.retry());
    }

}
