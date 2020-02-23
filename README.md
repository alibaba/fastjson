
# fastjson

[![Build Status](https://travis-ci.org/alibaba/fastjson.svg?branch=master)](https://travis-ci.org/alibaba/fastjson)
[![Codecov](https://codecov.io/gh/alibaba/fastjson/branch/master/graph/badge.svg)](https://codecov.io/gh/alibaba/fastjson/branch/master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.alibaba/fastjson/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.alibaba/fastjson/)
[![GitHub release](https://img.shields.io/github/release/alibaba/fastjson.svg)](https://github.com/alibaba/fastjson/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.io/#https://github.com/alibaba/fastjson) 

Fastjson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object. Fastjson can work with arbitrary Java objects including pre-existing objects that you do not have source-code of.

### Fastjson Goals
 * Provide best performance in server side and android client
 * Provide simple toJSONString() and parseObject() methods to convert Java objects to JSON and vice-versa
 * Allow pre-existing unmodifiable objects to be converted to and from JSON
 * Extensive support of Java Generics
 * Allow custom representations for objects
 * Support arbitrarily complex objects (with deep inheritance hierarchies and extensive use of generic types)

![fastjson](logo.jpg "fastjson")

## Documentation

- [Documentation Home](https://github.com/alibaba/fastjson/wiki)
- [Contributing Code](https://github.com/nschaffner/fastjson/blob/master/CONTRIBUTING.md)
- [Frequently Asked Questions](https://github.com/alibaba/fastjson/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98)

## Benchmark

https://github.com/eishay/jvm-serializers/wiki

## Download

- [maven][1]
- [the latest JAR][2]

[1]: http://repo1.maven.org/maven2/com/alibaba/fastjson/
[2]: https://search.maven.org/remote_content?g=com.alibaba&a=fastjson&v=LATEST

## Maven

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.61</version>
</dependency>
```

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.1.71.android</version>
</dependency>
```

## Gradle via JCenter

``` groovy
compile 'com.alibaba:fastjson:1.2.61'
```

``` groovy
compile 'com.alibaba:fastjson:1.1.71.android'
```

Please see this [Wiki Download Page][Wiki] for more repository infos.

[Wiki]: https://github.com/alibaba/fastjson/wiki#download

### *License*

Fastjson is released under the [Apache 2.0 license](license.txt).

```
Copyright 1999-2019 Alibaba Group Holding Ltd.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at following link.

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
