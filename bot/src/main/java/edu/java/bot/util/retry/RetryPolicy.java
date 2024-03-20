package edu.java.bot.util.retry;

import reactor.util.retry.Retry;

public record RetryPolicy(Retry retry) {
}
