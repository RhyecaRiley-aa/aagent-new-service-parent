/*
 * Original location: https://github.com/springdoc/springdoc-openapi/blob/master/springdoc-openapi-ui/src/test/java/test/org/springdoc/ui/app1/SpringDocApp1Test.java
 *
 *  * Copyright 2019-2020 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package com.aa.airportagent.demo

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import kotlin.Throws
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.lang.Exception

@SpringBootTest
@AutoConfigureMockMvc
class SwaggerDocsTest {
    @Autowired
    protected var mockMvc: MockMvc? = null

    @Test
    @Throws(Exception::class)
    fun shouldDisplaySwaggerUiPage() {
        mockMvc!!.perform(MockMvcRequestBuilders.get("/swagger-ui/index.html"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Swagger UI")))
    }
}
