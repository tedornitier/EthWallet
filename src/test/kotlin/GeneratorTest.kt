import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class GeneratorTest {
    @Test
    fun testMainWithArgs() {
        val argsList = listOf(
            // valid cases
            "",
            "--prefix a",
            "--prefix a --key b",
            "--key b",
            "-p a",
            "-p a -k b",
            "-k b",
            "-h",
            "--help",
            // invalid cases
            "--prefix",
            "something",
            "--key",
            "-k",
            "-p"
        )

        argsList.forEach { args ->
            assertDoesNotThrow { main(args.split(" ").toTypedArray()) }
        }
    }

    @Test
    fun testGeneratePrivateKey() {
        generatePrivateKey("a").let {
            println(it)
            assertTrue(it.first.startsWith("a"))
            assertTrue(it.second.length == 64)
        }
    }

    @Test
    fun testEncryptDecrypt() {
        val key = "a"
        val privateKey = "b"
        val encrypted = encryptString(privateKey, key)
        assertEquals(privateKey, decryptString(encrypted, key))
        assertNotEquals(privateKey, encrypted)
    }

    @Test
    fun testHexize() {
        hexize("a").also {
            assertEquals("a", it)
        }
        hexize("hello").also {
            assertEquals("4e770", it)
        }
    }
}
