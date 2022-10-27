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

package org.apache.pulsar.reactive.client.internal.api;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Function;

import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.reactive.client.api.GenericMessageConsumer;
import org.apache.pulsar.reactive.client.api.GenericMessageReader;
import org.apache.pulsar.reactive.client.api.GenericMessageSender;
import org.apache.pulsar.reactive.client.api.GenericPulsarClient;
import org.apache.pulsar.reactive.client.api.MessageResult;
import org.apache.pulsar.reactive.client.api.ReactiveMessageConsumer;
import org.apache.pulsar.reactive.client.api.ReactiveMessageConsumerBuilder;
import org.apache.pulsar.reactive.client.api.ReactiveMessagePipelineBuilder;
import org.apache.pulsar.reactive.client.api.ReactiveMessageReader;
import org.apache.pulsar.reactive.client.api.ReactiveMessageReaderBuilder;
import org.apache.pulsar.reactive.client.api.ReactiveMessageSender;
import org.apache.pulsar.reactive.client.api.ReactiveMessageSenderBuilder;
import org.apache.pulsar.reactive.client.api.ReactivePulsarClient;
import org.reactivestreams.Publisher;

public final class GenericPulsarClientProxyFactory {

	private static final GenericPulsarClientProxyFactory INSTANCE = new GenericPulsarClientProxyFactory();

	private GenericPulsarClientProxyFactory() {

	}

	public static GenericPulsarClientProxyFactory getInstance() {
		return INSTANCE;
	}

