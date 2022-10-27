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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.pulsar.client.api.ConsumerCryptoFailureAction;
import org.apache.pulsar.client.api.CryptoKeyReader;
import org.apache.pulsar.client.api.DeadLetterPolicy;
import org.apache.pulsar.client.api.KeySharedPolicy;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.RegexSubscriptionMode;
import org.apache.pulsar.client.api.SubscriptionMode;
import org.apache.pulsar.client.api.SubscriptionType;
import org.reactivestreams.Publisher;
import reactor.core.scheduler.Scheduler;

public interface GenericMessageConsumerBuilder<T, ONEMESSAGE extends Publisher<Message<T>>, MANYMESSAGES extends Publisher<Message<T>>, B extends GenericMessageConsumer<T, ONEMESSAGE, MANYMESSAGES>, SELF extends GenericMessageConsumerBuilder<T, ONEMESSAGE, MANYMESSAGES, B, SELF>> {

	default SELF applySpec(ReactiveMessageConsumerSpec consumerSpec) {
		getMutableSpec().applySpec(consumerSpec);
		return (SELF) this;
	}

	ReactiveMessageConsumerSpec toImmutableSpec();

	MutableReactiveMessageConsumerSpec getMutableSpec();

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

	default SELF topicsPattern(Pattern topicsPattern) {
		getMutableSpec().setTopicsPattern(topicsPattern);
		return (SELF) this;
	}

	default SELF topicsPatternSubscriptionMode(RegexSubscriptionMode topicsPatternSubscriptionMode) {
		getMutableSpec().setTopicsPatternSubscriptionMode(topicsPatternSubscriptionMode);
		return (SELF) this;
	}

	default SELF topicsPatternAutoDiscoveryPeriod(Duration topicsPatternAutoDiscoveryPeriod) {
		getMutableSpec().setTopicsPatternAutoDiscoveryPeriod(topicsPatternAutoDiscoveryPeriod);
		return (SELF) this;
	}

	default SELF subscriptionName(String subscriptionName) {
		getMutableSpec().setSubscriptionName(subscriptionName);
		return (SELF) this;
	}

	default SELF subscriptionMode(SubscriptionMode subscriptionMode) {
		getMutableSpec().setSubscriptionMode(subscriptionMode);
		return (SELF) this;
	}

	default SELF subscriptionType(SubscriptionType subscriptionType) {
		getMutableSpec().setSubscriptionType(subscriptionType);
		return (SELF) this;
	}

	default SELF keySharedPolicy(KeySharedPolicy keySharedPolicy) {
		getMutableSpec().setKeySharedPolicy(keySharedPolicy);
		return (SELF) this;
	}

	default SELF replicateSubscriptionState(boolean replicateSubscriptionState) {
		getMutableSpec().setReplicateSubscriptionState(replicateSubscriptionState);
		return (SELF) this;
	}

	default SELF subscriptionProperties(Map<String, String> subscriptionProperties) {
		getMutableSpec().setSubscriptionProperties(subscriptionProperties);
		return (SELF) this;
	}

	default SELF subscriptionProperty(String key, String value) {
		if (getMutableSpec().getSubscriptionProperties() == null) {
			getMutableSpec().setSubscriptionProperties(new LinkedHashMap<>());
		}
		getMutableSpec().getSubscriptionProperties().put(key, value);
		return (SELF) this;
	}

	default SELF consumerName(String consumerName) {
		getMutableSpec().setConsumerName(consumerName);
		return (SELF) this;
	}

	default SELF properties(Map<String, String> properties) {
		getMutableSpec().setProperties(properties);
		return (SELF) this;
	}

	default SELF property(String key, String value) {
		if (getMutableSpec().getProperties() == null) {
			getMutableSpec().setProperties(new LinkedHashMap<>());
		}
		getMutableSpec().getProperties().put(key, value);
		return (SELF) this;
	}

	default SELF priorityLevel(Integer priorityLevel) {
		getMutableSpec().setPriorityLevel(priorityLevel);
		return (SELF) this;
	}

