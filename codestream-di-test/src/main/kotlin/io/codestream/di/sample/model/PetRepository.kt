package io.codestream.di.sample.model

interface PetRepository {

    fun add(pet: Pet)
    fun remove(pet: Pet)
    fun findByName(name: String) : Pet?

}