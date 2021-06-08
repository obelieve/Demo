/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package retrofit2;

import static retrofit2.Utils.methodError;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import javax.annotation.Nullable;

abstract class ServiceMethod<T> {
  static <T> ServiceMethod<T> parseAnnotations(Retrofit retrofit, Method method) {
    // ZXYNOTE: 2021/6/8 22:28 =====z1.1.1.1===== ServiceMethod类开始解析Method第一步，解析生成RequestFactory对象
    RequestFactory requestFactory = RequestFactory.parseAnnotations(retrofit, method);
    // ZXYNOTE: 2021/6/8 22:30 =====z1.1.1.2===== ServiceMethod类开始解析Method第二步，再做一个方法返回类型的校验 Method#getGenericReturnType()
    Type returnType = method.getGenericReturnType();
    if (Utils.hasUnresolvableType(returnType)) {
      throw methodError(
          method,
          "Method return type must not include a type variable or wildcard: %s",
          returnType);
    }
    if (returnType == void.class) {
      throw methodError(method, "Service methods cannot return void.");
    }
    // ZXYNOTE: 2021/6/8 22:30 =====z1.1.1.3===== ServiceMethod类开始解析Method第三步，然后根据retrofit、method、requestFactory对象，返回HttpServiceMethod
    return HttpServiceMethod.parseAnnotations(retrofit, method, requestFactory);
  }

  abstract @Nullable T invoke(Object[] args);
}