	default SELF readCompacted(boolean readCompacted) {
		getMutableSpec().setReadCompacted(readCompacted);
		return (SELF) this;
	}

	default SELF batchIndexAckEnabled(boolean batchIndexAckEnabled) {
		getMutableSpec().setBatchIndexAckEnabled(batchIndexAckEnabled);
		return (SELF) this;
	}

	default SELF ackTimeout(Duration ackTimeout) {
		getMutableSpec().setAckTimeout(ackTimeout);
		return (SELF) this;
	}

	default SELF ackTimeoutTickTime(Duration ackTimeoutTickTime) {
		getMutableSpec().setAckTimeoutTickTime(ackTimeoutTickTime);
		return (SELF) this;
	}

	default SELF acknowledgementsGroupTime(Duration acknowledgementsGroupTime) {
		getMutableSpec().setAcknowledgementsGroupTime(acknowledgementsGroupTime);
		return (SELF) this;
	}

	/**
	 * When set to true, ignores the acknowledge operation completion and makes it
	 * asynchronous from the message consuming processing to improve performance by
	 * allowing the acknowledges and message processing to interleave. Defaults to true.
	 * @param acknowledgeAsynchronously when set to true, ignores the acknowledge
	 * operation completion
	 * @return the current ReactiveMessageConsumerFactory instance (this)
	 */
	default SELF acknowledgeAsynchronously(boolean acknowledgeAsynchronously) {
		getMutableSpec().setAcknowledgeAsynchronously(acknowledgeAsynchronously);
		return (SELF) this;
	}

	default SELF acknowledgeScheduler(Scheduler acknowledgeScheduler) {
		getMutableSpec().setAcknowledgeScheduler(acknowledgeScheduler);
		return (SELF) this;
	}

	default SELF negativeAckRedeliveryDelay(Duration negativeAckRedeliveryDelay) {
		getMutableSpec().setNegativeAckRedeliveryDelay(negativeAckRedeliveryDelay);
		return (SELF) this;
	}

	default SELF deadLetterPolicy(DeadLetterPolicy deadLetterPolicy) {
		getMutableSpec().setDeadLetterPolicy(deadLetterPolicy);
		return (SELF) this;
	}

	default SELF retryLetterTopicEnable(boolean retryLetterTopicEnable) {
		getMutableSpec().setRetryLetterTopicEnable(retryLetterTopicEnable);
		return (SELF) this;
	}

	default SELF receiverQueueSize(Integer receiverQueueSize) {
		getMutableSpec().setReceiverQueueSize(receiverQueueSize);
		return (SELF) this;
	}

	default SELF maxTotalReceiverQueueSizeAcrossPartitions(Integer maxTotalReceiverQueueSizeAcrossPartitions) {
		getMutableSpec().setMaxTotalReceiverQueueSizeAcrossPartitions(maxTotalReceiverQueueSizeAcrossPartitions);
		return (SELF) this;
	}

	default SELF autoUpdatePartitions(boolean autoUpdatePartitions) {
		getMutableSpec().setAutoUpdatePartitions(autoUpdatePartitions);
		return (SELF) this;
	}

	default SELF autoUpdatePartitionsInterval(Duration autoUpdatePartitionsInterval) {
		getMutableSpec().setAutoUpdatePartitionsInterval(autoUpdatePartitionsInterval);
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

	default SELF maxPendingChunkedMessage(Integer maxPendingChunkedMessage) {
		getMutableSpec().setMaxPendingChunkedMessage(maxPendingChunkedMessage);
		return (SELF) this;
	}

	default SELF autoAckOldestChunkedMessageOnQueueFull(boolean autoAckOldestChunkedMessageOnQueueFull) {
		getMutableSpec().setAutoAckOldestChunkedMessageOnQueueFull(autoAckOldestChunkedMessageOnQueueFull);
		return (SELF) this;
	}

	default SELF expireTimeOfIncompleteChunkedMessage(Duration expireTimeOfIncompleteChunkedMessage) {
		getMutableSpec().setExpireTimeOfIncompleteChunkedMessage(expireTimeOfIncompleteChunkedMessage);
		return (SELF) this;
	}

	B build();

}
