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
    fun testApplication() {
        val serviceLoader = ServiceLoader.load(DIModule::class.java).findFirst()

        val ctx = module {
            add { addType<MemoryRepository<Pet>>(MemoryRepository::class) withId id("petRepository") }
            add { addType<AnotherObject>(AnotherObject::class) }
        }
        val instance: AnotherObject = ctx.get(AnotherObject::class)!!
    }


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