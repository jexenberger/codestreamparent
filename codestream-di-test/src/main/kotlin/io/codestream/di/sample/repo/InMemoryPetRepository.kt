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

package io.codestream.di.sample.repo

import io.codestream.di.annotation.Qualified
import io.codestream.di.sample.model.Pet
import io.codestream.di.sample.model.PetRepository

class InMemoryPetRepository(@Qualified("petRepository") var repo:MemoryRepository<Pet>) : PetRepository {


    override fun add(pet: Pet) {
        repo[pet.name] = pet
    }

    override fun remove(pet: Pet) {
        repo.remove(pet.name)
    }

    override fun findByName(name: String) : Pet? {
        return repo[name]
    }
}