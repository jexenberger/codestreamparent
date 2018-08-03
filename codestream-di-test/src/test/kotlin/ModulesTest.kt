/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*
 *  Copyright 2018 Julian Exenberger
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    `*   `[`http://www.apache.org/licenses/LICENSE-2.0`](http://www.apache.org/licenses/LICENSE-2.0)
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import io.codestream.di.api.*
import io.codestream.di.sample.model.Pet
import io.codestream.di.sample.model.PetRepository
import io.codestream.di.sample.repo.InMemoryPetRepository
import io.codestream.di.sample.repo.MemoryRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.time.LocalDate
import java.util.*
import kotlin.test.assertNotNull

class ModulesTest {





    @Test
    internal fun testDefinePetstore() {
        val ctx = context {
            add { addType<MemoryRepository<Pet>>(MemoryRepository::class) withId id("petRepository") }
            add { addType<PetRepository>(InMemoryPetRepository::class) withId id(PetRepository::class) }
        }
        val repo: PetRepository = ctx.get(PetRepository::class) ?: fail { "should have returned a value" }
        repo.add(Pet("Perry", "platypus", LocalDate.now().minusYears(1)))

        assertNotNull(repo.findByName("Perry"))
    }
}