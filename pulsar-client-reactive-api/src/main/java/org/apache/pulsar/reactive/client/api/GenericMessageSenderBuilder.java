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
import java.util.Map;
import java.util.Set;

import org.apache.pulsar.client.api.BatcherBuilder;
import org.apache.pulsar.client.api.CompressionType;
import org.apache.pulsar.client.api.CryptoKeyReader;
import org.apache.pulsar.client.api.HashingScheme;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.MessageRouter;
import org.apache.pulsar.client.api.MessageRoutingMode;
import org.apache.pulsar.client.api.ProducerAccessMode;
import org.apache.pulsar.client.api.ProducerCryptoFailureAction;
import org.reactivestreams.Publisher;

public interface GenericMessageSenderBuilder<T, ONEMESSAGEID extends Publisher<MessageId>, MANYMESSAGEIDS extends Publisher<MessageId>,
		B extends GenericMessageSender<T, ONEMESSAGEID, MANYMESSAGEIDS>, SELF extends GenericMessageSenderBuilder<T, ONEMESSAGEID, MANYMESSAGEIDS, B, SELF>> {

	SELF cache(ReactiveMessageSenderCache producerCache);

	SELF maxInflight(int maxInflight);

	SELF maxConcurrentSenderSubscriptions(int maxConcurrentSenderSubscriptions);

	default SELF applySpec(ReactiveMessageSenderSpec senderSpec) {
		getMutableSpec().applySpec(senderSpec);
		return (SELF)this;
	}

	ReactiveMessageSenderSpec toImmutableSpec();

	MutableReactiveMessageSenderSpec getMutableSpec();

	default SELF topic(String topicName) {
		getMutableSpec().setTopicName(topicName);
		return (SELF)this;
	}

	default SELF producerName(String producerName) {
		getMutableSpec().setProducerName(producerName);
		return (SELF)this;
	}

	default SELF sendTimeout(Duration sendTimeout) {
		getMutableSpec().setSendTimeout(sendTimeout);
		return (SELF)this;
	}

	default SELF maxPendingMessages(int maxPendingMessages) {
		getMutableSpec().setMaxPendingMessages(maxPendingMessages);
		return (SELF)this;
	}

	default SELF maxPendingMessagesAcrossPartitions(int maxPendingMessagesAcrossPartitions) {
		getMutableSpec().setMaxPendingMessagesAcrossPartitions(maxPendingMessagesAcrossPartitions);
		return (SELF)this;
	}

	default SELF messageRoutingMode(MessageRoutingMode messageRoutingMode) {
		getMutableSpec().setMessageRoutingMode(messageRoutingMode);
		return (SELF)this;
	}

	default SELF hashingScheme(HashingScheme hashingScheme) {
		getMutableSpec().setHashingScheme(hashingScheme);
		return (SELF)this;
	}

	default SELF cryptoFailureAction(ProducerCryptoFailureAction cryptoFailureAction) {
		getMutableSpec().setCryptoFailureAction(cryptoFailureAction);
		return (SELF)this;
	}

	default SELF messageRouter(MessageRouter messageRouter) {
		getMutableSpec().setMessageRouter(messageRouter);
		return (SELF)this;
	}

	default SELF batchingMaxPublishDelay(Duration batchingMaxPublishDelay) {
		getMutableSpec().setBatchingMaxPublishDelay(batchingMaxPublishDelay);
		return (SELF)this;
	}

	default SELF roundRobinRouterBatchingPartitionSwitchFrequency(
			int roundRobinRouterBatchingPartitionSwitchFrequency) {
		getMutableSpec()
				.setRoundRobinRouterBatchingPartitionSwitchFrequency(roundRobinRouterBatchingPartitionSwitchFrequency);
		return (SELF)this;
	}

	default SELF batchingMaxMessages(int batchingMaxMessages) {
		getMutableSpec().setBatchingMaxMessages(batchingMaxMessages);
		return (SELF)this;
	}

	default SELF batchingMaxBytes(int batchingMaxBytes) {
		getMutableSpec().setBatchingMaxBytes(batchingMaxBytes);
		return (SELF)this;
	}

	default SELF batchingEnabled(boolean batchingEnabled) {
		getMutableSpec().setBatchingEnabled(batchingEnabled);
		return (SELF)this;
	}

	default SELF batcherBuilder(BatcherBuilder batcherBuilder) {
		getMutableSpec().setBatcherBuilder(batcherBuilder);
		return (SELF)this;
	}

	default SELF chunkingEnabled(boolean chunkingEnabled) {
		getMutableSpec().setChunkingEnabled(chunkingEnabled);
		return (SELF)this;
	}

	default SELF cryptoKeyReader(CryptoKeyReader cryptoKeyReader) {
		getMutableSpec().setCryptoKeyReader(cryptoKeyReader);
		return (SELF)this;
	}

	default SELF encryptionKeys(Set<String> encryptionKeys) {
		getMutableSpec().setEncryptionKeys(encryptionKeys);
		return (SELF)this;
	}

	default SELF compressionType(CompressionType compressionType) {
		getMutableSpec().setCompressionType(compressionType);
		return (SELF)this;
	}

	default SELF initialSequenceId(long initialSequenceId) {
		getMutableSpec().setInitialSequenceId(initialSequenceId);
		return (SELF)this;
	}

	default SELF autoUpdatePartitions(boolean autoUpdatePartitions) {
		getMutableSpec().setAutoUpdatePartitions(autoUpdatePartitions);
		return (SELF)this;
	}

	default SELF autoUpdatePartitionsInterval(Duration autoUpdatePartitionsInterval) {
		getMutableSpec().setAutoUpdatePartitionsInterval(autoUpdatePartitionsInterval);
		return (SELF)this;
	}

	default SELF multiSchema(boolean multiSchema) {
		getMutableSpec().setMultiSchema(multiSchema);
		return (SELF)this;
	}

	default SELF accessMode(ProducerAccessMode accessMode) {
		getMutableSpec().setAccessMode(accessMode);
		return (SELF)this;
	}

	default SELF lazyStartPartitionedProducers(boolean lazyStartPartitionedProducers) {
		getMutableSpec().setLazyStartPartitionedProducers(lazyStartPartitionedProducers);
		return (SELF)this;
	}

	default SELF properties(Map<String, String> properties) {
		getMutableSpec().setProperties(properties);
		return (SELF)this;
	}

	default SELF initialSubscriptionName(String initialSubscriptionName) {
		getMutableSpec().setInitialSubscriptionName(initialSubscriptionName);
		return (SELF)this;
	}

	B build();
}
