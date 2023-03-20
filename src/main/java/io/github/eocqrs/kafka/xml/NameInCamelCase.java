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

package io.github.eocqrs.kafka.xml;

import lombok.RequiredArgsConstructor;
import org.cactoos.list.ListOf;
import org.cactoos.text.Concatenated;
import org.cactoos.text.Joined;
import org.cactoos.text.TextOf;
import org.cactoos.text.Upper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * It takes a string in the form of `com.example.foo.bar`
 * and returns a string in the form of `comExampleFooBar`.
 *
 * @author Ivan Ivanchuk (l3r8y@duck.com)
 * @since 0.0.2
 */
@RequiredArgsConstructor
public final class NameInCamelCase {

  /**
   * The origin.
   */
  private final String origin;

  @Override
  public String toString() {
    final String[] result = this.origin.split("\\.");
    return new Concatenated(
      new TextOf(result[0]),
      new Joined(
        "",
        new StringsInCamelCase(
          new ListOf<>(result).subList(1, result.length)
        ).value()
      )
    ).toString();
  }
}
