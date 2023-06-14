/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023 Aliaksei Bialiauski, EO-CQRS
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
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

import io.github.eocqrs.kafka.Producer;
import io.github.eocqrs.kafka.data.KfData;
import io.github.eocqrs.xfake.InFile;
import io.github.eocqrs.xfake.Synchronized;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link FkProducer}.
 *
 * @author Aliaksei Bialiauski (abialiaski.dev@gmail.com)
 * @since 0.1.3
 */
final class FkProducerTest {

  @Test
  void createsFakeProducer() throws Exception {
    final Producer<String, String> producer =
      new FkProducer<>(
        new InXml(
          new Synchronized(
            new InFile(
              "test-producer-create", "<broker/>"
            )
          )
        )
      );
    MatcherAssert.assertThat(
      "Fake producer creates",
      producer,
      Matchers.notNullValue()
    );
    Assertions.assertDoesNotThrow(producer::close);
  }

  @Test
  void sendsMessageWithoutTopicExistence() throws Exception {
    final Producer<String, String> producer =
      new FkProducer<>(
        new InXml(
          new Synchronized(
            new InFile(
              "test-producer-no-topic",
              "<broker/>"
            )
          )
        )
      );
    Assertions.assertThrows(
      TopicDoesNotExists.class,
      () ->
        producer.send("test-key", new KfData<>("data", "does.not.exist", 0))
    );
    Assertions.assertDoesNotThrow(producer::close);
  }

  @Test
  void sendsMessage() throws Exception {
    final String topic = "test.fake";
    final String data = "data";
    final FkBroker broker = new InXml(
      new Synchronized(
        new InFile(
          "test-producer-send", "<broker/>"
        )
      )
    ).with(new TopicDirs(topic).value());
    final Producer<String, String> producer =
      new FkProducer<>(
        broker
      );
    producer.send("test-key", new KfData<>(data, topic, 0));
    MatcherAssert.assertThat(
      "Message is send in right format",
      broker.data(
        "broker/topics/topic[name = '%s']/datasets/dataset[value = '%s']/text()"
          .formatted(
            topic,
            data
          )
      ).isEmpty(),
      Matchers.equalTo(false)
    );
    Assertions.assertDoesNotThrow(producer::close);
  }
}
