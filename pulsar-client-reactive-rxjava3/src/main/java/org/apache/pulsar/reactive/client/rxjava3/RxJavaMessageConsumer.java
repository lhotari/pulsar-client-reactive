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
import org.apache.pulsar.reactive.client.api.GenericMessageConsumer;
import org.apache.pulsar.reactive.client.api.MessageResult;
import org.reactivestreams.Publisher;

public interface RxJavaMessageConsumer<T> extends GenericMessageConsumer<T, Flowable<Message<T>>, Flowable<Message<T>>> {
	@Override
	<R> Flowable<R> consumeOne(Function<Flowable<Message<T>>, Publisher<MessageResult<R>>> messageHandler);

	@Override
	<R> Flowable<R> consumeMany(Function<Flowable<Message<T>>, Publisher<MessageResult<R>>> messageHandler);

	@Override
	Flowable<Void> consumeNothing();
}
