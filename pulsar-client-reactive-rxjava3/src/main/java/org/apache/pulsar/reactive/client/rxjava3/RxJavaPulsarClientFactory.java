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

package org.apache.pulsar.reactive.client.rxjava3;

import java.util.function.Function;

import io.reactivex.rxjava3.core.Flowable;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.reactive.client.api.GenericMessageConsumer;
import org.apache.pulsar.reactive.client.api.GenericMessageReader;
import org.apache.pulsar.reactive.client.api.GenericMessageSender;
import org.apache.pulsar.reactive.client.api.MessageResult;
import org.apache.pulsar.reactive.client.api.MessageSpec;
import org.apache.pulsar.reactive.client.api.ReactiveMessageConsumer;
import org.apache.pulsar.reactive.client.api.ReactiveMessageReader;
import org.apache.pulsar.reactive.client.api.ReactiveMessageSender;
import org.apache.pulsar.reactive.client.api.ReactivePulsarClient;
import org.apache.pulsar.reactive.client.internal.api.GenericPulsarClientConverter;
import org.apache.pulsar.reactive.client.internal.api.GenericPulsarClientProxiedInstance;
import org.apache.pulsar.reactive.client.internal.api.GenericPulsarClientProxyFactory;
import org.reactivestreams.Publisher;

public final class RxJavaPulsarClientFactory {

	private static RxJavaPulsarClientConverter CONVERTER_INSTANCE = new RxJavaPulsarClientConverter();

	private RxJavaPulsarClientFactory() {

	}

	public static RxJavaPulsarClient create(ReactivePulsarClient reactivePulsarClient) {
		return GenericPulsarClientProxyFactory.getInstance().createProxiedClient(reactivePulsarClient,
				CONVERTER_INSTANCE);
	}

	private static class RxJavaPulsarClientConverter
			implements GenericPulsarClientConverter<RxJavaPulsarClient, Flowable<?>> {

		@Override
		public Class<RxJavaPulsarClient> getClientType() {
			return RxJavaPulsarClient.class;
		}

		@Override
		public Flowable<?> wrapMany(Publisher<?> publisher) {
			return Flowable.fromPublisher(publisher);
		}

		@Override
		public GenericMessageSender<?, ?, ?> convertMessageSender(ReactiveMessageSender<?> delegate) {
			return new RxJavaMessageSenderImpl(delegate);
		}

		@Override
		public GenericMessageReader<?, ?, ?> convertMessageReader(ReactiveMessageReader<?> delegate) {
			return new RxJavaMessageReaderImpl(delegate);
		}

		@Override
		public GenericMessageConsumer<?, ?, ?> convertMessageConsumer(ReactiveMessageConsumer<?> delegate) {
			return new RxJavaMessageConsumerImpl(delegate);
		}
	}

	private static class RxJavaMessageSenderImpl
			implements RxJavaMessageSender<Object>, GenericPulsarClientProxiedInstance {

		private final ReactiveMessageSender<?> delegate;

		public RxJavaMessageSenderImpl(ReactiveMessageSender<?> delegate) {
			this.delegate = delegate;
		}

		@Override
		public Flowable<MessageId> sendOne(MessageSpec<Object> messageSpec) {
			return Flowable.fromPublisher(delegate.sendOne(MessageSpec.class.cast(messageSpec)));
		}

		@Override
		public Flowable<MessageId> sendMany(Publisher<MessageSpec<Object>> messageSpecs) {
			return Flowable.fromPublisher(delegate.sendMany(Publisher.class.cast(messageSpecs)));
		}

		@Override
		public Object getDelegateInstance() {
			return delegate;
		}

	}

	private static class RxJavaMessageReaderImpl
			implements RxJavaMessageReader<Object>, GenericPulsarClientProxiedInstance {

		private final ReactiveMessageReader<?> delegate;

		public RxJavaMessageReaderImpl(ReactiveMessageReader<?> delegate) {
			this.delegate = delegate;
		}

		@Override
		public Flowable<Message<Object>> readOne() {
			return Flowable.fromPublisher(Publisher.class.cast(delegate.readOne()));
		}

		@Override
		public Flowable<Message<Object>> readMany() {
			return Flowable.fromPublisher(Publisher.class.cast(delegate.readMany()));
		}

		@Override
		public Object getDelegateInstance() {
			return delegate;
		}

	}

	private static class RxJavaMessageConsumerImpl
			implements RxJavaMessageConsumer<Object>, GenericPulsarClientProxiedInstance {

		private final ReactiveMessageConsumer<?> delegate;

		public RxJavaMessageConsumerImpl(ReactiveMessageConsumer<?> delegate) {
			this.delegate = delegate;
		}

		@Override
		public <R> Flowable<R> consumeOne(
				Function<Flowable<Message<Object>>, Publisher<MessageResult<R>>> messageHandler) {
			return Flowable.fromPublisher(delegate.consumeOne(
					messageMono -> messageHandler.apply(Flowable.fromPublisher(Publisher.class.cast(messageMono)))));
		}

		@Override
		public <R> Flowable<R> consumeMany(
				Function<Flowable<Message<Object>>, Publisher<MessageResult<R>>> messageHandler) {
			return Flowable.fromPublisher(delegate.consumeMany(
					messageFlux -> messageHandler.apply(Flowable.fromPublisher(Publisher.class.cast(messageFlux)))));
		}

		@Override
		public Flowable<Void> consumeNothing() {
			return Flowable.fromPublisher(delegate.consumeNothing());
		}

		@Override
		public Object getDelegateInstance() {
			return delegate;
		}
	}


}
