/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.pulsar.reactive.client.api;

import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.pulsar.client.api.Message;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public interface GenericMessagePipelineBuilder<T, P extends Publisher<Message<T>>>  {
	OneByOneMessagePipelineBuilder<T, P> messageHandler(Function<Message<T>, Publisher<Void>> messageHandler);

	GenericMessagePipelineBuilder<T, P> streamingMessageHandler(
			Function<P, Publisher<MessageResult<Void>>> streamingMessageHandler);

	GenericMessagePipelineBuilder<T, P> transformPipeline(Function<Mono<Void>, Publisher<Void>> transformer);

	GenericMessagePipelineBuilder<T, P> pipelineRetrySpec(Retry pipelineRetrySpec);

	ReactiveMessagePipeline build();

	interface OneByOneMessagePipelineBuilder<T, P extends Publisher<Message<T>>> extends GenericMessagePipelineBuilder<T, P> {

		OneByOneMessagePipelineBuilder<T, P> handlingTimeout(Duration handlingTimeout);

		OneByOneMessagePipelineBuilder<T, P> errorLogger(BiConsumer<Message<T>, Throwable> errorLogger);

		ConcurrentOneByOneMessagePipelineBuilder<T, P> concurrent();

	}

	interface ConcurrentOneByOneMessagePipelineBuilder<T, P extends Publisher<Message<T>>> extends OneByOneMessagePipelineBuilder<T, P> {

		ConcurrentOneByOneMessagePipelineBuilder<T, P> useKeyOrderedProcessing();

		ConcurrentOneByOneMessagePipelineBuilder<T, P> groupOrderedProcessing(MessageGroupingFunction groupingFunction);

		ConcurrentOneByOneMessagePipelineBuilder<T, P> concurrency(int concurrency);

		ConcurrentOneByOneMessagePipelineBuilder<T, P> maxInflight(int maxInflight);

	}

}
