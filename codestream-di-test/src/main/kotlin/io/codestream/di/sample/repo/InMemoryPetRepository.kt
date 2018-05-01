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