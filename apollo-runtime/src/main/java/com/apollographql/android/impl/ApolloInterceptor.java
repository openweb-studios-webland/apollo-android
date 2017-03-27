package com.apollographql.android.impl;

import com.apollographql.android.api.graphql.Operation;
import com.apollographql.android.api.graphql.Response;
import com.apollographql.android.api.graphql.internal.Optional;
import com.apollographql.android.cache.normalized.Record;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

import javax.annotation.Nonnull;

interface ApolloInterceptor {
  @Nonnull InterceptorResponse intercept(Operation operation, ApolloInterceptorChain chain) throws IOException;

  void interceptAsync(@Nonnull Operation operation, @Nonnull ApolloInterceptorChain chain,
      @Nonnull ExecutorService dispatcher, @Nonnull CallBack callBack);

  void dispose();

  interface CallBack {
    void onResponse(@Nonnull InterceptorResponse response);

    void onFailure(@Nonnull Throwable t);
  }

  final class InterceptorResponse {
    final Optional<okhttp3.Response> httpResponse;
    final Optional<Response> parsedResponse;
    final Optional<Collection<Record>> cacheRecords;

    InterceptorResponse(okhttp3.Response httpResponse) {
      this(httpResponse, null, null);
    }

    InterceptorResponse(okhttp3.Response httpResponse, Response parsedResponse, Collection<Record> cacheRecords) {
      this.httpResponse = Optional.fromNullable(httpResponse);
      this.parsedResponse = Optional.fromNullable(parsedResponse);
      this.cacheRecords = Optional.fromNullable(cacheRecords);
    }
  }
}