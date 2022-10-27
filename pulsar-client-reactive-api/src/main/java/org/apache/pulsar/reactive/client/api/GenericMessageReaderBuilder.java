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

import java.util.List;

import org.apache.pulsar.client.api.ConsumerCryptoFailureAction;
import org.apache.pulsar.client.api.CryptoKeyReader;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Range;
import org.reactivestreams.Publisher;

public interface GenericMessageReaderBuilder<T, ONEMESSAGE extends Publisher<Message<T>>, MANYMESSAGES extends Publisher<Message<T>>, B extends GenericMessageReader<T, ONEMESSAGE, MANYMESSAGES>, SELF extends GenericMessageReaderBuilder<T, ONEMESSAGE, MANYMESSAGES, B, SELF>> {

	SELF startAtSpec(StartAtSpec startAtSpec);

	SELF endOfStreamAction(EndOfStreamAction endOfStreamAction);

	default SELF applySpec(ReactiveMessageReaderSpec readerSpec) {
		getMutableSpec().applySpec(readerSpec);
		return (SELF) this;
	}

	ReactiveMessageReaderSpec toImmutableSpec();

	MutableReactiveMessageReaderSpec getMutableSpec();

	SELF clone();

	B build();

	default SELF topic(String topicName) {
		getMutableSpec().getTopicNames().add(topicName);
		return (SELF) this;
	}

	default SELF topic(String... topicNames) {
		for (String topicName : topicNames) {
			getMutableSpec().getTopicNames().add(topicName);
		}
		return (SELF) this;
	}

	default SELF topicNames(List<String> topicNames) {
		getMutableSpec().setTopicNames(topicNames);
		return (SELF) this;
	}

	default SELF readerName(String readerName) {
		getMutableSpec().setReaderName(readerName);
		return (SELF) this;
	}

	default SELF subscriptionName(String subscriptionName) {
		getMutableSpec().setSubscriptionName(subscriptionName);
		return (SELF) this;
	}

	default SELF generatedSubscriptionNamePrefix(String generatedSubscriptionNamePrefix) {
		getMutableSpec().setGeneratedSubscriptionNamePrefix(generatedSubscriptionNamePrefix);
		return (SELF) this;
	}

	default SELF receiverQueueSize(Integer receiverQueueSize) {
		getMutableSpec().setReceiverQueueSize(receiverQueueSize);
		return (SELF) this;
	}

	default SELF readCompacted(Boolean readCompacted) {
		getMutableSpec().setReadCompacted(readCompacted);
		return (SELF) this;
	}

	default SELF keyHashRanges(List<Range> keyHashRanges) {
		getMutableSpec().setKeyHashRanges(keyHashRanges);
		return (SELF) this;
	}

	default SELF cryptoKeyReader(CryptoKeyReader cryptoKeyReader) {
		getMutableSpec().setCryptoKeyReader(cryptoKeyReader);
		return (SELF) this;
	}

	default SELF cryptoFailureAction(ConsumerCryptoFailureAction cryptoFailureAction) {
		getMutableSpec().setCryptoFailureAction(cryptoFailureAction);
		return (SELF) this;
	}

}
