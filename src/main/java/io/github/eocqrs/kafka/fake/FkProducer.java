/*
 *  Copyright (c) 2022 Aliaksei Bialiauski, EO-CQRS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.eocqrs.kafka.fake;

import io.github.eocqrs.kafka.Data;
import io.github.eocqrs.kafka.Producer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Fake Kafka Producer.
 *
 * @param <K> The key
 * @param <X> The value
 * @author Aliaksei Bialiauski (abialiauski.dev@gmail.com)
 * @since 0.0.0
 */
public final class FkProducer<K, X> implements Producer<K, X> {

  /**
   * Broker.
   */
  private final FkBroker broker;

  /**
   * Ctor.
   *
   * @param brkr Broker
   */
  public FkProducer(final FkBroker brkr) {
    this.broker = brkr;
  }

  @Override
  public Future<RecordMetadata> send(
    final K key,
    final Data<X> message
  ) throws Exception {
    final boolean exists = this.broker.data(
        "broker/topics/topic[name = '%s']/name/text()"
          .formatted(message.topic())
      ).stream()
      .anyMatch(s ->
        s.equals(message.topic())
      );
    if (!exists) {
      throw new TopicDoesNotExists(
        "topic %s does not exits!"
          .formatted(
            message.topic()
          )
      );
    }
    this.broker.with(new DatasetDirs<>(key, message).value());
    return new FutureTask<>(
      () ->
        new RecordMetadata(
          new TopicPartition(
            message.topic(),
            message.partition()
          ),
          0L,
          0,
          0L,
          key.toString().getBytes().length,
          message.dataized()
            .dataize()
            .toString()
            .getBytes()
            .length
        )
    );
  }

  @Override
  public void close() {
    // should be empty;
  }
}