	public <CLIENTTYPE extends GenericPulsarClient<?>> CLIENTTYPE createProxiedClient(ReactivePulsarClient delegate,
			GenericPulsarClientConverter<CLIENTTYPE, ?> converter) {
		Class<CLIENTTYPE> clientType = converter.getClientType();
		ClassLoader classLoader = clientType.getClassLoader();
		return clientType.cast(Proxy.newProxyInstance(classLoader, new Class<?>[] { clientType, GenericPulsarClientProxiedInstance.class },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						switch (method.getName()) {
							case "getDelegateInstance":
								return delegate;
							case "equals":
								return this == args[0];
							case "hashCode":
								return this.hashCode();
							case "toString":
								return wrapToString(clientType, delegate.toString());
							case "messageSender":
								return wrapMessageSenderBuilder(delegate.messageSender((Schema<Object>) args[0]),
										method.getReturnType(), classLoader, converter::convertMessageSender);
							case "messageReader":
								return wrapMessageReaderBuilder(delegate.messageReader((Schema<Object>) args[0]),
										method.getReturnType(), classLoader, converter::convertMessageReader);
							case "messagePipeline":
								return wrapMessagePipelineBuilder(
										delegate.messagePipeline((Schema<Object>) args[0],
												(ReactiveMessageConsumer<?>) ((GenericPulsarClientProxiedInstance) args[1])
														.getDelegateInstance()),
										method.getReturnType(), classLoader, converter::wrapMany);
							case "messageConsumer":
								return wrapMessageConsumerBuilder(delegate.messageConsumer((Schema<Object>) args[0]),
										method.getReturnType(), classLoader, converter::convertMessageConsumer);
						}
						throw new UnsupportedOperationException("Unsupported method call " + method.getName());
					}
				}));
	}

	private static String wrapToString(Class<?> targetType, String toString) {
		return targetType.getName() + " proxy, proxied with " + GenericPulsarClientProxyFactory.class.getSimpleName()
				+ " to [" + toString + "]";
	}

	private Object wrapMessageSenderBuilder(ReactiveMessageSenderBuilder<Object> delegate,
			Class<?> messageSenderBuilderType, ClassLoader classLoader,
			Function<ReactiveMessageSender<?>, GenericMessageSender<?, ?, ?>> converter) {
		return Proxy.newProxyInstance(classLoader, new Class<?>[] { messageSenderBuilderType, GenericPulsarClientProxiedInstance.class },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						switch (method.getName()) {
							case "getDelegateInstance":
								return delegate;
							case "equals":
								return this == args[0];
							case "hashCode":
								return this.hashCode();
							case "toString":
								return wrapToString(messageSenderBuilderType, delegate.toString());
							case "build":
								ReactiveMessageSender<Object> sender = delegate.build();
								return converter.apply(sender);
							default:
								Method delegateMethod = delegate.getClass().getMethod(method.getName(),
										method.getParameterTypes());
								Object retval = delegateMethod.invoke(delegate, args);
								if (retval != delegate) {
									return retval;
								}
								else {
									return this;
								}
						}
					}
				});
	}

	private Object wrapMessageConsumerBuilder(ReactiveMessageConsumerBuilder<Object> delegate,
			Class<?> messageConsumerBuilderType, ClassLoader classLoader,
			Function<ReactiveMessageConsumer<?>, GenericMessageConsumer<?, ?, ?>> converter) {
		return Proxy.newProxyInstance(classLoader, new Class<?>[] { messageConsumerBuilderType, GenericPulsarClientProxiedInstance.class },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						switch (method.getName()) {
							case "getDelegateInstance":
								return delegate;
							case "equals":
								return this == args[0];
							case "hashCode":
								return this.hashCode();
							case "toString":
								return wrapToString(messageConsumerBuilderType, delegate.toString());
							case "build":
								ReactiveMessageConsumer<Object> sender = delegate.build();
								return converter.apply(sender);
							default:
								Method delegateMethod = delegate.getClass().getMethod(method.getName(),
										method.getParameterTypes());
								Object retval = delegateMethod.invoke(delegate, args);
								if (retval != delegate) {
									return retval;
								}
								else {
									return this;
								}
						}
					}
				});
	}

	private Object wrapMessageReaderBuilder(ReactiveMessageReaderBuilder<Object> delegate,
			Class<?> messageReaderBuilderType, ClassLoader classLoader,
			Function<ReactiveMessageReader<?>, GenericMessageReader<?, ?, ?>> converter) {
		return Proxy.newProxyInstance(classLoader, new Class<?>[] { messageReaderBuilderType, GenericPulsarClientProxiedInstance.class },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						switch (method.getName()) {
							case "getDelegateInstance":
								return delegate;
							case "equals":
								return this == args[0];
							case "hashCode":
								return this.hashCode();
							case "toString":
								return wrapToString(messageReaderBuilderType, delegate.toString());
							case "build":
								ReactiveMessageReader<Object> sender = delegate.build();
								return converter.apply(sender);
							default:
								Method delegateMethod = delegate.getClass().getMethod(method.getName(),
										method.getParameterTypes());
								Object retval = delegateMethod.invoke(delegate, args);
								if (retval != delegate) {
									return retval;
								}
								else {
									return this;
								}
						}
					}
				});
	}

	private Object wrapMessagePipelineBuilder(ReactiveMessagePipelineBuilder<Object> delegate,
			Class<?> messagePipelineBuilderType, ClassLoader classLoader,
			Function<Publisher<?>, Publisher<?>> publisherConverter) {
		return Proxy.newProxyInstance(classLoader, new Class<?>[] { messagePipelineBuilderType, GenericPulsarClientProxiedInstance.class },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						switch (method.getName()) {
							case "getDelegateInstance":
								return delegate;
							case "equals":
								return this == args[0];
							case "hashCode":
								return this.hashCode();
							case "toString":
								return wrapToString(messagePipelineBuilderType, delegate.toString());
							case "streamingMessageHandler":
								Function<Publisher<Message<?>>, Publisher<MessageResult<Void>>> streamingMessageHandler = (Function<Publisher<Message<?>>, Publisher<MessageResult<Void>>>) args[0];
								delegate.streamingMessageHandler(flux -> streamingMessageHandler
										.apply((Publisher<Message<?>>) publisherConverter.apply(flux)));
								return this;
							default:
								Method delegateMethod = delegate.getClass().getMethod(method.getName(),
										method.getParameterTypes());
								Object retval = delegateMethod.invoke(delegate, args);
								if (retval != delegate) {
									return retval;
								}
								else {
									return this;
								}
						}
					}
				});
	}

}
