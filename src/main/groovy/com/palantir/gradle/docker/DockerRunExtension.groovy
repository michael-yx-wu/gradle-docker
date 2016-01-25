/*
 * Copyright 2016 Palantir Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.palantir.gradle.docker

import com.google.common.base.Preconditions
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet


class DockerRunExtension {

    private String name
    private String image
    private List<String> command = ImmutableList.of()
    private Set<String> ports = ImmutableSet.of()

    public String getName() {
        return name
    }

    public void setName(String name) {
        this.name = name
    }

    public String getImage() {
        return image
    }

    public void setImage(String image) {
        this.image = image
    }

    public Set<String> getPorts() {
        return ports
    }

    public List<String> getCommand() {
        return command
    }

    public void command(String... command) {
        this.command = ImmutableList.copyOf(command)
    }

    public void ports(String... ports) {
        ImmutableSet.Builder builder = ImmutableSet.builder()
        for (String port : ports) {
            String[] mapping = port.split(':', 2)
            if (mapping.length == 1) {
                checkPortIsValid(mapping[0])
                builder.add("${mapping[0]}:${mapping[0]}")
            } else {
                checkPortIsValid(mapping[0])
                checkPortIsValid(mapping[1])
                builder.add("${mapping[0]}:${mapping[1]}")
            }
        }
        this.ports = builder.build()
    }

    private static void checkPortIsValid(String port) {
        int val = Integer.parseInt(port)
        Preconditions.checkArgument(0 < val && val <= 65536, "Port must be in the range [1,65536]")
    }

}
