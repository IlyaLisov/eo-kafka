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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test case for {@link FkConsumer}.
 *
 * @author Ivan Ivanchuck (l3r8y@duck.com)
 * @since 0.0.3
 */
final class FkConsumerTest {

    @Test
    void createsFakeConsumer() {
      final FkConsumer<String, String> consumer = new FkConsumer<>();
      MatcherAssert.assertThat(consumer, Matchers.is(Matchers.notNullValue()));
      assertThrows(
        UnsupportedOperationException.class,
        () -> consumer.subscribe("123")
      );
      assertThrows(
        UnsupportedOperationException.class,
        () -> consumer.subscribe(new ArrayList<>(0))
      );
      assertThrows(
        UnsupportedOperationException.class,
        () -> consumer.iterate("123", Duration.ofMillis(100L))
      );
      assertThrows(
        UnsupportedOperationException.class,
        () -> consumer.iterate("123", Duration.ofMillis(100L))
      );
      assertThrows(
        UnsupportedOperationException.class,
        consumer::close
      );
    }

}